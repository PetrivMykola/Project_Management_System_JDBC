package net.petriv.dao;

import net.petriv.dao.GeneralDao;
import net.petriv.model.Skill;

import java.sql.SQLException;
import java.util.List;

public class SkillDaoImpl implements GeneralDao<Skill> {

    private final String SAVE_SKILL = "INSERT INTO skills (id, name)\n" +
            "VALUES (?, ?);";
    private final String GET_SKILL_BY_ID = "SELECT * FROM skills WHERE ID = ?;";
    private final String GET_ALL = "SELECT * FROM skills;";
    private final String DELETE_REFERANCE = "DELETE FROM developer_skills WHERE skill_id = ?;";
    private final String DELETE_ENTITY = "DELETE FROM developers WHERE id = ?;";
    private final String JOIN = "SELECT skills.id, name from developers join developer_skills on ( ? = developer_skills.developer_id) join skills on (skills.id = developer_skills.skill_id) GROUP BY name;";
    private final String UPDATE = "Update skills SET name = ?  WHERE skills.id = ?;";

    public void save(Skill newEntity) throws SQLException {

    }

    public Skill getById(int id) throws SQLException {
        return null;
    }

    public List<Skill> getAll() throws SQLException {
        return null;
    }

    public void delete(int id) throws SQLException {

    }

    public void update(Skill Entity) throws SQLException {

    }
}