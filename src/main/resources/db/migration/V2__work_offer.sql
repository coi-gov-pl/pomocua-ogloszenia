create table work_offer (
    id bigserial,
    title text,
    mode text,
    city text,
    voivodeship text,
    description text
);

create table work_offer_type(
    work_offer_id bigint,
    type text
);

create table work_offer_language (
    work_offer_id bigint,
    language text
);