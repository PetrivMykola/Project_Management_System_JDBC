

INSERT INTO developers (id, first_name, last_name, specialty, experience, salary)
VALUES (1, 'Mykola', 'Petriv', 'Java', 1, 400);
INSERT INTO developers (id, first_name, last_name, specialty, experience, salary)
VALUES (2, 'Eugene', 'Suleimanov', 'C', 5, 2000);

 
INSERT INTO skills (id, name)
VALUES (1, 'HTML');
INSERT INTO skills (id, name)
VALUES (2, 'CSS');
INSERT INTO skills (id, name)
VALUES (3, 'Angular');


INSERT INTO developers_skills (developer_id, skill_id) 
VALUES (1, 1);
INSERT INTO developers_skills (developer_id, skill_id) 
VALUES (1, 2);
INSERT INTO developers_skills (developer_id, skill_id) 
VALUES (2, 1);
INSERT INTO developers_skills (developer_id, skill_id) 
VALUES (2, 2);

INSERT INTO teams (id, name)
VALUES (1, 'frontEnd');
INSERT INTO teams (id, name)
VALUES (1, 'backEnd');

INSERT INTO teams_developers (team_id, developer_id)
VALUES (1, 1);
INSERT INTO teams_developers (team_id, developer_id)
VALUES (1, 2);

INSERT INTO projects (id, name)
VALUES (1, 'facebook');
INSERT INTO projects (id, name)
VALUES (2, 'ebay');

INSERT INTO projects_teams (project_id, team_id)
VALUES (1, 1);
INSERT INTO projects_teams (project_id, team_id)
VALUES (1, 2);
INSERT INTO projects_teams (project_id, team_id)
VALUES (2, 1);
INSERT INTO projects_teams (project_id, team_id)
VALUES (2, 2);






