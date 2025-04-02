/*
Created: 30.11.2024
Modified: 17.12.2024
Project: Salon Samochodowy
Model: Salon_samochodowy
Author: Lukasz Panasiuk, Lukasz Kazulo
Database: Oracle 18c
*/


-- Create tables section -------------------------------------------------

-- Table Salony_Samochodowe

CREATE TABLE "Salony_Samochodowe"(
  "Nr_Salonu" Integer NOT NULL,
  "Nazwa" Varchar2(50 ) NOT NULL,
  "Data_otwarcia" Date NOT NULL,
  "Marka" Varchar2(20 ) NOT NULL,
  "Nr_Adresu" Integer NOT NULL,
  "Nr_Wlasciciela" Integer NOT NULL
)
TABLESPACE "TS_SALONY"
/

-- Create indexes for table Salony_Samochodowe

CREATE INDEX "adres_salonu" ON "Salony_Samochodowe" ("Nr_Adresu")
TABLESPACE "TS_INDEKSY"
/

CREATE INDEX "wlasciciel_salonu" ON "Salony_Samochodowe" ("Nr_Wlasciciela")
TABLESPACE "TS_INDEKSY"
/

-- Add keys for table Salony_Samochodowe

ALTER TABLE "Salony_Samochodowe" ADD CONSTRAINT "PK <Nr_salonu>" PRIMARY KEY ("Nr_Salonu")
   USING INDEX TABLESPACE "TS_INDEKSY"
/

-- Table Pracownicy

CREATE TABLE "Pracownicy"(
  "Nr_Pracownika" Integer NOT NULL,
  "Imie" Varchar2(20 ) NOT NULL,
  "Nazwisko" Varchar2(20 ) NOT NULL,
  "PESEL" Char(11 ),
  "Plec" Char(1 ) NOT NULL
        CHECK ("Plec" IN ('K','M')),
  "Nr_telefonu" Varchar2(12 ) NOT NULL,
  "Email" Varchar2(30 ) NOT NULL,
  "Data_urodzenia" Date NOT NULL,
  "Stanowisko" Varchar2(20 ) NOT NULL,
  "Wynagrodzenie" Number(10,2) NOT NULL,
  "Nr_salonu" Integer NOT NULL,
  "Nr_Adresu" Integer NOT NULL
)
TABLESPACE "TS_PRACOWNICY_KLIENCI"
/

-- Create indexes for table Pracownicy

CREATE INDEX "IX_salon_pracownika" ON "Pracownicy" ("Nr_salonu")
TABLESPACE "TS_INDEKSY"
/

CREATE INDEX "IX_adres_pracownika" ON "Pracownicy" ("Nr_Adresu")
TABLESPACE "TS_INDEKSY"
/

-- Add keys for table Pracownicy

ALTER TABLE "Pracownicy" ADD CONSTRAINT "PK <Nr_pracownika>" PRIMARY KEY ("Nr_Pracownika")
   USING INDEX TABLESPACE "TS_INDEKSY"
/

-- Table Sprzedawcy

CREATE TABLE "Sprzedawcy"(
  "Dzial" Varchar2(30 ) NOT NULL,
  "Komputer" Integer,
  "Nr_Pracownika" Integer NOT NULL
)
TABLESPACE "TS_PRACOWNICY_KLIENCI"
/

-- Add keys for table Sprzedawcy

ALTER TABLE "Sprzedawcy" ADD CONSTRAINT "Nr_Sprzedawcy" PRIMARY KEY ("Nr_Pracownika")
   USING INDEX TABLESPACE "TS_INDEKSY"
/

-- Table Mechanicy

CREATE TABLE "Mechanicy"(
  "Uprawnienie" Varchar2(50 ) NOT NULL,
  "Mobilnosc" Varchar2(3 ) NOT NULL
        CHECK ("Mobilnosc" IN ('TAK','NIE')),
  "Nr_narzedzi" Integer,
  "Nr_Pracownika" Integer NOT NULL
)
TABLESPACE "TS_PRACOWNICY_KLIENCI"
/

-- Add keys for table Mechanicy

