package net.petriv.jdbc.enricher;

import net.petriv.dao.DeveloperDao;
import net.petriv.dao.SkillDao;
import net.petriv.model.Developer;
import net.petriv.model.Skill;

import java.sql.SQLException;
import java.util.List;

public class DeveloperEnricher implements Enricher<Developer> {
    private final SkillDao skillDao;

    public DeveloperEnricher(SkillDao skillDao) {
        this.skillDao = skillDao;
    }

    public void enrich(Developer record) throws SQLException {
       List<Skill> skills = skillDao.selelectSkillsByDeveloperId(record.getId());
       record.setSkills(skills);
    }
}
