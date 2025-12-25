package org.acme.employeescheduling.domain;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class ShiftEmployeeAssignment {

    @PlanningVariable(valueRangeProviderRefs = "employeeRange")
    private Employee employee;
    
    private Shift shift;

    public ShiftEmployeeAssignment() {
    }

    public ShiftEmployeeAssignment(Shift shift) {
        this.shift = shift;
    }

    // ---------------- Getters / Setters ----------------

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "shift=" + shift +
                ", employee=" + employee +
                '}';
    }
}
