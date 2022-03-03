package pl.gov.coi.pomocua.ogloszenia;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestContainersTest {

    @Autowired
    private EntityManager em;

    @Test
    void shouldExecuteSimpleQueryOnTc() {
        Query query = em.createNativeQuery("SELECT 1");
        Integer result = (Integer) query.getSingleResult();
        assertTrue(result.equals(1));
    }
}
