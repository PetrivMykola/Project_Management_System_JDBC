package net.petriv.controller;


import net.petriv.dao.SkillDaoImpl;
import net.petriv.model.Skill;
import java.sql.SQLException;
import java.util.Scanner;

public class SkillController {

    Scanner in = new Scanner(System.in);
    Skill skill;
    SkillDaoImpl dao;

    public SkillController() {
        dao = new SkillDaoImpl();
    }

    public void saveSkill() throws SQLException {
        dao.save(createNewSkill());
        System.out.println("Create and save Skill was successful");
        System.out.println("##############################");
    }

    public void showListSkill() throws SQLException {
        System.out.println(dao.getAll());
        System.out.println("##############################");
    }

    public void updateSkill() throws SQLException {
        System.out.println("Enter id skill for update: ");
        int id = in.nextInt();
        System.out.println("Skill for udate: " + dao.getById(id));
        Skill newDev = enterFieldsForSkill(dao.getById(id));
        dao.update(newDev);
        System.out.println("###################################");
    }

    public void deleteSkill() throws SQLException{
        System.out.println("Enter id skill for delete: ");
        int id = in.nextInt();
        dao.delete(id);
        System.out.println("##############################");

    }

    public Skill createNewSkill() throws SQLException {
        skill = new Skill();
        System.out.println("Enter id:");
        skill.setId(in.nextInt());
        System.out.println("Enter name:");
        skill.setName(in.next());
        return skill;
    }

    public Skill enterFieldsForSkill(Skill skill) throws SQLException {
        skill = new Skill();
        System.out.println("Enter id:");
        skill.setId(in.nextInt());
        System.out.println("Enter name:");
        skill.setName(in.next());
        return skill;

    }
}
