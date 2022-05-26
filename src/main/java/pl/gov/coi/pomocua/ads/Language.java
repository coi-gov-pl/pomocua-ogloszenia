package pl.gov.coi.pomocua.ads;

public enum Language {
    UA, PL, EN, RU;

    public static Language fromIsoCode(String isoCode) {
        Language result;
        switch (isoCode) {
            case "uk" -> result = UA;
            default -> result = Language.valueOf(isoCode.toUpperCase());
        }
        return result;
    }
}
