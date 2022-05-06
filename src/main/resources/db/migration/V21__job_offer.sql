CREATE TABLE job_offer (
    id                  bigserial,
    user_id             text,
    title               text,
    description         text,
    mode                text,
    city                text,
    region              text,
    industry            text,
    work_time           text,
    contract_type       text,
    language            text,
    modified_date       timestamp not null,
    user_first_name     text,
    status              text default 'ACTIVE',
    phone_number        text,
    phone_country_code  text,
    CONSTRAINT PK_JO_ID PRIMARY KEY (id)
);

CREATE TABLE job_offer_AUD (
    id                  bigserial,
    user_id             text,
    title               text,
    description         text,
    mode                text,
    city                text,
    region              text,
    industry            text,
    work_time           text,
    contract_type       text,
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
    CONSTRAINT FK_job_offer_AUD_REV FOREIGN KEY (REV) REFERENCES revinfo (REV),
    CONSTRAINT FK_job_offer_AUD_REVEND FOREIGN KEY (REVEND) REFERENCES revinfo (REV)
);

CREATE INDEX idx_job_offer_city ON job_offer (upper(city));
CREATE INDEX idx_job_offer_region ON job_offer (upper(region));
CREATE INDEX idx_job_offer_status ON job_offer USING hash(status);
CREATE INDEX idx_job_offer_user_id ON job_offer USING hash(user_id);
CREATE INDEX idx_job_offer_modified_date ON job_offer (modified_date);
CREATE INDEX idx_job_offer_mode ON job_offer (mode);

