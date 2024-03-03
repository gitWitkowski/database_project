
# Sorting algorithms visualization

Project completed within "Databases 1" university class representing hotel reservation system with GUI.

-----

### Goal of the project

The goal of the project was to design a database with an adequate level of complexity and following the [given requirements](https://newton.fis.agh.edu.pl/~antek/docs/BD1/BD1_wymagania.pdf).

-----

### ERD

![erd](https://github.com/gitWitkowski/database_project/blob/main/Documentation/Demo/ERD.jpg)

-----

### Technologies used
- Java
- Swing
- [LGoodDatePicker](https://github.com/LGoodDatePicker/LGoodDatePicker)
- [PostgreSQL JDBC Driver](https://mvnrepository.com/artifact/org.postgresql/postgresql)
- [Spring JDBC](https://mvnrepository.com/artifact/org.springframework/spring-jdbc)
- PostgreSQL
- IntelliJ IDEA

-----

### Application demo

Offer search example:
![demo1](https://github.com/gitWitkowski/database_project/blob/main/Documentation/Demo/zarezerwuj2.png)

Admin panel:
![demo2](https://github.com/gitWitkowski/database_project/blob/main/Documentation/Demo/admin.png)

[More examples in the documentation](https://github.com/gitWitkowski/database_project/blob/main/Documentation/Bazy_Danych_Dokumentacja.pdf)

-----

### Usage/Features

The users can log in or register if they do not already have an account. The application allows browsing hotels with specified options, such as:
- location (city)
- start and end of the visit
- room standard
- number of guests.
Only offers meeting the required attributes will be listed. Additionally, users can browse all rooms grouped by hotels and then check their availability. Only logged in users can book rooms. They also gain access to their personal account tab, in which they can see all their reservations and bills. Admin can see all reservations and bills in the database, as well as the report of "best clients" (those who made at least 4 reservations for at least 2000 PLN). Admin also has access to a button which can perform a "factory reset" on the database.

-----

### How to run the project

1. Clone git repository:
```
git clone https://github.com/gitWitkowski/database_project.git
```
2. Open `MyFrame` directory using IntelliJ IDEA
3. Reload all Maven dependencies
4. Compile and run the project