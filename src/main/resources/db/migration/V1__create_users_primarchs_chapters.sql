CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS app_users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS primarchs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    legion_number INT NOT NULL UNIQUE CHECK (legion_number BETWEEN 1 AND 20),
    allegiance VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN',
    status VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN',
    homeworld VARCHAR(100),
    description TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS chapters (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    founding INT NOT NULL CHECK (founding >= 1),
    primarch_id UUID NOT NULL REFERENCES primarchs(id) ON DELETE RESTRICT,
    homeworld VARCHAR(100),
    fortress_monastery VARCHAR(120),
    codex_compliant BOOLEAN NOT NULL DEFAULT TRUE,
    approx_strength INT CHECK (approx_strength IS NULL OR approx_strength >= 0),
    description TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_chapters_primarch_id ON chapters(primarch_id);
CREATE INDEX IF NOT EXISTS idx_primarchs_allegiance ON primarchs(allegiance);
