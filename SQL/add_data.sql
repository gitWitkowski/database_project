ALTER TABLE projekt.rezerwacje 
ADD CONSTRAINT 
sprawdz_czy_daty_w_dobrej_kolejnosci 
CHECK ( funkcje.sprawdz_czy_daty_w_dobrej_kolejnosci(poczatek_pobytu, koniec_pobytu) );

-- Widok przedstawiajacy rezerwacje gosci: kategoria pokoju, nazwa hotelu, poczatek i koniec pobytu, liczba gosci, ID goscia i ID rezerwacji
CREATE VIEW projekt.rezerwacje_goscia AS 
SELECT kp.nazwa_kategorii, h.nazwa, h.miasto, r.poczatek_pobytu, r.koniec_pobytu, r.liczba_gosci, r.gosc_id, r.rezerwacja_id
FROM projekt.rezerwacje r join projekt.pokoje p using (pokoj_id)
join projekt.kategorie_pokoi kp using(kategoria_id)
join projekt.hotele h using(hotel_id);

-- Widok przedstawiajacy rachunki gosci: nazwa hotelu, forma platnosci za pobyt, suma kosztow za dana rezerwacje, podstawa cenowa przy liczeni rachunku, dlugosci pobytu, ilosc nocy oraz ID goscia i ID rezerwacji dla ktorej wystawiony jest rachunek
CREATE VIEW projekt.rachunki_goscia AS 
SELECT h.nazwa, r2.forma_platnosci, r2.suma_kosztow, kp.podstawa_cenowa, r.koniec_pobytu-r.poczatek_pobytu as ilosc_nocy, r.gosc_id, r2.rezerwacja_id
FROM projekt.rezerwacje r join projekt.rachunki r2 on r.gosc_id =r2.gosc_id and r.rezerwacja_id =r2.rezerwacja_id
join projekt.pokoje p on p.pokoj_id =r.pokoj_id 
join projekt.kategorie_pokoi kp on p.kategoria_id =kp.kategoria_id 
join projekt.hotele h using(hotel_id);

-- Widok zawierajacy stalych klientow serwisu: imie, nazwisko, sume wydanych na rezerwacje pieniedzy oraz ilosc zarezerwowanych pobytow.
-- Jako stalych klientow rozumiemy gosci, ktorzy zarezerwowali już co najmniej 4 pobyty oraz wydali ponad 2000 zl  
CREATE VIEW projekt.stali_klienci AS 
SELECT g.imie, g.nazwisko, sum(r2.suma_kosztow) as suma_kosztow, count(r.rezerwacja_id) as ilosc_pobytow
FROM projekt.rezerwacje r join projekt.rachunki r2 on r.gosc_id =r2.gosc_id and r.rezerwacja_id =r2.rezerwacja_id
join projekt.pokoje p on p.pokoj_id =r.pokoj_id 
join projekt.kategorie_pokoi kp on p.kategoria_id =kp.kategoria_id
join projekt.goscie g on g.gosc_id = r.gosc_id 
group by g.imie, g.nazwisko having count(r.rezerwacja_id) > 3 and sum(r2.suma_kosztow) > 2000;


-- dodajemy trigger ktory dodaje rachunek na konto uzytkownika po kazdej dokonanej rezerwacji
drop  TRIGGER if exists rachunek ON projekt.rezerwacje;
CREATE TRIGGER rachunek after insert ON projekt.rezerwacje 
FOR EACH ROW EXECUTE PROCEDURE funkcje.dodaj_rachunek();

drop  TRIGGER if exists rejstracja_walidacja ON projekt.uzytkownicy;
-- tworzymy trigger powiazany z tabela uzytkownicy
CREATE TRIGGER rejstracja_walidacja 
    AFTER INSERT OR UPDATE  ON projekt.uzytkownicy
    FOR EACH ROW EXECUTE PROCEDURE funkcje.walidacja(); 


-- DODANIE PRZYKLADOWYCH DANYCH DO BAZDY

insert into projekt.hotele(nazwa, miasto, adres) values('Hotel Swing', 'Krakow', 'Dobrego Pasterza 124');
insert into projekt.hotele(nazwa, miasto, adres) values('Hotel Marriott', 'Warszawa', 'al. Jerozolimskie 65/79');
insert into projekt.hotele(nazwa, miasto, adres) values('Hampton by Hilton Łódź City Center', 'Lodz', 'Piotrkowska 157');
insert into projekt.hotele(nazwa, miasto, adres) values('Radisson Hotel & Suites', 'Gdansk', 'Chmielna 10-25');
insert into projekt.hotele(nazwa, miasto, adres) values('AC Hotel by Marriott', 'Krakow', 'al. 3 Maja 51');

