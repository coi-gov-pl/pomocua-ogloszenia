-- polish ispell conf.
DROP TEXT SEARCH CONFIGURATION IF EXISTS polish_ispell;
DROP TEXT SEARCH DICTIONARY IF EXISTS polish_ispell;
DROP TEXT SEARCH DICTIONARY IF EXISTS polish_simple;

CREATE TEXT SEARCH DICTIONARY polish_ispell (
  Template = ispell,
  DictFile = polish,
  AffFile = polish,
  StopWords = polish
);

CREATE TEXT SEARCH DICTIONARY polish_simple (
  Template = simple,
  StopWords = polish
);

CREATE TEXT SEARCH CONFIGURATION polish_ispell(parser = default);

ALTER TEXT SEARCH CONFIGURATION polish_ispell
  ALTER MAPPING FOR asciiword, asciihword, hword_asciipart, word, hword, hword_part
  WITH polish_ispell, polish_simple;


-- ukrainian ispell conf.
DROP TEXT SEARCH CONFIGURATION IF EXISTS ukrainian_ispell;
DROP TEXT SEARCH DICTIONARY IF EXISTS ukrainian_ispell;
DROP TEXT SEARCH DICTIONARY IF EXISTS ukrainian_simple;

CREATE TEXT SEARCH DICTIONARY ukrainian_ispell (
  Template = ispell,
  DictFile = uk_UA,
  AffFile = uk_UA,
  StopWords = ukrainian
);

CREATE TEXT SEARCH DICTIONARY ukrainian_simple (
  Template = simple,
  StopWords = ukrainian
);

CREATE TEXT SEARCH CONFIGURATION ukrainian_ispell(parser = default);

ALTER TEXT SEARCH CONFIGURATION ukrainian_ispell
  ALTER MAPPING FOR asciiword, asciihword, hword_asciipart, word, hword, hword_part
  WITH ukrainian_ispell, ukrainian_simple;


-- russian ispell conf.
DROP TEXT SEARCH CONFIGURATION IF EXISTS russian_ispell;
DROP TEXT SEARCH DICTIONARY IF EXISTS russian_ispell;
DROP TEXT SEARCH DICTIONARY IF EXISTS russian_simple;

CREATE TEXT SEARCH DICTIONARY russian_ispell (
  Template = ispell,
  DictFile = ru_RU,
  AffFile = ru_RU,
  StopWords = russian
);

CREATE TEXT SEARCH DICTIONARY russian_simple (
  Template = simple,
  StopWords = russian
);

CREATE TEXT SEARCH CONFIGURATION russian_ispell(parser = default);

ALTER TEXT SEARCH CONFIGURATION russian_ispell
  ALTER MAPPING FOR asciiword, asciihword, hword_asciipart, word, hword, hword_part
  WITH russian_ispell, russian_simple;


-- english ispell conf.
DROP TEXT SEARCH CONFIGURATION IF EXISTS english_ispell;
DROP TEXT SEARCH DICTIONARY IF EXISTS english_ispell;
DROP TEXT SEARCH DICTIONARY IF EXISTS english_simple;

CREATE TEXT SEARCH DICTIONARY english_ispell (
  Template = ispell,
  DictFile = en_GB,
  AffFile = en_GB,
  StopWords = english
);

CREATE TEXT SEARCH DICTIONARY english_simple (
  Template = simple,
  StopWords = english
);

CREATE TEXT SEARCH CONFIGURATION english_ispell(parser = default);

ALTER TEXT SEARCH CONFIGURATION english_ispell
  ALTER MAPPING FOR asciiword, asciihword, hword_asciipart, word, hword, hword_part
  WITH english_ispell, english_simple;
