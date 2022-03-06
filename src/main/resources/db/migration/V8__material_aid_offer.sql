create table material_aid_offer (
    id          bigint,
    user_id     text,
    title       text,
    description text,
    category        text,
    city            text,
    region          text,
    CONSTRAINT PK_MAO_ID PRIMARY KEY (ID)
);
