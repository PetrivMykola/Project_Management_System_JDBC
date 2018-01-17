package net.petriv.jdbc.enricher;

import net.petriv.dao.DeveloperDao;
import net.petriv.model.Developer;
import net.petriv.model.Skill;

import java.sql.SQLException;
import java.util.List;

public class SkillEnricher implements Enricher<Skill> {

    private final DeveloperDao developerDao;

    public SkillEnricher(DeveloperDao developerDao) {
        this.developerDao = developerDao;
    }

    public void enrich(Skill record) throws SQLException {
        List<Developer> skills = developerDao.selectDevelopersBySkillId(record.getId());
        record.setDevelopers(skills);
    }
}
