package net.petriv.dao;

import net.petriv.model.Developer;
import net.petriv.model.Skill;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class Test1 {
    public static void main(String[] args) throws SQLException {

        DeveloperDao dao = new DeveloperDao();
        Developer dev = new Developer();

        Skill skill = new Skill();
        skill.setId(1);
        Set<Skill> set = new HashSet();
        set.add(skill);

        dev.setId(3);
        dev.setFirstName("Jony");
        dev.setLastName("Peterson");
        dev.setSpecialty("C#");
        dev.setExperience(6);
        dev.setSalary(6000);
        dev.setSkills(set);
        //dao.delete(1);
       //dao.save(dev);
       System.out.println(dao.getById(3));
       // System.out.println(dao.getAll());


       // DBConnection.getConnection();
    }
}
