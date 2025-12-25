package org.acme.employeescheduling.domain;

import java.util.List;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;


public class Shift {
    private String id;

    private LocalDateTime start;
    private LocalDateTime end;

    private String location;
    private String requiredSkill;
    
    private Task task;

    public Shift() {
    }


    public Shift(Task task, LocalDateTime start, LocalDateTime end, String location) {
        this(null,task, start, end, location);
    }

    public Shift(String id, Task task, LocalDateTime start, LocalDateTime end, String location) {
        this.id = id;
        this.task = task;
        this.start = start;
        this.end = end;
        this.location = location;
    }
    
    public Task getTasks() {
        return task;
    }

    public void setTasks(Task task) {
        this.task = task;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRequiredSkill() {
        return requiredSkill;
    }

    public void setRequiredSkill(String requiredSkill) {
        this.requiredSkill = requiredSkill;
    }

    public boolean isOverlappingWithDate(LocalDate date) {
        return getStart().toLocalDate().equals(date) || getEnd().toLocalDate().equals(date);
    }

    public int getOverlappingDurationInMinutes(LocalDate date) {
        LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(date, LocalTime.MAX);
        return getOverlappingDurationInMinutes(startDateTime, endDateTime, getStart(), getEnd());
    }

    private int getOverlappingDurationInMinutes(LocalDateTime firstStartDateTime, LocalDateTime firstEndDateTime,
            LocalDateTime secondStartDateTime, LocalDateTime secondEndDateTime) {
        LocalDateTime maxStartTime = firstStartDateTime.isAfter(secondStartDateTime) ? firstStartDateTime : secondStartDateTime;
        LocalDateTime minEndTime = firstEndDateTime.isBefore(secondEndDateTime) ? firstEndDateTime : secondEndDateTime;
        long minutes = maxStartTime.until(minEndTime, ChronoUnit.MINUTES);
        return minutes > 0 ? (int) minutes : 0;
    }

    @Override
    public String toString() {
        return location + " " + start + "-" + end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Shift shift)) {
            return false;
        }
        return Objects.equals(getId(), shift.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
