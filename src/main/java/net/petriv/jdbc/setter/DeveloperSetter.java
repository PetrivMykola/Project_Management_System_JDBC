package net.petriv.jdbc.setter;

import net.petriv.model.Developer;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeveloperSetter extends Setter<Developer> {

    public Developer setOne(Developer dev, PreparedStatement ps) {
        try {
            ps.setInt(1, dev.getId());
            ps.setString(2, dev.getFirstName());
            ps.setString(3, dev.getLastName());
            ps.setString(4, dev.getSpecialty());
            ps.setInt(5, dev.getExperience());
            ps.setInt(6, dev.getSalary());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.getErrorCode();
        }

        return dev;
    }
}
