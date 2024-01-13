drop schema IF exists projekt cascade;

create schema projekt;

CREATE TABLE projekt.uzytkownicy (
                login VARCHAR(20) NOT NULL,
                haslo VARCHAR(20) NOT NULL,
                adres_email VARCHAR,
                specjalne_uprawnienia BOOLEAN DEFAULT FALSE NOT NULL,
                CONSTRAINT uzytkownicy_pk PRIMARY KEY (login)
);


CREATE SEQUENCE projekt.hotele_hotel_id_seq_1;

CREATE TABLE projekt.hotele (
                hotel_id INTEGER NOT NULL DEFAULT nextval('projekt.hotele_hotel_id_seq_1'),
                nazwa VARCHAR NOT NULL,
                miasto VARCHAR NOT NULL,
                adres VARCHAR NOT NULL,
                srednia_ocena NUMERIC(3,2),
                CONSTRAINT hotele_pk PRIMARY KEY (hotel_id, nazwa)
);


ALTER SEQUENCE projekt.hotele_hotel_id_seq_1 OWNED BY projekt.hotele.hotel_id;

CREATE SEQUENCE projekt.kategorie_pokoi_kategoria_id_seq;

CREATE TABLE projekt.kategorie_pokoi (
                kategoria_id INTEGER NOT NULL DEFAULT nextval('projekt.kategorie_pokoi_kategoria_id_seq'),
                nazwa_kategorii VARCHAR NOT NULL,
                podstawa_cenowa DOUBLE PRECISION NOT NULL,
                CONSTRAINT kategorie_pokoi_pk PRIMARY KEY (kategoria_id)
);


ALTER SEQUENCE projekt.kategorie_pokoi_kategoria_id_seq OWNED BY projekt.kategorie_pokoi.kategoria_id;

CREATE SEQUENCE projekt.pokoje_pokoj_id_seq;

CREATE TABLE projekt.pokoje (
                pokoj_id INTEGER NOT NULL DEFAULT nextval('projekt.pokoje_pokoj_id_seq'),
                nazwa VARCHAR NOT NULL,
                hotel_id INTEGER NOT NULL,
                kategoria_id INTEGER NOT NULL,
                max_gosci INTEGER NOT NULL,
                obecnie_zajety BOOLEAN,
                CONSTRAINT pokoje_pk PRIMARY KEY (pokoj_id)
);


ALTER SEQUENCE projekt.pokoje_pokoj_id_seq OWNED BY projekt.pokoje.pokoj_id;

CREATE SEQUENCE projekt.goscie_gosc_id_seq;

CREATE TABLE projekt.goscie (
                gosc_id INTEGER NOT NULL DEFAULT nextval('projekt.goscie_gosc_id_seq'),
                login VARCHAR(20) NOT NULL,
                imie VARCHAR NOT NULL,
                nazwisko VARCHAR NOT NULL,
                pesel CHAR(11) NOT NULL,
                nr_telefonu VARCHAR(9) NOT NULL,
                CONSTRAINT gosc_id PRIMARY KEY (gosc_id)
);


ALTER SEQUENCE projekt.goscie_gosc_id_seq OWNED BY projekt.goscie.gosc_id;

CREATE SEQUENCE projekt.rezerwacje_rezerwacja_id_seq;

CREATE TABLE projekt.rezerwacje (
                rezerwacja_id INTEGER NOT NULL DEFAULT nextval('projekt.rezerwacje_rezerwacja_id_seq'),
                pokoj_id INTEGER NOT NULL,
                gosc_id INTEGER NOT NULL,
                poczatek_pobytu DATE NOT NULL,
                koniec_pobytu DATE NOT NULL,
                liczba_gosci INTEGER NOT NULL,
                CONSTRAINT rezerwacje_pk PRIMARY KEY (rezerwacja_id, pokoj_id, gosc_id)
);


ALTER SEQUENCE projekt.rezerwacje_rezerwacja_id_seq OWNED BY projekt.rezerwacje.rezerwacja_id;

CREATE SEQUENCE projekt.rachunki_rachunek_id_seq;

CREATE TABLE projekt.rachunki (
                rachunek_id INTEGER NOT NULL DEFAULT nextval('projekt.rachunki_rachunek_id_seq'),
                pokoj_id INTEGER NOT NULL,
                gosc_id INTEGER NOT NULL,
                rezerwacja_id INTEGER NOT NULL,
                suma_kosztow NUMERIC(7,2) NOT NULL,
                forma_platnosci VARCHAR NOT NULL,
                data_zaplaty DATE,
                CONSTRAINT rachunki_pk PRIMARY KEY (rachunek_id)
);


ALTER SEQUENCE projekt.rachunki_rachunek_id_seq OWNED BY projekt.rachunki.rachunek_id;

CREATE SEQUENCE projekt.opinie_opinia_id_seq;

CREATE TABLE projekt.opinie (
                opinia_id NUMERIC NOT NULL DEFAULT nextval('projekt.opinie_opinia_id_seq'),
                gosc_id INTEGER NOT NULL,
                pokoj_id INTEGER NOT NULL,
                rezerwacja_id INTEGER NOT NULL,
                ocena_wyposazenie INTEGER NOT NULL,
                ocena_czystosc INTEGER NOT NULL,
                ocena_obsluga INTEGER NOT NULL,
                ocena_ogolna INTEGER NOT NULL,
                opinia_tekst VARCHAR,
                CONSTRAINT opinie_pk PRIMARY KEY (opinia_id)
);


ALTER SEQUENCE projekt.opinie_opinia_id_seq OWNED BY projekt.opinie.opinia_id;

ALTER TABLE projekt.goscie ADD CONSTRAINT uzytkownicy_goscie_fk
FOREIGN KEY (login)
REFERENCES projekt.uzytkownicy (login)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE projekt.pokoje ADD CONSTRAINT hotele_pokoje_fk
FOREIGN KEY (hotel_id, nazwa)
REFERENCES projekt.hotele (hotel_id, nazwa)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE projekt.pokoje ADD CONSTRAINT kategorie_pokoi_pokoje_fk
FOREIGN KEY (kategoria_id)
REFERENCES projekt.kategorie_pokoi (kategoria_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE projekt.rezerwacje ADD CONSTRAINT pokoje_rezerwacje_fk
FOREIGN KEY (pokoj_id)
REFERENCES projekt.pokoje (pokoj_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE projekt.rezerwacje ADD CONSTRAINT goscie_rezerwacje_fk
FOREIGN KEY (gosc_id)
REFERENCES projekt.goscie (gosc_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE projekt.opinie ADD CONSTRAINT rezerwacje_opinie_fk
FOREIGN KEY (rezerwacja_id, pokoj_id, gosc_id)
REFERENCES projekt.rezerwacje (rezerwacja_id, pokoj_id, gosc_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE projekt.rachunki ADD CONSTRAINT rezerwacje_rachunki_fk
FOREIGN KEY (rezerwacja_id, gosc_id, pokoj_id)
REFERENCES projekt.rezerwacje (rezerwacja_id, gosc_id, pokoj_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;