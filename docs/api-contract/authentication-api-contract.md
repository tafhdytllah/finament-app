# Authentication API Contract

## Overview

Dokumen ini mendefinisikan kontrak API untuk module Authentication pada Finament.

| Item                   | Specification       |
| ---------------------- | ------------------- |
| Base Path              | `/api/v1/auth`      |
| Authentication         | JWT + Refresh Token |
| Access Token           | Bearer Token        |
| Refresh Token          | HttpOnly Cookie     |
| Access Token Lifetime  | 15 minutes          |
| Refresh Token Lifetime | 7 days              |

---

# Register

## Endpoint

```http
POST /api/v1/auth/register
```

### Authentication

Public.

### Request Body

```json
{
  "fullname": "Taufik",
  "email": "taufik@example.com",
  "password": "Password123!",
  "confirmPassword": "Password123!"
}
```

### Request Fields

| Field             | Type   | Required | Description                                        |
| ----------------- | ------ | -------- | -------------------------------------------------- |
| `fullname`        | String | Yes      | Nama lengkap pengguna.                             |
| `email`           | String | Yes      | Email pengguna.                                    |
| `password`        | String | Yes      | Password pengguna.                                 |
| `confirmPassword` | String | Yes      | Konfirmasi password. Harus sama dengan `password`. |

### Success Response

```http
201 Created
```

### Possible Errors

| Status | Error Code    | Condition                                  |
| ------ | ------------- | ------------------------------------------ |
| `400`  | `BAD_REQUEST` | Request tidak valid atau validation gagal. |
| `409`  | `CONFLICT`    | Email sudah terdaftar.                     |

---

# Login

## Endpoint

```http
POST /api/v1/auth/login
```

### Authentication

Public.

### Request Body

```json
{
  "email": "taufik@example.com",
  "password": "Password123!"
}
```

### Request Fields

| Field      | Type   | Required | Description        |
| ---------- | ------ | -------- | ------------------ |
| `email`    | String | Yes      | Email pengguna.    |
| `password` | String | Yes      | Password pengguna. |

### Success Response

```http
200 OK
```

Response body:

```json
{
  "data": {
    "accessToken": "<jwt-access-token>",
    "tokenType": "Bearer",
    "expiresIn": 900
  }
}
```

Response header:

```http
Set-Cookie: refresh_token=<refresh-token>; Max-Age=604800; HttpOnly; Secure; SameSite=None; Path=/api/v1/auth
```

Refresh token tidak dikembalikan dalam response body. Refresh token disimpan sebagai `HttpOnly` cookie.

### Possible Errors

| Status | Error Code     | Condition                        |
| ------ | -------------- | -------------------------------- |
| `400`  | `BAD_REQUEST`  | Request tidak valid.             |
| `401`  | `UNAUTHORIZED` | Email atau password tidak valid. |

---

# Refresh Access Token

## Endpoint

```http
POST /api/v1/auth/refresh
```

### Authentication

Refresh Token Cookie.

### Request

Tidak menggunakan request body.

Refresh token dikirim melalui cookie:

```http
Cookie: refresh_token=<refresh-token>
```

### Success Response

```http
200 OK
```

```json
{
  "data": {
    "accessToken": "<new-jwt-access-token>",
    "tokenType": "Bearer",
    "expiresIn": 900
  }
}
```

Response mengirim refresh token baru melalui `Set-Cookie` karena refresh token menggunakan rotation.

```http
Set-Cookie: refresh_token=<new-refresh-token>; Max-Age=604800; HttpOnly; Secure; SameSite=None; Path=/api/v1/auth
```

Refresh token sebelumnya menjadi tidak valid setelah rotation berhasil.

### Possible Errors

| Status | Error Code     | Condition                                                          |
| ------ | -------------- | ------------------------------------------------------------------ |
| `401`  | `UNAUTHORIZED` | Refresh token tidak ada, tidak valid, expired, atau telah dicabut. |

---

# Logout

## Endpoint

```http
POST /api/v1/auth/logout
```

### Authentication

Refresh Token Cookie.

### Request

Tidak menggunakan request body.

Refresh token dikirim melalui cookie:

```http
Cookie: refresh_token=<refresh-token>
```

