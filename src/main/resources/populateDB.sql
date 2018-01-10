INSERT INTO developers (id, first_name, last_name, specialty, experience, salary)
VALUES (1, 'Mykola', 'Petriv', 'Java', 1, 400);

INSERT INTO developers 
VALUES (2, 'Eugene', 'Suleimanov', 'C', 5, 2000);

 
INSERT INTO skills (id, Name)
VALUES (1, "HTML");

INSERT INTO skills
VALUES (2, "CSS");

INSERT INTO skills
VALUES (3, "Angular");


INSERT INTO developer_skills (developer_id, skill_id) 
VALUES (1, 1);
INSERT INTO developer_skills (developer_id, skill_id) 
VALUES (1, 2);
INSERT INTO developer_skills (developer_id, skill_id)
VALUES (2, 1);
INSERT INTO developer_skills (developer_id, skill_id)
VALUES (2, 2);


