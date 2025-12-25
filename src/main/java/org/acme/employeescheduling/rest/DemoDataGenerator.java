package org.acme.employeescheduling.rest;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.enterprise.context.ApplicationScoped;

import org.acme.employeescheduling.domain.Employee;
import org.acme.employeescheduling.domain.EmployeeSchedule;
import org.acme.employeescheduling.domain.Shift;
import org.acme.employeescheduling.domain.SickLeave;
import org.acme.employeescheduling.domain.Skill;
import org.acme.employeescheduling.domain.Task;
import org.acme.employeescheduling.domain.Preference;
import org.acme.employeescheduling.domain.Vacation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class DemoDataGenerator {
    public enum DemoData {
        SMALL,
        LARGE
    }

    public record CountDistribution(int count, double weight) {}

    public record DemoDataParameters(List<String> locations,
                                     List<String> requiredSkills,
                                     List<String> optionalSkills,
                                     int daysInSchedule,
                                     int employeeCount,
                                     List<CountDistribution> optionalSkillDistribution,
                                     List<CountDistribution> shiftCountDistribution,
                                     List<CountDistribution> availabilityCountDistribution,
                                     int randomSeed) {}

    private static final Duration SHIFT_LENGTH = Duration.ofHours(8);
    private static final LocalTime DAY_SHIFT_START_TIME = LocalTime.of(8, 0);
    private static final LocalTime AFTERNOON_SHIFT_START_TIME = LocalTime.of(16, 0);
    private static final LocalTime NIGHT_SHIFT_START_TIME = LocalTime.of(0, 0);

    static final LocalTime[][] SHIFT_START_TIMES_COMBOS = {
            { AFTERNOON_SHIFT_START_TIME },
            { AFTERNOON_SHIFT_START_TIME, NIGHT_SHIFT_START_TIME },
            { DAY_SHIFT_START_TIME, AFTERNOON_SHIFT_START_TIME, NIGHT_SHIFT_START_TIME },
    };

    Map<String, List<LocalTime>> locationToShiftStartTimeListMap = new HashMap<>();

    public EmployeeSchedule generateDemoData(DemoData demoData) {
        return generateDemoData();
    };
    		
    public EmployeeSchedule generateDemoData() {
    	return generateSmallDataSet();
    }
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeScheduleResource.class);
    
    private EmployeeSchedule generateSmallDataSet() {
        EmployeeSchedule employeeSchedule = new EmployeeSchedule();
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));

        // Create employees with predefined skills and availabilities
        List<Employee> employees = createSmallDataSetEmployees(startDate);
        employeeSchedule.setEmployees(employees);

        
        // Create shifts for 1 day
        List<Shift> shifts = createSmallDataSetShifts(startDate);
        LOGGER.info("Demo shifts created: {}", shifts.size());
        employeeSchedule.setShifts(shifts);

        return employeeSchedule;
    }
    
    private List<Employee> createSmallDataSetEmployees(LocalDate startDate) {
        List<Employee> employees = new ArrayList<>();

        employees.add(new Employee(
                "Jānis 1",
                Set.of(Skill.SKILL1, Skill.SKILL2),
                new Preference(LocalTime.of(8, 0)),
                Set.of(
                        new Vacation(
                            startDate.plusDays(0),
                            startDate.plusDays(1)
                        ),
                        new Vacation(
                            startDate.plusDays(2),
                            startDate.plusDays(3)
                        )
                    ),
                Set.of()
        ));

        employees.add(new Employee(
                "Jānis 2",
                Set.of(Skill.SKILL2),
                new Preference(LocalTime.of(8, 0)),
                Set.of(
                        new Vacation(
                            startDate.plusDays(0),
                            startDate.plusDays(1)
                        ),
                        new Vacation(
                            startDate.plusDays(2),
                            startDate.plusDays(3)
                        )
                    ),
                Set.of()
        ));

        employees.add(new Employee(
                "Jānis 3",
                Set.of(Skill.SKILL3, Skill.SKILL2),
                new Preference(LocalTime.of(8, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
                "Jānis 4",
                Set.of(Skill.SKILL1, Skill.SKILL3),
                new Preference(LocalTime.of(8, 0)),
                Set.of(),
                Set.of(
                        new SickLeave(
                            startDate.plusDays(0),
                            startDate.plusDays(1)
                        ),
                        new SickLeave(
                            startDate.plusDays(2),
                            startDate.plusDays(3)
                        )
                    )
        ));

        employees.add(new Employee(
        		"Jānis 5",
                Set.of(Skill.SKILL2, Skill.SKILL3),
                new Preference(LocalTime.of(8, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Jānis 6",
                Set.of(Skill.SKILL3, Skill.SKILL2),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Jānis 7",
                Set.of(Skill.SKILL1),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Jānis 8",
                Set.of(Skill.SKILL2),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Jānis 9",
                Set.of(Skill.SKILL3),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Jānis 10",
                Set.of(Skill.SKILL1),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Jānis 11",
                Set.of(Skill.SKILL2),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Jānis 12",
                Set.of(Skill.SKILL3),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Jānis 13",
                Set.of(Skill.SKILL1),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Jānis 14",
                Set.of(Skill.SKILL2),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Jānis 15",
                Set.of(Skill.SKILL3),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));
        
        employees.add(new Employee(
        		"Jānis 16",
        		Set.of(Skill.SKILL3, Skill.SKILL2),
        		new Preference(LocalTime.of(16, 0)),
        		Set.of(),
                Set.of()
        ));

        
        employees.add(new Employee(
        		"Jānis 17",
        		Set.of(Skill.SKILL3, Skill.SKILL2,Skill.SKILL1),
        		new Preference(LocalTime.of(0, 0)),
        		Set.of(),
                Set.of()
        ));

        
        employees.add(new Employee(
        		"Jānis 18",
                Set.of(Skill.SKILL3, Skill.SKILL2,Skill.SKILL1),
                new Preference(LocalTime.of(0, 0)),
                Set.of(),
                Set.of()
        ));


        return employees;
    }
    
    private List<Task> createSmallDataSetTasks() {
        List<Task> tasks = new ArrayList<>();

       
        tasks.add(new Task(
                "T1",
                8,
                Set.of(Skill.SKILL3)
        ));

        tasks.add(new Task(
                "T2",
                8,
                Set.of(Skill.SKILL1, Skill.SKILL2)
        ));

        tasks.add(new Task(
                "T3",
                8,
                Set.of(Skill.SKILL2)
        ));

        tasks.add(new Task(
                "T4",
                8,
                Set.of(Skill.SKILL3)
        ));
        
        tasks.add(new Task(
                "T5",
                8,
                Set.of(Skill.SKILL1)
        ));

        tasks.add(new Task(
                "T6",
                8,
                Set.of(Skill.SKILL2)
        ));

        tasks.add(new Task(
                "T7",
                8,
                Set.of(Skill.SKILL3)
        ));

        tasks.add(new Task(
                "T8",
                8,
                Set.of(Skill.SKILL1)
        ));
        
        tasks.add(new Task(
                "T9",
                8,
                Set.of(Skill.SKILL2)
        ));

        tasks.add(new Task(
                "T10",
                8,
                Set.of(Skill.SKILL3)
        ));

        tasks.add(new Task(
                "T11",
                8,
                Set.of(Skill.SKILL2)
        ));

        tasks.add(new Task(
                "T12",
                16,
                Set.of(Skill.SKILL1)
        ));

        

        return tasks;
    }
    
    private List<Shift> createSmallDataSetShifts(LocalDate startDate) {
        List<Shift> shifts = new LinkedList<>();
        AtomicInteger shiftId = new AtomicInteger(0);

        // Locations and their shift patterns
        String[] locations = {"Pirmā lete", "Otrā lete", "Trešā lete"};
        LocalTime[][] shiftPatterns = {
                { AFTERNOON_SHIFT_START_TIME},
                { AFTERNOON_SHIFT_START_TIME, NIGHT_SHIFT_START_TIME},
                { DAY_SHIFT_START_TIME, AFTERNOON_SHIFT_START_TIME, NIGHT_SHIFT_START_TIME}
        };

        // Generate shifts for 1 day
        for (int day = 0; day < 3; day++) {
            LocalDate date = startDate.plusDays(day);

            for (int locIdx = 0; locIdx < locations.length; locIdx++) {
                String location = locations[locIdx];
                LocalTime[] timesForLocation = shiftPatterns[locIdx];

                for (LocalTime startTime : timesForLocation) {
                    LocalDateTime shiftStart = date.atTime(startTime);
                    LocalDateTime shiftEnd = shiftStart.plus(SHIFT_LENGTH);

                    // Create 1-2 shifts per timeslot with varying required skills
                    int shiftsCount = (day + locIdx) % 2 == 0 ? 2 : 1;
                    
                    for (int i = 0; i < shiftsCount; i++) {
                    	List<Task> taskList = createSmallDataSetTasks();
                        Task randomTasks = pickRandom(taskList);
                        Shift shift = new Shift(randomTasks,shiftStart, shiftEnd, location);
                        shift.setId(String.valueOf(shiftId.getAndIncrement()));
                        shifts.add(shift);
                    }
                }
            }
        }

        return shifts;
    }

    public static <T> T pickRandom(List<T> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            throw new IllegalArgumentException("Task list must not be empty");
        }

        return tasks.get(ThreadLocalRandom.current().nextInt(tasks.size()));
    }

}
