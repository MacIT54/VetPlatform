package com.vet.appointment_service.appointment_service.core.repository;

import com.vet.appointment_service.appointment_service.core.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByVetIdAndStartTimeBetween(String vetId, LocalDateTime start, LocalDateTime end);
    boolean existsByVetIdAndStartTimeBetween(String vetId, LocalDateTime start, LocalDateTime end);
}