CREATE TABLE developers (
id         INTEGER PRIMARY KEY NOT NULL,
first_name  VARCHAR(45)     NOT NULL,
last_name   VARCHAR(45)     NOT NULL,
specialty  VARCHAR(45)     NOT NULL,
experience INTEGER         NOT NULL,
salary     INTEGER         NOT NULL
)Engine = InnoDB;

CREATE TABLE skills (
id INTEGER PRIMARY KEY NOT NULL,
name VARCHAR(45) NOT NULL
)Engine = InnoDB;

CREATE TABLE developers_skills (
developer_id INTEGER NOT NULL,
skill_id INTEGER NOT NULL,
FOREIGN KEY (developer_id) REFERENCES developers(id)
ON DELETE CASCADE
       ON UPDATE CASCADE,
FOREIGN KEY(skill_id) REFERENCES skills(id)
ON DELETE CASCADE
       ON UPDATE CASCADE,
UNIQUE (developer_id, skill_id)
)Engine = InnoDB;

CREATE TABLE teams (
id INTEGER PRIMARY KEY NOT NULL,
name VARCHAR(45)       NOT NULL
)Engine = InnoDB;

CREATE TABLE teams_developers (
team_id INTEGER NOT NULL,
developer_id INTEGER NOT NULL,
FOREIGN KEY (team_id) REFERENCES teams(id)
ON DELETE CASCADE
       ON UPDATE CASCADE,
FOREIGN KEY (developer_id) REFERENCES developers(id)
ON DELETE CASCADE
       ON UPDATE CASCADE,
UNIQUE (team_id, developer_id)
)Engine = InnoDB;

CREATE TABLE projects (
id INTEGER PRIMARY KEY NOT NULL,
name VARCHAR(45)       NOT NULL
);

CREATE TABLE projects_teams (
project_id INTEGER NOT NULL,
team_id INTEGER NOT NULL,
FOREIGN KEY (project_id) REFERENCES projects(id)
ON DELETE CASCADE
       ON UPDATE CASCADE,
FOREIGN KEY (team_id) REFERENCES teams(id)
ON DELETE CASCADE
       ON UPDATE CASCADE,
UNIQUE (project_id, team_id)
)Engine = InnoDB;

CREATE TABLE companies (
id INTEGER PRIMARY KEY NOT NULL,
name VARCHAR(45)       NOT NULL
);

CREATE TABLE companies_projects (
company_id INTEGER NOT NULL,
project_id INTEGER NOT NULL,
FOREIGN KEY (company_id) REFERENCES companies(id)
ON DELETE CASCADE
       ON UPDATE CASCADE,
FOREIGN KEY (project_id) REFERENCES projects(id)
ON DELETE CASCADE
       ON UPDATE CASCADE,
UNIQUE (company_id, project_id)
)Engine = InnoDB;

CREATE TABLE customers (
id         INTEGER PRIMARY KEY NOT NULL,
first_name  VARCHAR(45)     NOT NULL,
last_name   VARCHAR(45)     NOT NULL,
address  VARCHAR(45)     NOT NULL
)Engine = InnoDB;

CREATE TABLE customers_companies (
customer_id INTEGER NOT NULL,
company_id INTEGER NOT NULL,
FOREIGN KEY (customer_id) REFERENCES customers(id)
ON DELETE CASCADE
       ON UPDATE CASCADE,
FOREIGN KEY (company_id) REFERENCES companies(id)
ON DELETE CASCADE
       ON UPDATE CASCADE,
UNIQUE (customer_id, company_id)
)Engine = InnoDB;










