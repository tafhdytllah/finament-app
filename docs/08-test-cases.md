# Test Cases

## Authentication

| ID          | Use Case        | Scenario                                | Preconditions                        | Test Steps                                                                                        | Expected Result                                                 |
| ----------- | --------------- | --------------------------------------- | ------------------------------------ | ------------------------------------------------------------------------------------------------- | --------------------------------------------------------------- |
| TC-AUTH-001 | Register        | Register dengan data valid              | Email belum terdaftar                | Kirim request registration dengan fullname, email, password, dan confirmation password yang valid | User berhasil dibuat dan response `201 Created`                 |
| TC-AUTH-002 | Register        | Email sudah terdaftar                   | User dengan email tersebut sudah ada | Kirim request registration menggunakan email yang sama                                            | Registration ditolak dengan `409 Conflict`                      |
| TC-AUTH-003 | Register        | Email tidak valid                       | Tidak ada                            | Kirim request dengan format email tidak valid                                                     | Request ditolak dengan `400 Bad Request`                        |
| TC-AUTH-004 | Register        | Password tidak memenuhi policy          | Tidak ada                            | Kirim password yang tidak memenuhi password policy                                                | Request ditolak dengan `400 Bad Request`                        |
| TC-AUTH-005 | Register        | Confirmation password berbeda           | Tidak ada                            | Kirim password dan confirmation password yang berbeda                                             | Request ditolak dengan `400 Bad Request`                        |
| TC-AUTH-006 | Login           | Credential valid                        | User sudah terdaftar                 | Login menggunakan email dan password yang valid                                                   | Login berhasil dan access token serta refresh token diterbitkan |
| TC-AUTH-007 | Login           | Password salah                          | User sudah terdaftar                 | Login menggunakan password yang salah                                                             | Login ditolak dengan `401 Unauthorized`                         |
| TC-AUTH-008 | Login           | User tidak ditemukan                    | Email belum terdaftar                | Login menggunakan email yang tidak terdaftar                                                      | Login ditolak dengan `401 Unauthorized`                         |
| TC-AUTH-009 | Refresh Token   | Refresh token valid                     | Refresh token masih aktif            | Kirim refresh token yang valid                                                                    | Access token baru diterbitkan dan refresh token dirotasi        |
| TC-AUTH-010 | Refresh Token   | Refresh token sudah revoked             | Token sudah dicabut                  | Kirim refresh token yang sudah revoked                                                            | Request ditolak dengan `401 Unauthorized`                       |
| TC-AUTH-011 | Refresh Token   | Refresh token expired                   | Token sudah expired                  | Kirim refresh token yang sudah expired                                                            | Request ditolak dengan `401 Unauthorized`                       |
| TC-AUTH-012 | Logout          | Logout dengan token valid               | User memiliki refresh token aktif    | Kirim request logout                                                                              | Refresh token dicabut dan logout berhasil                       |
| TC-AUTH-013 | Change Password | Password valid                          | User sudah terautentikasi            | Kirim current password dan new password yang valid                                                | Password berhasil diubah                                        |
| TC-AUTH-014 | Change Password | Current password salah                  | User sudah terautentikasi            | Kirim current password yang salah                                                                 | Request ditolak dengan `401 Unauthorized`                       |
| TC-AUTH-015 | Change Password | Password baru sama dengan password lama | User sudah terautentikasi            | Kirim password baru yang sama dengan current password                                             | Request ditolak dengan `400 Bad Request`                        |
| TC-AUTH-016 | Change Password | Confirmation password berbeda           | User sudah terautentikasi            | Kirim new password dan confirmation password yang berbeda                                         | Request ditolak dengan `400 Bad Request`                        |

## User

| ID          | Use Case       | Scenario                          | Preconditions                | Test Steps                                               | Expected Result                                 |
| ----------- | -------------- | --------------------------------- | ---------------------------- | -------------------------------------------------------- | ----------------------------------------------- |
| TC-USER-001 | View Profile   | Melihat profile sendiri           | User sudah terautentikasi    | Request `GET /api/v1/users/me`                           | Profile user dikembalikan dengan `200 OK`       |
| TC-USER-002 | View Profile   | Tidak terautentikasi              | Tidak ada access token valid | Request `GET /api/v1/users/me`                           | Request ditolak dengan `401 Unauthorized`       |
| TC-USER-003 | Update Profile | Update fullname dengan data valid | User sudah terautentikasi    | Request `PATCH /api/v1/users/me` dengan fullname valid   | Profile berhasil diperbarui dengan `200 OK`     |
| TC-USER-004 | Update Profile | Fullname kosong                   | User sudah terautentikasi    | Request update dengan fullname kosong                    | Request ditolak dengan `400 Bad Request`        |
| TC-USER-005 | Update Profile | Fullname melebihi batas           | User sudah terautentikasi    | Request update dengan fullname lebih dari 100 characters | Request ditolak dengan `400 Bad Request`        |
| TC-USER-006 | Update Profile | Mencoba mengubah email            | User sudah terautentikasi    | Request update mengandung field email                    | Email tidak dapat diubah melalui profile update |

---

# Business Rule Coverage

| Business Rule | Test Case                             |
| ------------- | ------------------------------------- |
| BR-AUTH-001   | TC-AUTH-002                           |
| BR-AUTH-002   | TC-AUTH-003, TC-AUTH-006              |
| BR-AUTH-003   | TC-AUTH-003                           |
| BR-AUTH-004   | TC-AUTH-004                           |
| BR-AUTH-005   | TC-AUTH-004                           |
| BR-AUTH-006   | TC-AUTH-001                           |
| BR-AUTH-007   | TC-AUTH-006, TC-AUTH-007, TC-AUTH-008 |
| BR-AUTH-008   | TC-AUTH-006                           |
| BR-AUTH-009   | TC-AUTH-006                           |
| BR-AUTH-010   | TC-AUTH-012                           |
| BR-AUTH-011   | TC-AUTH-009                           |
| BR-AUTH-012   | TC-AUTH-009                           |
| BR-AUTH-013   | TC-AUTH-009                           |
| BR-AUTH-014   | TC-AUTH-012                           |
| BR-AUTH-015   | TC-AUTH-013                           |
| BR-AUTH-016   | TC-AUTH-014                           |
| BR-AUTH-017   | TC-AUTH-013                           |
| BR-AUTH-018   | TC-AUTH-015                           |
| BR-USER-001   | TC-AUTH-001                           |
| BR-USER-002   | TC-AUTH-001                           |
| BR-USER-003   | TC-USER-004                           |
| BR-USER-004   | TC-USER-005                           |
| BR-USER-005   | TC-USER-001                           |
| BR-USER-006   | TC-USER-003                           |
| BR-USER-007   | TC-USER-006                           |
