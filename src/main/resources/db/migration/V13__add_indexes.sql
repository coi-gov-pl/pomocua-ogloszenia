CREATE index idx_material_aid_offer_category ON material_aid_offer ( category);
CREATE index idx_material_aid_offer_city ON material_aid_offer (city);
CREATE index idx_material_aid_offer_region ON material_aid_offer (region);

CREATE index idx_accommodation_offer_city ON accommodation_offer (city);
CREATE index idx_accommodation_offer_region ON accommodation_offer (region);
CREATE index idx_accommodation_offer_guests ON accommodation_offer (guests);

CREATE index idx_city_city ON city (city);

CREATE index idx_transport_offer_origin_region ON transport_offer (origin_region);
CREATE index idx_transport_offer_origin_city ON transport_offer (origin_city);
CREATE index idx_transport_offer_destination_region ON transport_offer (destination_region);
CREATE index idx_transport_offer_destination_city ON transport_offer (destination_city);
CREATE index idx_transport_offer_capacity ON transport_offer (capacity);
