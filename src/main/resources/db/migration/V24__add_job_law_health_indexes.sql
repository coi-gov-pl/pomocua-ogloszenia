CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX idx_health_offer_mode ON health_offer USING gist (mode gist_trgm_ops);
CREATE INDEX idx_health_offer_language ON health_offer USING gist (language gist_trgm_ops);

CREATE INDEX idx_job_offer_industry ON job_offer USING gist (industry gist_trgm_ops);
CREATE INDEX idx_job_offer_work_time ON job_offer USING gist (work_time gist_trgm_ops);
CREATE INDEX idx_job_offer_contract_type ON job_offer USING gist (contract_type gist_trgm_ops);
CREATE INDEX idx_job_offer_language ON job_offer USING gist (language gist_trgm_ops);

CREATE INDEX idx_law_offer_help_mode ON law_offer USING gist (help_mode gist_trgm_ops);
CREATE INDEX idx_law_offer_help_kind ON law_offer USING gist (help_kind gist_trgm_ops);
CREATE INDEX idx_law_offer_language ON law_offer USING gist (language gist_trgm_ops);