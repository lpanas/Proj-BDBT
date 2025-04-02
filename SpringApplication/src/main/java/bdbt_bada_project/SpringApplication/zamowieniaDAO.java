package bdbt_bada_project.SpringApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class zamowieniaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public zamowieniaDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<zamowienia> listU() {
        // Pobierz login zalogowanego klienta z kontekstu bezpieczeństwa (Spring Security)
        String loggedInUsername = getLoggedInUsername();

        // Zapytanie SQL zwracające zamówienia dla zalogowanego klienta na podstawie loginu
        String sql = "SELECT z.NRZAMOWIENIA, z.DATAZAMOWIENIA, z.STATUS, z.NRPOJAZDU, z.NRKLIENTA, " +
                "k.IMIE, k.NAZWISKO, p.\"Marka\", p.\"Model\", p.\"Cena\" " +
                "FROM zamowienia z " +
                "JOIN klienci k ON z.NRKLIENTA = k.NRKLIENTA " +
                "JOIN \"Pojazdy\" p ON z.NRPOJAZDU = p.\"Nr_Pojazdu\" " +
                "WHERE k.LOGIN = ?";  // Używamy loginu zamiast NRKLIENTA

        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(zamowienia.class), loggedInUsername);
    }
    public boolean isPojazdZamowionyByKlient(int nrPojazdu, int nrKlienta) {
        String sql = "SELECT COUNT(*) FROM zamowienia WHERE NRPOJAZDU = ? AND NRKLIENTA = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, nrPojazdu, nrKlienta);
        return count > 0;  // Zwróć true, jeśli zamówienie już istnieje
    }

    // Metoda pomocnicza do pobrania identyfikatora zalogowanego klienta
    private String getLoggedInUsername() {
        // Pobierz szczegóły użytkownika z kontekstu bezpieczeństwa (Spring Security)
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Zwróć login klienta (zakładając, że jest to login użytkownika)
        return userDetails.getUsername(); // Zakładamy, że login klienta to username
    }
    // Pobierz listę zamówień z rozszerzonymi danymi (klient i pojazd)
    public List<zamowienia> list() {
        String sql = "SELECT z.NRZAMOWIENIA, z.DATAZAMOWIENIA, z.STATUS, z.NRPOJAZDU, z.NRKLIENTA, " +
                "k.IMIE, k.NAZWISKO, p.\"Marka\", p.\"Model\", p.\"Cena\" " +
                "FROM zamowienia z " +
                "JOIN klienci k ON z.NRKLIENTA = k.NRKLIENTA " +
                "JOIN \"Pojazdy\" p ON z.NRPOJAZDU = p.\"Nr_Pojazdu\"";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(zamowienia.class));
    }

    // Zapisz nowe zamówienie
    public void save(zamowienia zamowienie) {
        String sql = "INSERT INTO zamowienia (NRZAMOWIENIA, DATAZAMOWIENIA, STATUS, NRPOJAZDU, NRKLIENTA) " +
                "VALUES (zamowienia_seq.NEXTVAL, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                zamowienie.getDatazamowienia(),
                zamowienie.getStatus(),
                zamowienie.getNrpojazdu(),
                zamowienie.getNrklienta());
    }

    // Pobierz zamówienie według ID z dodatkowymi danymi
    public zamowienia get(int id) {
        try {
            String sql = "SELECT z.NRZAMOWIENIA, z.DATAZAMOWIENIA, z.STATUS, z.NRPOJAZDU, z.NRKLIENTA, " +
                    "k.IMIE, k.NAZWISKO, p.\"Marka\", p.\"Model\", p.\"Cena\" " +
                    "FROM zamowienia z " +
                    "JOIN klienci k ON z.NRKLIENTA = k.NRKLIENTA " +
                    "JOIN \"Pojazdy\" p ON z.NRPOJAZDU = p.\"Nr_Pojazdu\" " +
                    "WHERE z.NRZAMOWIENIA = ?";
            return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(zamowienia.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null; // Lub rzuć wyjątek, jeśli zamówienie musi istnieć
        }
    }

    // Aktualizuj zamówienie
    public void update(zamowienia zamowienie) {
        String sql = "UPDATE zamowienia SET " +
                "DATAZAMOWIENIA = :datazamowienia, " +
                "STATUS = :status, " +
                "NRPOJAZDU = :nrpojazdu, " +
                "NRKLIENTA = :nrklienta " +
                "WHERE NRZAMOWIENIA = :nrzamowienia";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(zamowienie);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
        template.update(sql, param);
    }
    
    // Usuń zamówienie według ID
    public void delete(int id) {
        String checkVehicleSql = "SELECT COUNT(*) FROM \"Pojazdy\" WHERE \"Nr_Pojazdu\" IN (SELECT NRPOJAZDU FROM zamowienia WHERE NRZAMOWIENIA = ?)";
        int vehicleCount = jdbcTemplate.queryForObject(checkVehicleSql, Integer.class, id);

        String sql = "DELETE FROM zamowienia WHERE NRZAMOWIENIA = ?";
        jdbcTemplate.update(sql, id);
    }

    // Zaakceptuj zamówienie: zmień status na 'zaakceptowane'
    public void akceptujZamowienie(int nrzamowienia) {
        // Weryfikacja, czy zamówienie istnieje
        String checkExistSql = "SELECT COUNT(*) FROM zamowienia WHERE NRZAMOWIENIA = ?";
        int count = jdbcTemplate.queryForObject(checkExistSql, Integer.class, nrzamowienia);
        if (count == 0) {
            throw new IllegalStateException("Zamówienie o podanym numerze nie istnieje.");
        }

        // Pobranie NRPOJAZDU dla danego zamówienia
        String getPojazdSql = "SELECT NRPOJAZDU FROM zamowienia WHERE NRZAMOWIENIA = ?";
        int nrPojazdu = jdbcTemplate.queryForObject(getPojazdSql, Integer.class, nrzamowienia);

        // Weryfikacja, czy pojazd został już zaakceptowany
        String checkPojazdSql = "SELECT COUNT(*) FROM zamowienia WHERE NRPOJAZDU = ? AND STATUS = 'zaakceptowane'";
        int pojazdCount = jdbcTemplate.queryForObject(checkPojazdSql, Integer.class, nrPojazdu);

        if (pojazdCount > 0) {
            throw new IllegalStateException("Ten pojazd został już zaakceptowany w innym zamówieniu.");
        }

        // Pobranie statusu zamówienia
        String checkStatusSql = "SELECT STATUS FROM zamowienia WHERE NRZAMOWIENIA = ?";
        String status = jdbcTemplate.queryForObject(checkStatusSql, String.class, nrzamowienia);

        if ("zaakceptowane".equalsIgnoreCase(status)) {
            throw new IllegalStateException("To zamówienie zostało już zaakceptowane.");
        }

        // Aktualizacja statusu
        String sql = "UPDATE zamowienia SET STATUS = 'zaakceptowane' WHERE NRZAMOWIENIA = ?";
        jdbcTemplate.update(sql, nrzamowienia);
    }

    // Odrzuć zamówienie: zmień status na 'odrzucone'
    public void odrzucZamowienie(int nrzamowienia) {
        String sql = "UPDATE zamowienia SET STATUS = 'odrzucone' WHERE NRZAMOWIENIA = ?";
        jdbcTemplate.update(sql, nrzamowienia);
    }
}
