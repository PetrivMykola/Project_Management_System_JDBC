package net.petriv.dao;

import net.petriv.model.Skill;

import java.sql.SQLException;
import java.util.List;

public interface SkillDao extends GeneralDao<Skill> {

     List<Skill> selelectSkillsByDeveloperId(int developerId) throws SQLException;

}
