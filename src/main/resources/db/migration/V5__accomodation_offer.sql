create table accommodation_offer (
    id              bigserial,
    user_id         text,
    title           text,
    city            text,
    region          text,
    description     text,
    guests          integer,
    length_of_stay  text,
    modified_date   timestamp not null,
    status          text,
    CONSTRAINT PK_AO_ID PRIMARY KEY (ID)
);

create index idx_accommodation_offer_modified_date
ON accommodation_offer(modified_date);

create table accommodation_offer_host_language (
    accommodation_offer_id bigint,
    host_language text
);

create table accommodation_offer_host_language_AUD (
    accommodation_offer_id bigint,
    host_language text,
    REV         INTEGER NOT NULL,
    REVTYPE     SMALLINT,
    REVEND      INTEGER,
    PRIMARY KEY ( host_language, accommodation_offer_id, REV ),
    CONSTRAINT FK_accommodation_offer_host_language_AUD_REV
        FOREIGN KEY(rev)
            REFERENCES revinfo(rev),
    CONSTRAINT FK_accommodation_offer_host_language_AUD_REVEND
        FOREIGN KEY(revend)
            REFERENCES revinfo(rev)
);

CREATE TABLE accommodation_offer_AUD
(
    id              bigserial,
    user_id         text,
    title           text,
    city            text,
    region          text,
    description     text,
    guests          integer,
    length_of_stay  text,
    modified_date   timestamp,
    status          text,
    REV             INTEGER NOT NULL,
    REVTYPE         SMALLINT,
    REVEND          INTEGER,
    PRIMARY KEY ( id, REV ),
    CONSTRAINT FK_accommodation_offer_AUD_REV
        FOREIGN KEY(rev)
    	    REFERENCES revinfo(rev),
    CONSTRAINT FK_accommodation_offer_AUD_REVEND
        FOREIGN KEY(revend)
            REFERENCES revinfo(rev)
);
