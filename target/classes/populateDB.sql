

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

 select s.* from skills s
     inner join developers d on s.id = d.id
     inner join skills sk on sk.id = d.id
     where d.id = 1;


     select s.* from skills s
     inner join developers d on s.id = d.id
     inner join skills sk on sk.id = d.id
     where d.id = 1;

     select skills.id, developers.id, developers.first_name, name from developers
     join developers_skills on
     ( 1 = developers_skills.skill_id)
     join skills on (skills.id = developers_skills.skill_id)
     GROUP BY name;

     select developers.id, developers.first_name,
     developers.last_name, developers.specialty,
     developers.experience, developers.salary from skills
     join developers_skills on
     ( 1 = developers_skills.skill_id)
     join developers on (developers.id = developers_skills.developer_id)
     GROUP BY first_name;

      select teams.id, teams.name from projects
     join projects_teams  on
     ( 1 = projects_teams.project_id)
     join teams on (teams.id = projects_teams.team_id)
     GROUP BY name;

     select projects.id, projects.name from companies
     join companies_projects  on
     ( ? = companies_projects.company_id)
     join projects on (projects.id = companies_projects.project_id)
     GROUP BY name;

      select companies.id, companies.name from customers
     join customers_companies  on
     ( 1 = customers_companies.customer_id)
     join companies on (companies.id = customers_companies.company_id)
     GROUP BY name;





