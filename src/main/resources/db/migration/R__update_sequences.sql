-- SQL code here --
SELECT setval('city_seq', (SELECT MAX(id) FROM city));