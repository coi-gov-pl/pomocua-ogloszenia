package pl.gov.coi.pomocua.ads;

import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.spi.MetadataBuilderContributor;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.BooleanType;

public class SqlFunctionsMetadataBuilderContributor implements MetadataBuilderContributor {
    @Override
    public void contribute(MetadataBuilder metadataBuilder) {
        metadataBuilder
                .applySqlFunction("fts",
                        new SQLFunctionTemplate(BooleanType.INSTANCE, "text_searchable @@ plainto_tsquery('public.polish_ispell', ?1)"))
                .applySqlFunction("fts_ua",
                        new SQLFunctionTemplate(BooleanType.INSTANCE, "text_searchable_ua @@ plainto_tsquery('public.ukrainian_ispell', ?1)"))
                .applySqlFunction("fts_en",
                        new SQLFunctionTemplate(BooleanType.INSTANCE, "text_searchable_en @@ plainto_tsquery('public.english_ispell', ?1)"))
                .applySqlFunction("fts_ru",
                        new SQLFunctionTemplate(BooleanType.INSTANCE, "text_searchable_ru @@ plainto_tsquery('public.russian_ispell', ?1)"));
    }
}
