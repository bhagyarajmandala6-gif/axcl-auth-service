--liquibase formatted sql
--changeset axcl:create-tables
CREATE TABLE axcl.o_users (
    id UUID default gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    mobileNumber VARCHAR(50) NOT NULL UNIQUE,
    iso2CountryCode VARCHAR(10) NOT NULL,
    passwordHash VARCHAR(120) NOT NULL,
    gender INTEGER NOT NULL,
    profileImg VARCHAR(500),
    userRole INTEGER NOT NULL,
    userStatus INTEGER NOT NULL,

    lastLogoutAt TIMESTAMP DEFAULT NULL,
    createdOn TIMESTAMP DEFAULT NULL,
    updatedOn TIMESTAMP DEFAULT NULL,
    deletedOn TIMESTAMP DEFAULT NULL,

    createdBy VARCHAR(255) DEFAULT NULL,
    updatedBy VARCHAR(255) DEFAULT NULL,
    deletedBy VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE axcl.o_transportation_provider_profile (
    id UUID default  gen_random_uuid(),
    userId UUID NOT NULL,
    companyName VARCHAR(255),
    licNumber VARCHAR(30),
    address VARCHAR(100),
    city VARCHAR(100),
    state CHAR(2),
    zipCode VARCHAR(10),

    documents TEXT[],  -- array of document URLs or paths

    createdOn TIMESTAMP DEFAULT NULL,
    updatedOn TIMESTAMP DEFAULT NULL,
    deletedOn TIMESTAMP DEFAULT NULL,

    createdBy VARCHAR(255) DEFAULT NULL,
    updatedBy VARCHAR(255) DEFAULT NULL,
    deletedBy VARCHAR(255) DEFAULT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (userId) REFERENCES axcl.o_users(id)
);

CREATE TABLE axcl.o_driver_profile (
    id UUID default  gen_random_uuid(),
    userId UUID NOT NULL,
    transportProviderId UUID NOT NULL,
    firstName VARCHAR(50) NOT NULL,
    middleName VARCHAR(50),
    lastName VARCHAR(50) NOT NULL,
    socialSecurityNumber CHAR(4) NOT NULL,
    gender INTEGER NOT NULL,
    dob DATE NOT NULL,
    userStatus INTEGER NOT NULL,
    phoneNumber VARCHAR(50) NOT NULL UNIQUE,

    address VARCHAR(100),
    city VARCHAR(100),
    state CHAR(2),
    zipCode VARCHAR(10),

    createdOn TIMESTAMP DEFAULT NULL,
    updatedOn TIMESTAMP DEFAULT NULL,
    deletedOn TIMESTAMP DEFAULT NULL,

    createdBy VARCHAR(255) DEFAULT NULL,
    updatedBy VARCHAR(255) DEFAULT NULL,
    deletedBy VARCHAR(255) DEFAULT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (userId) REFERENCES axcl.o_users(id),
    FOREIGN KEY (transportProviderId) REFERENCES axcl.o_transportation_provider_profile(id)
);

CREATE TABLE axcl.o_dmv_license (
    id UUID DEFAULT gen_random_uuid(),
    driverId UUID NOT NULL UNIQUE,
    licenseNumber VARCHAR(256) NOT NULL UNIQUE,
    stateCode CHAR(10) NOT NULL,
    licenseClass VARCHAR(10) NOT NULL,
    documentUrl TEXT,
    effectiveDate DATE NOT NULL,
    expirationDate DATE NOT NULL,

    createdOn TIMESTAMP DEFAULT NULL,
    updatedOn TIMESTAMP DEFAULT NULL,
    deletedOn TIMESTAMP DEFAULT NULL,

    createdBy VARCHAR(255) DEFAULT NULL,
    updatedBy VARCHAR(255) DEFAULT NULL,
    deletedBy VARCHAR(255) DEFAULT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (driverId) REFERENCES axcl.o_driver_profile(id)
);

CREATE TABLE axcl.o_extra_commercial_license (
    id UUID DEFAULT gen_random_uuid(),
    driverId UUID NOT NULL,
    licenseNumber VARCHAR(256) NOT NULL UNIQUE,
    typeId INTEGER NOT NULL,
    documentUrl TEXT,
    effectiveDate DATE NOT NULL,
    expirationDate DATE NOT NULL,

    createdOn TIMESTAMP DEFAULT NULL,
    updatedOn TIMESTAMP DEFAULT NULL,
    deletedOn TIMESTAMP DEFAULT NULL,

    createdBy VARCHAR(255) DEFAULT NULL,
    updatedBy VARCHAR(255) DEFAULT NULL,
    deletedBy VARCHAR(255) DEFAULT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (driverId) REFERENCES axcl.o_driver_profile(id)
);

CREATE TABLE axcl.o_background_check (
    id UUID DEFAULT gen_random_uuid(),
    driverId UUID NOT NULL UNIQUE,
    documentUrl TEXT,
    effectiveDate DATE NOT NULL,
    expirationDate DATE NOT NULL,

    createdOn TIMESTAMP DEFAULT NULL,
    updatedOn TIMESTAMP DEFAULT NULL,
    deletedOn TIMESTAMP DEFAULT NULL,

    createdBy VARCHAR(255) DEFAULT NULL,
    updatedBy VARCHAR(255) DEFAULT NULL,
    deletedBy VARCHAR(255) DEFAULT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (driverId) REFERENCES axcl.o_driver_profile(id)
);

CREATE TABLE axcl.o_vehicle (
    id UUID default  gen_random_uuid(),
    transportProviderId UUID NOT NULL,
    vin VARCHAR(50) NOT NULL UNIQUE,
        -- Must be unique for vehicles with status_id = 1
        -- Cannot contain I, i, O, o, Q, q (enforced via application/later rule)
        -- Cannot be updated

    fleetNumber VARCHAR(50) NOT NULL,
    productionYear INTEGER NOT NULL,
    typeId INTEGER NOT NULL,
        -- 1 - Livery, 3 - Ambulette, 6 - Ambulance

    make VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    color VARCHAR(50) NOT NULL,
    seatNumber INTEGER NOT NULL,
    statusId INTEGER NOT NULL,

    -- DMV Registration
    dmvLicensePlateNumber VARCHAR(256),
        -- Cannot be updated
        -- Allows letters, digits, spaces

    dmvLicensePlateCategoryId INTEGER NOT NULL,
        -- 1 - Non-Commercial
        -- 2 - Commercial - Livery (DOT)
        -- 3 - Commercial - TLC
        -- 4 - Commercial - BUS (DOT)
        -- 5 - Commercial - Ambulance

    dmvStateCode CHAR(20) NOT NULL,
        -- US state abbreviation (e.g., NY)

    dmvEffectiveDate DATE NOT NULL,
    dmvExpirationDate DATE NOT NULL,
        -- effective_date < expiration_date should be enforced via trigger or application logic

    dmvDocumentUrl TEXT,

    -- Extra Commercial License (optional)
    extraLicenseNumber VARCHAR(100),
    extraTypeId INTEGER CHECK (extraTypeId IN (1, 2, 3)),
        -- 1 - For-Hire Vehicle (FHV)
        -- 2 - Paratransit (PR)
        -- 3 - EMT

    extraEffectiveDate DATE,
    extraExpirationDate DATE,
        -- effective_date < expiration_date

    extraDocumentUrl TEXT,

    -- Insurance
    insurancePolicyNumber VARCHAR(250) NOT NULL,
        -- Allows letters and digits

    insuranceInsurerName VARCHAR(255) NOT NULL,

    insuranceEffectiveDate DATE NOT NULL,
    insuranceExpirationDate DATE NOT NULL,
        -- effective_date < expiration_date

    insuranceDocumentUrl TEXT NOT NULL,

    -- Inspection
    inspectionEffectiveDate DATE NOT NULL,
    inspectionExpirationDate DATE NOT NULL,
        -- effective_date < expiration_date

    inspectionDocumentUrl TEXT NOT NULL,

    createdOn TIMESTAMP DEFAULT NULL,
    updatedOn TIMESTAMP DEFAULT NULL,
    deletedOn TIMESTAMP DEFAULT NULL,

    createdBy VARCHAR(255) DEFAULT NULL,
    updatedBy VARCHAR(255) DEFAULT NULL,
    deletedBy VARCHAR(255) DEFAULT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (transportProviderId) REFERENCES axcl.o_transportation_provider_profile(id)
);
