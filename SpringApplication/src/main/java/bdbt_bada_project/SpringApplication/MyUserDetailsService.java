package bdbt_bada_project.SpringApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private KlientRepository klientRepository; // Wstrzykiwanie zależności

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Sprawdzamy, czy użytkownik to "admin"
        if ("admin".equals(username)) {
            // Jeśli login to admin, zwróćmy użytkownika "admin" z rolą "ADMIN"
            return new User(
                    "admin",  // login
                    "admin",  // hasło
                    new ArrayList<>() {{ add(() -> "ROLE_ADMIN"); }}  // przypisujemy rolę "ADMIN"
            );
        }

        // Jeśli użytkownik nie jest adminem, sprawdzamy bazę danych
        klienci klient = klientRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika: " + username));

        // Zwróć dane użytkownika do Spring Security
        return new User(
                klient.getUsername(),
                klient.getPassword(),
                new ArrayList<>() {{
                    // Dodajemy rolę "USER"
                    add(() -> "ROLE_USER");
                }}
        );
    }
}
