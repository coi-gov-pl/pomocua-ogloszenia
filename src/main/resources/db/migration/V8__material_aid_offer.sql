create table material_aid_offer (
    id              bigserial,
    user_id         text,
    title           text,
    description     text,
    category        text,
    city            text,
    region          text,

    CONSTRAINT PK_MAO_ID PRIMARY KEY (ID)
);

create table material_aid_offer_language_AUD (
    material_offer_id bigint,
    language text,
    REV         INTEGER NOT NULL,
    REVTYPE     SMALLINT,
    REVEND      INTEGER,
    PRIMARY KEY ( language, material_offer_id, REV ),
    CONSTRAINT FK_material_offer_language_AUD_REV
        FOREIGN KEY(rev)
            REFERENCES revinfo(rev),
    CONSTRAINT FK_material_offer_language_AUD_REVEND
        FOREIGN KEY(revend)
            REFERENCES revinfo(rev)
);

CREATE TABLE material_aid_offer_AUD
(
    id              bigserial,
    user_id         text,
    title           text,
    description     text,
    category        text,
    city            text,
    region          text,
    REV             INTEGER NOT NULL,
    REVTYPE         SMALLINT,
    REVEND           INTEGER,
    PRIMARY KEY ( id, REV ),
    CONSTRAINT FK_material_offer_AUD_REV
        FOREIGN KEY(rev)
    	    REFERENCES revinfo(rev),
    CONSTRAINT FK_material_offer_AUD_REVEND
        FOREIGN KEY(revend)
            REFERENCES revinfo(rev)
);