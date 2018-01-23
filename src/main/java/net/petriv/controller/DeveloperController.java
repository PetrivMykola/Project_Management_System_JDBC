package net.petriv.controller;

import net.petriv.dao.DeveloperDaoImpl;
import net.petriv.dao.SkillDaoImpl;
import net.petriv.model.Developer;
import net.petriv.model.Skill;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DeveloperController {

    Scanner in = new Scanner(System.in);
    Developer dev;
    DeveloperDaoImpl dao;

    public DeveloperController() {
        dao = new DeveloperDaoImpl();
    }

    public Developer createNewDeveloper() throws SQLException {
        dev = new Developer();
        System.out.println("Enter id:");
        dev.setId(in.nextInt());
        System.out.println("Enter name:");
        dev.setFirstName(in.next());
        System.out.println("Enter lastName:");
        dev.setLastName(in.next());
        System.out.println("Enter specialty");
        dev.setSpecialty(in.next());
        System.out.println("Enter experience");
        dev.setExperience(in.nextInt());
        System.out.println("Enter Salary:");
        dev.setSalary(in.nextInt());
        System.out.println("Enter Skills Id:");
        dev.setSkills(getSkillsForDeveloper(in.nextInt()));

        return dev = new Developer(dev.getId(), dev.getFirstName(), dev.getLastName(), dev.getSpecialty(),dev.getExperience(), dev.getSalary(), dev.getSkills());
    }

    public Developer enterFieldsForDeveloper(Developer dev) throws SQLException {
        dev = new Developer();
        System.out.println("Enter id:");
        dev.setId(in.nextInt());
        System.out.println("Enter name:");
        dev.setFirstName(in.next());
        System.out.println("Enter lastName:");
        dev.setLastName(in.next());
        System.out.println("Enter specialty");
        dev.setSpecialty(in.next());
        System.out.println("Enter experience");
        dev.setExperience(in.nextInt());
        System.out.println("Enter Salary:");
        dev.setSalary(in.nextInt());
        System.out.println(new SkillDaoImpl().getAll());
        System.out.println("Enter Skills Id:");
        dev.setSkills(getSkillsForDeveloper(in.nextInt()));

        return dev = new Developer(dev.getId(), dev.getFirstName(), dev.getLastName(), dev.getSpecialty(), dev.getExperience(), dev.getSalary(), dev.getSkills());
    }

    public void saveDeveloper() {
        try {
            dao.save(createNewDeveloper());
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        System.out.println("Create and save Developer was successful");
        System.out.println("##############################");
    }
    public void showListDevelopers() {
        try {
            System.out.println(dao.getAll());
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        System.out.println("##############################");
    }

    public void updateDeveloper() {
        System.out.println("Enter id developer for update: ");
        int id = in.nextInt();
        try {
            System.out.println("Developer for udate: " + dao.getById(id));
            Developer newDev = enterFieldsForDeveloper(dao.getById(id));
            dao.update(newDev);
            System.out.println("###################################");
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }

    }

    public void deleteDeveloper() {
        System.out.println("Enter id developer for delete: ");
        int id = in.nextInt();
        try {
            dao.delete(id);
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        System.out.println("##############################");

    }

    public List<Skill> getSkillsForDeveloper(int id) {
        List<Skill> list = new ArrayList<Skill>();
        try {
            SkillDaoImpl skillDao = new SkillDaoImpl();
            Skill skill = skillDao.getById(id);
            list.add(skill);
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        return list;
    }

}

