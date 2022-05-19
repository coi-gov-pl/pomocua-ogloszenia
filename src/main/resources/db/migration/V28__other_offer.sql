CREATE TABLE other_offer (
    id                  bigserial,
    user_id             text,
    title               text,
    description         text,
    city                text,
    region              text,
    modified_date       timestamp not null,
    user_first_name     text,
    status              text default 'ACTIVE',
    phone_number        text,
    phone_country_code  text,
    CONSTRAINT PK_OO_ID PRIMARY KEY (id)
);

CREATE TABLE other_offer_AUD (
    id                  bigserial,
    user_id             text,
    title               text,
    description         text,
    city                text,
    region              text,
    modified_date       timestamp not null,
    user_first_name     text,
    status              text default 'ACTIVE',
    phone_number        text,
    phone_country_code  text,
    REV                 integer not null,
    REVTYPE             smallint,
    REVEND              integer,
    PRIMARY KEY (id, REV),
    CONSTRAINT FK_other_offer_AUD_REV FOREIGN KEY (REV) REFERENCES revinfo (REV),
    CONSTRAINT FK_other_offer_AUD_REVEND FOREIGN KEY (REVEND) REFERENCES revinfo (REV)
);

CREATE INDEX idx_other_offer_city ON other_offer (upper(city));
CREATE INDEX idx_other_offer_region ON other_offer (upper(region));
CREATE INDEX idx_other_offer_status ON other_offer USING hash(status);
CREATE INDEX idx_other_offer_user_id ON other_offer USING hash(user_id);
CREATE INDEX idx_other_offer_modified_date ON other_offer (modified_date);
