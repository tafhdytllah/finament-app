# Class Diagram

## Authentication & User

```mermaid
classDiagram

    class User {
        +String fullname
        +String email
        +String passwordHash
        +updateProfile()
        +changePassword()
    }

    class RefreshToken {
        +String tokenHash
        +Instant expiresAt
        +Instant revokedAt
        +isValid()
        +revoke()
    }

    User "1" --> "0..*" RefreshToken : owns

    ```text
User
├── id
├── fullname
├── email
└── passwordHash

RefreshToken
├── id
├── tokenHash
├── expiresAt
└── revokedAt