ALTER TABLE "Mechanicy" ADD CONSTRAINT "Nr_Mechanika" PRIMARY KEY ("Nr_Pracownika")
   USING INDEX TABLESPACE "TS_INDEKSY"
/

-- Table Pojazdy

CREATE TABLE "Pojazdy"(
  "Nr_Pojazdu" Integer NOT NULL,
  "VIN" Char(17 ) NOT NULL,
  "Marka" Varchar2(20 ) NOT NULL,
  "Model" Varchar2(50 ) NOT NULL,
  "Rok_produkcji" Date NOT NULL,
  "Nadwozie" Varchar2(9 ) NOT NULL
        CHECK ("Nadwozie" IN ('Sedan','Kombi','Minivan','Coupe','Kabriolet','SUV')),
  "Paliwo" Varchar2(11 ) NOT NULL
        CHECK ("Paliwo" IN ('Benzyna','Benzyna+LPG','Diesel','Hybryda','Elektryk')),
  "Kraj" Varchar2(30 ) NOT NULL,
  "Moc" Integer NOT NULL,
  "Silnik" Integer NOT NULL,
  "Skrzynia" Varchar2(12 ) NOT NULL
        CHECK ("Skrzynia" IN ('Manualna','Automatyczna')),
  "Naped" Varchar2(5 ) NOT NULL
        CHECK ("Naped" IN ('Przod','Tyl','4x4')),
  "Cena" Number(10,2),
  "Dzialanie" Varchar2(30 ),
  "Koszt_naprawy" Number(10,2),
  "Data_zostawienia" Date,
  "Nr_salonu" Integer NOT NULL,
  "Nr_Klienta" Integer NOT NULL
)
TABLESPACE "TS_SALONY"
/

-- Create indexes for table Pojazdy

CREATE INDEX "IX_salon_pojazdu" ON "Pojazdy" ("Nr_salonu")
TABLESPACE "TS_INDEKSY"
/

CREATE INDEX "IX_Relationship4" ON "Pojazdy" ("Nr_Klienta")
TABLESPACE "TS_TRANSAKCJE"
/

-- Add keys for table Pojazdy

ALTER TABLE "Pojazdy" ADD CONSTRAINT "PK <Nr_pojazdu>" PRIMARY KEY ("Nr_Pojazdu")
   USING INDEX TABLESPACE "TS_INDEKSY"
/

-- Table Klienci

CREATE TABLE "Klienci"(
  "Nr_Klienta" Integer NOT NULL,
  "Imie" Varchar2(20 ) NOT NULL,
  "Nazwisko" Varchar2(20 ) NOT NULL,
  "Nr_telefonu" Varchar2(12 ) NOT NULL,
  "Email" Varchar2(30 ),
  "Nr_salonu" Integer NOT NULL,
  "Nr_Adresu" Integer NOT NULL,
  "Nr_dokumentu" Integer NOT NULL
)
TABLESPACE "TS_PRACOWNICY_KLIENCI"
/

-- Create indexes for table Klienci

CREATE INDEX "IX_salon_klienta" ON "Klienci" ("Nr_salonu")
TABLESPACE "TS_INDEKSY"
/

CREATE INDEX "IX_adres_klienta" ON "Klienci" ("Nr_Adresu")
TABLESPACE "TS_INDEKSY"
/

CREATE INDEX "IX_dokument_klienta" ON "Klienci" ("Nr_dokumentu")
TABLESPACE "TS_INDEKSY"
/

-- Add keys for table Klienci

ALTER TABLE "Klienci" ADD CONSTRAINT "PK <Nr_klienta>" PRIMARY KEY ("Nr_Klienta")
   USING INDEX TABLESPACE "TS_INDEKSY"
/

-- Table Naprawy

CREATE TABLE "Naprawy"(
  "Nr_Pojazdu" Integer NOT NULL,
  "Nr_Pracownika" Integer NOT NULL
)
TABLESPACE "TS_TRANSAKCJE"
/

-- Table Sprzedaże

CREATE TABLE "Sprzedaże"(
  "Nr_Pojazdu" Integer NOT NULL,
  "Nr_Pracownika" Integer NOT NULL
)
TABLESPACE "TS_TRANSAKCJE"
/

