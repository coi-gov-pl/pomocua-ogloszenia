create table translation_offer (
    id          bigserial,
    user_id     text,
    title       text,
    mode        text,
    sworn       bool,
    city        text,
    region      text,
    description text
);

create table translation_offer_language (
    translation_offer_id bigint,
    language             text
);


create table translation_offer_language_AUD (
    translation_offer_id bigint,
    language text,
    REV         INTEGER NOT NULL,
    REVTYPE     SMALLINT,
    REVEND      INTEGER,
    PRIMARY KEY ( language, translation_offer_id, REV ),
    CONSTRAINT FK_translation_offer_language_AUD_REV
        FOREIGN KEY(rev)
            REFERENCES revinfo(rev),
    CONSTRAINT FK_translation_offer_language_AUD_REVEND
        FOREIGN KEY(revend)
            REFERENCES revinfo(rev)
);

CREATE TABLE translation_offer_AUD
(
    id          bigserial,
    user_id     text,
    title       text,
    mode        text,
    sworn       bool,
    city        text,
    region      text,
    description text,
    REV         INTEGER NOT NULL,
    REVTYPE     SMALLINT,
    REVEND      INTEGER,
    PRIMARY KEY ( id, REV ),
    CONSTRAINT FK_translation_offer_AUD_REV
        FOREIGN KEY(rev)
    	    REFERENCES revinfo(rev),
    CONSTRAINT FK_translation_offer_AUD_REVEND
        FOREIGN KEY(revend)
            REFERENCES revinfo(rev)
);