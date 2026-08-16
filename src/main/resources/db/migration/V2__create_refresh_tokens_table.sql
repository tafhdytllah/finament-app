-- =============================
-- V2: Create refresh_tokens table
-- =============================

CREATE TABLE public.refresh_tokens
(
    id         UUID PRIMARY KEY,
    user_id    UUID                                NOT NULL,
    token_hash CHAR(64)                            NOT NULL,
    expires_at TIMESTAMP                           NOT NULL,
    revoked_at TIMESTAMP                           NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by UUID                                NULL,

    CONSTRAINT fk_refresh_tokens_user
        FOREIGN KEY (user_id)
            REFERENCES public.users (id)
            ON DELETE CASCADE
);

CREATE UNIQUE INDEX ux_refresh_tokens_token_hash
    ON public.refresh_tokens (token_hash);

CREATE INDEX idx_refresh_tokens_user_id
    ON public.refresh_tokens (user_id);

CREATE INDEX idx_refresh_tokens_expires_at
    ON public.refresh_tokens (expires_at);