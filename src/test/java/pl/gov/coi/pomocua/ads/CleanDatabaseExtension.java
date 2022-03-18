package pl.gov.coi.pomocua.ads;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;

public class CleanDatabaseExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        Collection<CrudRepository> repositories = applicationContext.getBeansOfType(CrudRepository.class).values();
        repositories.forEach(CrudRepository::deleteAll);
    }
}
