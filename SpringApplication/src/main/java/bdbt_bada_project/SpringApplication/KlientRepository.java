package bdbt_bada_project.SpringApplication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KlientRepository extends JpaRepository<klienci, Long> {
    Optional<klienci> findByLogin(String login); // Definicja metody do wyszukiwania po loginie
}
