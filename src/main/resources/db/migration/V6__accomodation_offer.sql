create table accommodation_offer (
    id bigserial,
    title text,
    city text,
    region text,
    description text,
    guests integer,
    length_of_stay text
);

create table accommodation_offer_host_language (
    accommodation_offer_id bigint,
    host_language text
);

