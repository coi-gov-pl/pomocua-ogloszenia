DROP INDEX idx_material_aid_offer_city;
DROP INDEX idx_material_aid_offer_region;
DROP INDEX idx_accommodation_offer_city;
DROP INDEX idx_accommodation_offer_region;
DROP INDEX idx_transport_offer_origin_city;
DROP INDEX idx_transport_offer_origin_region;
DROP INDEX idx_transport_offer_destination_city;
DROP INDEX idx_transport_offer_destination_region;
DROP INDEX idx_city_city;

CREATE index idx_material_aid_offer_city ON material_aid_offer (upper(city));
CREATE index idx_material_aid_offer_region ON material_aid_offer (upper(region));
CREATE index idx_material_aid_offer_status ON material_aid_offer USING hash(status);
CREATE index idx_material_aid_offer_user_id ON material_aid_offer USING hash(user_id);

CREATE index idx_accommodation_offer_city ON accommodation_offer (upper(city));
CREATE index idx_accommodation_offer_region ON accommodation_offer (upper(region));
CREATE index idx_accommodation_offer_status ON accommodation_offer USING hash(status);
CREATE index idx_accommodation_offer_user_id ON material_aid_offer USING hash(user_id);

CREATE index idx_transport_offer_origin_city ON transport_offer (upper(origin_city));
CREATE index idx_transport_offer_origin_region ON transport_offer (upper(origin_region));
CREATE index idx_transport_offer_destination_city ON transport_offer (upper(destination_city));
CREATE index idx_transport_offer_destination_region ON transport_offer (upper(destination_region));
CREATE index idx_transport_offer_status ON transport_offer USING hash(status);
CREATE index idx_transport_offer_user_id ON transport_offer USING hash(user_id);

CREATE index idx_city_city ON city (upper(city)); -- Spring Data uses "upper" function when using "ignoreCase" modifier in repository
