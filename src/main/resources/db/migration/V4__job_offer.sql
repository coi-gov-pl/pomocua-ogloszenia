create table job_offer (
    id bigserial,
    title text,
    mode text,
    city text,
    voivodeship text,
    description text
);

create table job_offer_type(
    job_offer_id bigint,
    type text
);

create table job_offer_language (
    job_offer_id bigint,
    language text
);