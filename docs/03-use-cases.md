# Use Cases

## Authentication

| ID          | Actor | Use Case             | Description |
|-------------|-------|----------------------|-------------|
| UC-AUTH-001 | User  | Register             | User membuat akun menggunakan fullname, email, password, dan confirmation password. |
| UC-AUTH-002 | User  | Login                | User melakukan autentikasi menggunakan email dan password. |
| UC-AUTH-003 | User  | Logout               | User mengakhiri sesi autentikasi dengan mencabut refresh token. |
| UC-AUTH-004 | User  | Refresh Access Token | User memperoleh access token baru menggunakan refresh token yang valid. |
| UC-AUTH-005 | User  | Change Password      | User mengubah password dengan memberikan password saat ini, password baru, dan confirmation password. |

## User

| ID          | Actor | Use Case             | Description |
|-------------|-------|----------------------|-------------|
| UC-USER-001 | User  | View Profile         | User melihat informasi profil miliknya. |
| UC-USER-002 | User  | Update Profile       | User memperbarui fullname pada profil miliknya. |