CREATE TABLE translation_offer (
    id                  bigserial,
    user_id             text,
    title               text,
    description         text,
    mode                text,
    city                text,
    region              text,
    language            text,
    modified_date       timestamp not null,
    user_first_name     text,
    status              text default 'ACTIVE',
    phone_number        text,
    phone_country_code  text,
    CONSTRAINT PK_TO_ID PRIMARY KEY (id)
);

CREATE TABLE translation_offer_AUD (
    id                  bigserial,
    user_id             text,
    title               text,
    description         text,
    mode                text,
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
    CONSTRAINT FK_translation_offer_AUD_REV FOREIGN KEY (REV) REFERENCES revinfo (REV),
    CONSTRAINT FK_translation_offer_AUD_REVEND FOREIGN KEY (REVEND) REFERENCES revinfo (REV)
);

CREATE INDEX idx_translation_offer_city ON translation_offer (upper(city));
CREATE INDEX idx_translation_offer_region ON translation_offer (upper(region));
CREATE INDEX idx_translation_offer_status ON translation_offer USING hash(status);
CREATE INDEX idx_translation_offer_user_id ON translation_offer USING hash(user_id);
CREATE INDEX idx_translation_offer_modified_date ON translation_offer (modified_date);
CREATE INDEX idx_translation_offer_mode ON translation_offer USING gist(mode gist_trgm_ops);
CREATE INDEX idx_translation_offer_language ON translation_offer USING gist(language gist_trgm_ops);