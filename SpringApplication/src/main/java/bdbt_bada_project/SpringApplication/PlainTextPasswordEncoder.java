package bdbt_bada_project.SpringApplication;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PlainTextPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        // Zwracamy surowe hasło, ponieważ nie chcemy go haszować
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // Porównanie hasła znak po znaku
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }

        // Porównujemy oba ciągi znaków
        return rawPassword.toString().equals(encodedPassword);
    }
}
