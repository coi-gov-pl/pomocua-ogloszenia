CREATE INDEX idx_other_offer_title_description ON other_offer USING gist ((title || description) gist_trgm_ops);
CREATE INDEX idx_other_offer_title_description_ua ON other_offer USING gist ((title_ua || description_ua) gist_trgm_ops);
CREATE INDEX idx_other_offer_title_description_en ON other_offer USING gist ((title_en || description_en) gist_trgm_ops);
CREATE INDEX idx_other_offer_title_description_ru ON other_offer USING gist ((title_ru || description_ru) gist_trgm_ops);
