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








CREATE OR REPLACE FUNCTION projekt.wolne_pokoje(
		miastoID INTEGER,
		poczatek DATE,
		koniec DATE,
		kategoriaID INTEGER,
		iloscOsob INTEGER
	)
	RETURNS TABLE (
        pokoj_id INTEGER,
		nazwa VARCHAR,
        hotel_id INTEGER,
        kategoria_id INTEGER,
        max_gosci INTEGER
	) AS $$
    DECLARE
    	rec_pokoj RECORD;
	   	rec_rezerwacja RECORD;
    	czy_nachodza boolean;
    BEGIN
         FOR rec_pokoj IN (	select * from projekt.pokoje p )
         	LOOP
	         	czy_nachodza := false;

	         	pokoj_id := rec_pokoj.pokoj_id;
				nazwa := rec_pokoj.nazwa;
        		hotel_id := rec_pokoj.hotel_id;
        		kategoria_id := rec_pokoj.kategoria_id;
        		max_gosci := rec_pokoj.max_gosci;

	        	FOR rec_rezerwacja IN ( select * from projekt.rezerwacje r where r.pokoj_id=rec_pokoj.pokoj_id )
         			LOOP
						IF ((rec_rezerwacja.poczatek_pobytu, rec_rezerwacja.koniec_pobytu) overlaps (poczatek, koniec)) then
        					czy_nachodza := true;
        				elsif rec_rezerwacja.poczatek_pobytu = koniec then
        					czy_nachodza := true;
        				elsif rec_rezerwacja.koniec_pobytu = poczatek then
        					czy_nachodza := true;
        				end if;
                	end loop;

                	if not czy_nachodza then
               			return next;
               		end if;
         	END LOOP;
	END;
	$$  LANGUAGE 'plpgsql';

    select * from projekt.pokoje where kategoria_id=1;

insert into projekt.uzytkownicy(login, haslo, specjalne_uprawnienia) values('admin', 'admin', false);
insert into projekt.goscie(login, imie, nazwisko, pesel, nr_telefonu) values('admin', 'jacek', 'placek', '12121212', '121212112');

insert into projekt.rezerwacje (pokoj_id, gosc_id, poczatek_pobytu, koniec_pobytu, liczba_gosci)
values(2,1, '2024-01-10', '2024-01-15', 3);

select * from projekt.rezerwacje r ;

--
select * from projekt.wolne_pokoje(1, '2024-01-16','2024-01-22', 1, 3);
--

select * from projekt.rezerwacje r where pokoj_id=2;