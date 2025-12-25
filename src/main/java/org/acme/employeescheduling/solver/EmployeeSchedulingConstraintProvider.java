package org.acme.employeescheduling.solver;

import static ai.timefold.solver.core.api.score.stream.Joiners.equal;
import static ai.timefold.solver.core.api.score.stream.Joiners.lessThanOrEqual;
import static ai.timefold.solver.core.api.score.stream.Joiners.overlapping;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.function.Function;

import ai.timefold.solver.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintCollectors;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import ai.timefold.solver.core.api.score.stream.Joiners;
import ai.timefold.solver.core.api.score.stream.common.LoadBalance;

import org.acme.employeescheduling.domain.Employee;
import org.acme.employeescheduling.domain.Shift;
import org.acme.employeescheduling.domain.ShiftEmployeeAssignment;
import org.acme.employeescheduling.domain.Skill;

public class EmployeeSchedulingConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
        		// Hard constraints
        		employeeOneShiftPerDay(constraintFactory),
        		shiftEmployeeHasRequiredSkills(constraintFactory),
        		employeePreferenceSoftConstraint(constraintFactory),
        		employeeNotOnVacation(constraintFactory),
        		employeeNotSick(constraintFactory),
        		eachShiftHasEmployee(constraintFactory)
                // Soft constraints
                //undesiredDayForEmployee(constraintFactory),
                //desiredDayForEmployee(constraintFactory),
                //balanceEmployeeShiftAssignments(constraintFactory)
        };
    }
    
    private Constraint employeeOneShiftPerDay(ConstraintFactory factory) {
        return factory
                .forEach(ShiftEmployeeAssignment.class)
                .filter(a -> a.getEmployee() != null)
                .groupBy(
                        ShiftEmployeeAssignment::getEmployee,
                        a -> toLocalDate(a),
                        // count assignments per employee per day
                        ConstraintCollectors.count()
                )
                .filter((employee, date, count) -> count > 1)
                .penalize(HardSoftBigDecimalScore.ONE_HARD)
                .asConstraint("No nore than one shift");
    }

    private LocalDate toLocalDate(ShiftEmployeeAssignment assignment) {
        return assignment.getShift()
                .getStart()
                .toLocalDate();
    }
    
    private Constraint shiftEmployeeHasRequiredSkills(ConstraintFactory factory) {
        return factory.forEach(ShiftEmployeeAssignment.class)
                .filter(assignment -> assignment.getEmployee() != null &&
                        !hasRequiredSkills(assignment))
                .penalize(HardSoftBigDecimalScore.ONE_HARD)
                .asConstraint("Appropriate skills");
    }

    private boolean hasRequiredSkills(ShiftEmployeeAssignment assignment) {
        Set<Skill> employeeSkills = assignment.getEmployee().getSkills();
        Set<Skill> shiftSkills = assignment.getShift().getTasks().getSkills();

        // Check that the employee has ALL required skills for the shift
        return employeeSkills.containsAll(shiftSkills);
    }
    
    private Constraint employeePreferenceSoftConstraint(ConstraintFactory factory) {
        return factory.forEach(ShiftEmployeeAssignment.class)
                .filter(assignment -> assignment.getEmployee() != null 
                        && assignment.getEmployee().getTime() != null)
                .penalize(HardSoftBigDecimalScore.ONE_SOFT, assignment -> {
            LocalTime assignedStart = assignment.getShift().getStart().toLocalTime();
            LocalTime preferred = assignment.getEmployee().getTime().getPreference();
            return (int) Math.abs(Duration.between(assignedStart, preferred).toMinutes());
        })
        .asConstraint("Undesired time for employee");
        
    }
    
    private Constraint employeeNotOnVacation(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(ShiftEmployeeAssignment.class)
                .filter(assignment -> assignment.getEmployee() != null)
                .filter(assignment -> {
                    Employee employee = assignment.getEmployee();
                    Shift shift = assignment.getShift();

                    LocalDate shiftStart = shift.getStart().toLocalDate();
                    LocalDate shiftEnd = shift.getEnd().toLocalDate();

                    return employee.getVacation().stream().anyMatch(vacation ->
                            !shiftEnd.isBefore(vacation.getStart())
                            && !shiftStart.isAfter(vacation.getEnd())
                    );
                })
                .penalize(HardSoftBigDecimalScore.ONE_HARD)
                .asConstraint("Employee cannot work during vacation");
    }
    
    private Constraint employeeNotSick(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(ShiftEmployeeAssignment.class)
                .filter(assignment -> assignment.getEmployee() != null)
                .filter(assignment -> {
                    Employee employee = assignment.getEmployee();
                    Shift shift = assignment.getShift();

                    LocalDate shiftStart = shift.getStart().toLocalDate();
                    LocalDate shiftEnd = shift.getEnd().toLocalDate();

                    return employee.getSick().stream().anyMatch(vacation ->
                            !shiftEnd.isBefore(vacation.getStart())
                            && !shiftStart.isAfter(vacation.getEnd())
                    );
                })
                .penalize(HardSoftBigDecimalScore.ONE_HARD)
                .asConstraint("Employee cannot work when sick");
    }
    
    
    private Constraint eachShiftHasEmployee(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Shift.class)
                .join(ShiftEmployeeAssignment.class,
                        Joiners.equal(
                                Shift::getId,
                                assignment -> assignment.getShift().getId()
                        ))
                // Only count assignments that actually have an employee
                .filter((shift, assignment) -> assignment.getEmployee() != null)
                // Group by shift, count employees
                .groupBy(
                        (shift, assignment) -> shift,
                        ConstraintCollectors.countBi()
                )
                // Penalize shifts with zero employees
                .filter((shift, employeeCount) -> employeeCount == 0)
                .penalize(HardSoftBigDecimalScore.ONE_HARD)
                .asConstraint("Each shift must have at least one employee");
    }

    /*
    Constraint requiredSkill(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Shift.class)
                .filter(shift -> !shift.getEmployee().getSkills().contains(shift.getRequiredSkill()))
                .penalize(HardSoftBigDecimalScore.ONE_HARD)
                .asConstraint("Missing required skill");
    }

    Constraint noOverlappingShifts(ConstraintFactory constraintFactory) {
        return constraintFactory.forEachUniquePair(Shift.class, equal(Shift::getEmployee),
                overlapping(Shift::getStart, Shift::getEnd))
                .penalize(HardSoftBigDecimalScore.ONE_HARD,
                        EmployeeSchedulingConstraintProvider::getMinuteOverlap)
                .asConstraint("Overlapping shift");
    }

    Constraint atLeast10HoursBetweenTwoShifts(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Shift.class)
                .join(Shift.class, equal(Shift::getEmployee), lessThanOrEqual(Shift::getEnd, Shift::getStart))
                .filter((firstShift,
                        secondShift) -> Duration.between(firstShift.getEnd(), secondShift.getStart()).toHours() < 10)
                .penalize(HardSoftBigDecimalScore.ONE_HARD,
                        (firstShift, secondShift) -> {
                            int breakLength = (int) Duration.between(firstShift.getEnd(), secondShift.getStart()).toMinutes();
                            return (10 * 60) - breakLength;
                        })
                .asConstraint("At least 10 hours between 2 shifts");
    }

    Constraint oneShiftPerDay(ConstraintFactory constraintFactory) {
        return constraintFactory.forEachUniquePair(Shift.class, equal(Shift::getEmployee),
                equal(shift -> shift.getStart().toLocalDate()))
                .penalize(HardSoftBigDecimalScore.ONE_HARD)
                .asConstraint("Max one shift per day");
    }

    Constraint unavailableEmployee(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Shift.class)
                .join(Employee.class, equal(Shift::getEmployee, Function.identity()))
                .flattenLast(Employee::getUnavailableDates)
                .filter(Shift::isOverlappingWithDate)
                .penalize(HardSoftBigDecimalScore.ONE_HARD, Shift::getOverlappingDurationInMinutes)
                .asConstraint("Unavailable employee");
    }

    Constraint undesiredDayForEmployee(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Shift.class)
                .join(Employee.class, equal(Shift::getEmployee, Function.identity()))
                .flattenLast(Employee::getUndesiredDates)
                .filter(Shift::isOverlappingWithDate)
                .penalize(HardSoftBigDecimalScore.ONE_SOFT, Shift::getOverlappingDurationInMinutes)
                .asConstraint("Undesired day for employee");
    }

    Constraint desiredDayForEmployee(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Shift.class)
                .join(Employee.class, equal(Shift::getEmployee, Function.identity()))
                .flattenLast(Employee::getDesiredDates)
                .filter(Shift::isOverlappingWithDate)
                .reward(HardSoftBigDecimalScore.ONE_SOFT, Shift::getOverlappingDurationInMinutes)
                .asConstraint("Desired day for employee");
    }

    Constraint balanceEmployeeShiftAssignments(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Shift.class)
                .groupBy(Shift::getEmployee, ConstraintCollectors.count())
                .complement(Employee.class, e -> 0) // Include all employees which are not assigned to any shift.c
                .groupBy(ConstraintCollectors.loadBalance((employee, shiftCount) -> employee,
                        (employee, shiftCount) -> shiftCount))
                .penalizeBigDecimal(HardSoftBigDecimalScore.ONE_SOFT, LoadBalance::unfairness)
                .asConstraint("Balance employee shift assignments");
    }*/

}
