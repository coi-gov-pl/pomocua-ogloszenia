DROP INDEX idx_city_region_city;

CREATE UNIQUE INDEX idx_city_region_city ON city
    USING btree ((UPPER(city || ', ' || region)) text_pattern_ops);