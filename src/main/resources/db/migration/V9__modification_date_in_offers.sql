alter table accommodation_offer
add column modified_date timestamp not null;

create index idx_accommodation_offer_modified_date
ON accommodation_offer(modified_date);

alter table job_offer
add column modified_date timestamp not null;

create index idx_job_offer_modified_date
ON job_offer(modified_date);

alter table translation_offer
add column modified_date timestamp not null;

create index idx_translation_offer_modified_date
ON translation_offer(modified_date);

alter table transport_offer
add column modified_date timestamp not null;

create index idx_transport_offer_modified_date
ON transport_offer(modified_date);
