ALTER TABLE other_offer ADD COLUMN text_searchable tsvector;
ALTER TABLE other_offer ADD COLUMN text_searchable_ua tsvector;
ALTER TABLE other_offer ADD COLUMN text_searchable_en tsvector;
ALTER TABLE other_offer ADD COLUMN text_searchable_ru tsvector;

UPDATE other_offer
SET text_searchable = to_tsvector('public.polish_ispell', coalesce(title, '') || ' ' || coalesce(description, ''))
WHERE text_searchable IS NULL;

UPDATE other_offer
SET text_searchable_ua = to_tsvector('public.ukrainian_ispell', coalesce(title_ua, '') || ' ' || coalesce(description_ua, ''))
WHERE text_searchable_ua IS NULL;

UPDATE other_offer
SET text_searchable_en = to_tsvector('public.english_ispell', coalesce(title_en, '') || ' ' || coalesce(description_en, ''))
WHERE text_searchable_en IS NULL;

UPDATE other_offer
SET text_searchable_ru = to_tsvector('public.russian_ispell', coalesce(title_ru, '') || ' ' || coalesce(description_ru, ''))
WHERE text_searchable_ru IS NULL;

CREATE TRIGGER other_offer_text_searchable_update BEFORE INSERT OR UPDATE
    ON other_offer FOR EACH ROW EXECUTE FUNCTION
    tsvector_update_trigger(text_searchable, 'public.polish_ispell', title, description);
CREATE TRIGGER other_offer_text_searchable_ua_update BEFORE INSERT OR UPDATE
    ON other_offer FOR EACH ROW EXECUTE FUNCTION
    tsvector_update_trigger(text_searchable_ua, 'public.ukrainian_ispell', title_ua, description_ua);
CREATE TRIGGER other_offer_text_searchable_en_update BEFORE INSERT OR UPDATE
    ON other_offer FOR EACH ROW EXECUTE FUNCTION
    tsvector_update_trigger(text_searchable_en, 'public.english_ispell', title_en, description_en);
CREATE TRIGGER other_offer_text_searchable_ru_update BEFORE INSERT OR UPDATE
    ON other_offer FOR EACH ROW EXECUTE FUNCTION
    tsvector_update_trigger(text_searchable_ru, 'public.russian_ispell', title_ru, description_ru);

CREATE INDEX idx_other_offer_text_searchable ON other_offer USING GIST(text_searchable);
CREATE INDEX idx_other_offer_text_searchable_ua ON other_offer USING GIST(text_searchable_ua);
CREATE INDEX idx_other_offer_text_searchable_en ON other_offer USING GIST(text_searchable_en);
CREATE INDEX idx_other_offer_text_searchable_ru ON other_offer USING GIST(text_searchable_ru);