-- Table Obslugi

CREATE TABLE "Obslugi"(
  "Nr_Klienta" Integer NOT NULL,
  "Nr_Pracownika" Integer NOT NULL
)
TABLESPACE "TS_TRANSAKCJE"
/

-- Table Adresy

CREATE TABLE "Adresy"(
  "Nr_Adresu" Integer NOT NULL,
  "Kraj" Varchar2(50 ) NOT NULL,
  "Wojewodztwo" Varchar2(50 ),
  "Stan" Varchar2(50 ),
  "Miasto" Varchar2(50 ) NOT NULL,
  "Ulica" Varchar2(50 ) NOT NULL,
  "Numer_budynku" Integer NOT NULL,
  "Kod_pocztowy" Varchar2(6 ) NOT NULL,
  "Poczta" Varchar2(50 ) NOT NULL
)
TABLESPACE "TS_SALONY"
/

-- Add keys for table Adresy

ALTER TABLE "Adresy" ADD CONSTRAINT "PK_Adresy" PRIMARY KEY ("Nr_Adresu")
   USING INDEX TABLESPACE "TS_INDEKSY"
/

-- Table and Columns comments section

COMMENT ON COLUMN "Adresy"."Nr_Adresu" IS 'Unikalny identyfikator adresu'
/
COMMENT ON COLUMN "Adresy"."Wojewodztwo" IS 'Nazwa województwa'
/
COMMENT ON COLUMN "Adresy"."Stan" IS 'Nazwa stanu'
/
COMMENT ON COLUMN "Adresy"."Miasto" IS 'Nazwa miasta'
/
COMMENT ON COLUMN "Adresy"."Ulica" IS 'Nazwa ulicy'
/
COMMENT ON COLUMN "Adresy"."Numer_budynku" IS 'Numer budynku'
/
COMMENT ON COLUMN "Adresy"."Kod_pocztowy" IS 'Kod pocztowy, ma postać XX-XXX'
/
COMMENT ON COLUMN "Adresy"."Poczta" IS 'Miasto poczty'
/

-- Table Wlasciciele

CREATE TABLE "Wlasciciele"(
  "Nr_Wlasciciela" Integer NOT NULL,
  "Imie" Varchar2(30 ) NOT NULL,
  "Nazwisko" Varchar2(30 ) NOT NULL,
  "Nr_telefonu" Varchar2(12 ) NOT NULL,
  "Email" Varchar2(30 ) NOT NULL,
  "Nr_Adresu" Integer NOT NULL
)
TABLESPACE "TS_SALONY"
/

-- Create indexes for table Wlasciciele

CREATE INDEX "adres_wlasciciela" ON "Wlasciciele" ("Nr_Adresu")
TABLESPACE "TS_INDEKSY"
/

-- Add keys for table Wlasciciele

ALTER TABLE "Wlasciciele" ADD CONSTRAINT "PK_Wlasciciele" PRIMARY KEY ("Nr_Wlasciciela")
   USING INDEX TABLESPACE "TS_INDEKSY"
/

-- Table and Columns comments section

COMMENT ON COLUMN "Wlasciciele"."Nr_Wlasciciela" IS 'Unikalny identyfikator wlasciciela'
/
COMMENT ON COLUMN "Wlasciciele"."Imie" IS 'Imie wlasciciela'
/
COMMENT ON COLUMN "Wlasciciele"."Nazwisko" IS 'Nazwisko wlasciciela'
/
COMMENT ON COLUMN "Wlasciciele"."Nr_telefonu" IS 'Nr telefonu wlasciciela'
/

-- Table Dokumenty

CREATE TABLE "Dokumenty"(
  "Nr_dokumentu" Integer NOT NULL,
  "Nr_seryjny" Varchar2(30 ),
  "Data_waznosci" Date NOT NULL
)
TABLESPACE "TS_STATYCZNE"
/

-- Add keys for table Dokumenty

ALTER TABLE "Dokumenty" ADD CONSTRAINT "PK_Dokumenty" PRIMARY KEY ("Nr_dokumentu")
   USING INDEX TABLESPACE "TS_INDEKSY"
