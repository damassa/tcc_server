insert into tcc_server_test.role (id, name)
values (1, 'ROLE_ADMIN');
insert into tcc_server_test.role (id, name)
VALUES (2, 'ROLE_USER');

insert into tcc_server_test.users (id, email, name, password)
VALUES (1, 'admin@admin.com', 'Administrador', '$2a$10$HKveMsPlst41Ie2LQgpijO691lUtZ8cLfcliAO1DD9TtZxEpaEoJe');
insert into tcc_server_test.users (id, email, name, password)
VALUES (2, 'fdfreekazoid@gmail.com', 'Felipe Leal Damasceno',
        '$2a$10$HKveMsPlst41Ie2LQgpijO691lUtZ8cLfcliAO1DD9TtZxEpaEoJe');

insert into tcc_server_test.user_roles (user_id, role_id)
values (1, 1);
insert into tcc_server_test.user_roles (user_id, role_id)
VALUES (2, 2);

insert into tcc_server_test.series (id, big_image, image, name, opening_video, plot, year)
values (1,
        'asdasdasd',
        'https://image.tmdb.org/t/p/w600_and_h900_bestv2/og6g7Ei0HIjj3bdi7mmNdNK2W3v.jpg', 'Himitsu Sentai Gorenger',
        'https://www.youtube.com/watch?v=gbuEg2v9Arg', 'The first super sentai heroes.', 1975);
insert into tcc_server_test.series (id, big_image, image, name, opening_video, plot, year)
values (2,
        'gdfddgdfdf',
        'https://static.tvtropes.org/pmwiki/pub/images/jakq_v1_2.jpg', 'J.A.Q.K Dengekitai',
        'https://www.youtube.com/watch?v=MoXfpRa1bP8', 'The second super sentai heroes.', 1977);

insert into tcc_server_test.categories (id, name)
VALUES (1, 'Super Sentai');
insert into tcc_server_test.categories (id, name)
VALUES (2, 'Ultraman');

insert into tcc_server_test.episodes (id, duration, name, series_id)
VALUES (1, 20, 'O sol vermelho! Os incríveis Goranger', 1);
insert into tcc_server_test.episodes (id, duration, name, series_id)
VALUES (2, 20, '4 cartas! O grande trunfo é J.A.K.Q.', 2);

insert into tcc_server_test.favorites (serie_id, user_id)
VALUES (1, 2);
insert into tcc_server_test.favorites (serie_id, user_id)
VALUES (2, 2);

insert into tcc_server_test.histories (paused_at, episode_id, user_id)
VALUES (01.34, 1, 2);
insert into tcc_server_test.histories (paused_at, episode_id, user_id)
VALUES (15.01, 2, 2);

insert into tcc_server_test.ratings (comment, stars, serie_id, user_id)
VALUES ('Muito bom.', 5, 1, 2);
insert into tcc_server_test.ratings (comment, stars, serie_id, user_id)
VALUES ('Muito bom.', 5, 2, 2);