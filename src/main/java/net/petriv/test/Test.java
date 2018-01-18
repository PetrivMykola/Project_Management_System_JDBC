package net.petriv.test;

import net.petriv.dao.DeveloperDaoImpl;
import net.petriv.jdbc.setting.ConnectionFactoryFactory;
import net.petriv.model.Developer;
import net.petriv.model.Skill;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws SQLException {
        ConnectionFactoryFactory.setType(ConnectionFactoryFactory.FactoryType.JDBC);

        DeveloperDaoImpl dao = new DeveloperDaoImpl();
        Developer dev = new Developer();
        List<Skill> skillList = new ArrayList<Skill>();
        Skill skill = new Skill(1);
        skillList.add(skill);

        dev.setId(5);
        dev.setFirstName("Peter");
        dev.setLastName("Brosnan");
        dev.setSpecialty("Delphi");
        dev.setExperience(1);
        dev.setSalary(100);
        dev.setSkills(skillList);

       dao.save(dev);

        //System.out.println(dao.getAll());
        //dao.save(dev);
        //System.out.println(dao.getById(6));
         System.out.println(dao.getAll());


        // ConnectionFactoryJdbc.getConnection();
    }
}
