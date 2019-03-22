delete from address;
delete from doctor_speciality;
delete from office_worker;
delete from medicalworker;
delete from appointment;
delete from office_hour;
delete from office;
delete from chat_thread;
delete from chat_message;
delete from office_patient;

insert into address (city,country,door,floor,number,place,street,zip) values ('Wien','Oesterreich','1','7','7','Wien','Zieglergasse',1070);
insert into address (city,country,door,floor,number,place,street,zip) values ('Wien','Oesterreich','7','7','5','Wien','Mariahilferstr.',1060);
insert into address (city,country,door,floor,number,place,street,zip) values ('Wien','Oesterreich','6','6','10','Wien','Herzgasse',1100);
insert into address (city,country,door,floor,number,place,street,zip) values ('Wien','Oesterreich','1','1','3','Wien','Karlsplatz',1010);
insert into address (city,country,door,floor,number,place,street,zip) values ('Wien','Oesterreich','3','7','7','Wien','Leyserstraße',1140);
insert into address (city,country,door,floor,number,place,street,zip) values ('Wien','Oesterreich','2','7','5','Wien','Rochusgasse',1030);
insert into address (city,country,door,floor,number,place,street,zip) values ('Wien','Oesterreich','9a','6','10','Wien','Fuchsthallergasse',1090);
insert into address (city,country,door,floor,number,place,street,zip) values ('Wien','Oesterreich','27','1','3','Wien','Hildebrandgasse',1010);

insert into office (name,phone,fax,address_id,email) values ('Praxis Dr. Mayer','125478','02658 741258',1,'office@hugo.at');
insert into office (name,phone,fax,address_id,email) values ('Praxis Dr. Hubert','125478','02658 741258',2,'dagobert@duck.eh');
insert into office (name,phone,fax,address_id,email) values ('Ordination Dr. Berger','125478','02658 741258',3,'psycho@farma.fa');
insert into office (name,phone,fax,address_id,email) values ('Praxis Dr. Müller','125478','02658 741258',4,'iron@man.dc');
insert into office (name,phone,fax,address_id,email) values ('Doktor Hase','125478','02658 741258',5,'hase@hugo.at');
insert into office (name,phone,fax,address_id,email) values ('Praxis Dr. Hager','125478','02658 741258',6,'doc@hager.eh');
insert into office (name,phone,fax,address_id,email) values ('Ordination Dr. Mall','125478','02658 741258',7,'doctor@mall.fa');
insert into office (name,phone,fax,address_id,email) values ('Praxis Mag. Moritzer','125478','02658 741258',8,'mag@mann.dc');

