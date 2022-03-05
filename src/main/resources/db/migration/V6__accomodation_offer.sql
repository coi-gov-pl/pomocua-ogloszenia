create table accommodation_offer (
    id              bigserial,
    user_id         text,
    title           text,
    city            text,
    region          text,
    description     text,
    guests          integer,
    length_of_stay  text,
    modified_date   date not null
);

create table accommodation_offer_host_language (
    accommodation_offer_id bigint,
    host_language text
);

create index idx_accommodation_offer_modified_date
ON accommodation_offer(modified_date);

