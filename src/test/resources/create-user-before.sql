delete from user_role;
delete  from usr;

insert into usr(id, active, username, password) values
(1, true, 'admin', '$2a$08$Vgyw9D38nLTAUkvc3ExHkO5r8YUUyMyL/N741wRVncDzYk2HjNZ1O'),
(2, true, 'Roman', '$2a$08$lQURRc0VWbFKGis7QQhYz.cJ3HgGn.mosAXcnByzCTdD688q5I/Qa');

insert into user_role(user_id, roles)
values (1, 'ADMIN'), (1, 'USER'), (2, 'USER');