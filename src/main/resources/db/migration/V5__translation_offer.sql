create table translation_offer (
    id          bigserial,
    title       text,
    mode        text,
    sworn       bool,
    city        text,
    voivodeship text,
    description text
);

create table translation_offer_language (
    translation_offer_id bigint,
    language             text
);