create table job_offer (
    id          bigserial,
    mode        text,
    city        text,
    region      text,
    CONSTRAINT fk_offer_id FOREIGN KEY(id) REFERENCES base_offer(id)
);

create table job_offer_type(
    job_offer_id bigint,
    type text
);

create table job_offer_language (
    job_offer_id bigint,
    language text
);