# Clinic API

API для управления клиниками и ветеринарами. Позволяет создавать клиники, добавлять ветеринаров, получать информацию о клиниках.

## Базовый URL
`/api/profiles/clinics`

## Авторизация
Все запросы требуют заголовка `Authorization` с JWT-токеном.

## DTO модели

### ClinicRegistrationDto
Используется для регистрации новой клиники.

Поля:
- `name` (String, обязательное) - Название клиники
- `description` (String, обязательное) - Описание клиники
- `phone` (String, обязательное) - Телефон клиники
- `email` (String, обязательное) - Email клиники
- `address` (AddressDto, обязательное) - Адрес клиники
- `licenseNumber` (String, обязательное) - Номер лицензии
- `workingHours` (List<String>, необязательное) - График работы

Пример:
```json
{
  "name": "ВетКлиника",
  "description": "Круглосуточная ветеринарная клиника",
  "phone": "+79991234567",
  "email": "vet@example.com",
  "address": {
    "city": "Москва",
    "street": "Ленина",
    "building": "42",
    "postalCode": "123456"
  },
  "licenseNumber": "VET123456",
  "workingHours": ["Пн-Пт: 9:00-21:00", "Сб-Вс: 10:00-18:00"]
}
```

### ClinicShortDto
Краткая информация о клинике.

Поля:

- `id` (String) - ID клиники
- `name` (String, обязательное) - Название клиники
- `logoUrl` (String) - URL логотипа

### ClinicDto
Полная информация о клинике.

Поля:

- `id` (String) - ID клиники
- `name` (String) - Название клиники
- `description` (String) - Описание клиники
- `phone` (String) - Телефон клиники
- `email` (String) - Email клиники
- `city` (String) - Город
- `street` (String) - Улица
- `building` (String) - Номер дома
- `postalCode` (String) - Почтовый индекс
- `services` (Set<String>) - Услуги клиники
- `logoUrl` (String) - URL логотипа
- `workingHours` (List<String>) - График работы
- `vets` (List<VetShortDto>) - Список ветеринаров


## Endpoints
#### Создать клинику
POST /api/profiles/clinics

Тело запроса: ClinicRegistrationDto
Ответ: ClinicDto - созданная клиника

Пример запроса:

```json
{
  "name": "ВетКлиника",
  "description": "Круглосуточная ветеринарная клиника",
  "phone": "+79991234567",
  "email": "vet@example.com",
  "address": {
    "city": "Москва",
    "street": "Ленина",
    "building": "42",
    "postalCode": "123456"
  },
  "licenseNumber": "VET123456",
  "workingHours": ["Пн-Пт: 9:00-21:00", "Сб-Вс: 10:00-18:00"]
}
```

### Добавить ветеринара в клинику
POST /api/profiles/clinics/{clinicId}/vets/{vetId}

Параметры пути:

- `clinicId` (Long) - ID клиники 
- `vetId` (Long) - ID ветеринара

Ответ: ClinicDto - обновлённая клиника

### Получить информацию о клинике
GET /api/profiles/clinics/{clinicId}

Параметры пути:

- `clinicId` (Long) - ID клиники

Ответ: ClinicShortDto - информация о клинике

### Получить все клиники
GET /api/profiles/clinics/all

Ответ: List<ClinicShortDto> - список всех клиник