CREATE TABLE health_offer (
    id                  bigserial,
    user_id             text,
    title               text,
    description         text,
    mode                text,
    specialization      text,
    city                text,
    region              text,
    language            text,
    modified_date       timestamp not null,
    user_first_name     text,
    status              text default 'ACTIVE',
    phone_number        text,
    phone_country_code  text,
    CONSTRAINT PK_HCO_ID PRIMARY KEY (id)
);

CREATE TABLE health_offer_AUD (
    id                  bigserial,
    user_id             text,
    title               text,
    description         text,
    mode                text,
    specialization      text,
    city                text,
    region              text,
    language            text,
    modified_date       timestamp not null,
    user_first_name     text,
    status              text default 'ACTIVE',
    phone_number        text,
    phone_country_code  text,
    REV                 integer not null,
    REVTYPE             smallint,
    REVEND              integer,
    PRIMARY KEY (id, REV),
    CONSTRAINT FK_health_offer_AUD_REV FOREIGN KEY (REV) REFERENCES revinfo (REV),
    CONSTRAINT FK_health_offer_AUD_REVEND FOREIGN KEY (REVEND) REFERENCES revinfo (REV)
);

CREATE INDEX idx_health_offer_city ON health_offer (upper(city));
CREATE INDEX idx_health_offer_region ON health_offer (upper(region));
CREATE INDEX idx_health_offer_status ON health_offer USING hash(status);
CREATE INDEX idx_health_offer_user_id ON health_offer USING hash(user_id);
CREATE INDEX idx_health_offer_modified_date ON health_offer (modified_date);
CREATE INDEX idx_health_offer_specialization ON health_offer (specialization);