drop schema IF exists funkcje cascade;

create schema funkcje;

-- Celem funkcji jest sprawdzenie jakie konkretnie pokoje są dostępne dla użytkownika do zarezerwowania przy podanych parametrach. Funckja przyjmuje parametry takie jak nazwa miasta, poczatek i koniec pobytu, ID kategorii pokoju, ilosc osob, ktora ma się pomieścić w pokoju. Funkcja zwraca tabele, która przechowuje informacje o pokoje, ktore mozna zarezerwowac zgodnie z podanymi szczegółami. Zwracana tabela zawiera kolumny: z ID pokoju, z nazwą hotelu, w którym jest pokój, z ID hotelu, w którym jest pokój, z ID kategorii pokoju, z maksymalną ilością osób, która mieści się w pokoju.
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
    	rec_pokoj RECORD; -- record zawierajacy pokoj
	   	rec_rezerwacja RECORD; -- record zawierajacy rezerwacje
    	czy_nachodza boolean; -- zmienna informujaca czy podana przez uzytkownika data i daty rezerwacji danego pokoju nachodza na siebie (jesli tak, odrzucamy pokoj - nie da sie go zarezerwowac)
    BEGIN
        -- petla w ktorej bierzemy wszystkie pokoje, ktorespelniaja wymagania co do kategorii pokoju, ilosci osob oraz miasta pobytu
         FOR rec_pokoj IN (	select * from projekt.pokoje p where p.max_gosci>=iloscOsob and p.kategoria_id=kategoriaID and p.hotel_id IN (select h.hotel_id from projekt.hotele h where h.miasto = miasto_nazwa) )
         	LOOP
	         	czy_nachodza := false;

	         	pokoj_id := rec_pokoj.pokoj_id;
				nazwa := rec_pokoj.nazwa;
        		hotelID := rec_pokoj.hotel_id;
        		kategoria_id := rec_pokoj.kategoria_id;
        		max_gosci := rec_pokoj.max_gosci;

                -- w wewnetrznej petli sprawdzamy wszystkie rezerwacje dla konkretnego pokoju
	        	FOR rec_rezerwacja IN ( select * from projekt.rezerwacje r where r.pokoj_id=rec_pokoj.pokoj_id)
         			LOOP
                        -- wbudowana funckja OVERLAPS() - sprawdzamy czy terminy ze soba koliduja
						IF ((rec_rezerwacja.poczatek_pobytu, rec_rezerwacja.koniec_pobytu) overlaps (poczatek, koniec)) then
        					czy_nachodza := true;
                        -- sprawdzamy czy poczatki i konce terminow ze soba koliduja - nie mozna wynajac hotelu tego samego dnia, ktorego konczy sie inna rezerwacja i analogicznie odwrotnie
        				elsif rec_rezerwacja.poczatek_pobytu = koniec then
        					czy_nachodza := true;
        				elsif rec_rezerwacja.koniec_pobytu = poczatek then
        					czy_nachodza := true;
        				end if;
                	end loop;

                    -- jesli dany pokoj nie posiada w podanym terminie rezerwacji i spelnia inne wymogi to dodajemy go do zwracanej tablei 
                	if not czy_nachodza then
               			return next;
               		end if;
         	END LOOP;
	END;
$$  LANGUAGE 'plpgsql';

-- Funkcja ktora autoryzuje logowanie uzytkownika do aplikacji. Funkcja przyjmuje 2 parametry: login oraz hasło oraz zwraca tabele skladajaca sie z dwoch kolumn: czy autoryzacja uzytkownika przebiegla pomyslnie oraz czy autoryzowany uzytkownik posiada specjalne uprawnienia (admin)
CREATE OR REPLACE FUNCTION funkcje.autoryzuj(
		user_login TEXT,
		user_haslo TEXT
	)
	RETURNS TABLE (
        autoryzowano bool,
		uprawnienia_admina bool
	) AS $$
	declare
		czy_specjalne_uprawnienia bool; -- zmienna przechowujaca informacje o specjalnych uprawnieniach
    begin
        -- jezeli istnieje w bazie danych uzytkownik o danym logonie i hasle to autoryzujemy logowanie
	    if exists (select * from projekt.uzytkownicy where login = user_login and haslo = user_haslo) then
        	autoryzowano := true;
            -- sprawdzamy czy zalogowany uzytkownik posiada specjalne uprawnienia
        	select specjalne_uprawnienia into czy_specjalne_uprawnienia from projekt.uzytkownicy where login = user_login and haslo = user_haslo;
        	uprawnienia_admina := czy_specjalne_uprawnienia;
    	else 
        	autoryzowano := false;
        	uprawnienia_admina := false;
    	end if;
        -- zwracamy rekod
    	return next;
	END;
	$$  LANGUAGE 'plpgsql';

