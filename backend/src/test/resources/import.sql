delete from address;
delete from doctor_speciality;
delete from office_worker;
delete from medicalworker;
delete from appointment;
delete from office_hour;
delete from office;


insert into address (id,city,country,door,floor,number,place,street,zip) values (1,'Vienna','Austria','1','7','7','Vienna','Zieglergasse',1070);
insert into address (id,city,country,door,floor,number,place,street,zip) values (2,'Vienna','Austria','7','7','5','Vienna','Loewengasse',1030);
insert into address (id,city,country,door,floor,number,place,street,zip) values (3,'Vienna','Austria','6','6','10','Vienna','Herzgasse',1100);
insert into address (id,city,country,door,floor,number,place,street,zip) values (4,'Vienna','Austria','1','1','3','Vienna','Karlsplatz',1010);

insert into office (id,name,phone,fax,address_id,email) values (1,'Praxis Dr. Mayer','125478','02658 741258',1,'office@hugo.at');
insert into office (id,name,phone,fax,address_id,email) values (2,'Praxis Dr. Hubert','125478','02658 741258',2,'dagobert@duck.eh');
insert into office (id,name,phone,fax,address_id,email) values (3,'Praxis Dr. Hubert','125478','02658 741258',3,'psycho@farma.fa');
insert into office (id,name,phone,fax,address_id,email) values (4,'Praxis Dr. Mueller','125478','02658 741258',4,'iron@man.dc');

insert into office_hour(id,office_id,begin_time,end_time,daytype) values (1,1,'07:00:00','12:00:00','MONTAG');
insert into office_hour(id,office_id,begin_time,end_time,daytype) values (2,1,'13:00:00','16:00:00','MONTAG');
insert into office_hour(id,office_id,begin_time,end_time,daytype) values (3,1,'08:00:00','11:00:00','DIENSTAG');
insert into office_hour(id,office_id,begin_time,end_time,daytype) values (4,1,'13:00:00','18:00:00','DONNERSTAG');

insert into office_hour(id,office_id,begin_time,end_time,daytype) values (5,2,'09:00:00','12:00:00','DIENSTAG');
insert into office_hour(id,office_id,begin_time,end_time,daytype) values (6,2,'14:00:00','17:00:00','DIENSTAG');
insert into office_hour(id,office_id,begin_time,end_time,daytype) values (7,2,'09:30:00','13:30:00','MITTWOCH');
insert into office_hour(id,office_id,begin_time,end_time,daytype) values (8,2,'08:00:00','12:00:00','DONNERSTAG');
insert into office_hour(id,office_id,begin_time,end_time,daytype) values (9,2,'14:00:00','16:00:00','DONNERSTAG');
insert into office_hour(id,office_id,begin_time,end_time,daytype) values (10,2,'13:00:00','18:00:00','FREITAG');

insert into office_hour(id,office_id,begin_time,end_time,daytype) values (11,3,'08:00:00','13:00:00','MONTAG');
insert into office_hour(id,office_id,begin_time,end_time,daytype) values (12,3,'08:00:00','13:00:00','DIENSTAG');
insert into office_hour(id,office_id,begin_time,end_time,daytype) values (13,3,'08:00:00','13:00:00','MITTWOCH');
insert into office_hour(id,office_id,begin_time,end_time,daytype) values (14,3,'08:00:00','13:00:00','DONNERSTAG');
insert into office_hour(id,office_id,begin_time,end_time,daytype) values (15,3,'08:00:00','13:00:00','FREITAG');

insert into office_hour(id,office_id,begin_time,end_time,daytype) values (16,4,'09:00:00','13:00:00','DIENSTAG');
insert into office_hour(id,office_id,begin_time,end_time,daytype) values (17,4,'09:00:00','12:00:00','MITTWOCH');
insert into office_hour(id,office_id,begin_time,end_time,daytype) values (18,4,'14:00:00','18:00:00','MITTWOCH');

insert into speciality (id,speciality_name) values (1,'Gynaekologe');
insert into speciality (id,speciality_name) values (2,'Technician');
insert into speciality (id,speciality_name) values (3,'Dentist');
insert into speciality (id,speciality_name) values (4,'Common Allergic');
insert into speciality (id,speciality_name) values (5,'Technician');
insert into speciality (id,speciality_name) values (6,'Psychology');

