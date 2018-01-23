package net.petriv.controller;

import net.petriv.dao.DeveloperDaoImpl;
import net.petriv.dao.TeamDaoImpl;
import net.petriv.model.Developer;
import net.petriv.model.Team;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TeamController {

    Scanner in = new Scanner(System.in);
    Team team;
    TeamDaoImpl dao;

    public TeamController() {
        dao = new TeamDaoImpl();
    }

    public void saveTeam() throws SQLException {
        dao.save(createNewTeam());
        System.out.println("Create and save Team was successful");
        System.out.println("##############################");
    }

    public void showListTeam() throws SQLException {
        System.out.println(dao.getAll());
        System.out.println("##############################");
    }

    public void updateTeam()throws SQLException {
        System.out.println("Enter id Team for update: ");
        int id = in.nextInt();
        System.out.println("Team for udate: " + dao.getById(id));
        Team newTeam = enterFieldsForTeam(dao.getById(id));
        dao.update(newTeam);
        System.out.println("###################################");
    }

    public void deleteTeam() throws SQLException {
        System.out.println("Enter id Team for delete: ");
        int id = in.nextInt();
        dao.delete(id);
        System.out.println("##############################");
    }

    public Team createNewTeam() {
        team = new Team();
        System.out.println("Enter id:");
        team.setId(in.nextInt());
        System.out.println("Enter name:");
        team.setName(in.next());
        System.out.println("Enter id developers for team: ");
        team.setDevelopers(getDevelopersForTeam(in.nextInt()));
        return team;
    }

    public Team enterFieldsForTeam(Team team) {
        System.out.println("Enter id:");
        team.setId(in.nextInt());
        System.out.println("Enter name:");
        team.setName(in.next());
        System.out.println("Enter id developers for team");
        team.setDevelopers(getDevelopersForTeam(in.nextInt()));
        return team;
    }

    private List<Developer> getDevelopersForTeam (int id) {
        List<Developer> list = new ArrayList<Developer>();
        try {
            DeveloperDaoImpl developerDao = new DeveloperDaoImpl();
            Developer developer = developerDao.getById(id);
            list.add(developer);
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        return list;
    }
}
