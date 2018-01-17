package net.petriv.dao.impl;

import net.petriv.dao.AbstractDao;
import net.petriv.dao.DeveloperDao;
import net.petriv.jdbc.enricher.DeveloperEnricher;
import net.petriv.jdbc.extractor.DeveloperExtractor;
import net.petriv.jdbc.setter.DeveloperSetter;
import net.petriv.model.Developer;

import java.sql.*;
import java.util.*;

public class DeveloperDaoImpl extends AbstractDao<Developer> implements DeveloperDao {

    private final String SAVE_DEVELOPER="INSERT INTO developers (id, first_name, last_name, specialty, experience, salary)\n"+
        "VALUES (?, ?, ?, ?, ?, ?);";
    private final String GET_DEVELOPER_BY_ID="SELECT * FROM developers WHERE ID = ?;";
    private final String GET_ALL = "SELECT * FROM developers;";
    private final String DELETE_REFERANCE = "DELETE FROM developer_skills WHERE developer_id = ?;";
    private  final String DELETE_ENTITY = "DELETE FROM developers WHERE id = ?;";
    private final String JOIN_TABLE = "select skills.id, name from developers join developer_skills on ( ? = developer_skills.developer_id) join skills on (skills.id = developer_skills.skill_id) GROUP BY name;";
    private final String UPDATE = "Update developers SET first_name = ?, last_name = ?,\" +\n" +
            "                    \"specialty = ?, experience = ? , salary = ? WHERE developers.id = ?;";
    private final String RELETION = "INSERT INTO developer_skills (developer_id, skill_id) \n" +
            "VALUES (?, ?);";

    public void save(Developer newEntity) throws SQLException {
        save(newEntity, SAVE_DEVELOPER, new DeveloperSetter());
        chooseSkillForDeveloper(newEntity);
    }

    public Developer getById(int id) throws SQLException {
        return getById(id, GET_DEVELOPER_BY_ID, new DeveloperExtractor(), new DeveloperEnricher(new SkillDaoImpl()));
    }

    public List<Developer> getAll() throws SQLException {
       return getAll(GET_ALL, new DeveloperExtractor(),new DeveloperEnricher(new SkillDaoImpl()));
    }

    public void delete(int id) throws SQLException {
        delete(id, DELETE_REFERANCE, DELETE_ENTITY);

    }

    public void update(Developer Entity) throws SQLException {
        update(Entity, UPDATE, new DeveloperSetter());

    }

    public List<Developer> selectDevelopersBySkillId(int skillId) throws SQLException {
        return  null;

    }

    private void chooseSkillForDeveloper(Developer developer){
        relationship(developer.getId(), developer.getSkillsId() , RELETION);
    }
}






