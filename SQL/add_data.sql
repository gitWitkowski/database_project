ALTER TABLE projekt.rezerwacje 
ADD CONSTRAINT 
sprawdz_czy_daty_w_dobrej_kolejnosci 
CHECK ( funkcje.sprawdz_czy_daty_w_dobrej_kolejnosci(poczatek_pobytu, koniec_pobytu) );

CREATE VIEW projekt.rezerwacje_goscia AS 
SELECT kp.nazwa_kategorii, h.nazwa, h.miasto, r.poczatek_pobytu, r.koniec_pobytu, r.liczba_gosci, r.gosc_id, r.rezerwacja_id
FROM projekt.rezerwacje r join projekt.pokoje p using (pokoj_id)
join projekt.kategorie_pokoi kp using(kategoria_id)
join projekt.hotele h using(hotel_id);

CREATE VIEW projekt.rachunki_goscia AS 
SELECT r2.rezerwacja_id, h.nazwa, r2.forma_platnosci, r2.suma_kosztow, kp.podstawa_cenowa, r.koniec_pobytu-r.poczatek_pobytu as ilosc_nocy, r.gosc_id 
FROM projekt.rezerwacje r join projekt.rachunki r2 on r.gosc_id =r2.gosc_id and r.rezerwacja_id =r2.rezerwacja_id
join projekt.pokoje p on p.pokoj_id =r.pokoj_id 
join projekt.kategorie_pokoi kp on p.kategoria_id =kp.kategoria_id 
join projekt.hotele h using(hotel_id);

drop  TRIGGER if exists rachunek ON projekt.rezerwacje;
CREATE TRIGGER rachunek after insert ON projekt.rezerwacje 
FOR EACH ROW EXECUTE PROCEDURE funkcje.dodaj_rachunek();


insert into projekt.hotele(nazwa, miasto, adres) values('hotel 1', 'Krakow', 'adres 1');
insert into projekt.hotele(nazwa, miasto, adres) values('hotel 2', 'Lodz', 'adres 2');
insert into projekt.hotele(nazwa, miasto, adres) values('hotel 3', 'Warszawa', 'adres 3');
insert into projekt.hotele(nazwa, miasto, adres) values('hotel 4', 'Warszawa', 'adres 4');
insert into projekt.hotele(nazwa, miasto, adres) values('hotel 5', 'Warszawa', 'adres 5');
insert into projekt.hotele(nazwa, miasto, adres) values('hotel 6', 'Warszawa', 'adres 6');
insert into projekt.hotele(nazwa, miasto, adres) values('hotel 7', 'Warszawa', 'adres 7');



insert into projekt.kategorie_pokoi(nazwa_kategorii, podstawa_cenowa) values('kategoria 1', 500);
insert into projekt.kategorie_pokoi(nazwa_kategorii, podstawa_cenowa) values('kategoria 2', 400);
insert into projekt.kategorie_pokoi(nazwa_kategorii, podstawa_cenowa) values('kategoria 3', 300);
insert into projekt.kategorie_pokoi(nazwa_kategorii, podstawa_cenowa) values('kategoria 4', 200);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='hotel 1'), 'hotel 1',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='kategoria 1'),
5
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='hotel 1'), 'hotel 1',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='kategoria 2'),
5
);

insert into projekt.pokoje(hotel_id, nazwa, kategoria_id, max_gosci) values(
(select hotel_id from projekt.hotele where nazwa='hotel 1'), 'hotel 1',
(select kategoria_id from projekt.kategorie_pokoi where nazwa_kategorii='kategoria 3'),
5
);

insert into projekt.uzytkownicy(login, haslo, specjalne_uprawnienia) values ('admin', 'admin', true);
insert into projekt.uzytkownicy(login, haslo, specjalne_uprawnienia) values('test', 'test', false);
insert into projekt.goscie(login, imie, nazwisko, pesel, nr_telefonu) values('test', 'jacek', 'placek', '12121212', '121212112');