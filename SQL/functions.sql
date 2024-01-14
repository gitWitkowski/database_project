drop schema IF exists funkcje cascade;

create schema funkcje;


CREATE OR REPLACE FUNCTION funkcje.wolne_pokoje(
		miasto_nazwa text,
		poczatek DATE,
		koniec DATE,
		kategoriaID INTEGER,
		iloscOsob INTEGER
	)
	RETURNS TABLE (
        pokoj_id INTEGER,
		nazwa VARCHAR,
        hotelID INTEGER,
        kategoria_id INTEGER,
        max_gosci INTEGER
	) AS $$
    DECLARE
    	rec_pokoj RECORD;
	   	rec_rezerwacja RECORD;
    	czy_nachodza boolean;
    BEGIN
         FOR rec_pokoj IN (	select * from projekt.pokoje p where p.max_gosci>=iloscOsob and p.hotel_id IN (select h.hotel_id from projekt.hotele h where h.miasto = miasto_nazwa) )
         	LOOP
	         	czy_nachodza := false;

	         	pokoj_id := rec_pokoj.pokoj_id;
				nazwa := rec_pokoj.nazwa;
        		hotelID := rec_pokoj.hotel_id;
        		kategoria_id := rec_pokoj.kategoria_id;
        		max_gosci := rec_pokoj.max_gosci;

	        	FOR rec_rezerwacja IN ( select * from projekt.rezerwacje r where r.pokoj_id=rec_pokoj.pokoj_id)
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

CREATE OR REPLACE FUNCTION funkcje.autoryzuj(
		user_login TEXT,
		user_haslo TEXT
	)	
	RETURNS TABLE (
        autoryzowano bool,
		uprawnienia_admina bool
	) AS $$
	declare
		czy_specjalne_uprawnienia bool;
    begin
	    if exists (select * from projekt.uzytkownicy where login = user_login and haslo = user_haslo) then
        	autoryzowano := true;
        	select specjalne_uprawnienia into czy_specjalne_uprawnienia from projekt.uzytkownicy where login = user_login and haslo = user_haslo;
        	uprawnienia_admina := czy_specjalne_uprawnienia;
    	else 
        	autoryzowano := false;
        	uprawnienia_admina := false;
    	end if;
    	return next;
	END;
	$$  LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION funkcje.dodaj_uzytkownika(
		user_login TEXT,
		user_haslo text,
		user_mail text,
		user_imie text,
		user_nazwisko text,
		user_pesel text,
		user_telefon text
	)	
	RETURNS integer AS $$
	declare
		czy_dodano integer := 3;
--		0 - dodano bez bledow
--		1 - nie dodano, jest taki login juz
--		2 - nie dodano, jest taki mail juz
-- 		3 - inny blad
    begin
	    if exists (select * from projekt.uzytkownicy where login = user_login) then
        	czy_dodano := 1;
    	elsif exists (select * from projekt.uzytkownicy where adres_email = user_mail) then
        	czy_dodano := 2;
        else
        	insert into projekt.uzytkownicy(login, haslo, adres_email, specjalne_uprawnienia) values(user_login, user_haslo, user_mail, false);
        	insert into projekt.goscie(login, imie, nazwisko, pesel, nr_telefonu) values(user_login, user_imie, user_nazwisko, user_pesel, user_telefon);
    		czy_dodano := 0;
        end if;
    		return czy_dodano;
	END;
$$  LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION funkcje.walidacja ()
    RETURNS TRIGGER
    LANGUAGE plpgsql
    AS $$
    BEGIN
    IF LENGTH(NEW.login) = 0 or LENGTH(NEW.login) < 4 THEN
        raise exception 'Login nie moze byc pusty'
		using hint = 'Wprowadz login skladajacy sie z 4-20 znakow.#';
		RETURN NULL; --Anulujemy
    END IF;
   
   IF LENGTH(NEW.haslo) = 0 or LENGTH(NEW.haslo) < 4 THEN
        raise exception 'Haslo nie moze byc puste!'
		using hint = 'Wprowadz hasło skladajace sie z 4-20 znakow.#';
		RETURN NULL; --Anulujemy
    END IF;
   
   IF LENGTH(NEW.adres_email) = 0 THEN
        raise exception 'Adres email nie moze byc pusty!#';
		RETURN NULL; --Anulujemy
    END IF;
   
    RETURN NEW;  --Akceputacja modyfikacji                                                        
END;
    $$;
  
CREATE TRIGGER rejstracja_walidacja 
    AFTER INSERT OR UPDATE  ON projekt.uzytkownicy
    FOR EACH ROW EXECUTE PROCEDURE funkcje.walidacja(); 

CREATE OR REPLACE FUNCTION funkcje.dodaj_rezerwacje(
		pokojID integer,
		goscID integer,
		start date,
		koniec date,
		iloscGosci integer
	)	
	RETURNS bool AS $$
	declare
		czy_dodano bool := false;
		rec record;
    begin
	    select miasto as miasto_temp, p.kategoria_id as kateogria_pokoju_temp  into rec from projekt.pokoje p join projekt.kategorie_pokoi kp using(kategoria_id) join projekt.hotele h using(hotel_id) where p.pokoj_id = pokojID;
        if  (select count(*) from funkcje.wolne_pokoje(rec.miasto_temp,start,koniec,rec.kateogria_pokoju_temp,iloscGosci))>0 then
	    	insert into projekt.rezerwacje(pokoj_id, gosc_id, poczatek_pobytu, koniec_pobytu, liczba_gosci) values(pokojID,	goscID,	start,koniec, iloscGosci);
    		czy_dodano := true;
    	end if;
    	return czy_dodano;
	END;
	$$  LANGUAGE 'plpgsql';
 
 CREATE OR REPLACE FUNCTION funkcje.dodaj_rachunek()RETURNS trigger AS $$
 declare
 	ilosc_dni integer ;
 	cena_za_noc numeric(7,2) ;
begin
	select into cena_za_noc podstawa_cenowa from projekt.kategorie_pokoi where kategoria_id = (select kategoria_id from projekt.pokoje where pokoj_id=new.pokoj_id);
	ilosc_dni := new.koniec_pobytu - new.poczatek_pobytu;
	insert into projekt.rachunki(pokoj_id, gosc_id, rezerwacja_id, suma_kosztow, forma_platnosci) 
	values(new.pokoj_id, new.gosc_id, new.rezerwacja_id, ilosc_dni*cena_za_noc, 'karta');
    RAISE NOTICE 'odpala sie';
    RETURN NEW;
END $$
LANGUAGE 'plpgsql';

CREATE TRIGGER rachunek after insert ON projekt.rezerwacje 
FOR EACH ROW EXECUTE PROCEDURE funkcje.dodaj_rachunek();
 
create function funkcje.sprawdz_czy_daty_w_dobrej_kolejnosci(start date, koniec date)
  returns boolean
as
$$
begin
  if start < koniec then 
    return true;
  end if;
  raise 'Data [%] nie poprzedza daty [%]#', start, koniec;
end;
$$
language plpgsql;