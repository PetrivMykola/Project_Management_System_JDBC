CREATE TABLE Developers (
id         INTEGER PRIMARY KEY NOT NULL,
FirstName  VARCHAR(45)     NOT NULL,
LastName   VARCHAR(45)     NOT NULL,
Specialty  VARCHAR(45)     NOT NULL,
Experience INTEGER         NOT NULL,
Salary     INTEGER         NOT NULL
)Engine=InnoDB;

CREATE TABLE Skills (
id INTEGER PRIMARY KEY NOT NULL,
Name VARCHAR(45) NOT NULL
)Engine=InnoDB;

CREATE TABLE DeveloperSkill (
DeveloperId INTEGER NOT NULL PRIMARY KEY,
SkillId INTEGER NOT NULL PRIMARY KEY,
FOREIGN KEY (DeveloperId) REFERENCES Developers(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
FOREIGN KEY(SkillId) REFERENCES Skills(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)Engine=InnoDB;



