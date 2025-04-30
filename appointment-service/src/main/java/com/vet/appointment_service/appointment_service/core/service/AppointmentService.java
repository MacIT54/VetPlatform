package com.vet.appointment_service.appointment_service.core.service;

import com.vet.appointment_service.appointment_service.api.client.ClinicServiceClient;
import com.vet.appointment_service.appointment_service.api.dto.AppointmentDto;
import com.vet.appointment_service.appointment_service.api.dto.BookAppointmentRequest;
import com.vet.appointment_service.appointment_service.api.dto.TimeSlotDto;
import com.vet.appointment_service.appointment_service.core.entity.Appointment;
import com.vet.appointment_service.appointment_service.core.entity.AppointmentStatus;
import com.vet.appointment_service.appointment_service.core.exception.ErrorCode;
import com.vet.appointment_service.appointment_service.core.exception.ServiceException;
import com.vet.appointment_service.appointment_service.core.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ClinicServiceClient clinicServiceClient;

    @Value("${app.redis.timeout}")
    private int redisTimeout;

    public List<TimeSlotDto> getAvailableSlots(String vetId, LocalDate date) {
        String cacheKey = "slots:vet:" + vetId + ":date:" + date;
        var cached = redisTemplate.opsForValue().get(cacheKey);

        if (cached != null) {
            return (List<TimeSlotDto>) cached;
        }

        List<TimeSlotDto> slots = generateTimeSlots(vetId, date);
        redisTemplate.opsForValue().set(cacheKey, slots, redisTimeout, TimeUnit.SECONDS);
        return slots;
    }

    private List<TimeSlotDto> generateTimeSlots(String vetId, LocalDate date) {
        LocalDateTime start = date.atTime(9, 0);
        LocalDateTime end = date.atTime(18, 0);

        List<Appointment> existing = appointmentRepository.findByVetIdAndStartTimeBetween(
                vetId, start, end);

        return Stream.iterate(start, time -> time.plusMinutes(30))
                .limit(18)
                .map(time -> new TimeSlotDto(
                        vetId,
                        time,
                        time.plusMinutes(30),
                        isSlotAvailable(existing, time)
                ))
                .toList();
    }

    private boolean isSlotAvailable(List<Appointment> appointments, LocalDateTime time) {
        return appointments.stream()
                .noneMatch(a -> !a.getStartTime().isAfter(time) && !a.getEndTime().isBefore(time.plusMinutes(30)));
    }

    @Transactional
    public AppointmentDto bookAppointment(BookAppointmentRequest request) {
        if (appointmentRepository.existsByVetIdAndStartTimeBetween(
                request.vetId(), request.startTime(), request.endTime())) {
            throw new ServiceException(ErrorCode.CONFLICT, "Time slot already booked");
        }

        Appointment appointment = new Appointment();
        appointment.setClinicId(request.clinicId());
        appointment.setVetId(request.vetId());
        appointment.setPetId(request.petId());
        appointment.setStartTime(request.startTime());
        appointment.setEndTime(request.endTime());
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointment.setNotes(request.notes());

        Appointment saved = appointmentRepository.save(appointment);
        clearCacheForVet(request.vetId(), request.startTime().toLocalDate());

        return mapToDto(saved);
    }

    private void clearCacheForVet(String vetId, LocalDate date) {
        String cacheKey = "slots:vet:" + vetId + ":date:" + date;
        redisTemplate.delete(cacheKey);
    }

    private AppointmentDto mapToDto(Appointment saved) {
        return AppointmentDto.builder()
                .id(saved.getId())
                .status(saved.getStatus())
                .vetId(saved.getVetId())
                .clinicId(saved.getClinicId())
                .petId(saved.getPetId())
                .status(saved.getStatus())
                .startTime(saved.getStartTime())
                .endTime(saved.getEndTime())
                .notes(saved.getNotes())
                .build();
    }

}