-- Funkcja ktora tworzy konto uzytkownika w serwisie i dodaje go do bazdy klientow. Funkcja przyjmuje login, haslo, adres mailowy, imie, nazwisko, pesel oraz nuemr telfonu od uzytkownika. Funckja zwraca liczbe calkowita ktora sluzy informuje o powodzeniu operacji
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
		czy_dodano integer := 3; -- zmienia przechowujaca informacje o powodzniu operacji
--		0 - dodano bez bledow
--		1 - nie dodano, jest taki login juz
--		2 - nie dodano, jest taki mail juz
-- 		3 - inny blad
    begin
        -- sprawdzamy czy uzytkownik o takim loginie juz istnieje w bazie
	    if exists (select * from projekt.uzytkownicy where login = user_login) then
        	czy_dodano := 1;
        -- sprawdzamy czy uzytkownik o takim adresie email juz isteniej w bazie danych
    	elsif exists (select * from projekt.uzytkownicy where adres_email = user_mail) then
        	czy_dodano := 2;
        else
        -- jesli nie ma takiego uzytkownika to tworzymy konto uzytkownika i powiazane z nim konto goscia
        	insert into projekt.uzytkownicy(login, haslo, adres_email, specjalne_uprawnienia) values(user_login, user_haslo, user_mail, false);
        	insert into projekt.goscie(login, imie, nazwisko, pesel, nr_telefonu) values(user_login, user_imie, user_nazwisko, user_pesel, user_telefon);
    		czy_dodano := 0;
        end if;
        -- zwracamy status informujacy o powodzeniu wykonanai informacji
    		return czy_dodano;
	END;
$$  LANGUAGE 'plpgsql';

-- Funckja ktora waliduje wprowadzane do bazdy danych dane. Walidacji poddane sa login, haslo, adres mailowy. Zadne z nich nie moze byc puste, a takze login i haslo musza sie skladac z conajmniej 4 znakow.
CREATE OR REPLACE FUNCTION funkcje.walidacja ()
    RETURNS TRIGGER -- zwracamy trigger
    LANGUAGE plpgsql
    AS $$
    BEGIN
    IF LENGTH(NEW.login) = 0 or LENGTH(NEW.login) < 4 THEN
        -- jesli login nie spelnia wymagan to rzucamy wyjatek
        raise exception 'Login nie moze byc pusty'
		using hint = 'Wprowadz login skladajacy sie z 4-20 znakow.#';
		RETURN NULL; --Anulujemy
    END IF;
   
   IF LENGTH(NEW.haslo) = 0 or LENGTH(NEW.haslo) < 4 THEN
        -- jesli haslo nie spelnia wymagan to rzucamy wyjatek
        raise exception 'Haslo nie moze byc puste!'
		using hint = 'Wprowadz hasło skladajace sie z 4-20 znakow.#';
		RETURN NULL; --Anulujemy
    END IF;
   
   IF LENGTH(NEW.adres_email) = 0 THEN
        -- jesli email nie spelnia wymagan to rzucamy wyjatek
        raise exception 'Adres email nie moze byc pusty!#';
		RETURN NULL; --Anulujemy
    END IF;
   
    RETURN NEW;  --Akceputacja modyfikacji                                                        
END;
    $$;
  
-- tworzymy trigger powiazany z tabela uzytkownicy
CREATE TRIGGER rejstracja_walidacja 
    AFTER INSERT OR UPDATE  ON projekt.uzytkownicy
    FOR EACH ROW EXECUTE PROCEDURE funkcje.walidacja(); 

