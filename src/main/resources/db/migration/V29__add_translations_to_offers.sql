ALTER TABLE accommodation_offer ADD COLUMN detected_language text;
ALTER TABLE accommodation_offer ADD COLUMN title_ua text;
ALTER TABLE accommodation_offer ADD COLUMN description_ua text;
ALTER TABLE accommodation_offer ADD COLUMN title_en text;
ALTER TABLE accommodation_offer ADD COLUMN description_en text;
ALTER TABLE accommodation_offer ADD COLUMN title_ru text;
ALTER TABLE accommodation_offer ADD COLUMN description_ru text;
ALTER TABLE accommodation_offer ADD COLUMN translation_error_counter smallint DEFAULT 0;

ALTER TABLE accommodation_offer_AUD ADD COLUMN detected_language text;
ALTER TABLE accommodation_offer_AUD ADD COLUMN title_ua text;
ALTER TABLE accommodation_offer_AUD ADD COLUMN description_ua text;
ALTER TABLE accommodation_offer_AUD ADD COLUMN title_en text;
ALTER TABLE accommodation_offer_AUD ADD COLUMN description_en text;
ALTER TABLE accommodation_offer_AUD ADD COLUMN title_ru text;
ALTER TABLE accommodation_offer_AUD ADD COLUMN description_ru text;
ALTER TABLE accommodation_offer_AUD ADD COLUMN translation_error_counter smallint;

ALTER TABLE health_offer ADD COLUMN detected_language text;
ALTER TABLE health_offer ADD COLUMN title_ua text;
ALTER TABLE health_offer ADD COLUMN description_ua text;
ALTER TABLE health_offer ADD COLUMN title_en text;
ALTER TABLE health_offer ADD COLUMN description_en text;
ALTER TABLE health_offer ADD COLUMN title_ru text;
ALTER TABLE health_offer ADD COLUMN description_ru text;
ALTER TABLE health_offer ADD COLUMN translation_error_counter smallint DEFAULT 0;

ALTER TABLE health_offer_AUD ADD COLUMN detected_language text;
ALTER TABLE health_offer_AUD ADD COLUMN title_ua text;
ALTER TABLE health_offer_AUD ADD COLUMN description_ua text;
ALTER TABLE health_offer_AUD ADD COLUMN title_en text;
ALTER TABLE health_offer_AUD ADD COLUMN description_en text;
ALTER TABLE health_offer_AUD ADD COLUMN title_ru text;
ALTER TABLE health_offer_AUD ADD COLUMN description_ru text;
ALTER TABLE health_offer_AUD ADD COLUMN translation_error_counter smallint;

ALTER TABLE job_offer ADD COLUMN detected_language text;
ALTER TABLE job_offer ADD COLUMN title_ua text;
ALTER TABLE job_offer ADD COLUMN description_ua text;
ALTER TABLE job_offer ADD COLUMN title_en text;
ALTER TABLE job_offer ADD COLUMN description_en text;
ALTER TABLE job_offer ADD COLUMN title_ru text;
ALTER TABLE job_offer ADD COLUMN description_ru text;
ALTER TABLE job_offer ADD COLUMN translation_error_counter smallint DEFAULT 0;

ALTER TABLE job_offer_AUD ADD COLUMN detected_language text;
ALTER TABLE job_offer_AUD ADD COLUMN title_ua text;
ALTER TABLE job_offer_AUD ADD COLUMN description_ua text;
ALTER TABLE job_offer_AUD ADD COLUMN title_en text;
ALTER TABLE job_offer_AUD ADD COLUMN description_en text;
ALTER TABLE job_offer_AUD ADD COLUMN title_ru text;
ALTER TABLE job_offer_AUD ADD COLUMN description_ru text;
ALTER TABLE job_offer_AUD ADD COLUMN translation_error_counter smallint;

ALTER TABLE law_offer ADD COLUMN detected_language text;
ALTER TABLE law_offer ADD COLUMN title_ua text;
ALTER TABLE law_offer ADD COLUMN description_ua text;
ALTER TABLE law_offer ADD COLUMN title_en text;
ALTER TABLE law_offer ADD COLUMN description_en text;
ALTER TABLE law_offer ADD COLUMN title_ru text;
ALTER TABLE law_offer ADD COLUMN description_ru text;
ALTER TABLE law_offer ADD COLUMN translation_error_counter smallint DEFAULT 0;

ALTER TABLE law_offer_AUD ADD COLUMN detected_language text;
ALTER TABLE law_offer_AUD ADD COLUMN title_ua text;
ALTER TABLE law_offer_AUD ADD COLUMN description_ua text;
ALTER TABLE law_offer_AUD ADD COLUMN title_en text;
ALTER TABLE law_offer_AUD ADD COLUMN description_en text;
ALTER TABLE law_offer_AUD ADD COLUMN title_ru text;
ALTER TABLE law_offer_AUD ADD COLUMN description_ru text;
ALTER TABLE law_offer_AUD ADD COLUMN translation_error_counter smallint;

