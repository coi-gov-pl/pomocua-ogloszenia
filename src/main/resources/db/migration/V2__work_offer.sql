create table work_offer (
    id uuid,
    title text,
    mode text,
    city text,
    voivodeship text,
    description text
);

create table work_offer_type(
    work_offer_id uuid,
    type text
);

create table work_offer_language (
    work_offer_id uuid,
    language text
);