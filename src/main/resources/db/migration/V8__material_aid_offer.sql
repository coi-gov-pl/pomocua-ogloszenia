create table material_aid_offer (
    id              bigserial,
    user_id         text,
    title           text,
    description     text,
    category        text,
    city            text,
    region          text,
    modified_date   timestamp not null,

    CONSTRAINT PK_MAO_ID PRIMARY KEY (ID)
);

create index idx_material_aid_offer_modified_date
ON material_aid_offer(modified_date);
