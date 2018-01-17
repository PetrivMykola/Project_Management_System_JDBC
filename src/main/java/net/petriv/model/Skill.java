package net.petriv.model;

import java.util.List;

public class Skill {
    private int id;
    private String name;
    List<Developer> developers;

    public Skill() { }

    public Skill(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public List<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<Developer> developers) {
        this.developers = developers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return
                "id=" + id +
                        ", name='" + name + '\'';
    }
}
