package bdbt_bada_project.SpringApplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class PojazdyDAOTest {
    private PojazdyDAO dao;
    @BeforeEach
    void setUp()throws Exception {
        DriverManagerDataSource datasource= new DriverManagerDataSource();
        datasource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        datasource.setUsername("LPANA7");
        datasource.setPassword("panasiuk");
        datasource.setDriverClassName("oracle.jdbc.OracleDriver");
        dao= new PojazdyDAO(new JdbcTemplate(datasource));
    }

    @Test
    void list() {
        List<Pojazd> listPojazd = dao.list();
        assertFalse(listPojazd.isEmpty());
    }

    @Test
    void save() {
        Pojazd pojazd = new Pojazd(52,"141","Volkswagen","Jetta", null,56.0,"Polska", LocalDate.of(2025,12,03));
        dao.save(pojazd);

    }

    @Test
    void get() {
        int id=52;
        Pojazd pojazd= dao.get(id);
        assertNotNull(pojazd);
    }

    @Test
    void update() {
        Pojazd pojazd = new Pojazd();
        pojazd.setNr_Pojazdu(52);
        pojazd.setVIN("1221u3uyuy");
        pojazd.setMarka("Chevrolet");
        pojazd.setModel("Seria 1");
        pojazd.setKraj("Portugalia");
        pojazd.setRok_produkcji(LocalDate.of(2001,7,9));
        pojazd.setCena(509241);
        dao.update(pojazd);
    }

    @Test
    void delete() {
        int id =28;
        dao.delete(id);
    }
}