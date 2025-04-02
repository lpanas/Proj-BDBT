package bdbt_bada_project.SpringApplication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MyUserDetailsServiceTest {

    @Autowired
    private KlientRepository klientRepository;

    @Test
    public void testFindByLogin() {
        // Przygotowanie testowych danych
        klienci klient = new klienci();
        klient.setNrklienta(87);
        klient.setImie("Marek");
        klient.setNazwisko("Bogdanski");
        klient.setNrtelefonu("214");
        klient.setEmail("abaabab@123.com");
        klient.setLogin("testUser");
        klient.setHaslo("opasdaj");
        klientRepository.save(klient);

        // Wywołanie metody i sprawdzenie wyniku
        Optional<klienci> result = klientRepository.findByLogin("testUser");
        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getUsername());
    }
}