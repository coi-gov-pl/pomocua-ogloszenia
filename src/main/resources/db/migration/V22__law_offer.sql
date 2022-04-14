CREATE TABLE law_offer (
    id                  bigserial,
    user_id             text,
    title               text,
    description         text,
    city                text,
    region              text,
    help_mode           text,
    help_kind           text,
    language            text,
    modified_date       timestamp not null,
    user_first_name     text,
    status              text default 'ACTIVE',
    phone_number        text,
    phone_country_code  text,
    CONSTRAINT PK_LO_ID PRIMARY KEY (id)
);

CREATE TABLE law_offer_AUD (
    id                  bigserial,
    user_id             text,
    title               text,
    description         text,
    city                text,
    region              text,
    help_mode           text,
    help_kind           text,
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
    CONSTRAINT FK_law_offer_AUD_REV FOREIGN KEY (REV) REFERENCES revinfo (REV),
    CONSTRAINT FK_law_offer_AUD_REVEND FOREIGN KEY (REVEND) REFERENCES revinfo (REV)
);

CREATE INDEX idx_law_offer_city ON law_offer (upper(city));
CREATE INDEX idx_law_offer_region ON law_offer (upper(region));
CREATE INDEX idx_law_offer_status ON law_offer USING hash(status);
CREATE INDEX idx_law_offer_user_id ON law_offer USING hash(user_id);
CREATE INDEX idx_law_offer_modified_date ON law_offer (modified_date);

