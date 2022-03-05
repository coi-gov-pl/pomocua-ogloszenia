create table legal_assistance_offer
(
    id                      bigserial,
    title                   text,
    description             text,
    voivodeship             text,
    city                    text,
    mode                    text
);

create table legal_assistance_offer_language
(
    legal_assistance_offer_id bigint,
    language text
);

create table legal_assistance_offer_type
(
    legal_assistance_offer_id bigint,
    type text
);