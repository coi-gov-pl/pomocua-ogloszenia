alter table accommodation_offer add column status text default 'ACTIVE';
alter table transport_offer add column status text default 'ACTIVE';
alter table material_aid_offer add column status text default 'ACTIVE';

alter table accommodation_offer_AUD add column status text;
alter table transport_offer_AUD add column status text;
alter table material_aid_offer_AUD add column status text;
