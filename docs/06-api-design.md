# API Design

## Overview

| Item         | Specification         |
| ------------ | --------------------- |
| Protocol     | HTTP/HTTPS            |
| API Style    | REST                  |
| Base Path    | `/api/v1`             |
| Content Type | `application/json`    |

## API Conventions

### HTTP Methods

| Method  | Purpose                                        |
| ------- | ---------------------------------------------- |
| `POST`  | Membuat resource baru atau menjalankan suatu action |
| `GET`   | Mengambil resource |
| `PUT`   | Mengganti seluruh representasi resource |
| `PATCH` | Memperbarui sebagian data resource |
| `DELETE`| Menghapus resource atau menandai resource sebagai deleted |            |

### HTTP Status Codes

| Status                      | Usage                                                            |
| --------------------------- | ---------------------------------------------------------------- |
| `200 OK`                    | Request berhasil untuk pengambilan atau perubahan data           |
| `201 Created`               | Resource berhasil dibuat                                         |
| `204 No Content`            | Operasi berhasil tanpa response body                             |
| `400 Bad Request`           | Request tidak valid atau gagal validasi                          |
| `401 Unauthorized`          | Authentication diperlukan atau credential tidak valid            |
| `403 Forbidden`             | User terautentikasi tetapi tidak memiliki izin melakukan operasi |
| `404 Not Found`             | Resource tidak ditemukan                                         |
| `409 Conflict`              | Request bertentangan dengan kondisi resource saat ini            |
| `500 Internal Server Error` | Terjadi kesalahan pada server yang tidak terduga                 |

### Identifier

| Item   | Specification |
| ------ | ------------- |
| Type   | UUID          |
| Format | String        |
| Length | 36 characters |

Example:

```text
550e8400-e29b-41d4-a716-446655440000
```

### Date and Time

| Item     | Specification          |
| -------- | ---------------------- |
| API Type | ISO 8601               |
| Timezone | UTC                    |
| Example  | `2026-08-08T06:30:00Z` |

---

# Standard API Response

Semua response API menggunakan struktur yang konsisten.

## Success Response

Response untuk single resource menggunakan struktur:

```json
{
  "data": {},
  "message": "optional"
}
```

Contoh:

```json
{
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "fullname": "Taufik",
    "email": "taufik@example.com"
  },
  "message": "Profile retrieved successfully"
}
```

## Error Response

Semua error API menggunakan struktur:

```json
{
  "errors": {
    "code": "BAD_REQUEST",
    "message": "Validation failed",
    "details": {
      "field": "Error message"
    }
  }
}
```

Untuk error yang tidak memiliki detail berdasarkan field:

```json
{
  "errors": {
    "code": "NOT_FOUND",
    "message": "User not found",
    "details": {}
  }
}
```

---

# Authentication

| Method  | Endpoint                | Authentication | Description                                              |
| ------- | ----------------------- | -------------- | -------------------------------------------------------- |
| `POST`  | `/api/v1/auth/register` | Public         | Mendaftarkan user baru.                                  |
| `POST`  | `/api/v1/auth/login`    | Public         | Melakukan authentication menggunakan email dan password. |
| `POST`  | `/api/v1/auth/refresh`  | Refresh Token  | Mendapatkan access token baru menggunakan refresh token. |
| `POST`  | `/api/v1/auth/logout`   | Required       | Mencabut refresh token pada sesi saat ini.               |
| `PATCH` | `/api/v1/auth/password` | Required       | Mengubah password user yang sedang terautentikasi.       |

# User

| Method  | Endpoint           | Authentication | Description                                          |
| ------- | ------------------ | -------------- | ---------------------------------------------------- |
| `GET`   | `/api/v1/users/me` | Required       | Mendapatkan profile user yang sedang terautentikasi. |
| `PATCH` | `/api/v1/users/me` | Required       | Memperbarui fullname user yang sedang terautentikasi. |

---

# Validation Rules

## Registration

| Field             | Rule                                                                                                          |
| ----------------- | ------------------------------------------------------------------------------------------------------------- |
| `fullname`        | Wajib diisi, 2–100 characters, tidak boleh kosong atau hanya whitespace                                       |
| `email`           | Wajib diisi, format email valid, maksimal 254 characters                                                      |
| `password`        | Wajib diisi, 8–72 characters, minimal satu uppercase, satu lowercase, satu number, dan satu special character |
| `confirmPassword` | Wajib diisi dan harus sama dengan `password`                                                                  |

## Login

| Field      | Rule                                                   |
| ---------- | ------------------------------------------------------ |
| `email`    | Wajib diisi dan harus memiliki format email yang valid |
| `password` | Wajib diisi                                            |

## Change Password

| Field             | Rule                                                                                  |
| ----------------- | ------------------------------------------------------------------------------------- |
| `currentPassword` | Wajib diisi                                                                           |
| `newPassword`     | Wajib diisi, harus memenuhi password policy, dan harus berbeda dari `currentPassword` |
| `confirmPassword` | Wajib diisi dan harus sama dengan `newPassword`                                       |

## Profile

| Field      | Rule                                                                              |
| ---------- | --------------------------------------------------------------------------------- |
| `fullname` | Jika dikirim, harus 2–100 characters dan tidak boleh kosong atau hanya whitespace |

---

# Error Handling

| HTTP Status | Error Code              | Usage                                                |
| ----------: | ----------------------- | ---------------------------------------------------- |
|       `400` | `BAD_REQUEST`           | Request tidak valid atau gagal validasi              |
|       `401` | `UNAUTHORIZED`          | Authentication credential tidak ada atau tidak valid |
|       `403` | `FORBIDDEN`             | User tidak memiliki izin melakukan operasi           |
|       `404` | `NOT_FOUND`             | Resource yang diminta tidak ditemukan                |
|       `409` | `CONFLICT`              | Terjadi konflik dengan kondisi resource saat ini     |
|       `500` | `INTERNAL_SERVER_ERROR` | Terjadi kesalahan server yang tidak terduga          |

### Validation Error

```http
400 Bad Request
```

```json
{
  "errors": {
    "code": "BAD_REQUEST",
    "message": "Validation failed",
    "details": {
      "password": "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    }
  }
}
```

### Invalid Authentication

```http
401 Unauthorized
```

```json
{
  "errors": {
    "code": "UNAUTHORIZED",
    "message": "Invalid credentials",
    "details": {}
  }
}
```

### Conflict

```http
409 Conflict
```

```json
{
  "errors": {
    "code": "CONFLICT",
    "message": "Email is already registered",
    "details": {}
  }
}
```

---

# Use Case Mapping

| Use Case                           | API                           |
| ---------------------------------- | ----------------------------- |
| UC-AUTH-001 — Register             | `POST /api/v1/auth/register`  |
| UC-AUTH-002 — Login                | `POST /api/v1/auth/login`     |
| UC-AUTH-003 — Refresh Access Token | `POST /api/v1/auth/refresh`   |
| UC-AUTH-004 — Logout               | `POST /api/v1/auth/logout`    |
| UC-AUTH-005 — Change Password      | `PATCH /api/v1/auth/password` |
| UC-USER-001 — View Profile         | `GET /api/v1/users/me`        |
| UC-USER-002 — Update Profile       | `PATCH /api/v1/users/me`      |
