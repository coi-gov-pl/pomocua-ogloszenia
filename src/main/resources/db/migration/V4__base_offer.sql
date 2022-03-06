CREATE SEQUENCE hibernate_sequence START 1 INCREMENT 1;

create table base_offer (
    id          bigserial,
    user_id     text,
    title       text,
    description text,
    CONSTRAINT PK_OFFER_ID PRIMARY KEY (ID)
);