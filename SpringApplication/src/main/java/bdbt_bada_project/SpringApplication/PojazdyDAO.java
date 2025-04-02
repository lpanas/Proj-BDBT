package bdbt_bada_project.SpringApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class PojazdyDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PojazdyDAO(JdbcTemplate jdbcTemplate){
        super();
        this.jdbcTemplate=jdbcTemplate;
    }

    public List<Pojazd> list(){
        String sql = "SELECT \"Nr_Pojazdu\", \"VIN\", \"Marka\", \"Model\", \"Rok_produkcji\", \"Kraj\", \"Cena\", \"Nr_Klienta\" FROM \"Pojazdy\"";

        List<Pojazd> listPojazd = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Pojazd.class));
        return listPojazd;
    }
    public void save(Pojazd pojazd) {
        String sql = "INSERT INTO \"Pojazdy\" (\"Nr_Pojazdu\", \"VIN\", \"Marka\", \"Model\", \"Rok_produkcji\", \"Kraj\", \"Cena\", \"Nr_Klienta\") " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // Wstawianie danych do bazy za pomocą JdbcTemplate
        jdbcTemplate.update(sql,
                pojazd.getNr_Pojazdu(),
                pojazd.getVIN(),
                pojazd.getMarka(),
                pojazd.getModel(),
                pojazd.getRok_produkcji(),
                pojazd.getKraj(),
                pojazd.getCena(),
                pojazd.getNr_Klienta()
        );
    }
    public Pojazd get(int id){
        Object[] args = {id};
        String sql = "SELECT * FROM \"Pojazdy\" WHERE \"Nr_Pojazdu\" = " + args[0]; Pojazd pojazd = jdbcTemplate.queryForObject(sql,
                BeanPropertyRowMapper.newInstance(Pojazd.class));
        return pojazd;
    }

    public void update(Pojazd pojazd) {
        String sql = "UPDATE \"Pojazdy\" SET" +
                "\"VIN\" = :VIN, " +
                "\"Marka\" = :marka, " +
                "\"Model\" = :model, " +
                "\"Kraj\" = :kraj, " +
                "\"Rok_produkcji\" = :rok_produkcji, " +
                "\"Cena\" = :cena, " +
                "\"Nr_Klienta\" = :nr_Klienta " +
                "WHERE \"Nr_Pojazdu\" = :nr_Pojazdu";
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(pojazd);
        NamedParameterJdbcTemplate template=new NamedParameterJdbcTemplate(jdbcTemplate);
        template.update(sql,param);
    }


    public void delete(int id){
        String sql = "DELETE FROM \"Pojazdy\" WHERE \"Nr_Pojazdu\" = ?";
        jdbcTemplate.update(sql,id);
    }
    // Sprawdza, czy klient jest powiązany z jakimkolwiek zamówieniem
    public boolean isCarAssociatedWithOrders(int carId) {
        String checkOrderSql = "SELECT COUNT(*) FROM zamowienia WHERE NRPOJAZDU = ?";
        int orderCount = jdbcTemplate.queryForObject(checkOrderSql, Integer.class, carId);
        return orderCount > 0;
    }

    // Metoda pomocnicza, która zwraca prawdę, jeśli klient jest powiązany z zamówieniami
    public boolean canDeleteClient(int id) {
        return !isCarAssociatedWithOrders(id);
    }
}
