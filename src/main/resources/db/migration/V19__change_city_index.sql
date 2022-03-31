CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX idx_city_region_city_gin
    ON city using gin ((UPPER(city || ', ' || region)) gin_trgm_ops);