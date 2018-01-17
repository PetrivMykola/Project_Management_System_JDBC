package net.petriv.jdbc.extractor;

import net.petriv.model.Developer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DeveloperExtractor extends Extractor<Developer> {

    public Developer extractOne(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String specialty = rs.getString("specialty");
        int experience = rs.getInt("experience");
        int salary = rs.getInt("salary");
        return new Developer(id, firstName, lastName, specialty, experience, salary);

    }
}