ALTER TABLE material_aid_offer ADD COLUMN detected_language text;
ALTER TABLE material_aid_offer ADD COLUMN title_ua text;
ALTER TABLE material_aid_offer ADD COLUMN description_ua text;
ALTER TABLE material_aid_offer ADD COLUMN title_en text;
ALTER TABLE material_aid_offer ADD COLUMN description_en text;
ALTER TABLE material_aid_offer ADD COLUMN title_ru text;
ALTER TABLE material_aid_offer ADD COLUMN description_ru text;
ALTER TABLE material_aid_offer ADD COLUMN translation_error_counter smallint DEFAULT 0;

ALTER TABLE material_aid_offer_AUD ADD COLUMN detected_language text;
ALTER TABLE material_aid_offer_AUD ADD COLUMN title_ua text;
ALTER TABLE material_aid_offer_AUD ADD COLUMN description_ua text;
ALTER TABLE material_aid_offer_AUD ADD COLUMN title_en text;
ALTER TABLE material_aid_offer_AUD ADD COLUMN description_en text;
ALTER TABLE material_aid_offer_AUD ADD COLUMN title_ru text;
ALTER TABLE material_aid_offer_AUD ADD COLUMN description_ru text;
ALTER TABLE material_aid_offer_AUD ADD COLUMN translation_error_counter smallint;

ALTER TABLE other_offer ADD COLUMN detected_language text;
ALTER TABLE other_offer ADD COLUMN title_ua text;
ALTER TABLE other_offer ADD COLUMN description_ua text;
ALTER TABLE other_offer ADD COLUMN title_en text;
ALTER TABLE other_offer ADD COLUMN description_en text;
ALTER TABLE other_offer ADD COLUMN title_ru text;
ALTER TABLE other_offer ADD COLUMN description_ru text;
ALTER TABLE other_offer ADD COLUMN translation_error_counter smallint DEFAULT 0;

ALTER TABLE other_offer_AUD ADD COLUMN detected_language text;
ALTER TABLE other_offer_AUD ADD COLUMN title_ua text;
ALTER TABLE other_offer_AUD ADD COLUMN description_ua text;
ALTER TABLE other_offer_AUD ADD COLUMN title_en text;
ALTER TABLE other_offer_AUD ADD COLUMN description_en text;
ALTER TABLE other_offer_AUD ADD COLUMN title_ru text;
ALTER TABLE other_offer_AUD ADD COLUMN description_ru text;
ALTER TABLE other_offer_AUD ADD COLUMN translation_error_counter smallint;

ALTER TABLE translation_offer ADD COLUMN detected_language text;
ALTER TABLE translation_offer ADD COLUMN title_ua text;
ALTER TABLE translation_offer ADD COLUMN description_ua text;
ALTER TABLE translation_offer ADD COLUMN title_en text;
ALTER TABLE translation_offer ADD COLUMN description_en text;
ALTER TABLE translation_offer ADD COLUMN title_ru text;
ALTER TABLE translation_offer ADD COLUMN description_ru text;
ALTER TABLE translation_offer ADD COLUMN translation_error_counter smallint DEFAULT 0;

ALTER TABLE translation_offer_AUD ADD COLUMN detected_language text;
ALTER TABLE translation_offer_AUD ADD COLUMN title_ua text;
ALTER TABLE translation_offer_AUD ADD COLUMN description_ua text;
ALTER TABLE translation_offer_AUD ADD COLUMN title_en text;
ALTER TABLE translation_offer_AUD ADD COLUMN description_en text;
ALTER TABLE translation_offer_AUD ADD COLUMN title_ru text;
ALTER TABLE translation_offer_AUD ADD COLUMN description_ru text;
ALTER TABLE translation_offer_AUD ADD COLUMN translation_error_counter smallint;

ALTER TABLE transport_offer ADD COLUMN detected_language text;
ALTER TABLE transport_offer ADD COLUMN title_ua text;
ALTER TABLE transport_offer ADD COLUMN description_ua text;
ALTER TABLE transport_offer ADD COLUMN title_en text;
ALTER TABLE transport_offer ADD COLUMN description_en text;
ALTER TABLE transport_offer ADD COLUMN title_ru text;
ALTER TABLE transport_offer ADD COLUMN description_ru text;
ALTER TABLE transport_offer ADD COLUMN translation_error_counter smallint DEFAULT 0;

ALTER TABLE transport_offer_AUD ADD COLUMN detected_language text;
ALTER TABLE transport_offer_AUD ADD COLUMN title_ua text;
ALTER TABLE transport_offer_AUD ADD COLUMN description_ua text;
ALTER TABLE transport_offer_AUD ADD COLUMN title_en text;
ALTER TABLE transport_offer_AUD ADD COLUMN description_en text;
ALTER TABLE transport_offer_AUD ADD COLUMN title_ru text;
ALTER TABLE transport_offer_AUD ADD COLUMN description_ru text;
ALTER TABLE transport_offer_AUD ADD COLUMN translation_error_counter smallint;
