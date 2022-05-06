UPDATE job_offer SET industry=substring(industry, 0, position(',' in industry)) WHERE industry LIKE '%,%';

UPDATE job_offer SET industry='HEALTH' WHERE industry='HEALTH_AND_SAFETY';

UPDATE job_offer SET industry='E-COMMERCE' WHERE industry='INTERNET';

UPDATE job_offer SET industry='MISC' WHERE industry='ENTERTAINMENT';

UPDATE job_offer SET industry='HR' WHERE industry='DESK_JOB';

UPDATE job_offer SET industry='SALES' WHERE industry='TRADE';

DROP INDEX idx_job_offer_industry;
CREATE INDEX idx_job_offer_industry ON job_offer (industry);


