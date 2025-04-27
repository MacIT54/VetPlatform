# 🛡️ Auth Microservice

Микросервис для регистрации, аутентификации пользователей и управления паролями с использованием **JWT** и **Spring Security**.

---

## 📋 Оглавление

- [Описание](#описание)
- [Функциональность](#функциональность)
- [Технологии](#технологии)
- [Требования](#требования)
- [Переменные окружения](#переменные-окружения)
- [API Эндпоинты](#api-эндпоинты)
- [Структура запросов и ответов](#структура-запросов-и-ответов)
- [Безопасность](#безопасность)
- [Комментарии к реализации](#комментарии-к-реализации)
- [Автор](#автор)

---

## 📖 Описание

Микросервис обеспечивает регистрацию, вход в систему и смену пароля с использованием JWT-аутентификации.

---

## 🚀 Функциональность

- Регистрация новых пользователей
- Вход с выдачей JWT-токена
- Смена пароля для авторизованных пользователей
- Хэширование паролей
- Обработка ошибок с корректными HTTP-статусами

---

## 🛠️ Технологии

- Java 17+
- Spring Boot 3
- Spring Security
- JWT
- PostgreSQL
- Hibernate (JPA)
- Gradle

---

## ⚙️ Требования

- JDK 17+
- PostgreSQL 13+

---

## 🌐 Переменные окружения

| Переменная | Описание |
| :--- | :--- |
| `spring.datasource.url` | URL базы данных |
| `spring.datasource.username` | Имя пользователя БД |
| `spring.datasource.password` | Пароль от БД |
| `jwt.secret` | Секрет для подписи JWT |
| `jwt.expiration` | Срок жизни токена |

---

## 🔥 API Эндпоинты

| Метод | URL | Описание |
| :--- | :--- | :--- |
| `POST` | `/signup` | Регистрация пользователя |
| `POST` | `/login` | Аутентификация и получение JWT |
| `POST` | `/change-password` | Смена пароля |

---

## 📂 Структура запросов и ответов

### 🔹 Signup

```json
{
  "login": "user123",
  "password": "Password123!",
  "email": "user@example.com",
  "name": "John",
  "surname": "Doe",
  "userType": "REGULAR"
}
```

### 🔹 Login

```json
{
  "login": "user123",
  "password": "Password123!"
}
```

**Ответ:**

```json
{
  "token": "...",
  "type" : "Bearer"
}
```

### 🔹 Change Password

Заголовок:

```http
Authorization: Bearer your.jwt.token
```

Тело запроса:

```json
{
  "oldPassword": "Password123!",
  "newPassword": "NewPassword456!"
}
```

---

## 🛡️ Безопасность

- Хэширование паролей с помощью bcrypt
- Полная защита API через JWT
- Автентифика

## 💬 Комментарии к реализации

Необходимо продумать механизм создания пользователей с ролью `ADMIN`.

Перед запуском необходимо позаботиться о БД, ориентировочно необходимо настроить файл `/VETPLATFORM/docker-compose.yml` так, чтобы он поднимал контейнер с PostgreSQL базой внутри.

- `Database`: security_db
- `User`: postgres
- `Password`: 471979

Пример docker-compose.yml для PostgreSQL:

```yml
version: '3.8'
services:
  postgres:
    image: postgres:14
    environment:
      POSTGRES_DB: security_db
      POSTGRES_USER: your_db_username
      POSTGRES_PASSWORD: your_db_password
    ports:
      - "5432:5432"
```

Также необходимо перенастроить `/VETPLATFORM/auth-service/src/main/resources/application.yml` на работу с БД через Docker.

## 🧑‍💻 Автор

`Гузенков Семён ИП - 214`

- `Telegram`: @rocketbunny727
- `GitHub`: RocketBunny727
- `Email`: guzenkovsk@gmail.com