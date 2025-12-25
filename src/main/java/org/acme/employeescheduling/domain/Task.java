package org.acme.employeescheduling.domain;

import java.util.Set;

public class Task {

    private String id;

    private int hoursNeededForCompletion;
    private Set<Skill> skills;


    public Task() {
    }


    public Task(String id, int hoursNeededForCompletion, Set<Skill> skills) {
        this.id = id;
        this.hoursNeededForCompletion = hoursNeededForCompletion;
        this.skills = skills;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public int getHoursNeededForCompletion() {
        return hoursNeededForCompletion;
    }

    public void setHoursNeededForCompletion(int hoursNeededForCompletion) {
        this.hoursNeededForCompletion = hoursNeededForCompletion;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
