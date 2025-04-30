# Vet API

API для управления ветеринарами. Позволяет создавать, обновлять и получать информацию о ветеринарах, их специализациях и приёмах.

## Базовый URL
`/api/vets`

## Авторизация
Все запросы требуют заголовка `Authorization` с JWT-токеном.

## DTO модели

### VetRegistrationDto
Используется для регистрации нового ветеринара.

Поля:
- `firstName` (String, обязательное) - Имя ветеринара
- `lastName` (String, обязательное) - Фамилия ветеринара
- `specialization` (String, обязательное) - Специализация
- `qualification` (String, обязательное) - Квалификация
- `bio` (String, необязательное) - Биография
- `email` (String, обязательное) - Email
- `password` (String, обязательное) - Пароль
- `clinicId` (String, необязательное) - ID клиники (если есть)
- `address` (AddressDto, обязательное) - Адрес

### VetUpdateDto
Используется для обновления данных ветеринара.

Поля:
- `firstName` (String, необязательное) - Имя
- `lastName` (String, необязательное) - Фамилия
- `specialization` (String, необязательное) - Специализация
- `qualification` (String, необязательное) - Квалификация
- `bio` (String, необязательное) - Биография
- `avatarUrl` (String, необязательное) - URL аватара
- `address` (AddressDto, необязательное) - Адрес

### VetDto
Полная информация о ветеринаре.

Поля:
- `id` (String) - ID ветеринара
- `firstName` (String) - Имя
- `lastName` (String) - Фамилия
- `specialization` (String) - Специализация
- `qualification` (String) - Квалификация
- `bio` (String) - Биография
- `email` (String) - Email
- `avatarUrl` (String) - URL аватара
- `clinic` (ClinicShortDto) - Клиника (если есть)
- `services` (Set<String>) - Услуги
- `addressDto` (AddressDto) - Адрес

### AppointmentDto
Информация о записи на приём.

Поля:
- `id` (String) - ID записи
- `dateTime` (LocalDateTime) - Дата и время приёма
- `petName` (String) - Имя питомца
- `ownerName` (String) - Имя владельца
- `status` (String) - Статус записи

## Endpoints

### Получить всех ветеринаров
`GET /`

**Ответ**:  
Список всех ветеринаров (List<VetDto>)

**Пример ответа**:
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "firstName": "Иван",
    "lastName": "Петров",
    "specialization": "Хирургия",
    "qualification": "Высшая категория",
    "bio": "Опыт работы 10 лет",
    "email": "ivan.petrov@example.com",
    "avatarUrl": "https://example.com/avatars/1.jpg",
    "clinic": {
      "id": "987e6543-e21b-12d3-a456-426614174000",
      "name": "ВетКлиника",
      "logoUrl": "https://example.com/logos/1.jpg"
    },
    "services": ["Операции", "УЗИ", "Анализы"],
    "addressDto": {
      "city": "Москва",
      "street": "Ленина",
      "building": "42",
      "postalCode": "123456"
    }
  }
]
```

### Получить ветеринаров по специализации
`GET /by-specialization?specialization={специализация}`

Параметры:

specialization (String) - Специализация для фильтрации

Ответ:
Список ветеринаров с указанной специализацией (List<VetDto>)

### Получить независимых ветеринаров
`GET /independent
`
Ответ:
Список ветеринаров без привязки к клинике (List<VetDto>)

### Получить ветеринаров клиники
`GET /by-clinic/{clinicId}
`
Параметры пути:

- `clinicId` (String) - ID клиники

Ответ:
Список ветеринаров указанной клиники (List<VetDto>)

### Добавить нового ветеринара
`POST /
`
Тело запроса: VetRegistrationDto

Ответ:
Созданный ветеринар (VetDto) с кодом 201

### Обновить данные ветеринара
`PUT /{vetId}
`
Параметры пути:

- `vetId` (String) - ID ветеринара

Тело запроса: VetUpdateDto

Ответ:
Обновлённые данные ветеринара (VetDto)

### Получить записи на приём
`GET /{vetId}/appointments
`
Параметры пути:

- `vetId` (String) - ID ветеринара

Ответ:
Список записей на приём к ветеринару (List<AppointmentDto>)

### Добавить услугу ветеринару
`POST /{vetId}/services
`
Параметры пути:

- `vetId` (String) - ID ветеринара
