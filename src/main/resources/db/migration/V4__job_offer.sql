create table job_offer (
    id              bigserial,
    user_id         text,
    title           text,
    mode            text,
    city            text,
    region          text,
    description     text,
    modified_date   timestamp not null
);

create table job_offer_type(
    job_offer_id bigint,
    type text
);

create index idx_job_offer_modified_date
ON job_offer(modified_date);

create table job_offer_language (
    job_offer_id bigint,
    language text
);