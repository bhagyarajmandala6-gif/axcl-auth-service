--liquibase formatted sql
--changeset axcl:create-tables
CREATE TABLE public.o_users (
    id UUID,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    mobile_number VARCHAR(20) NOT NULL UNIQUE,
    iso2_country_code VARCHAR(2) NOT NULL,
    password_hash VARCHAR(120) NOT NULL,
    gender INTEGER NOT NULL,
    profile_img VARCHAR(100),
    user_role INTEGER NOT NULL,
    user_status INTEGER NOT NULL,

    last_logout_at TIMESTAMP DEFAULT NULL,
    created_on TIMESTAMP DEFAULT NULL,
    updated_on TIMESTAMP DEFAULT NULL,
    deleted_on TIMESTAMP DEFAULT NULL,

    created_by UUID DEFAULT NULL,
    updated_by UUID DEFAULT NULL,
    deleted_by UUID DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE public.o_transportation_provider_profile (
    id UUID,
    user_id UUID NOT NULL,
    company_name VARCHAR(100),
    lic_number VARCHAR(100),
    address VARCHAR(100),
    city VARCHAR(100),
    state CHAR(10),
    zip_code VARCHAR(10),

    documents TEXT[],  -- array of document URLs or paths

    created_on TIMESTAMP DEFAULT NULL,
    updated_on TIMESTAMP DEFAULT NULL,
    deleted_on TIMESTAMP DEFAULT NULL,

    created_by UUID DEFAULT NULL,
    updated_by UUID DEFAULT NULL,
    deleted_by UUID DEFAULT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES public.o_users(id)
);

CREATE TABLE public.o_driver_profile (
    id UUID,
    user_id UUID NOT NULL,
    transport_provider_id UUID NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    last_name VARCHAR(100) NOT NULL,
    social_security_number CHAR(4) NOT NULL,
    gender INTEGER NOT NULL,
    dob DATE NOT NULL,
    user_status INTEGER NOT NULL,
    phone_number VARCHAR(50) NOT NULL UNIQUE,

    address VARCHAR(100),
    city VARCHAR(100),
    state CHAR(2),
    zip_code VARCHAR(10),

    created_on TIMESTAMP DEFAULT NULL,
    updated_on TIMESTAMP DEFAULT NULL,
    deleted_on TIMESTAMP DEFAULT NULL,

    created_by UUID DEFAULT NULL,
    updated_by UUID DEFAULT NULL,
    deleted_by UUID DEFAULT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES public.o_users(id),
    FOREIGN KEY (transport_provider_id) REFERENCES public.o_transportation_provider_profile(id)
);

CREATE TABLE public.o_dmv_license (
    id UUID,
    driver_id UUID NOT NULL UNIQUE,
    license_number VARCHAR(256) NOT NULL UNIQUE,
    state_code CHAR(10) NOT NULL,
    license_class VARCHAR(10) NOT NULL,
    document_url TEXT,
    effective_date DATE NOT NULL,
    expiration_date DATE NOT NULL,
    endorsements TEXT[],  -- array of endorsements URLs or paths
    restrictions TEXT[],  -- array of restrictions URLs or paths

    created_on TIMESTAMP DEFAULT NULL,
    updated_on TIMESTAMP DEFAULT NULL,
    deleted_on TIMESTAMP DEFAULT NULL,

    created_by UUID DEFAULT NULL,
    updated_by UUID DEFAULT NULL,
    deleted_by UUID DEFAULT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (driver_id) REFERENCES public.o_driver_profile(id)
);

CREATE TABLE public.o_extra_commercial_license (
    id UUID,
    driver_id UUID NOT NULL,
    license_number VARCHAR(256) NOT NULL,
    type_id INTEGER NOT NULL,
    document_url TEXT,
    effective_date DATE NOT NULL,
    expiration_date DATE NOT NULL,

    created_on TIMESTAMP DEFAULT NULL,
    updated_on TIMESTAMP DEFAULT NULL,
    deleted_on TIMESTAMP DEFAULT NULL,

    created_by UUID DEFAULT NULL,
    updated_by UUID DEFAULT NULL,
    deleted_by UUID DEFAULT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (driver_id) REFERENCES public.o_driver_profile(id)
);

CREATE TABLE public.o_background_check (
    id UUID,
    driver_id UUID NOT NULL UNIQUE,
    document_url TEXT,
    effective_date DATE NOT NULL,
    expiration_date DATE NOT NULL,

    created_on TIMESTAMP DEFAULT NULL,
    updated_on TIMESTAMP DEFAULT NULL,
    deleted_on TIMESTAMP DEFAULT NULL,

    created_by UUID DEFAULT NULL,
    updated_by UUID DEFAULT NULL,
    deleted_by UUID DEFAULT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (driver_id) REFERENCES public.o_driver_profile(id)
);

CREATE TABLE public.o_vehicle (
    id UUID,
    transport_provider_id UUID NOT NULL,
    vin VARCHAR(50) NOT NULL UNIQUE,
        -- Must be unique for vehicles with status_id = 1
        -- Cannot contain I, i, O, o, Q, q (enforced via application/later rule)
        -- Cannot be updated

    fleet_number VARCHAR(50) NOT NULL,
    production_year INTEGER NOT NULL,
    type_id INTEGER NOT NULL,
        -- 1 - Livery, 3 - Ambulette, 6 - Ambulance

    make VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    color VARCHAR(50) NOT NULL,
    seat_number INTEGER NOT NULL,
    status_id INTEGER NOT NULL,

    -- DMV Registration
    dmv_license_plate_number VARCHAR(256),
        -- Cannot be updated
        -- Allows letters, digits, spaces

    dmv_license_plate_category_id INTEGER NOT NULL,
        -- 1 - Non-Commercial
        -- 2 - Commercial - Livery (DOT)
        -- 3 - Commercial - TLC
        -- 4 - Commercial - BUS (DOT)
        -- 5 - Commercial - Ambulance

    dmv_state_code CHAR(20) NOT NULL,
        -- US state abbreviation (e.g., NY)

    dmv_effective_date DATE NOT NULL,
    dmv_expiration_date DATE NOT NULL,
        -- effective_date < expiration_date should be enforced via trigger or application logic

    dmv_document_url TEXT,

    -- Extra Commercial License (optional)
    extra_license_number VARCHAR(100),
    extra_type_id INTEGER CHECK (extra_type_id IN (1, 2, 3)),
        -- 1 - For-Hire Vehicle (FHV)
        -- 2 - Paratransit (PR)
        -- 3 - EMT

    extra_effective_date DATE,
    extra_expiration_date DATE,
        -- effective_date < expiration_date

    extra_document_url TEXT,

    -- Insurance
    insurance_policy_number VARCHAR(250) NOT NULL,
        -- Allows letters and digits

    insurance_insurer_name VARCHAR(255) NOT NULL,

    insurance_effective_date DATE NOT NULL,
    insurance_expiration_date DATE NOT NULL,
        -- effective_date < expiration_date

    insurance_document_url TEXT NOT NULL,

    -- Inspection
    inspection_effective_date DATE NOT NULL,
    inspection_expiration_date DATE NOT NULL,
        -- effective_date < expiration_date

    inspection_document_url TEXT NOT NULL,

    created_on TIMESTAMP DEFAULT NULL,
    updated_on TIMESTAMP DEFAULT NULL,
    deleted_on TIMESTAMP DEFAULT NULL,

    created_by UUID DEFAULT NULL,
    updated_by UUID DEFAULT NULL,
    deleted_by UUID DEFAULT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (transport_provider_id) REFERENCES public.o_transportation_provider_profile(id)
);

CREATE TABLE public.o_auth_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),  -- use gen_random_uuid() from pgcrypto

    refresh_token VARCHAR(1024) NOT NULL UNIQUE,
    user_id VARCHAR(256) NOT NULL,

    expires_at TIMESTAMP,

    created_on TIMESTAMP DEFAULT NULL,
    updated_on TIMESTAMP DEFAULT NULL,
    deleted_on TIMESTAMP DEFAULT NULL,

    created_by UUID DEFAULT NULL,
    updated_by UUID DEFAULT NULL,
    deleted_by UUID DEFAULT NULL
);

