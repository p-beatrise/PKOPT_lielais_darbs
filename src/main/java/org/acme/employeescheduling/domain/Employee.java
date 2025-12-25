package org.acme.employeescheduling.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Employee {

    private String name;
    private Set<Skill> skills;
    private Preference time;
    private Set<Vacation> vacation;
    private Set<SickLeave> sick;
    
    public Employee() {

    }

    public Employee(String name, Set<Skill> skills, Preference time, Set<Vacation> vacation, Set<SickLeave> sick) {
        this.name = name;
        this.skills = skills;
        this.time = time;
        this.vacation = vacation != null ? vacation : new HashSet<>();
        this.sick = sick != null ? sick : new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }
    
    public Preference getTime() {
        return time;
    }

    public void setTime(Preference time) {
        this.time = time;
    }
    
    public Set<Vacation> getVacation() {
        return vacation;
    }

    public void setVacation(Set<Vacation> vacation) {
        this.vacation = vacation;
    }
    
    public Set<SickLeave> getSick() {
        return sick;
    }

    public void setSick(Set<SickLeave> sick) {
        this.sick = sick;
    }
    
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee employee)) {
            return false;
        }
        return Objects.equals(getName(), employee.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
