package bdbt_bada_project.SpringApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final MyUserDetailsService myUserDetailsService;

    // Konstruktor wstrzykujący MyUserDetailsService
    public SecurityConfiguration(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Używamy naszego niestandardowego PasswordEncoder
        auth.userDetailsService(myUserDetailsService)
                .passwordEncoder(plainTextPasswordEncoder()); // Ustawiamy niestandardowy PasswordEncoder
    }

    @Bean
    public PasswordEncoder plainTextPasswordEncoder() {
        return new PlainTextPasswordEncoder(); // Zwracamy naszą implementację PasswordEncoder
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/index", "/login", "/register").permitAll() // Dostęp bez logowania do stron publicznych
                .antMatchers("/resources/static/**","/o-nas","/on_as","/saveK","rej").permitAll()
                .antMatchers("/main").authenticated() // Wymaga zalogowania
                .antMatchers("/main_admin","/pojazdy","/new","/save","/edit/{nrPojazdu}","/update","/delete/{id}","/editK/{nrklienta}","/deleteK/{id}","/updateK","/klient_wszyscy","/odrzutZ/{nrzamowienia}","/akceptZ/{nrzamowienia}","/zamowienia_wszystkie").hasRole("ADMIN") // Tylko admin może wejść na /main_admin
                .antMatchers("/main_user","/updateU","/editU/{nrklienta}","/klient_wszyscyU/{login}","/pojazdyU","/deleteZU/{id}","/zamowienia_wszystkieU","/bladZamowienia","/zamowU/{nrPojazdu}").hasRole("USER") // Tylko użytkownicy mogą wejść na /main_user
                .and()
                .formLogin() // Konfiguracja logowania
                .loginPage("/login")
                .defaultSuccessUrl("/main") // Strona po udanym logowaniu
                .permitAll()
                .and()
                .logout() // Konfiguracja wylogowania
                .logoutUrl("/logout")  // Ustawiamy URL wylogowania na /logout
                .logoutSuccessUrl("/index") // Strona po wylogowaniu
                .invalidateHttpSession(true) // Inwalidacja sesji
                .clearAuthentication(true) // Usunięcie autentykacji
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied");
    }
}
