create table job_offer
(
    id            bigserial,
    user_id       text,
    title         text,
    mode          text,
    city          text,
    region        text,
    description   text,
    modified_date timestamp not null,
    status        text not null default 'ACTIVE',
    CONSTRAINT PK_JO_ID PRIMARY KEY (ID)
);

create table job_offer_type
(
    job_offer_id bigint,
    type         text
);

create index idx_job_offer_modified_date
    ON job_offer (modified_date);

create table job_offer_language
(
    job_offer_id bigint,
    language     text
);

create table job_offer_language_AUD
(
    job_offer_id bigint,
    language     text,
    REV          INTEGER NOT NULL,
    REVTYPE      SMALLINT,
    REVEND       INTEGER,
    PRIMARY KEY (language, job_offer_id, REV),
    CONSTRAINT FK_job_offer_language_AUD_REV
        FOREIGN KEY (rev)
            REFERENCES revinfo (rev),
    CONSTRAINT FK_job_offer_language_AUD_REVEND
        FOREIGN KEY (revend)
            REFERENCES revinfo (rev)
);

create table job_offer_type_AUD
(
    job_offer_id bigint,
    type         text,
    REV          INTEGER NOT NULL,
    REVTYPE      SMALLINT,
    REVEND       INTEGER,
    PRIMARY KEY (type, job_offer_id, REV),
    CONSTRAINT FK_job_offer_type_AUD_REV
        FOREIGN KEY (rev)
            REFERENCES revinfo (rev),
    CONSTRAINT FK_job_offer_type_AUD_REVEND
        FOREIGN KEY (revend)
            REFERENCES revinfo (rev)
);

CREATE TABLE job_offer_AUD
(
    id            bigserial,
    user_id       text,
    title         text,
    mode          text,
    city          text,
    region        text,
    description   text,
    modified_date timestamp,
    status        text,
    REV           INTEGER NOT NULL,
    REVTYPE       SMALLINT,
    REVEND        INTEGER,
    PRIMARY KEY (id, REV),
    CONSTRAINT FK_job_offer_AUD_REV
        FOREIGN KEY (rev)
            REFERENCES revinfo (rev),
    CONSTRAINT FK_job_offer_AUD_REVEND
        FOREIGN KEY (revend)
            REFERENCES revinfo (rev)
);

