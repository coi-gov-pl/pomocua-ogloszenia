drop table accommodation_offer_host_language;
drop table accommodation_offer_host_language_AUD;

alter table accommodation_offer add column host_language text;
alter table accommodation_offer_AUD add column host_language text;

update accommodation_offer set host_language = 'PL' where host_language is null;
