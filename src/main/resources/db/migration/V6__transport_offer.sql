create table transport_offer
(
    id                 bigserial,
    user_id            text,
    title              text,
    description        text,
    origin_region      text,
    origin_city        text,
    destination_region text,
    destination_city   text,
    capacity           int,
    transport_date     date,
    modified_date      timestamp not null,
    CONSTRAINT PK_TRO_ID PRIMARY KEY (ID)
);

create index idx_transport_offer_modified_date
    ON transport_offer (modified_date);

create table transport_offer_language_AUD
(
    transport_offer_id bigint,
    language           text,
    REV                INTEGER NOT NULL,
    REVTYPE            SMALLINT,
    REVEND             INTEGER,
    PRIMARY KEY (language, transport_offer_id, REV),
    CONSTRAINT FK_transport_offer_language_AUD_REV
        FOREIGN KEY (rev)
            REFERENCES revinfo (rev),
    CONSTRAINT FK_transport_offer_language_AUD_REVEND
        FOREIGN KEY (revend)
            REFERENCES revinfo (rev)
);

CREATE TABLE transport_offer_AUD
(
    id                 bigserial,
    user_id            text,
    title              text,
    description        text,
    origin_region      text,
    origin_city        text,
    destination_region text,
    destination_city   text,
    capacity           int,
    transport_date     date,
    modified_date      timestamp,
    REV                INTEGER NOT NULL,
    REVTYPE            SMALLINT,
    REVEND             INTEGER,
    PRIMARY KEY (id, REV),
    CONSTRAINT FK_transport_offer_AUD_REV
        FOREIGN KEY (rev)
            REFERENCES revinfo (rev),
    CONSTRAINT FK_transport_offer_AUD_REVEND
        FOREIGN KEY (revend)
            REFERENCES revinfo (rev)
);
