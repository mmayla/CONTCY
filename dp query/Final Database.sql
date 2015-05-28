CREATE DATABASE Crawler_Database;
create table Crawler_Database.Websites 
( 
W_ID int NOT NULL auto_increment,
Website varchar(1000) NOT NULL,
PRIMARY KEY (W_ID)
) ;
create table Crawler_Database.URLs 
( 
U_ID integer PRIMARY KEY NOT NULL auto_increment ,
URL varchar(1000) NOT NULL,
W_id INTEGER NOT NULL,
Insertion_Date date NOT NULL,
Last_revision_Date date NULL,
Crawled BOOLEAN NULL,
Locked BOOLEAN DEFAULT false,
Depth integer NOT NULL,
FOREIGN KEY (W_id) REFERENCES Crawler_Database.Websites (W_ID) ON DELETE CASCADE
) ;

create table Crawler_Database.content 
( 
C_ID INTEGER PRIMARY KEY NOT NULL ,
Title text NOT NULL,
T_Page text NOT NULL,
FOREIGN KEY (C_ID) REFERENCES Crawler_Database.URLs (U_ID) ON DELETE CASCADE
) ;

