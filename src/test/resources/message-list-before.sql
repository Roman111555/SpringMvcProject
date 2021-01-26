delete from message;


insert into message(id, text, tag, user_id) values
(1, 'first', 'new-tag', 1),
(2, 'second', 'more', 1),
(3, 'third', 'new-tag', 1),
(4, 'foth', 'another', 1);

alter sequence hibernate_sequence restart with 10;