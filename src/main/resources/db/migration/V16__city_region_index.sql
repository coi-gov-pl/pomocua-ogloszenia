UPDATE city SET city = 'Krzanowice (miasto)' WHERE id = 31866;
UPDATE city SET city = 'Lipno (miasto)' WHERE id = 2482;
UPDATE city SET city = 'Stronie Śląskie (miasto)' WHERE id = 513;
UPDATE city SET city = 'Poniatowa (miasto)' WHERE id = 7296;
UPDATE city SET city = 'Żukowo (miasto)' WHERE id = 30225;
UPDATE city SET city = 'Małachów (miasto)' WHERE id = 34365;
UPDATE city SET city = 'Rakoniewice (miasto)' WHERE id = 41321;
DELETE FROM city WHERE id = 3350;

CREATE UNIQUE INDEX idx_city_region_city ON city (UPPER(city || ', ' || region));