-- Funckja ktora dodaje rezerwacje do bazdy danych, o ile argumenty podane przez uzytkownika na to pozwalaja. Funkcja przyjmuje ID konkretnego pokoju, ktory uzytkownik chce zarezrwowac, ID goscia - uzytkownika, date poczatkowa i koncowa rezerwacji oraz ilosc gosci jaka ma przebywac w pokoju. Funckja zwraca wartosc typu bool informujaca o tym czy dokonano rezerwacji pokoju.
CREATE OR REPLACE FUNCTION funkcje.dodaj_rezerwacje(
		pokojID integer,
		goscID integer,
		start date,
		koniec date,
		iloscGosci integer
	)	
	RETURNS bool AS $$
	declare
		czy_dodano bool := false; -- wartosc bool informujaca o powodzeniu
		rec record; -- pomocniczy rekord
    begin
        -- do rekordu wpisujemy pomocniczne wartosci, uzyte do sprawdzenia czy dana rezerwacje mozna zrealizowac
	    select miasto as miasto_temp, p.kategoria_id as kateogria_pokoju_temp, max_gosci as iloscGosciMAX into rec from projekt.pokoje p join projekt.kategorie_pokoi kp using(kategoria_id) join projekt.hotele h using(hotel_id) where p.pokoj_id = pokojID;
        
        -- uzycie istniejacej juz funkcji funkcje.wolne_pokoje aby sprawdzic czy dla danych parametrow istnieja wolne pokoje, sprawdzenie czy daty podane przez uzytkownika nie sa NULL oraz czy liczba gosci nie przekracza pojemnosci pokoju   
        if  (select count(*) from funkcje.wolne_pokoje(rec.miasto_temp,start,koniec,rec.kateogria_pokoju_temp,iloscGosci))>0 and start is not NULL and koniec is not NULL and iloscGosci <= rec.iloscGosciMAX then 
            -- w przypadku spelnionych powyzszych warunkow, dodajemy rezerwacje do bazy danych
	    	insert into projekt.rezerwacje(pokoj_id, gosc_id, poczatek_pobytu, koniec_pobytu, liczba_gosci) values(pokojID,	goscID,	start, koniec, iloscGosci);
    		czy_dodano := true;
    	end if;
        -- zwracamy informacje o powodzeniu rezerwacji
    	return czy_dodano;
	END;
	$$  LANGUAGE 'plpgsql';
 
--  Funckja zwracajaca trigger, ktory przy każdej dokonanej rezerwacji, wystawia dla goscia rachnuek z informacjami o pobycie i jego koszcie.
 CREATE OR REPLACE FUNCTION funkcje.dodaj_rachunek()RETURNS trigger AS $$
 declare
 	ilosc_dni integer ;
 	cena_za_noc numeric(7,2) ;
begin
    -- wpisujemy do zmiennej podstawe cenowa, ktorej uzyjemy do obliczenia calkowitej ceny za pobyt
	select into cena_za_noc podstawa_cenowa from projekt.kategorie_pokoi where kategoria_id = (select kategoria_id from projekt.pokoje where pokoj_id=new.pokoj_id);
	-- ilosc dni - ile trwal pobyt (liczymy tylko noce)
    ilosc_dni := new.koniec_pobytu - new.poczatek_pobytu;
    -- zapisujemy rekord do tabeli projekt.rachunki
    -- cena za pobyt liczona jest na podstawie dlugosci pobytu i ceny bazowej powiazanej z kategoria pokoju
    -- ilosc osob podczas rezerwacji nie ma wplywu na cene pobytu !!!
	insert into projekt.rachunki(pokoj_id, gosc_id, rezerwacja_id, suma_kosztow, forma_platnosci) 
	values(new.pokoj_id, new.gosc_id, new.rezerwacja_id, ilosc_dni*cena_za_noc, 'karta');
    RETURN NEW;
END $$
LANGUAGE 'plpgsql';

-- dodajemy trigger ktory dodaje rachunek na konto uzytkownika po kazdej dokonanej rezerwacji
CREATE TRIGGER rachunek after insert ON projekt.rezerwacje 
FOR EACH ROW EXECUTE PROCEDURE funkcje.dodaj_rachunek();
 
--  Funkcja sprawdzajaca czy poczatek pobytu jest przed koncem. 
--  Funckja zastosowana jest do sprawdzania wartosci wprowadzanych do tabli projekt.rezerwacje (pierwsza linijka kodu w pliku add_data.sql)
CREATE OR REPLACE FUNCTION funkcje.sprawdz_czy_daty_w_dobrej_kolejnosci(start date, koniec date)
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