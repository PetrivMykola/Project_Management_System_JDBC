package net.petriv.dao;

import net.petriv.model.Developer;
import net.petriv.model.Skill;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class Test1 {
    public static void main(String[] args) throws SQLException {

        DeveloperDaoJdbc dao = new DeveloperDaoJdbc();
        Developer dev = new Developer();

        Skill skill = new Skill(1, "Frontend");
        Set<Skill> set = new HashSet();
        set.add(skill);

        dev.setId(2);
        dev.setFirstName("Mike");
        dev.setLastName("Thomson");
        dev.setSpecialty("JavaScript");
        dev.setExperience(2);
        dev.setSalary(2000);
        dev.setSkills(set);
        dao.save(dev);
        System.out.println(dao.getAll());
    }
}
