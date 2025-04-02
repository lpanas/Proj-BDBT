package bdbt_bada_project.SpringApplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class zamowieniaDAOTest {
    private zamowieniaDAO daoz;
    @BeforeEach
    void setUp()throws Exception {
        DriverManagerDataSource datasource= new DriverManagerDataSource();
        datasource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        datasource.setUsername("LPANA7");
        datasource.setPassword("panasiuk");
        datasource.setDriverClassName("oracle.jdbc.OracleDriver");
        daoz= new zamowieniaDAO(new JdbcTemplate(datasource));
    }

    @Test
    void list() {
        List<zamowienia> listZamowienie = daoz.list();
        assertFalse(listZamowienie.isEmpty());
    }

    @Test
    void save() {
        zamowienia zamowienie = new zamowienia(1,null, null,0,1);
        daoz.save(zamowienie);

    }

    /*@Test
    void get() {
        int id=1;
        zamowienia zamowienie= daoz.get(id);
        assertNotNull(zamowienie);
    }
    */
    @Test
    void update() {
        zamowienia zamowienie = new zamowienia();
        zamowienie.setNrzamowienia(1);
        zamowienie.setNrklienta(1);
        zamowienie.setNrpojazdu(52);
        daoz.update(zamowienie);
        zamowienie.setStatus("zaakceptowane");
        daoz.update(zamowienie);

    }

    @Test
    void delete() {
        int id =1;
        daoz.delete(id);
    }
}