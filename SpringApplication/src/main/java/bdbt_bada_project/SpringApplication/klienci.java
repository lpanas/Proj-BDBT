package bdbt_bada_project.SpringApplication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity // Nazwa tabeli w bazie danych
public class klienci implements UserDetails {

    @Id
    @Column(name = "nrklienta")
    private int nrklienta;

    private String imie;

    private String nazwisko;

    private String nrtelefonu;

    private String email;

    private String login;

    private String haslo;

    // Konstruktor bezargumentowy
    public klienci() {
    }

    // Konstruktor z parametrami
    public klienci(int nrKlienta, String imie, String nazwisko, String nrTelefonu, String email, String login, String haslo) {
        this.nrklienta = nrKlienta;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.nrtelefonu = nrTelefonu;
        this.email = email;
        this.login = login;
        this.haslo = haslo;
    }

    // Gettery i settery
    public int getNrklienta() {
        return nrklienta;
    }

    public void setNrklienta(int nrklienta) {
        this.nrklienta = nrklienta;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getNrtelefonu() {
        return nrtelefonu;
    }

    public void setNrtelefonu(String nrtelefonu) {
        this.nrtelefonu = nrtelefonu;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    @Override
    public String toString() {
        return "Klient{" +
                "nrklienta=" + nrklienta +
                ", imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", nrtelefonu='" + nrtelefonu + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", haslo='" + haslo + '\'' +
                '}';
    }

    // Implementacje metod z interfejsu UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return haslo;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