insert into projekt.kategorie_pokoi(nazwa_kategorii, podstawa_cenowa) values('Kategoria PREMIUM+', 700);
insert into projekt.kategorie_pokoi(nazwa_kategorii, podstawa_cenowa) values('Kategoria PREMIUM', 500);
insert into projekt.kategorie_pokoi(nazwa_kategorii, podstawa_cenowa) values('Kategoria STANDARD', 300);
insert into projekt.kategorie_pokoi(nazwa_kategorii, podstawa_cenowa) values('Kategoria BUDGET', 100);

-- Hotel Swing
insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Hotel Swing'), 'Hotel Swing',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria BUDGET'),3
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Hotel Swing'), 'Hotel Swing',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria STANDARD'),6
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Hotel Swing'), 'Hotel Swing',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria STANDARD'),2
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Hotel Swing'), 'Hotel Swing',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria STANDARD'),3
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Hotel Swing'), 'Hotel Swing',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria BUDGET'),7
);

-- Hotel Marriott
insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Hotel Marriott'), 'Hotel Marriott',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria PREMIUM+'),3
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Hotel Marriott'), 'Hotel Marriott',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria PREMIUM+'),2
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Hotel Marriott'), 'Hotel Marriott',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria PREMIUM'),4
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Hotel Marriott'), 'Hotel Marriott',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria PREMIUM'),2
);

-- Hampton by Hilton Łódź City Center
insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Hampton by Hilton Łódź City Center'), 'Hampton by Hilton Łódź City Center',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria PREMIUM'),4
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Hampton by Hilton Łódź City Center'), 'Hampton by Hilton Łódź City Center',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria PREMIUM'),3
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Hampton by Hilton Łódź City Center'), 'Hampton by Hilton Łódź City Center',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria STANDARD'),5
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Hampton by Hilton Łódź City Center'), 'Hampton by Hilton Łódź City Center',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria STANDARD'),2
);

-- Radisson Hotel & Suites
insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Radisson Hotel & Suites'), 'Radisson Hotel & Suites',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria STANDARD'),2
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Radisson Hotel & Suites'), 'Radisson Hotel & Suites',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria STANDARD'),5
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Radisson Hotel & Suites'), 'Radisson Hotel & Suites',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria STANDARD'),3
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Radisson Hotel & Suites'), 'Radisson Hotel & Suites',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria BUDGET'),4
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Radisson Hotel & Suites'), 'Radisson Hotel & Suites',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria PREMIUM+'),2
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Radisson Hotel & Suites'), 'Radisson Hotel & Suites',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria PREMIUM+'),5
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Radisson Hotel & Suites'), 'Radisson Hotel & Suites',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria PREMIUM'),3
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='Radisson Hotel & Suites'), 'Radisson Hotel & Suites',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria PREMIUM'),4
);

-- AC Hotel by Marriott
insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='AC Hotel by Marriott'), 'AC Hotel by Marriott',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria BUDGET'),4
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='AC Hotel by Marriott'), 'AC Hotel by Marriott',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria BUDGET'),2
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='AC Hotel by Marriott'), 'AC Hotel by Marriott',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria STANDARD'),5
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='AC Hotel by Marriott'), 'AC Hotel by Marriott',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria STANDARD'),4
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='AC Hotel by Marriott'), 'AC Hotel by Marriott',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria PREMIUM'),2
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='AC Hotel by Marriott'), 'AC Hotel by Marriott',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='Kategoria PREMIUM+'),2
);


-- ADMIN
insert into projekt.uzytkownicy(login, haslo, specjalne_uprawnienia) values ('admin', 'admin', true);
insert into projekt.goscie(login, imie, nazwisko, pesel, nr_telefonu) values('admin', 'admin', 'admin', '', '');

-- przykladowi goscie
insert into projekt.uzytkownicy(login, haslo, specjalne_uprawnienia) values('test', 'test', false);
insert into projekt.goscie(login, imie, nazwisko, pesel, nr_telefonu) values('test', 'Jacek', 'Placek', '12345678901', '123456789');

insert into projekt.uzytkownicy(login, haslo, specjalne_uprawnienia) values('adam', '1234', false);
insert into projekt.goscie(login, imie, nazwisko, pesel, nr_telefonu) values('adam', 'Adam', 'Nowak', '09876545678', '999998997');

insert into projekt.uzytkownicy(login, haslo, specjalne_uprawnienia) values('maria', 'abcd', false);
insert into projekt.goscie(login, imie, nazwisko, pesel, nr_telefonu) values('maria', 'Maria', 'Sienkiewicz', '07890564321', '765195019');