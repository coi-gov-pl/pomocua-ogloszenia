create table transport_offer
(
    id                      bigserial,
    title                   text,
    description             text,
    origin_voivodeship      text,
    origin_city             text,
    destination_voivodeship text,
    destination_city        text,
    capacity                int,
    transport_date          date
);
