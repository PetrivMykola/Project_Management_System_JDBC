package net.petriv.test;

import net.petriv.dao.impl.DeveloperDaoImpl;
import net.petriv.jdbc.setting.ConnectionFactoryFactory;
import net.petriv.model.Developer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws SQLException {
        ConnectionFactoryFactory.setType(ConnectionFactoryFactory.FactoryType.C3P0);

        DeveloperDaoImpl dao = new DeveloperDaoImpl();
        Developer dev = new Developer();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        dev.setId(5);
        dev.setFirstName("Oleh");
        dev.setLastName("Kupyna");
        dev.setSpecialty("Plumber");
        dev.setExperience(3);
        dev.setSalary(2000);
        dev.setSkillsId(list);

        // dao.save(dev);

        System.out.println(dao.getById(5));
        // dao.delete(5);
        // dao.save(dev);
        // dao.delete(5);
        //dao.delete(1);
        //dao.save(dev);
        //System.out.println(dao.getById(3));
        // System.out.println(dao.getAll());


        // ConnectionFactoryJdbc.getConnection();
    }
}
