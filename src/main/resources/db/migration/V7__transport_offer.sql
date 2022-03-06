create table transport_offer
(
    id                      bigserial,
    user_id                 text,
    title                   text,
    description             text,
    origin_region           text,
    origin_city             text,
    destination_region      text,
    destination_city        text,
    capacity                int,
    transport_date          date,
    modified_date           timestamp not null,
    CONSTRAINT PK_TRO_ID PRIMARY KEY (ID)
);

create index idx_transport_offer_modified_date
ON transport_offer(modified_date);