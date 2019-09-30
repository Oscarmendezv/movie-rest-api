INSERT INTO MOVIES (title, movie_genre, year) VALUES
('TestMovie1', 'ACTION', 2005),
('TestMovie2', 'ADVENTURE', 1990),
('TestMovie3', 'SCI_FI', 2009),
('TestMovie4', 'DRAMA', 2010),
('TestMovie5', 'COMEDY', 1955),
('TestMovie6', 'ACTION',2005),
('TestMovie7', 'ADVENTURE', 1970),
('TestMovie8', 'SCI_FI', 2019),
('TestMovie9', 'DRAMA', 2001),
('TestMovie10', 'ACTION', 2012);

INSERT INTO actors (name, date_of_birth) VALUES
('TestActor1', '1990-03-20'),
('TestActor2', '1953-08-03'),
('TestActor3', '1990-11-30');

INSERT INTO movies_actors (actor_id, movie_id, role) VALUES
(1, 1, 'PRINCIPAL_ACTOR'),
(3,1, 'EXTRA_PERFORMER'),
(1,2, 'PRINCIPAL_ACTOR'),
(2,2, 'SUPPORTING_ACTOR'),
(3,2, 'SUPPORTING_ACTOR');