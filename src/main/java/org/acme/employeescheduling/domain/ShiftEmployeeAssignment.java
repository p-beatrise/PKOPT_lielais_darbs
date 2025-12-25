package org.acme.employeescheduling.domain;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class ShiftEmployeeAssignment {

    /** Planning variable: which employee is assigned */
    @PlanningVariable(valueRangeProviderRefs = "employeeRange")
    private Employee employee;
    
    /** Problem fact: which shift this assignment belongs to */
    private Shift shift;


    // ---------------- Constructors ----------------

    /** Required by Timefold / Jackson */
    public ShiftEmployeeAssignment() {
    }

    /** ✅ This constructor fixes your error */
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

    // ---------------- Equality ----------------
    // Optional but recommended if referenced in constraints

    @Override
    public String toString() {
        return "Assignment{" +
                "shift=" + shift +
                ", employee=" + employee +
                '}';
    }
}
