package net.petriv.jdbc.setter;

import net.petriv.model.Skill;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SkillSetter extends Setter<Skill> {

    public Skill setOne(Skill skill, PreparedStatement ps) {
        try {
            ps.setInt(1, skill.getId());
            ps.setString(2, skill.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.getErrorCode();
        }

        return skill;
    }
}
