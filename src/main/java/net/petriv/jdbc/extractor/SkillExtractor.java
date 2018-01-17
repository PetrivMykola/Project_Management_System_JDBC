package net.petriv.jdbc.extractor;

import net.petriv.model.Skill;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillExtractor extends Extractor<Skill> {

    public Skill extractOne(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        return new Skill(id, name);

    }
}
