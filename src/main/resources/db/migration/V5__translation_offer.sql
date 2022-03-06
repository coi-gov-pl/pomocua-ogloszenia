create table translation_offer (
    id              bigserial,
    user_id         text,
    title           text,
    mode            text,
    sworn           bool,
    city            text,
    region          text,
    description     text,
    modified_date   timestamp not null
);

create table translation_offer_language (
    translation_offer_id bigint,
    language             text
);

create index idx_translation_offer_modified_date
ON translation_offer(modified_date);