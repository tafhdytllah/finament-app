# Business Rules

## Authentication

| ID          | Rule |
|-------------|------|
| BR-AUTH-001 | Email pengguna harus unik. |
| BR-AUTH-002 | Email harus diperlakukan secara case-insensitive. |
| BR-AUTH-003 | Email harus memiliki format yang valid dan panjang maksimal 254 karakter. |
| BR-AUTH-004 | Password harus memiliki panjang antara 8 dan 72 karakter serta mengandung minimal satu huruf besar, satu huruf kecil, satu angka, dan satu karakter khusus. |
| BR-AUTH-005 | Password dan confirmation password saat registrasi harus sama. |
| BR-AUTH-006 | Password tidak boleh disimpan dalam bentuk plaintext. |
| BR-AUTH-007 | Pengguna hanya dapat melakukan login menggunakan kredensial yang valid. |
| BR-AUTH-008 | Access token harus memiliki masa berlaku 15 menit. |
| BR-AUTH-009 | Refresh token harus memiliki masa berlaku 7 hari. |
| BR-AUTH-010 | Refresh token harus dapat dicabut sebelum masa berlakunya berakhir. |
| BR-AUTH-011 | Refresh token harus disimpan dalam bentuk hash dan tidak boleh disimpan dalam bentuk plaintext. |
| BR-AUTH-012 | Refresh token harus dirotasi ketika digunakan untuk memperoleh access token baru. |
| BR-AUTH-013 | Refresh token lama harus menjadi tidak valid setelah berhasil dirotasi. |
| BR-AUTH-014 | Setelah logout, refresh token pada sesi tersebut harus dicabut. |
| BR-AUTH-015 | Pengguna harus terautentikasi untuk mengubah password. |
| BR-AUTH-016 | Pengguna harus memberikan password saat ini yang valid sebelum mengubah password. |
| BR-AUTH-017 | Password baru harus memenuhi password policy yang sama dengan password saat registrasi. |
| BR-AUTH-018 | Password baru dan confirmation password harus sama. |
| BR-AUTH-019 | Password baru tidak boleh sama dengan password saat ini. |

## User

| ID          | Rule |
|-------------|------|
| BR-USER-001 | Setiap pengguna harus memiliki identifier yang unik. |
| BR-USER-002 | Fullname wajib diisi saat registrasi. |
| BR-USER-003 | Fullname tidak boleh kosong atau hanya terdiri dari whitespace. |
| BR-USER-004 | Fullname harus memiliki panjang antara 2 dan 100 karakter. |
| BR-USER-005 | Pengguna hanya dapat melihat profil miliknya sendiri. |
| BR-USER-006 | Pengguna hanya dapat memperbarui profil miliknya sendiri. |
| BR-USER-007 | Email tidak dapat diubah melalui profile update. |