package net.petriv.dao.impl;

import net.petriv.dao.AbstractDao;
import net.petriv.dao.SkillDao;
import net.petriv.jdbc.enricher.SkillEnricher;
import net.petriv.jdbc.extractor.SkillExtractor;
import net.petriv.jdbc.setter.SkillSetter;
import net.petriv.model.Skill;

import java.sql.SQLException;
import java.util.List;

public class SkillDaoImpl extends AbstractDao<Skill> implements SkillDao {

    private final String SAVE_SKILL = "INSERT INTO skills (id, name)\n" +
            "VALUES (?, ?);";
    private final String GET_SKILL_BY_ID = "SELECT * FROM skills WHERE ID = ?;";
    private final String GET_ALL = "SELECT * FROM skills;";
    private final String DELETE_REFERANCE = "DELETE FROM developer_skills WHERE skill_id = ?;";
    private final String DELETE_ENTITY = "DELETE FROM developers WHERE id = ?;";
    private final String JOIN = "SELECT skills.id, name from developers join developer_skills on ( ? = developer_skills.developer_id) join skills on (skills.id = developer_skills.skill_id) GROUP BY name;";
    private final String UPDATE = "Update skills SET name = ?  WHERE skills.id = ?;";


    public void save(Skill newEntity) throws SQLException {
        save(newEntity, SAVE_SKILL, new SkillSetter());
    }

    public Skill getById(int id) throws SQLException {
        return getById(id, GET_SKILL_BY_ID, new SkillExtractor(), new SkillEnricher(new DeveloperDaoImpl()));
    }

    public List<Skill> getAll() throws SQLException {
        return getAll(GET_ALL, new SkillExtractor(), new SkillEnricher(new DeveloperDaoImpl()));
    }

    public void delete(int id) throws SQLException {
        delete(id, DELETE_REFERANCE, DELETE_ENTITY);

    }

    public void update(Skill Entity) throws SQLException {
        update(Entity, UPDATE, new SkillSetter());
    }

    public List<Skill> selelectSkillsByDeveloperId(int developerId) throws SQLException {
        return selectEntityById(developerId, JOIN, new SkillSetter(), new SkillExtractor());
    }
}

