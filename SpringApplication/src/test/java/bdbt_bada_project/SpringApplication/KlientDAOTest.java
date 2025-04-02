package bdbt_bada_project.SpringApplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.internal.matchers.text.ValuePrinter.print;

class KlientDAOTest {
    private KlientDAO dao;
    @BeforeEach
    void setUp()throws Exception {
        DriverManagerDataSource datasource= new DriverManagerDataSource();
        datasource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        datasource.setUsername("LPANA7");
        datasource.setPassword("panasiuk");
        datasource.setDriverClassName("oracle.jdbc.OracleDriver");
        dao= new KlientDAO(new JdbcTemplate(datasource));
    }
    @Test
    void list() {
        List<klienci> listKlient = dao.list();
        assertFalse(listKlient.isEmpty());
    }

    @Test
    void save() {
        klienci klient = new klienci(87,"Tomasz","Tomacki","123456789","tomasz@tomackqwqr.com","koole","mole");
        dao.save(klient);

    }

    @Test
    void get() {
        int id=87;
        klienci klient= dao.get(id);
        assertNotNull(klient);
    }
    @Test
    void getByLogin() {
        String login="kola";
        klienci klient= dao.getByLogin(login);
        assertNotNull(klient);
    }

    @Test
    void update() {
        klienci klient = new klienci();
        klient.setNrklienta(87);
        klient.setImie("Marek");
        klient.setNazwisko("Bogdanski");
        klient.setNrtelefonu("214");
        klient.setEmail("abaabab@123.com");
        klient.setLogin("koadoa");
        klient.setHaslo("opasdaj");
        dao.update(klient);
        klient.setNazwisko("Borowik");
        dao.update(klient);
    }

    @Test
    void delete() {
        int id =87;
        dao.delete(id);
    }
}