create table translation_offer (
    id          bigint,
    user_id     text,
    title       text,
    description text,
    mode        text,
    sworn       bool,
    city        text,
    region      text,
    CONSTRAINT PK_TO_ID PRIMARY KEY (ID)
);

create table translation_offer_language (
    translation_offer_id bigint,
    language             text
);