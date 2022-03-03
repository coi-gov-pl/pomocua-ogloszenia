create table material_aid_offer (
    id bigserial,
    creationDate timestamp,
    category text,
    location text,
    title text,
    description text,

    CONSTRAINT PK_MAO_ID PRIMARY KEY (ID)
);