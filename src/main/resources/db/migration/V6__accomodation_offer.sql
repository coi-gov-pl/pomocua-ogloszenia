create table accommodation_offer (
    id          bigint,
    user_id     text,
    title       text,
    description text,
    city            text,
    region          text,
    guests          integer,
    length_of_stay  text,
    CONSTRAINT PK_AO_ID PRIMARY KEY (ID)
);

create table accommodation_offer_host_language (
    accommodation_offer_id bigint,
    host_language text
);

