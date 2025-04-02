package bdbt_bada_project.SpringApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KlientDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public KlientDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    // Pobierz listę klientów
    public List<klienci> list() {
        String sql = "SELECT NRKLIENTA, IMIE, NAZWISKO, NRTELEFONU, EMAIL, LOGIN, HASLO FROM klienci";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(klienci.class));
    }

    // Zapisz nowego klienta
    public void save(klienci klient) {
        String sql = "INSERT INTO klienci (\n" +
                "    NRKLIENTA, IMIE, NAZWISKO, NRTELEFONU, EMAIL, LOGIN, HASLO) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                klient.getNrklienta(),
                klient.getImie(),
                klient.getNazwisko(),
                klient.getNrtelefonu(),
                klient.getEmail(),
                klient.getLogin(),
                klient.getHaslo());
    }
    public boolean isLoginTaken(String login) {
        String sql = "SELECT COUNT(*) FROM klienci WHERE LOGIN = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, login);
        return count != null && count > 0;
    }

    // Sprawdza, czy dany numer ID już istnieje w bazie
    public boolean isIdTaken(int id) {
        String sql = "SELECT COUNT(*) FROM klienci WHERE NRKLIENTA = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public klienci getByLogin(String login) {
        try {
            String sql = "SELECT * FROM klienci WHERE LOGIN = ?";
            return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(klienci.class), login);
        } catch (EmptyResultDataAccessException e) {
            return null; // lub rzuć wyjątek, jeśli wymagana jest obecność klienta
        }
    }

    // Pobierz klienta według ID
    public klienci get(int id) {
        try {
            String sql = "SELECT * FROM klienci WHERE NRKLIENTA = ?";
            return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(klienci.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null; // lub rzuć wyjątek, jeśli wymagana jest obecność klienta
        }
    }

    // Aktualizuj dane klienta
    public void update(klienci klient) {
        String sql = "UPDATE klienci SET " +
                "IMIE = :imie, " +
                "NAZWISKO = :nazwisko, " +
                "NRTELEFONU = :nrtelefonu, " +
                "EMAIL = :email, " +
                "LOGIN = :login, " +
                "HASLO = :haslo " +
                "WHERE NRKLIENTA = :nrklienta";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(klient);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
        template.update(sql, param);
    }

    // Usuń klienta według ID
    public void delete(int id) {
        String sql = "DELETE FROM klienci WHERE NRKLIENTA = ?";
        jdbcTemplate.update(sql, id);
    }

    // Sprawdza, czy klient jest powiązany z jakimkolwiek zamówieniem
    public boolean isClientAssociatedWithOrders(int clientId) {
        String checkOrderSql = "SELECT COUNT(*) FROM zamowienia WHERE NRKLIENTA = ?";
        int orderCount = jdbcTemplate.queryForObject(checkOrderSql, Integer.class, clientId);
        return orderCount > 0;
    }

    // Metoda pomocnicza, która zwraca prawdę, jeśli klient jest powiązany z zamówieniami
    public boolean canDeleteClient(int id) {
        return !isClientAssociatedWithOrders(id);
    }
}
