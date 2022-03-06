create table material_aid_offer (
    id              bigserial,
    category        text,
    city            text,
    region          text,
    CONSTRAINT fk_offer_id FOREIGN KEY(id) REFERENCES base_offer(id)
);
