create table transport_offer
(
    id                      bigserial,
    origin_region           text,
    origin_city             text,
    destination_region      text,
    destination_city        text,
    capacity                int,
    transport_date          date,
    CONSTRAINT fk_offer_id FOREIGN KEY(id) REFERENCES base_offer(id)
);