insert into office_hour(office_id,begin_time,end_time,daytype) values (1,'07:00:00','12:00:00','MONTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (1,'13:00:00','16:00:00','DIENSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (1,'08:00:00','11:00:00','DIENSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (1,'13:00:00','18:00:00','DONNERSTAG');

insert into office_hour(office_id,begin_time,end_time,daytype) values (2,'09:00:00','12:00:00','DIENSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (2,'14:00:00','17:00:00','DIENSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (2,'09:30:00','13:30:00','MITTWOCH');
insert into office_hour(office_id,begin_time,end_time,daytype) values (2,'08:00:00','12:00:00','DONNERSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (2,'14:00:00','16:00:00','DONNERSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (2,'13:00:00','18:00:00','FREITAG');

insert into office_hour(office_id,begin_time,end_time,daytype) values (3,'08:00:00','13:00:00','MONTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (3,'08:00:00','13:00:00','DIENSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (3,'08:00:00','13:00:00','MITTWOCH');
insert into office_hour(office_id,begin_time,end_time,daytype) values (3,'08:00:00','13:00:00','DONNERSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (3,'08:00:00','13:00:00','FREITAG');

insert into office_hour(office_id,begin_time,end_time,daytype) values (4,'09:00:00','13:00:00','DIENSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (4,'09:00:00','12:00:00','MITTWOCH');
insert into office_hour(office_id,begin_time,end_time,daytype) values (4,'14:00:00','18:00:00','MITTWOCH');

insert into office_hour(office_id,begin_time,end_time,daytype) values (5,'09:00:00','13:00:00','DIENSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (5,'09:00:00','12:00:00','MITTWOCH');
insert into office_hour(office_id,begin_time,end_time,daytype) values (5,'14:00:00','18:00:00','MITTWOCH');
insert into office_hour(office_id,begin_time,end_time,daytype) values (5,'08:00:00','13:00:00','DONNERSTAG');

insert into office_hour(office_id,begin_time,end_time,daytype) values (6,'09:00:00','12:00:00','DIENSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (6,'14:00:00','17:00:00','DIENSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (6,'09:30:00','13:30:00','MITTWOCH');
insert into office_hour(office_id,begin_time,end_time,daytype) values (6,'08:00:00','12:00:00','DONNERSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (6,'14:00:00','16:00:00','DONNERSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (6,'13:00:00','18:00:00','FREITAG');

insert into office_hour(office_id,begin_time,end_time,daytype) values (7,'09:00:00','13:00:00','DIENSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (7,'09:00:00','12:00:00','MITTWOCH');
insert into office_hour(office_id,begin_time,end_time,daytype) values (7,'14:00:00','18:00:00','MITTWOCH');
insert into office_hour(office_id,begin_time,end_time,daytype) values (7,'08:00:00','12:00:00','DONNERSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (7,'14:00:00','16:00:00','DONNERSTAG');

insert into office_hour(office_id,begin_time,end_time,daytype) values (8,'09:00:00','12:00:00','DIENSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (8,'14:00:00','17:00:00','DIENSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (8,'09:30:00','13:30:00','MITTWOCH');
insert into office_hour(office_id,begin_time,end_time,daytype) values (8,'08:00:00','12:00:00','DONNERSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (8,'14:00:00','16:00:00','DONNERSTAG');
insert into office_hour(office_id,begin_time,end_time,daytype) values (8,'13:00:00','18:00:00','FREITAG');

insert into speciality (speciality_name) values ('Zahnarzt');                           -- #1
insert into speciality (speciality_name) values ('Frauenheilkunde und Geburtshilfe');   -- #2
insert into speciality (speciality_name) values ('Augenheilkunde');                     -- #3
insert into speciality (speciality_name) values ('Hals-Nasen-Ohren-Heilkunde');         -- #4
insert into speciality (speciality_name) values ('Humangenetik');                       -- #5
insert into speciality (speciality_name) values ('Arbeitsmedizin');                     -- #6
insert into speciality (speciality_name) values ('Biochemie');                          -- #7
insert into speciality (speciality_name) values ('Gefäßchirurgie');                     -- #8
insert into speciality (speciality_name) values ('Thoraxchirurgie');                    -- #9
insert into speciality (speciality_name) values ('Viszeralchirurgie');                  -- #10

insert into medicalworker (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,type) values ('bS9nqS956xUFYYy26bWmFvT8noo1','graf@dracu.la','Marcus','Mayer','Vamp','ire','','',0);        -- #1
insert into medicalworker (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,type) values ('7EWSTzVnlqgb7bC0dPbjAdmZUV12','max.muster@dracu.la','Max','Muster','Ass','tent','','',1);    -- #2
insert into medicalworker (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,type) values ('PmgPFW3CsZXht2IuOFiAqldozVV2','berta.hadel@dracu.la','Berta','Hadel','Dr','MSc','','',0);    -- #3
insert into medicalworker (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,type) values ('igi3pmYNRRWeQurhojiX8fDJmGj2','mist.eria@dracu.la','Mist','Eria','Dipl.Ing','','','',1);     -- #4
insert into medicalworker (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,type) values ('Ea3D4hYZjAU8UNMM3JYpwACMI303','manuel.meister@dracu.la','Manuel','Meister','Dr.','','','',0);-- #5
insert into medicalworker (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,type) values ('V3ZYOd3fn5b8q7INqUG06Akh5uI3','eva.jung@dracu.la','Eva','Jung','Dr.','','','',0);            -- #6
insert into medicalworker (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,type) values ('G2CDDaoK6PbFBQzZWGWkY5mJ19z2','hase@dracu.la','Hans','Hase','Dr.','','','',0);               -- #7
insert into medicalworker (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,type) values ('k5dv2CGJvRcwaNe1dofMNDZQZdx1','hager@dracu.la','Gerri','Hager','Dr.','','','',0);            -- #8
insert into medicalworker (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,type) values ('jovpbPRz8Wci0kgXi6F2YTndxXO2','mall@dracu.la','Sara','Mall','Dr.','','','',0);               -- #9
insert into medicalworker (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,type) values ('JWtWGss70HgeHAHeP4LONF6ReMt1','moritzer@dracu.la','Susi','Moritzer','Dr.','','','',0);       -- #10

insert into doctor_speciality (doctorid,specialityid) values (1,1);
insert into doctor_speciality (doctorid,specialityid) values (2,2);
insert into doctor_speciality (doctorid,specialityid) values (3,3);
insert into doctor_speciality (doctorid,specialityid) values (4,4);
insert into doctor_speciality (doctorid,specialityid) values (5,5);
insert into doctor_speciality (doctorid,specialityid) values (6,6);
insert into doctor_speciality (doctorid,specialityid) values (7,7);
insert into doctor_speciality (doctorid,specialityid) values (8,8);
insert into doctor_speciality (doctorid,specialityid) values (9,9);
insert into doctor_speciality (doctorid,specialityid) values (10,10);

insert into office_worker (officeid,medical_workerid) values (1,1);   -- graf@dracu.la
insert into office_worker (officeid,medical_workerid) values (1,2);   -- max.muster@dracu.la
insert into office_worker (officeid,medical_workerid) values (1,7);   -- hase@dracu.la
insert into office_worker (officeid,medical_workerid) values (1,8);   -- berta.hadel@dracu.la
insert into office_worker (officeid,medical_workerid) values (2,4);   -- mist.eria@dracu.la
insert into office_worker (officeid,medical_workerid) values (2,3);   -- hager@dracu.la
insert into office_worker (officeid,medical_workerid) values (2,5);   -- manuel.meister@dracu.la
insert into office_worker (officeid,medical_workerid) values (2,9);   -- mall@dracu.la
insert into office_worker (officeid,medical_workerid) values (2,10);  -- moritzer@dracu.la
insert into office_worker (officeid,medical_workerid) values (2,6);   -- eva.jung@dracu.la
-- insert into office_worker (officeid,medical_workerid) values (3,5);
-- insert into office_worker (officeid,medical_workerid) values (4,6);
-- insert into office_worker (officeid,medical_workerid) values (5,7);
-- insert into office_worker (officeid,medical_workerid) values (6,8);
-- insert into office_worker (officeid,medical_workerid) values (7,9);
-- insert into office_worker (officeid,medical_workerid) values (8,10);

insert into patient (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,svnr) values ('1qxo6HiFksd7UYXXY9gtcaGKYCu2','hans@patient.com','Hans','Mueller','','Dr.','','','1111111198');              -- #1 / 7
insert into patient (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,svnr) values ('wCjBKGC7EMZo49VTMCDb92toPXf2','franz@patient.com','Franz','Walder','Bsc.','','','','2222010105');            -- #2 / 8
insert into patient (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,svnr) values ('9VB82zVsNLaOiKDOuRfS6VmLzwx2','maria@patient.com','Maria','Zotter','','','','','3333101080'); -- Dea         -- #3     - Office 1
insert into patient (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,svnr) values ('t8phNC2PYqP0aHnGfLtGoFCaMDJ3','dominik.schwarz1994@gmail.com','Dominik','Schwarz','','','','','123456789');  -- #4
insert into patient (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,svnr) values ('97FGlrKrKiMm5XfsE0Ag9ueU3vO2','herbert@patient.com','Haerbert','Weber','','','','','9999121090'); -- Gerald  -- #5     - Office 1
insert into patient (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,svnr) values ('ZE2KNZTsiITeq2QVksiG3PcXqRJ3','susanne@patient.com','Susanne','Sauer','','','','','3333121294');             -- #6
insert into patient (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,svnr) values ('7kwjCTRek2hNEtuuprbIilPfa9h1','elisa@patient.com','Elisa','Heuer','','','','','3333091294');  -- Vanessa     -- #7 / 1 - Office 1
insert into patient (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,svnr) values ('BVhFd10jwqOwwtN0gKpMlHi6G3o1','manni@patient.com','Manni','Mager','','','','','3333121294');  -- Dominik     -- #8 / 2 - Office 1
insert into patient (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,svnr) values ('mXFHQCQmtyaCZiobALpnru2AZiS2','harri@patient.com','Jakob','Huber','','','','','3333121294');  -- Jakob       -- #9     - Office 2
insert into patient (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,svnr) values ('qqbX5jcI1nOJP5gpvjdA0Ub6jfg2','julian@patient.com','julian','Juls','','','','','3333121274'); -- Martin      -- #10    - Office 2
insert into patient (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,svnr) values ('JFbV1Wf0pGhug7z6pNxYg3gsOwV2','martin@patient.com','Martin','Matt','','','','','3333121244');                -- #11
insert into patient (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,svnr) values ('KZqAE0JtpCSOeAuwBXxDGtZtV0o2','gerri@patient.com','Gerri','Gold','','','','','3333121034');                  -- #12
insert into patient (uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,svnr) values ('1HkGqepeMQgLTE1cN1NJFUxtt632','julia@patient.com','Julia','Jaeger','','','','','3333121034');                -- #13

insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (1,'Theresa Bauer','2019-01-03 11:10:00','2019-01-03 12:10:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (1,'Getrude Maier','2019-01-07 11:10:00','2019-01-07 12:10:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (1,'Josef Bidermann','2019-01-14 08:10:00','2019-01-14 09:10:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (1,'Vanessa Sax','2019-01-21 11:10:00','2019-01-21 12:00:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (1,'Thomas Zachs','2019-01-22 13:30:00','2019-01-22 14:00:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (1,'Bettina Zachs','2019-01-22 14:15:00','2019-01-22 14:45:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (1,'Anton Mandl','2019-01-22 15:00:00','2019-01-22 16:00:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (1,'Gerald Sohnemann','2019-01-22 08:15:00','2019-01-22 09:00:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (1,'David Mandl','2019-01-22 09:15:00','2019-01-22 09:45:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (1,'Antonia Schwarz','2019-01-22 10:15:00','2019-01-22 10:45:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (1,'Theresa Bertram','2019-01-24 15:15:00','2019-01-24 15:45:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (1,'Florian Niedermann','2019-01-24 16:00:00','2019-01-24 17:00:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (1,'Maria Heideman','2019-01-24 17:10:00','2019-01-24 17:45:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (1,'Makki Sulvi','2019-01-24 13:00:00','2019-01-24 15:00:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (1,'Manuel Silver','2019-01-21 07:40:00','2019-01-21 10:00:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (1,'Vanessa Braun','2019-01-21 10:15:00','2019-01-21 11:00:00');

insert into appointment (office_id,patient_id,appointment_begin,appointment_end) values (1,7,'2019-01-15 12:30:00','2019-01-15 13:00:00');
insert into appointment (office_id,patient_id,appointment_begin,appointment_end) values (1,8,'2019-01-15 09:15:00','2019-01-15 09:30:00');
insert into appointment (office_id,patient_id,appointment_begin,appointment_end) values (1,3,'2019-01-16 16:30:00','2019-01-16 17:00:00');
insert into appointment (office_id,patient_id,appointment_begin,appointment_end) values (1,7,'2019-01-28 16:30:00','2019-01-16 17:00:00');

insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Franz Glock','2019-01-03 11:10:00','2019-01-03 12:10:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Vanessa Schab','2019-01-07 11:10:00','2019-01-07 12:10:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Robert Lind','2019-01-14 08:10:00','2019-01-14 09:10:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Thomas Zachs','2019-01-23 10:10:00','2019-01-23 10:30:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Gerald Sohnemann','2019-01-23 11:00:00','2019-01-23 11:30:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Christoph Tanzer','2019-01-23 11:30:00','2019-01-23 12:30:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Maximilian Muster','2019-01-22 11:40:00','2019-01-22 12:00:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Johann Pleyer','2019-01-22 15:00:00','2019-01-22 15:30:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Gudrun Sauer','2019-01-22 15:30:00','2019-01-22 16:00:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Max Lustig','2019-01-22 16:00:00','2019-01-22 16:30:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Hermine Salz','2019-01-24 08:10:00','2019-01-24 09:10:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Sabine Teuer','2019-01-24 09:10:00','2019-01-24 09:30:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Stefanie Teufel','2019-01-24 09:30:00','2019-01-24 10:30:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Hermann Gott','2019-01-24 10:30:00','2019-01-24 10:45:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Gottfried Lakinger','2019-01-24 11:00:00','2019-01-24 11:45:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Martin Silbereisen','2019-01-24 15:00:00','2019-01-24 15:30:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Divan Sulfat','2019-01-24 15:30:00','2019-01-24 16:00:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Manuel Krazer','2019-01-25 15:00:00','2019-01-25 15:45:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Sebastian Kurz','2019-01-25 16:00:00','2019-01-25 16:20:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Josef Schreder','2019-01-25 16:30:00','2019-01-25 17:00:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Martin Luther','2019-01-25 17:00:00','2019-01-25 18:00:00');
insert into appointment (office_id,patient_name,appointment_begin,appointment_end) values (2,'Franz Hans','2019-01-23 09:30:00','2019-01-23 10:00:00')

insert into appointment (office_id,patient_id,appointment_begin,appointment_end) values (2,1,'2019-01-23 12:30:00','2019-01-23 13:00:00');
insert into appointment (office_id,patient_id,appointment_begin,appointment_end) values (2,2,'2019-01-25 13:15:00','2019-01-25 14:30:00');
insert into appointment (office_id,patient_id,appointment_begin,appointment_end) values (2,11,'2019-01-22 09:15:00','2019-01-22 11:00:00');
insert into appointment (office_id,patient_id,appointment_begin,appointment_end) values (2,12,'2019-01-22 11:15:00','2019-01-22 11:30:00');
insert into appointment (office_id,patient_id,appointment_begin,appointment_end) values (2,13,'2019-01-22 14:15:00','2019-01-22 15:00:00');
insert into appointment (office_id,patient_id,appointment_begin,appointment_end) values (2,9,'2019-01-22 16:30:00','2019-01-22 17:00:00');
insert into appointment (office_id,patient_id,appointment_begin,appointment_end) values (2,1,'2019-01-24 14:00:00','2019-01-24 15:00:00');


insert into appointment (office_id,appointment_begin,appointment_end) values (2,'2018-01-07 07:00:00','2018-01-07 08:00:00');
insert into appointment (office_id,appointment_begin,appointment_end) values (2,'2018-12-11 09:00:00','2018-12-12 10:00:00');
insert into appointment (office_id,appointment_begin,appointment_end) values (2,'2018-12-12 10:00:00','2018-12-12 11:00:00');
insert into appointment (office_id,appointment_begin,appointment_end) values (2,'2018-12-12 12:00:00','2018-12-12 13:00:00');

insert into appointment (office_id,appointment_begin,appointment_end) values (3,'2018-03-12 14:15:00','2018-03-12 16:15:00');
insert into appointment (office_id,appointment_begin,appointment_end) values (3,'2018-12-11 09:00:00','2018-12-12 10:00:00');
insert into appointment (office_id,appointment_begin,appointment_end) values (3,'2018-12-12 10:00:00','2018-12-12 11:00:00');
insert into appointment (office_id,appointment_begin,appointment_end) values (3,'2018-12-12 12:00:00','2018-12-12 13:00:00');

insert into appointment (office_id,appointment_begin,appointment_end) values (4,'2018-09-09 09:00:00','2018-09-09 09:30:00');
insert into appointment (office_id,appointment_begin,appointment_end) values (4,'2018-12-11 09:00:00','2018-12-12 10:00:00');
insert into appointment (office_id,appointment_begin,appointment_end) values (4,'2018-12-12 10:00:00','2018-12-12 11:00:00');
insert into appointment (office_id,appointment_begin,appointment_end) values (4,'2018-12-12 12:00:00','2018-12-12 13:00:00');

insert into appointment (office_id,appointment_begin,appointment_end) values (5,'2018-09-09 09:00:00','2018-09-09 09:30:00');
insert into appointment (office_id,appointment_begin,appointment_end) values (5,'2018-12-11 09:00:00','2018-12-12 10:00:00');
insert into appointment (office_id,appointment_begin,appointment_end) values (5,'2018-12-12 10:00:00','2018-12-12 11:00:00');
insert into appointment (office_id,appointment_begin,appointment_end) values (5,'2018-12-12 12:00:00','2018-12-12 13:00:00');
insert into appointment (office_id,appointment_begin,appointment_end) values (5,'2018-12-13 08:00:00','2018-12-12 09:00:00');

insert into appointment (office_id,appointment_begin,appointment_end) values (6,'2018-09-09 09:00:00','2018-09-09 09:30:00');
insert into appointment (office_id,appointment_begin,appointment_end) values (6,'2018-12-11 09:00:00','2018-12-12 10:00:00');
insert into appointment (office_id,appointment_begin,appointment_end) values (6,'2018-12-12 10:00:00','2018-12-12 11:00:00');
insert into appointment (office_id,appointment_begin,appointment_end) values (6,'2018-12-12 12:00:00','2018-12-12 13:00:00');
insert into appointment (office_id,appointment_begin,appointment_end) values (6,'2018-12-13 14:00:00','2018-12-12 15:00:00');
insert into appointment (office_id,appointment_begin,appointment_end) values (6,'2018-12-13 15:00:00','2018-12-12 16:00:00');


insert into office_patient (officeid,patientid) values (1,1);
insert into office_patient (officeid,patientid) values (1,2);
insert into office_patient (officeid,patientid) values (1,3);  -- Dea
insert into office_patient (officeid,patientid) values (1,11); -- Martin
insert into office_patient (officeid,patientid) values (1,9);  -- Jakob
insert into office_patient (officeid,patientid) values (1,12); -- Gerald
insert into office_patient (officeid,patientid) values (1,7);  -- Vanessa
insert into office_patient (officeid,patientid) values (1,8);  -- Dominik

insert into office_patient (officeid,patientid) values (2,12); -- Gerald
insert into office_patient (officeid,patientid) values (2,3);  -- Dea
insert into office_patient (officeid,patientid) values (2,11); -- Martin
insert into office_patient (officeid,patientid) values (2,7);  -- Vanessa
insert into office_patient (officeid,patientid) values (2,8);  -- Dominik
insert into office_patient (officeid,patientid) values (2,9);  -- Jakob
insert into office_patient (officeid,patientid) values (2,1);
insert into office_patient (officeid,patientid) values (2,4);
insert into office_patient (officeid,patientid) values (2,5);
insert into office_patient (officeid,patientid) values (2,13);

insert into office_patient (officeid,patientid) values (3,1);
insert into office_patient (officeid,patientid) values (3,2);
insert into office_patient (officeid,patientid) values (3,3);
insert into office_patient (officeid,patientid) values (3,4);
insert into office_patient (officeid,patientid) values (3,5);
insert into office_patient (officeid,patientid) values (3,6);
insert into office_patient (officeid,patientid) values (3,7);
insert into office_patient (officeid,patientid) values (3,8);
insert into office_patient (officeid,patientid) values (3,9);
insert into office_patient (officeid,patientid) values (3,10);
insert into office_patient (officeid,patientid) values (3,11);
insert into office_patient (officeid,patientid) values (3,12);
insert into office_patient (officeid,patientid) values (3,13);

insert into office_patient (officeid,patientid) values (4,7);
insert into office_patient (officeid,patientid) values (4,8);
insert into office_patient (officeid,patientid) values (4,9);
insert into office_patient (officeid,patientid) values (4,11);
insert into office_patient (officeid,patientid) values (4,12);
insert into office_patient (officeid,patientid) values (4,13);

insert into office_patient (officeid,patientid) values (5,3);
insert into office_patient (officeid,patientid) values (5,4);
insert into office_patient (officeid,patientid) values (5,5);
insert into office_patient (officeid,patientid) values (5,8);
insert into office_patient (officeid,patientid) values (5,9);
insert into office_patient (officeid,patientid) values (5,10);
insert into office_patient (officeid,patientid) values (5,11)

insert into office_patient (officeid,patientid) values (6,1);
insert into office_patient (officeid,patientid) values (6,2);
insert into office_patient (officeid,patientid) values (6,3);
insert into office_patient (officeid,patientid) values (6,5);
insert into office_patient (officeid,patientid) values (6,6);
insert into office_patient (officeid,patientid) values (6,8);
insert into office_patient (officeid,patientid) values (6,9);
insert into office_patient (officeid,patientid) values (6,11);
insert into office_patient (officeid,patientid) values (6,13);

insert into office_patient (officeid,patientid) values (7,4);
insert into office_patient (officeid,patientid) values (7,5);
insert into office_patient (officeid,patientid) values (7,6);
insert into office_patient (officeid,patientid) values (7,7);
insert into office_patient (officeid,patientid) values (7,8);
insert into office_patient (officeid,patientid) values (7,9);
insert into office_patient (officeid,patientid) values (7,10);

insert into office_patient (officeid,patientid) values (8,7);
insert into office_patient (officeid,patientid) values (8,8);
insert into office_patient (officeid,patientid) values (8,9);
insert into office_patient (officeid,patientid) values (8,10);
insert into office_patient (officeid,patientid) values (8,11);
insert into office_patient (officeid,patientid) values (8,12);
insert into office_patient (officeid,patientid) values (8,13);

insert into chat_thread (office_id,patient_id) values (1,1);
insert into chat_thread (office_id,patient_id) values (1,2);
insert into chat_thread (office_id,patient_id) values (1,3);  -- Dea
insert into chat_thread (office_id,patient_id) values (1,11); -- Martin
insert into chat_thread (office_id,patient_id) values (1,9);  -- Jakob
insert into chat_thread (office_id,patient_id) values (1,12); -- Gerald
insert into chat_thread (office_id,patient_id) values (1,7);  -- Vanessa
insert into chat_thread (office_id,patient_id) values (1,8);  -- Dominik

insert into chat_thread (office_id,patient_id) values (2,12); -- Gerald
insert into chat_thread (office_id,patient_id) values (2,3);  -- Dea
insert into chat_thread (office_id,patient_id) values (2,11); -- Martin
insert into chat_thread (office_id,patient_id) values (2,7);  -- Vanessa
insert into chat_thread (office_id,patient_id) values (2,8);  -- Dominik
insert into chat_thread (office_id,patient_id) values (2,9);  -- Jakob
insert into chat_thread (office_id,patient_id) values (2,1);
insert into chat_thread (office_id,patient_id) values (2,4);
insert into chat_thread (office_id,patient_id) values (2,5);
insert into chat_thread (office_id,patient_id) values (2,13);

insert into chat_thread (office_id,patient_id) values (3,1);
insert into chat_thread (office_id,patient_id) values (3,2);
insert into chat_thread (office_id,patient_id) values (3,3);
insert into chat_thread (office_id,patient_id) values (3,4);
insert into chat_thread (office_id,patient_id) values (3,5);
insert into chat_thread (office_id,patient_id) values (3,6);
insert into chat_thread (office_id,patient_id) values (3,7);
insert into chat_thread (office_id,patient_id) values (3,8);
insert into chat_thread (office_id,patient_id) values (3,9);
insert into chat_thread (office_id,patient_id) values (3,10);
insert into chat_thread (office_id,patient_id) values (3,11);
insert into chat_thread (office_id,patient_id) values (3,12);
insert into chat_thread (office_id,patient_id) values (3,13);

insert into chat_thread (office_id,patient_id) values (4,7);
insert into chat_thread (office_id,patient_id) values (4,8);
insert into chat_thread (office_id,patient_id) values (4,9);
insert into chat_thread (office_id,patient_id) values (4,11);
insert into chat_thread (office_id,patient_id) values (4,12);
insert into chat_thread (office_id,patient_id) values (4,13);

insert into chat_thread (office_id,patient_id) values (5,3);
insert into chat_thread (office_id,patient_id) values (5,4);
insert into chat_thread (office_id,patient_id) values (5,5);
insert into chat_thread (office_id,patient_id) values (5,8);
insert into chat_thread (office_id,patient_id) values (5,9);
insert into chat_thread (office_id,patient_id) values (5,10);
insert into chat_thread (office_id,patient_id) values (5,11)

insert into chat_thread (office_id,patient_id) values (6,1);
insert into chat_thread (office_id,patient_id) values (6,2);
insert into chat_thread (office_id,patient_id) values (6,3);
insert into chat_thread (office_id,patient_id) values (6,5);
insert into chat_thread (office_id,patient_id) values (6,6);
insert into chat_thread (office_id,patient_id) values (6,8);
insert into chat_thread (office_id,patient_id) values (6,9);
insert into chat_thread (office_id,patient_id) values (6,11);
insert into chat_thread (office_id,patient_id) values (6,13);

insert into chat_thread (office_id,patient_id) values (7,4);
insert into chat_thread (office_id,patient_id) values (7,5);
insert into chat_thread (office_id,patient_id) values (7,6);
insert into chat_thread (office_id,patient_id) values (7,7);
insert into chat_thread (office_id,patient_id) values (7,8);
insert into chat_thread (office_id,patient_id) values (7,9);
insert into chat_thread (office_id,patient_id) values (7,10);

insert into chat_thread (office_id,patient_id) values (8,7);
insert into chat_thread (office_id,patient_id) values (8,8);
insert into chat_thread (office_id,patient_id) values (8,9);
insert into chat_thread (office_id,patient_id) values (8,10);
insert into chat_thread (office_id,patient_id) values (8,11);
insert into chat_thread (office_id,patient_id) values (8,12);
insert into chat_thread (office_id,patient_id) values (8,13);
