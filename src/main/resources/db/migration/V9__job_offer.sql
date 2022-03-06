create table job_offer (
    id          bigint,
    user_id     text,
    title       text,
    description text,
    mode        text,
    city        text,
    region      text,
    CONSTRAINT PK_JO_ID PRIMARY KEY (ID)
);

create table job_offer_type(
    job_offer_id bigint,
    type text
);

create table job_offer_language (
    job_offer_id bigint,
    language text
);