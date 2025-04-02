package bdbt_bada_project.SpringApplication;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Pojazd {
    private int Nr_Pojazdu;
    private String VIN;
    private String Marka;
    private String Model;
    private Integer Nr_Klienta;
    private double Cena;
    private String Kraj;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate Rok_produkcji;

    public Pojazd(){

    }

    public Pojazd(int nr_Pojazdu, String VIN, String marka, String model, Integer nr_Klienta,  double cena, String kraj, LocalDate rok_produkcji) {
        super();
        Nr_Pojazdu = nr_Pojazdu;
        this.VIN = VIN;
        Marka = marka;
        Model = model;
        Nr_Klienta = nr_Klienta;
        Cena = cena;
        Kraj = kraj;
        Rok_produkcji=rok_produkcji;
    }

    public int getNr_Pojazdu() {
        return Nr_Pojazdu;
    }

    public void setNr_Pojazdu(int nr_Pojazdu) {
        Nr_Pojazdu = nr_Pojazdu;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public String getMarka() {
        return Marka;
    }

    public void setMarka(String marka) {
        Marka = marka;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public LocalDate getRok_produkcji() {
        return Rok_produkcji;
    }

    public void setRok_produkcji(LocalDate rok_produkcji) {
        Rok_produkcji = rok_produkcji;
    }

    public String getKraj() {
        return Kraj;
    }

    public void setKraj(String kraj) {
        Kraj = kraj;
    }

    public double getCena() {
        return Cena;
    }

    public void setCena(double cena) {
        Cena = cena;
    }

    public Integer getNr_Klienta() {
        return Nr_Klienta;
    }

    public void setNr_Klienta(Integer nr_Klienta) {
        Nr_Klienta = nr_Klienta;
    }

    @Override
    public String toString() {
        return "Pojazd{" +
                "Nr_Pojazdu=" + Nr_Pojazdu +
                ", VIN=" + VIN +
                ", Marka='" + Marka + '\'' +
                ", Model='" + Model + '\'' +
                ", Rok_produkcji=" + Rok_produkcji +
                ", Kraj='" + Kraj + '\'' +
                ", Cena=" + Cena +
                ", Nr_Klienta=" + Nr_Klienta +
                '}';
    }
}



