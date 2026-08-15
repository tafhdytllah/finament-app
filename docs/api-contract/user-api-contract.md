# User API Contract

## Overview

Dokumen ini mendefinisikan kontrak API untuk module User pada Finament.

| Item | Specification |
| --- | --- |
| Base Path | `/api/v1/users` |
| Authentication | Access Token |
| Content Type | `application/json` |

---

# Get My Profile

## Endpoint

```text
GET /api/v1/users/me
```

### Authentication

Required.

```text
Authorization: Bearer <access-token>
```

### Request

Tidak menggunakan request body.

### Success Response

```text
200 OK
```

Response body:
```json
{
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "fullname": "Taufik",
    "email": "taufik@example.com"
  }
}
```

### Response Fields

| Field | Type | Description |
| --- | --- | --- |
| `id` | String | Unique identifier user dalam format UUID. |
| `fullname` | String | Nama lengkap pengguna. |
| `email` | String | Email pengguna. |

### Possible Errors

| Status | Error Code | Condition |
| --- | --- | --- |
| `401` | `UNAUTHORIZED` | Access token tidak ada, tidak valid, atau expired. |
| `404` | `NOT_FOUND` | User tidak ditemukan. |

---

# Update My Profile

## Endpoint

```text
PATCH /api/v1/users/me
```

### Authentication

Required.

```text
Authorization: Bearer <access-token>
```

### Request Body

```json
{
  "fullname": "Taufik Ahmad"
}
```

### Request Fields

| Field | Type | Required | Description |
| --- | --- | --- | --- |
| `fullname` | String | No | Nama lengkap pengguna. |

Field yang tidak dikirim tidak akan diubah.

### Success Response

```text
200 OK
```

Response body:

```json
{
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "fullname": "Taufik Ahmad",
    "email": "taufik@example.com"
  }
}
```

### Possible Errors

| Status | Error Code | Condition |
| --- | --- | --- |
| `400` | `BAD_REQUEST` | Request tidak valid atau gagal validasi. |
| `401` | `UNAUTHORIZED` | Access token tidak ada, tidak valid, atau expired. |
| `404` | `NOT_FOUND` | User tidak ditemukan. |

---

# Validation Rules

## Update Profile

| Field | Rule |
| --- | --- |
| `fullname` | Jika dikirim, wajib 2–100 characters dan tidak boleh kosong atau hanya whitespace. |

---

# Endpoint Summary

| Method | Endpoint | Authentication | Success |
| --- | --- | --- | --- |
| `GET` | `/api/v1/users/me` | Access Token | `200 OK` |
| `PATCH` | `/api/v1/users/me` | Access Token | `200 OK` |

---

# Related Documentation

| Document | Relation |
| --- | --- |
| `functional-requirements.md` | Mendefinisikan functional requirements User. |
| `business-rules.md` | Mendefinisikan business rules User. |
| `use-cases.md` | Mendefinisikan use case User. |
| `api-design.md` | Mendefinisikan API conventions dan standard API. |
| `authentication-api-contract.md` | Mendefinisikan authentication dan token contract. |