create table translation_offer (
    id          bigserial,
    mode        text,
    sworn       bool,
    city        text,
    region      text,
    CONSTRAINT fk_offer_id FOREIGN KEY(id) REFERENCES base_offer(id)
);

create table translation_offer_language (
    translation_offer_id bigint,
    language             text
);