insert into medicalworker (id,uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,type) values (1,'bS9nqS956xUFYYy26bWmFvT8noo1','graf@dracu.la','Marcus','Mayer','Vamp','ire','','',0);
insert into medicalworker (id,uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,type) values (2,'7EWSTzVnlqgb7bC0dPbjAdmZUV12','max.muster@dracu.la','Max','Muster','Ass','tent','','',1);
insert into medicalworker (id,uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,type) values (3,'PmgPFW3CsZXht2IuOFiAqldozVV2','berta.hadel@dracu.la','Berta','Hadel','Dr','MSc','','',0);
insert into medicalworker (id,uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,type) values (4,'igi3pmYNRRWeQurhojiX8fDJmGj2','mist.eria@dracu.la','Mist','Eria','Dipl.Ing','','','',1);
insert into medicalworker (id,uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,type) values (5,'Ea3D4hYZjAU8UNMM3JYpwACMI303','manuel.meister@dracu.la','Manuel','Meister','Dr.','','','',0);
insert into medicalworker (id,uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,type) values (6,'V3ZYOd3fn5b8q7INqUG06Akh5uI3','eva.jung@dracu.la','Eva','Jung','Dr.','','','',0);

insert into doctor_speciality (doctorid,specialityid) values (1,1);
insert into doctor_speciality (doctorid,specialityid) values (2,2);
insert into doctor_speciality (doctorid,specialityid) values (3,3);
insert into doctor_speciality (doctorid,specialityid) values (4,4);
insert into doctor_speciality (doctorid,specialityid) values (5,5);
insert into doctor_speciality (doctorid,specialityid) values (6,6);

insert into office_worker (officeid,medical_workerid) values (1,1);
insert into office_worker (officeid,medical_workerid) values (1,2);
insert into office_worker (officeid,medical_workerid) values (2,3);
insert into office_worker (officeid,medical_workerid) values (2,4);
insert into office_worker (officeid,medical_workerid) values (3,5);
insert into office_worker (officeid,medical_workerid) values (4,6);

insert into patient (id,uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,svnr) values (1,'1qxo6HiFksd7UYXXY9gtcaGKYCu2','hans@patient.com','Hans','Mueller','','Dr.','','','1111111111');
insert into patient (id,uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,svnr) values (2,'wCjBKGC7EMZo49VTMCDb92toPXf2','franz@patient.com','Franz','Walder','Bsc.','','','','2222111111');
insert into patient (id,uid,e_mail,first_name,last_name,pos_title,pre_title,private_key,public_key,svnr) values (3,'9VB82zVsNLaOiKDOuRfS6VmLzwx2','maria@patient.com','Maria','Zotter','','','','','3333111111');

insert into office_patient (officeid,patientid) values (1,1);
insert into office_patient (officeid,patientid) values (1,2);
insert into office_patient (officeid,patientid) values (2,1);
insert into office_patient (officeid,patientid) values (2,2);
insert into office_patient (officeid,patientid) values (3,1);
insert into office_patient (officeid,patientid) values (4,1);

insert into appointment (id,office_id,patient_id,appointment_begin,appointment_end) values (1,1,1,'2018-12-10 11:00:00','2018-12-10 11:30:00');
insert into appointment (id,office_id,patient_id,appointment_begin,appointment_end) values (2,2,1,'2018-12-11 09:30:00','2018-12-11 10:00:00');
insert into appointment (id,office_id,patient_id,appointment_begin,appointment_end) values (3,3,1,'2018-12-4 12:00:00','2018-12-4 12:30:00');
insert into appointment (id,office_id,patient_id,appointment_begin,appointment_end) values (4,4,1,'2018-12-12 15:30:00','2018-12-12 16:00:00');
insert into appointment (id,office_id,patient_id,appointment_begin,appointment_end) values (5,1,2, '2018-12-10 09:30:00','2018-12-10 10:00:00');

insert into appointment (id,office_id,patient_id,appointment_begin,appointment_end) values (6,3,3,'2019-01-14 12:30:00','2019-01-14 13:00:00');
insert into appointment (id,office_id,patient_id,appointment_begin,appointment_end) values (7,4,2,'2019-01-15 12:00:00','2019-01-15 12:30:00');
insert into appointment (id,office_id,patient_id,appointment_begin,appointment_end) values (8,4,3,'2019-01-15 12:30:00','2019-01-15 13:00:00');

--medicine
insert into medicine (id,name) values ('1329497','Seractil forte 400mg-Filmtabletten');
insert into medicine (id,name) values ('0001370','Aknichthol Lotion');

