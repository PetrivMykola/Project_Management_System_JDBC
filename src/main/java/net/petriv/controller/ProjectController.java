package net.petriv.controller;

import net.petriv.dao.ProjectDaoImpl;
import net.petriv.dao.TeamDaoImpl;
import net.petriv.model.Project;
import net.petriv.model.Team;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProjectController {
    
    Scanner in = new Scanner(System.in);
    Project project;
    ProjectDaoImpl dao;

    public ProjectController() {
        dao = new ProjectDaoImpl();
    }

    public void saveProject()throws SQLException {
        dao.save(createNewProject());
        System.out.println("Create and save Project was successful");
        System.out.println("##############################");
    }

    public void showListProject()throws SQLException {
        System.out.println(dao.getAll());
        System.out.println("##############################");
    }

    public void updateProject() throws SQLException {
        System.out.println("Enter id Project for update: ");
        int id = in.nextInt();
        System.out.println("Project for udate: " + dao.getById(id));
        Project newProject = enterFieldsForTeam(dao.getById(id));
        dao.update(newProject);
        System.out.println("###################################");
    }

    public void deleteProject() throws SQLException {
        System.out.println("Enter id Project for delete: ");
        int id = in.nextInt();
        dao.delete(id);
        System.out.println("##############################");
    }

    public Project createNewProject() {
        project = new Project();
        System.out.println("Enter id:");
        project.setId(in.nextInt());
        System.out.println("Enter name:");
        project.setName(in.next());
        System.out.println("Enter id teams for company: ");
        project.setTeams(getTeamsForProject(in.nextInt()));
        return project;
    }

    public Project enterFieldsForTeam(Project project) {
        System.out.println("Enter id:");
        project.setId(in.nextInt());
        System.out.println("Enter name:");
        project.setName(in.next());
        System.out.println("Enter id teams for company: ");
        project.setTeams(getTeamsForProject(in.nextInt()));
        return project;
    }

    public List<Team> getTeamsForProject(int id) {
        List<Team> list = new ArrayList<Team>();
        try {
            TeamDaoImpl teamDao = new TeamDaoImpl();
            Team team = teamDao.getById(id);
            list.add(team);
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        return list;
    }
}
