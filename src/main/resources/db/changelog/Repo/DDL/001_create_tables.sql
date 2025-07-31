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