-- chat values
--insert into office_patient (officeid,patientid) values (1,3);
insert into office_patient (officeid,patientid) values (1,4);
insert into office_patient (officeid,patientid) values (1,5);
insert into office_patient (officeid,patientid) values (1,6);
insert into office_patient (officeid,patientid) values (1,7);
insert into office_patient (officeid,patientid) values (1,8);

insert into office_patient (officeid,patientid) values (2,4);
insert into office_patient (officeid,patientid) values (2,5);
insert into office_patient (officeid,patientid) values (2,6);
insert into office_patient (officeid,patientid) values (2,7);
insert into office_patient (officeid,patientid) values (2,8);
insert into office_patient (officeid,patientid) values (2,9);
insert into office_patient (officeid,patientid) values (2,10);

insert into office_patient (officeid,patientid) values (3,2);
--insert into office_patient (officeid,patientid) values (3,3);
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

--insert into office_patient (officeid,patientid) values (5,3);
insert into office_patient (officeid,patientid) values (5,4);
insert into office_patient (officeid,patientid) values (5,5);
insert into office_patient (officeid,patientid) values (5,8);
insert into office_patient (officeid,patientid) values (5,9);
insert into office_patient (officeid,patientid) values (5,10);
insert into office_patient (officeid,patientid) values (5,11)

insert into office_patient (officeid,patientid) values (6,1);
insert into office_patient (officeid,patientid) values (6,2);
--insert into office_patient (officeid,patientid) values (6,3);
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
--insert into chat_thread (office_id,patient_id) values (1,3);
insert into chat_thread (office_id,patient_id) values (1,4);
insert into chat_thread (office_id,patient_id) values (1,5);
insert into chat_thread (office_id,patient_id) values (1,6);
insert into chat_thread (office_id,patient_id) values (1,7);
insert into chat_thread (office_id,patient_id) values (1,8);

insert into chat_thread (office_id,patient_id) values (2,4);
insert into chat_thread (office_id,patient_id) values (2,5);
insert into chat_thread (office_id,patient_id) values (2,6);
insert into chat_thread (office_id,patient_id) values (2,7);
insert into chat_thread (office_id,patient_id) values (2,8);
insert into chat_thread (office_id,patient_id) values (2,9);
insert into chat_thread (office_id,patient_id) values (2,10);

insert into chat_thread (office_id,patient_id) values (3,1);
insert into chat_thread (office_id,patient_id) values (3,2);
--insert into chat_thread (office_id,patient_id) values (3,3);
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

--insert into chat_thread (office_id,patient_id) values (5,3);
insert into chat_thread (office_id,patient_id) values (5,4);
insert into chat_thread (office_id,patient_id) values (5,5);
insert into chat_thread (office_id,patient_id) values (5,8);
insert into chat_thread (office_id,patient_id) values (5,9);
insert into chat_thread (office_id,patient_id) values (5,10);
insert into chat_thread (office_id,patient_id) values (5,11)

insert into chat_thread (office_id,patient_id) values (6,1);
insert into chat_thread (office_id,patient_id) values (6,2);
--insert into chat_thread (office_id,patient_id) values (6,3);
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


insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 08:11:00', 'Mir geht es nicht gut!', true, 1);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 11:01:00', 'Was fehlt Ihnen denn', false, 1);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 12:12:00', 'Bauchschmerzen', true, 1);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:07:00', 'Ok, ich werde schauen was ich tun kann.', false, 1);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:34:00', 'Vielen Dank!', true, 1);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:50:00', 'Nehmen sie einmal taeglich XYZ', false, 1);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:12:00', 'Hallo!', true, 2);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 16:11:00', 'Hallo!', false, 2);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 17:11:00', 'Was fehlt Ihnen?', false, 2);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 08:11:00', 'Mir geht es nicht gut!', true, 4);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 11:01:00', 'Was fehlt Ihnen denn', false, 4);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 12:12:00', 'Bauchschmerzen', true, 4);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:07:00', 'Ok, ich werde schauen was ich tun kann.', false, 4);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:34:00', 'Vielen Dank!', true, 4);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:01:00', 'Nehmen sie einmal taeglich XYZ', false, 4);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 08:11:00', 'Mir geht es nicht gut!', true, 5);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 11:01:00', 'Was fehlt Ihnen denn', false, 5);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 12:12:00', 'Bauchschmerzen', true, 5);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:07:00', 'Ok, ich werde schauen was ich tun kann.', false, 5);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:34:00', 'Vielen Dank!', true, 5);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:01:00', 'Nehmen sie einmal taeglich XYZ', false, 5);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 08:11:00', 'Mir geht es nicht gut!', true, 10);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 11:01:00', 'Was fehlt Ihnen denn', false, 10);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 12:12:00', 'Bauchschmerzen', true, 10);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:07:00', 'Ok, ich werde schauen was ich tun kann.', false, 10);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:34:00', 'Vielen Dank!', true, 10);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:01:00', 'Nehmen sie einmal taeglich XYZ', false, 10);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 08:11:00', 'Mir geht es nicht gut!', true, 12);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 11:01:00', 'Was fehlt Ihnen denn', false, 12);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 12:12:00', 'Bauchschmerzen', true, 12);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:07:00', 'Ok, ich werde schauen was ich tun kann.', false, 12);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:34:00', 'Vielen Dank!', true, 12);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:01:00', 'Nehmen sie einmal taeglich XYZ', false, 12);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 08:11:00', 'Mir geht es nicht gut!', true, 13);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 11:01:00', 'Was fehlt Ihnen denn', false, 13);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 12:12:00', 'Bauchschmerzen', true, 13);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:07:00', 'Ok, ich werde schauen was ich tun kann.', false, 13);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:34:00', 'Vielen Dank!', true, 13);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:01:00', 'Nehmen sie einmal taeglich XYZ', false, 13);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 08:11:00', 'Mir geht es nicht gut!', true, 15);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 11:01:00', 'Was fehlt Ihnen denn', false, 15);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 12:12:00', 'Bauchschmerzen', true, 15);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:07:00', 'Ok, ich werde schauen was ich tun kann.', false, 15);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:34:00', 'Vielen Dank!', true, 15);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:01:00', 'Nehmen sie einmal taeglich XYZ', false, 15);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 08:11:00', 'Mir geht es nicht gut!', true, 21);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 11:01:00', 'Was fehlt Ihnen denn', false, 21);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 12:12:00', 'Bauchschmerzen', true, 21);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:07:00', 'Ok, ich werde schauen was ich tun kann.', false, 21);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:34:00', 'Vielen Dank!', true, 21);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:01:00', 'Nehmen sie einmal taeglich XYZ', false, 21);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 08:11:00', 'Mir geht es nicht gut!', true, 23);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 11:01:00', 'Was fehlt Ihnen denn', false, 23);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 12:12:00', 'Bauchschmerzen', true, 23);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:07:00', 'Ok, ich werde schauen was ich tun kann.', false, 23);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:34:00', 'Vielen Dank!', true, 23);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:01:00', 'Nehmen sie einmal taeglich XYZ', false, 23);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 08:11:00', 'Mir geht es nicht gut!', true, 25);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 11:01:00', 'Was fehlt Ihnen denn', false, 25);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 12:12:00', 'Bauchschmerzen', true, 25);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:07:00', 'Ok, ich werde schauen was ich tun kann.', false, 25);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:34:00', 'Vielen Dank!', true, 25);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:01:00', 'Nehmen sie einmal taeglich XYZ', false, 25);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 08:11:00', 'Mir geht es nicht gut!', true, 27);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 11:01:00', 'Was fehlt Ihnen denn', false, 27);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 12:12:00', 'Bauchschmerzen', true, 27);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:07:00', 'Ok, ich werde schauen was ich tun kann.', false, 27);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:34:00', 'Vielen Dank!', true, 27);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:01:00', 'Nehmen sie einmal taeglich XYZ', false, 27);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 08:11:00', 'Mir geht es nicht gut!', true, 29);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 11:01:00', 'Was fehlt Ihnen denn', false, 29);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 12:12:00', 'Bauchschmerzen', true, 29);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:07:00', 'Ok, ich werde schauen was ich tun kann.', false, 29);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:34:00', 'Vielen Dank!', true, 29);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:01:00', 'Nehmen sie einmal taeglich XYZ', false, 29);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 08:11:00', 'Mir geht es nicht gut!', true, 31);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 11:01:00', 'Was fehlt Ihnen denn', false, 31);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 12:12:00', 'Bauchschmerzen', true, 31);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:07:00', 'Ok, ich werde schauen was ich tun kann.', false, 31);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:34:00', 'Vielen Dank!', true, 31);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:01:00', 'Nehmen sie einmal taeglich XYZ', false, 31);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 08:11:00', 'Mir geht es nicht gut!', true, 33);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 11:01:00', 'Was fehlt Ihnen denn', false, 33);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 12:12:00', 'Bauchschmerzen', true, 33);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:07:00', 'Ok, ich werde schauen was ich tun kann.', false, 33);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:34:00', 'Vielen Dank!', true, 33);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:01:00', 'Nehmen sie einmal taeglich XYZ', false, 33);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 08:11:00', 'Mir geht es nicht gut!', true, 35);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 11:01:00', 'Was fehlt Ihnen denn', false, 35);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 12:12:00', 'Bauchschmerzen', true, 35);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:07:00', 'Ok, ich werde schauen was ich tun kann.', false, 35);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 13:34:00', 'Vielen Dank!', true, 35);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:01:00', 'Nehmen sie einmal taeglich XYZ', false, 35);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:12:00', 'Hallo!', true, 3);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 16:11:00', 'Hallo!', false, 3);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 17:11:00', 'Was fehlt Ihnen?', false, 3);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 18:11:00', 'Meine Leber macht mir wieder zu schaffen!', true, 3);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 19:11:00', 'Aber die Narbe am Fuß heilt sehr schnell', true, 3);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 20:11:00', 'Ok. Trinken Sie am Besten weniger Alkohol ;)', false, 3);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:12:00', 'Hallo!', true, 16);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 16:11:00', 'Hallo!', false, 16);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 17:11:00', 'Was fehlt Ihnen?', false, 16);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 18:11:00', 'Meine Leber macht mir wieder zu schaffen!', true, 16);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 19:11:00', 'Aber die Narbe am Fuß heilt sehr schnell', true, 16);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 20:11:00', 'Ok. Trinken Sie am Besten weniger Alkohol ;)', false, 16);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:12:00', 'Hallo!', true, 18);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 16:11:00', 'Hallo!', false, 18);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 17:11:00', 'Was fehlt Ihnen?', false, 18);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 18:11:00', 'Meine Leber macht mir wieder zu schaffen!', true, 18);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 19:11:00', 'Aber die Narbe am Fuß heilt sehr schnell', true, 18);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 20:11:00', 'Ok. Trinken Sie am Besten weniger Alkohol ;)', false, 18);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:12:00', 'Hallo!', true, 22);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 16:11:00', 'Hallo!', false, 22);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 17:11:00', 'Was fehlt Ihnen?', false, 22);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 18:11:00', 'Meine Leber macht mir wieder zu schaffen!', true, 22);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 19:11:00', 'Aber die Narbe am Fuß heilt sehr schnell', true, 22);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 20:11:00', 'Ok. Trinken Sie am Besten weniger Alkohol ;)', false, 22);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:12:00', 'Hallo!', true, 24);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 16:11:00', 'Hallo!', false, 24);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 17:11:00', 'Was fehlt Ihnen?', false, 24);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 18:11:00', 'Meine Leber macht mir wieder zu schaffen!', true, 24);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 19:11:00', 'Aber die Narbe am Fuß heilt sehr schnell', true, 24);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 20:11:00', 'Ok. Trinken Sie am Besten weniger Alkohol ;)', false, 24);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:12:00', 'Hallo!', true, 26);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 16:11:00', 'Hallo!', false, 26);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 17:11:00', 'Was fehlt Ihnen?', false, 26);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 18:11:00', 'Meine Leber macht mir wieder zu schaffen!', true, 26);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 19:11:00', 'Aber die Narbe am Fuß heilt sehr schnell', true, 26);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 20:11:00', 'Ok. Trinken Sie am Besten weniger Alkohol ;)', false, 26);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:12:00', 'Hallo!', true, 28);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 16:11:00', 'Hallo!', false, 28);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 17:11:00', 'Was fehlt Ihnen?', false, 28);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 18:11:00', 'Meine Leber macht mir wieder zu schaffen!', true, 28);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 19:11:00', 'Aber die Narbe am Fuß heilt sehr schnell', true, 28);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 20:11:00', 'Ok. Trinken Sie am Besten weniger Alkohol ;)', false, 28);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 14:12:00', 'Hallo!', true, 30);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 16:11:00', 'Hallo!', false, 30);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 17:11:00', 'Was fehlt Ihnen?', false, 30);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 18:11:00', 'Meine Leber macht mir wieder zu schaffen!', true, 30);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 19:11:00', 'Aber die Narbe am Fuß heilt sehr schnell', true, 30);
insert into chat_message(create_date_time, message, patient_message, chat_thread_id) values ('2017-02-11 20:11:00', 'Ok. Trinken Sie am Besten weniger Alkohol ;)', false, 30);
