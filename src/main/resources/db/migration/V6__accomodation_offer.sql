create table accommodation_offer (
    id              bigserial,
    city            text,
    region          text,
    guests          integer,
    length_of_stay  text,
    CONSTRAINT fk_offer_id FOREIGN KEY(id) REFERENCES base_offer(id)
);

create table accommodation_offer_host_language (
    accommodation_offer_id bigint,
    host_language text
);

