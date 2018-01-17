package net.petriv.dao;

import net.petriv.model.Developer;
import net.petriv.model.Skill;

import java.sql.SQLException;
import java.util.List;

public interface DeveloperDao extends GeneralDao<Developer> {

    List<Developer> selectDevelopersBySkillId(int skillId) throws SQLException;


}
