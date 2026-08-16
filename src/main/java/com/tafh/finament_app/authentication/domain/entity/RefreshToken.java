package com.tafh.finament_app.authentication.domain.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class RefreshToken {

    private final UUID id;
    private final UUID userId;
    private final String tokenHash;
    private final LocalDateTime expiresAt;
    private final LocalDateTime createdAt;

    private LocalDateTime revokedAt;

    public RefreshToken(
            UUID id,
            UUID userId,
            String tokenHash,
            LocalDateTime expiresAt,
            LocalDateTime createdAt
    ) {
        this.id = Objects.requireNonNull(id);
        this.userId = Objects.requireNonNull(userId);
        this.tokenHash = Objects.requireNonNull(tokenHash);
        this.expiresAt = Objects.requireNonNull(expiresAt);
        this.createdAt = Objects.requireNonNull(createdAt);

        if (tokenHash.isBlank()) {
            throw new IllegalArgumentException("tokenHash must not be blank");
        }

        if (!createdAt.isBefore(expiresAt)) {
            throw new IllegalArgumentException("expiresAt must be after createdAt");
        }
     }

    public boolean isExpired(LocalDateTime now) {
        Objects.requireNonNull(now);
        return !now.isBefore(this.expiresAt);
    }

    public boolean isRevoked() {
        return this.revokedAt != null;
    }

    public boolean isActive(LocalDateTime now) {
        Objects.requireNonNull(now);

        return !this.isRevoked() && !this.isExpired(now);
    }

    public void revoke(LocalDateTime revokedAt) {
        Objects.requireNonNull(revokedAt);

        if (this.revokedAt != null) {
            return;
        }

        this.revokedAt = revokedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getRevokedAt() {
        return revokedAt;
    }
}