### Success Response

```http
204 No Content
```

Server mencabut refresh token yang sedang digunakan dan menghapus cookie refresh token dari client.

Response header:

```http
Set-Cookie: refresh_token=; Max-Age=0; HttpOnly; Secure; SameSite=None; Path=/api/v1/auth
```

### Possible Errors

| Status | Error Code     | Condition                                      |
| ------ | -------------- | ---------------------------------------------- |
| `401`  | `UNAUTHORIZED` | Refresh token tidak valid atau tidak tersedia. |

---

# Change Password

## Endpoint

```http
PATCH /api/v1/auth/password
```

### Authentication

Required.

```http
Authorization: Bearer <access-token>
```

### Request Body

```json
{
  "currentPassword": "OldPassword123!",
  "newPassword": "NewPassword456!",
  "confirmPassword": "NewPassword456!"
}
```

### Request Fields

| Field             | Type   | Required | Description                 |
| ----------------- | ------ | -------- | --------------------------- |
| `currentPassword` | String | Yes      | Password pengguna saat ini. |
| `newPassword`     | String | Yes      | Password baru.              |
| `confirmPassword` | String | Yes      | Konfirmasi password baru.   |

### Success Response

```http
204 No Content
```

Tidak ada response body.

### Possible Errors

| Status | Error Code     | Condition                                                     |
| ------ | -------------- | ------------------------------------------------------------- |
| `400`  | `BAD_REQUEST`  | Request tidak valid atau password baru tidak memenuhi policy. |
| `401`  | `UNAUTHORIZED` | Access token tidak valid atau current password salah.         |
| `409`  | `CONFLICT`     | Password baru sama dengan password saat ini.                  |

---

# Authentication Headers

## Access Token

Endpoint yang membutuhkan authentication menggunakan:

```http
Authorization: Bearer <access-token>
```

## Refresh Token

Refresh token dikirim secara otomatis melalui cookie:

```http
Cookie: refresh_token=<refresh-token>
```

Refresh token tidak dikirim melalui request body maupun `Authorization` header.

---

# Refresh Token Cookie

| Attribute | Value |
| --------- | ----- |
| Name | `refresh_token` |
| HttpOnly | `true` |
| Secure | `true` |
| SameSite | `None` |
| Path | `/api/v1/auth` |
| Max-Age | `604800` seconds |

---

# Token Response

Access token menggunakan struktur berikut:

| Field         | Type    | Description                            |
| ------------- | ------- | -------------------------------------- |
| `accessToken` | String  | JWT access token.                      |
| `tokenType`   | String  | Selalu `Bearer`.                       |
| `expiresIn`   | Integer | Masa berlaku access token dalam detik. |

Struktur ini digunakan oleh endpoint `Login` dan `Refresh Access Token`.

Access token memiliki masa berlaku:

```text
900 seconds (15 minutes)
```

Refresh token disimpan dalam HttpOnly cookie sehingga tidak dapat dibaca melalui JavaScript pada client.

Refresh token memiliki masa berlaku:

```text
7 days
```

Refresh token tidak pernah dikembalikan melalui response body.

---

# Endpoint Summary

| Method  | Endpoint                | Authentication       | Success          |
| ------- | ----------------------- | -------------------- | ---------------- |
| `POST`  | `/api/v1/auth/register` | Public               | `201 Created`    |
| `POST`  | `/api/v1/auth/login`    | Public               | `200 OK`         |
| `POST`  | `/api/v1/auth/refresh`  | Refresh Token Cookie | `200 OK`         |
| `POST`  | `/api/v1/auth/logout`   | Refresh Token Cookie | `204 No Content` |
| `PATCH` | `/api/v1/auth/password` | Access Token         | `204 No Content` |

---

# Related Documentation

| Document                     | Relation                                               |
| ---------------------------- | ------------------------------------------------------ |
| `functional-requirements.md` | Mendefinisikan functional requirements Authentication. |
| `business-rules.md`          | Mendefinisikan business rules Authentication.          |
| `use-cases.md`               | Mendefinisikan use case Authentication.                |
| `api-design.md`              | Mendefinisikan API conventions dan standard API.       |