/

-- Table Jezyki_obce

CREATE TABLE "Jezyki_obce"(
  "Nr_Jezyka" Integer NOT NULL,
  "Nazwa" Varchar2(30 ) NOT NULL
)
TABLESPACE "TS_STATYCZNE"
/

-- Add keys for table Jezyki_obce

ALTER TABLE "Jezyki_obce" ADD CONSTRAINT "PK_Jezyki_obce" PRIMARY KEY ("Nr_Jezyka")
   USING INDEX TABLESPACE "TS_INDEKSY"
/

-- Table Znajomosci_jezykow

CREATE TABLE "Znajomosci_jezykow"(
  "Nr_Pracownika" Integer NOT NULL,
  "Nr_Jezyka" Integer NOT NULL,
  "Poziom" Char(2 ) NOT NULL
        CHECK ("Poziom" IN ('A1','A2','B1','B2','C1','C2'))
)
TABLESPACE "TS_STATYCZNE"
/

-- Add keys for table Znajomosci_jezykow

ALTER TABLE "Znajomosci_jezykow" ADD CONSTRAINT "PK_Znajomosci_jezykow" PRIMARY KEY ("Nr_Pracownika","Nr_Jezyka")
   USING INDEX TABLESPACE "TS_INDEKSY"
/


-- Create foreign keys (relationships) section ------------------------------------------------- 

ALTER TABLE "Pojazdy" ADD CONSTRAINT "Oferuje pojazd" FOREIGN KEY ("Nr_salonu") REFERENCES "Salony_Samochodowe" ("Nr_Salonu")
/



ALTER TABLE "Pracownicy" ADD CONSTRAINT "Zatrudnia pracownika" FOREIGN KEY ("Nr_salonu") REFERENCES "Salony_Samochodowe" ("Nr_Salonu")
/



ALTER TABLE "Klienci" ADD CONSTRAINT "Posiada klienta" FOREIGN KEY ("Nr_salonu") REFERENCES "Salony_Samochodowe" ("Nr_Salonu")
/



ALTER TABLE "Salony_Samochodowe" ADD CONSTRAINT "Salon_ma_adres" FOREIGN KEY ("Nr_Adresu") REFERENCES "Adresy" ("Nr_Adresu")
/



ALTER TABLE "Pracownicy" ADD CONSTRAINT "Pracownika_ma_adres" FOREIGN KEY ("Nr_Adresu") REFERENCES "Adresy" ("Nr_Adresu")
/



ALTER TABLE "Klienci" ADD CONSTRAINT "Klient_ma_adres" FOREIGN KEY ("Nr_Adresu") REFERENCES "Adresy" ("Nr_Adresu")
/



ALTER TABLE "Salony_Samochodowe" ADD CONSTRAINT "Salon_ma_wlasciciela" FOREIGN KEY ("Nr_Wlasciciela") REFERENCES "Wlasciciele" ("Nr_Wlasciciela")
/



ALTER TABLE "Klienci" ADD CONSTRAINT "Klient_ma_dokument" FOREIGN KEY ("Nr_dokumentu") REFERENCES "Dokumenty" ("Nr_dokumentu")
/



ALTER TABLE "Wlasciciele" ADD CONSTRAINT "Wlasciciel_ma_adres" FOREIGN KEY ("Nr_Adresu") REFERENCES "Adresy" ("Nr_Adresu")
/



ALTER TABLE "Znajomosci_jezykow" ADD CONSTRAINT "Sprzedawca_zna_jezyk" FOREIGN KEY ("Nr_Pracownika") REFERENCES "Sprzedawcy" ("Nr_Pracownika")
/



ALTER TABLE "Znajomosci_jezykow" ADD CONSTRAINT "Jezyk_jest_znany" FOREIGN KEY ("Nr_Jezyka") REFERENCES "Jezyki_obce" ("Nr_Jezyka")
/



ALTER TABLE "Pojazdy" ADD CONSTRAINT "Kupuje" FOREIGN KEY ("Nr_Klienta") REFERENCES "Klienci" ("Nr_Klienta")
/





