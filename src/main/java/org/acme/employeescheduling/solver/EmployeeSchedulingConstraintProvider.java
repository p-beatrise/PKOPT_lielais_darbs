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
        		employeeNotOnVacation(constraintFactory),
        		employeeNotSick(constraintFactory),
        		eachShiftHasEmployee(constraintFactory),
                // Soft constraints
        		employeePreferenceSoftConstraint(constraintFactory),                employeePreferenceReward(constraintFactory),                balanceWorkload(constraintFactory),
                penalizeExcessiveNightShifts(constraintFactory)
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
    
    /**
     * S1: Darbinieka maiņas preferenču ievērošana
     * Ja Worker.Preference.ShiftStartTimePreference sakrīt ar Shift.StartTime:
     *   - Bonuss: SoftScore += 1
     * Ja nesakrīt:
     *   - Sods: SoftScore -= 1
     */
    private Constraint employeePreferenceSoftConstraint(ConstraintFactory factory) {
        return factory.forEach(ShiftEmployeeAssignment.class)
                .filter(assignment -> assignment.getEmployee() != null 
                        && assignment.getEmployee().getTime() != null)
                .filter(assignment -> {
                    LocalTime preferredTime = assignment.getEmployee().getTime().getPreference();
                    LocalTime shiftStartTime = assignment.getShift().getStart().toLocalTime();
                    // Nesakrīt - sodām
                    return !preferredTime.equals(shiftStartTime);
                })
                .penalize(HardSoftBigDecimalScore.ONE_SOFT)
                .asConstraint("Preference mismatch penalty");
    }
    
    /**
     * S1 (Part 2): Bonuss par preferenču sakritību
     */
    private Constraint employeePreferenceReward(ConstraintFactory factory) {
        return factory.forEach(ShiftEmployeeAssignment.class)
                .filter(assignment -> assignment.getEmployee() != null 
                        && assignment.getEmployee().getTime() != null)
                .filter(assignment -> {
                    LocalTime preferredTime = assignment.getEmployee().getTime().getPreference();
                    LocalTime shiftStartTime = assignment.getShift().getStart().toLocalTime();
                    // Sakrīt - bonuss
                    return preferredTime.equals(shiftStartTime);
                })
                .reward(HardSoftBigDecimalScore.ONE_SOFT)
                .asConstraint("Preference match reward");
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
    
    // ========== NEW SOFT CONSTRAINTS ==========
    
    /**
     * Balance workload across employees using loadBalance.
     * Penalizes unfair distribution of shift assignments.
     */
    private Constraint balanceWorkload(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(ShiftEmployeeAssignment.class)
                .filter(assignment -> assignment.getEmployee() != null)
                .groupBy(
                        ShiftEmployeeAssignment::getEmployee,
                        ConstraintCollectors.count()
                )
                .groupBy(ConstraintCollectors.loadBalance(
                        (employee, shiftCount) -> employee,
                        (employee, shiftCount) -> shiftCount
                ))
                .penalizeBigDecimal(HardSoftBigDecimalScore.ONE_SOFT, LoadBalance::unfairness)
                .asConstraint("Balance workload");
    }
    
    /**
     * Penalize excessive night shifts per employee.
     * Night shift = shift starting after 22:00.
     * Threshold: max 8 night shifts per month.
     */
    private Constraint penalizeExcessiveNightShifts(ConstraintFactory constraintFactory) {
        int maxNightShiftsPerMonth = 8;
        
        return constraintFactory
                .forEach(ShiftEmployeeAssignment.class)
                .filter(assignment -> assignment.getEmployee() != null)
                .filter(assignment -> {
                    LocalTime startTime = assignment.getShift().getStart().toLocalTime();
                    return startTime.isAfter(LocalTime.of(22, 0));
                })
                .groupBy(
                        ShiftEmployeeAssignment::getEmployee,
                        assignment -> assignment.getShift().getStart().toLocalDate().withDayOfMonth(1),
                        ConstraintCollectors.count()
                )
                .filter((employee, month, nightShiftCount) -> nightShiftCount > maxNightShiftsPerMonth)
                .penalize(HardSoftBigDecimalScore.ONE_SOFT,
                        (employee, month, nightShiftCount) -> nightShiftCount - maxNightShiftsPerMonth)
                .asConstraint("Limit night shifts per employee");
    }
}