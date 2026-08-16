-- =============================
-- V1: Create users table
-- =============================

CREATE TABLE public.users
(
    id            UUID PRIMARY KEY,
    email         VARCHAR(254) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    is_active     BOOLEAN      DEFAULT TRUE NOT NULL,

    created_at    TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by    UUID         NULL,
    updated_at    TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by    UUID         NULL,
    deleted_at    TIMESTAMPTZ  NULL,
    deleted_by    UUID         NULL
);

CREATE UNIQUE INDEX ux_users_email
    ON public.users (email);

CREATE INDEX idx_users_created_at
    ON public.users (created_at DESC);