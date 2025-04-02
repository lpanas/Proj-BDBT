package bdbt_bada_project.SpringApplication;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class zamowienia {
    private int nrzamowienia;
    private String status;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate datazamowienia;
    private int nrpojazdu;
    private int nrklienta;
    private String imie; // Imię klienta
    private String nazwisko; // Nazwisko klienta
    private String marka; // Marka pojazdu
    private String model; // Model pojazdu
    private double cena; // Cena pojazdu

    // Konstruktor bezargumentowy (domyślny)
    public zamowienia() {
        this.datazamowienia = LocalDate.now(); // Ustawienie bieżącej daty
        this.status = "oczekujący";           // Ustawienie domyślnego statusu
    }

    public zamowienia(int nrzamowienia, String status, LocalDate datazamowienia, int nrpojazdu, int nrklienta) {
        this.nrzamowienia = nrzamowienia;
        this.status = (status != null) ? status : "oczekujący"; // Domyślny status, jeśli status to null
        this.datazamowienia = (datazamowienia != null) ? datazamowienia : LocalDate.now(); // Domyślna data
        this.nrpojazdu = nrpojazdu;
        this.nrklienta = nrklienta;
    }
    // Konstruktor z argumentami dla wszystkich pól
    public zamowienia(int nrzamowienia, String status, LocalDate datazamowienia, int nrpojazdu, int nrklienta, String imie, String nazwisko, String marka, String model, double cena) {
        this.nrzamowienia = nrzamowienia;
        this.status = (status != null) ? status : "oczekujący";
        this.datazamowienia = (datazamowienia != null) ? datazamowienia : LocalDate.now();
        this.nrpojazdu = nrpojazdu;
        this.nrklienta = nrklienta;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.marka = marka;
        this.model = model;
        this.cena = cena;
    }

    // Settery
    public void setNrzamowienia(int nrzamowienia) {
        this.nrzamowienia = nrzamowienia;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDatazamowienia(LocalDate datazamowienia) {
        this.datazamowienia = datazamowienia;
    }

    public void setNrpojazdu(int nrpojazdu) {
        this.nrpojazdu = nrpojazdu;
    }

    public void setNrklienta(int nrklienta) {
        this.nrklienta = nrklienta;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    // Gettery
    public int getNrzamowienia() {
        return nrzamowienia;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getDatazamowienia() {
        return datazamowienia;
    }

    public int getNrpojazdu() {
        return nrpojazdu;
    }

    public int getNrklienta() {
        return nrklienta;
    }

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public String getMarka() {
        return marka;
    }

    public String getModel() {
        return model;
    }

    public double getCena() {
        return cena;
    }

    // Metoda toString
    @Override
    public String toString() {
        return "zamowienia{" +
                "nrzamowienia=" + nrzamowienia +
                ", status='" + status + '\'' +
                ", datazamowienia=" + datazamowienia +
                ", nrpojazdu=" + nrpojazdu +
                ", nrklienta=" + nrklienta +
                ", imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", marka='" + marka + '\'' +
                ", model='" + model + '\'' +
                ", cena=" + cena +
                '}';
    }
}
