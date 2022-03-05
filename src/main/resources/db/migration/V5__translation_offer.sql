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