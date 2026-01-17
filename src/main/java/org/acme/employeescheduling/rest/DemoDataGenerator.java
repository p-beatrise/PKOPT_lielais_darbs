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
import org.acme.employeescheduling.domain.ShiftEmployeeAssignment;
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
        MEDIUM,
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
        return switch (demoData) {
            case SMALL -> generateSmallDataSet();
            case MEDIUM -> generateConfigurableDataSet(20, 15, 3);
            case LARGE -> generateConfigurableDataSet(50, 30, 7);
        };
    }
    		
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
                "Jānis Bērziņš",
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
                "Anna Kalniņa",
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
                "Māris Ozols",
                Set.of(Skill.SKILL3, Skill.SKILL2),
                new Preference(LocalTime.of(8, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
                "Līga Liepiņa",
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
        		"Andris Kļaviņš",
                Set.of(Skill.SKILL2, Skill.SKILL3),
                new Preference(LocalTime.of(8, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Kristīne Ozola",
                Set.of(Skill.SKILL3, Skill.SKILL2),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Pēteris Vītols",
                Set.of(Skill.SKILL1),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Inese Jansone",
                Set.of(Skill.SKILL2),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Edgars Strazds",
                Set.of(Skill.SKILL3),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Ilze Rozīte",
                Set.of(Skill.SKILL1),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Roberts Kalējs",
                Set.of(Skill.SKILL2),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Dace Siliņa",
                Set.of(Skill.SKILL3),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Aigars Grīnbergs",
                Set.of(Skill.SKILL1),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Sanita Kārkliņa",
                Set.of(Skill.SKILL2),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));

        employees.add(new Employee(
        		"Valdis Balodis",
                Set.of(Skill.SKILL3),
                new Preference(LocalTime.of(16, 0)),
                Set.of(),
                Set.of()
        ));
        
        employees.add(new Employee(
        		"Elīna Meža",
        		Set.of(Skill.SKILL3, Skill.SKILL2),
        		new Preference(LocalTime.of(16, 0)),
        		Set.of(),
                Set.of()
        ));

        
        employees.add(new Employee(
        		"Guntis Liepa",
        		Set.of(Skill.SKILL3, Skill.SKILL2,Skill.SKILL1),
        		new Preference(LocalTime.of(0, 0)),
        		Set.of(),
                Set.of()
        ));

        
        employees.add(new Employee(
        		"Zane Kronberga",
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
    
    /**
     * Generate a configurable dataset for benchmarking.
     * @param employeeCount Number of employees to generate
     * @param shiftCount Number of shifts per day
     * @param days Number of days in the schedule
     */
    public EmployeeSchedule generateConfigurableDataSet(int employeeCount, int shiftCount, int days) {
        EmployeeSchedule employeeSchedule = new EmployeeSchedule();
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        
        // Create employees with random skills
        List<Employee> employees = generateEmployees(employeeCount, startDate);
        employeeSchedule.setEmployees(employees);
        
        // Create shifts
        List<Shift> shifts = generateShifts(shiftCount, days, startDate);
        LOGGER.info("Generated configurable dataset: {} employees, {} shifts over {} days", 
                    employeeCount, shifts.size(), days);
        employeeSchedule.setShifts(shifts);
        
        // Create assignments (one per shift, initially unassigned)
        List<ShiftEmployeeAssignment> assignments = new ArrayList<>();
        for (Shift shift : shifts) {
            ShiftEmployeeAssignment assignment = new ShiftEmployeeAssignment();
            assignment.setShift(shift);
            assignment.setEmployee(null); // Solver will assign
            assignments.add(assignment);
        }
        employeeSchedule.setAssignmentList(assignments);
        
        return employeeSchedule;
    }
    
    private List<Employee> generateEmployees(int count, LocalDate startDate) {
        List<Employee> employees = new ArrayList<>();
        Skill[] allSkills = Skill.values();
        LocalTime[] preferredTimes = {
            LocalTime.of(8, 0),
            LocalTime.of(16, 0),
            LocalTime.of(0, 0)
        };
        
        // Diverse Latvian names
        String[] employeeNames = {
            "Jānis Bērziņš", "Anna Kalniņa", "Māris Ozols", "Līga Liepiņa", "Andris Kļaviņš",
            "Kristīne Ozola", "Pēteris Vītols", "Inese Jansone", "Edgars Strazds", "Ilze Rozīte",
            "Roberts Kalējs", "Dace Siliņa", "Aigars Grīnbergs", "Sanita Kārkliņa", "Valdis Balodis",
            "Elīna Meža", "Guntis Liepa", "Zane Kronberga", "Artūrs Priede", "Ieva Ķirsone",
            "Kaspars Osis", "Laura Lapa", "Uldis Upītis", "Līva Lūse", "Ivars Ērglis",
            "Rita Rīga", "Normunds Neimanis", "Vija Vētra", "Eduards Ēce", "Agnese Alksne",
            "Gatis Gailītis", "Jolanta Jaunsaule", "Raimonds Rūsis", "Sandra Skujiņa", "Ģirts Grauds",
            "Māra Mārtiņa", "Viktors Veits", "Laima Lazdiņa", "Rūdolfs Rasa", "Signe Spāre",
            "Juris Jansons", "Aija Avotiņa", "Kārlis Krūmiņš", "Baiba Bērza", "Indulis Indriksons",
            "Velta Vilde", "Staņislavs Strauss", "Māra Mākoņa", "Igors Inka", "Anita Āboliņa"
        };
        
        for (int i = 0; i < count; i++) {
            // Random 1-3 skills per employee
            Set<Skill> skills = new java.util.HashSet<>();
            int skillCount = ThreadLocalRandom.current().nextInt(1, 4);
            for (int j = 0; j < skillCount; j++) {
                skills.add(allSkills[ThreadLocalRandom.current().nextInt(allSkills.length)]);
            }
            
            // Random time preference
            LocalTime preferredTime = preferredTimes[ThreadLocalRandom.current().nextInt(preferredTimes.length)];
            
            // 20% chance of vacation
            Set<Vacation> vacations = new java.util.HashSet<>();
            if (ThreadLocalRandom.current().nextDouble() < 0.2) {
                LocalDate vacStart = startDate.plusDays(ThreadLocalRandom.current().nextInt(7));
                vacations.add(new Vacation(vacStart, vacStart.plusDays(1)));
            }
            
            // 10% chance of sick leave
            Set<SickLeave> sickLeaves = new java.util.HashSet<>();
            if (ThreadLocalRandom.current().nextDouble() < 0.1) {
                LocalDate sickStart = startDate.plusDays(ThreadLocalRandom.current().nextInt(7));
                sickLeaves.add(new SickLeave(sickStart, sickStart.plusDays(1)));
            }
            
            String employeeName = i < employeeNames.length ? employeeNames[i] : "Darbinieks " + (i + 1);
            
            employees.add(new Employee(
                employeeName,
                skills,
                new Preference(preferredTime),
                vacations,
                sickLeaves
            ));
        }
        
        return employees;
    }
    
    private List<Shift> generateShifts(int shiftsPerDay, int days, LocalDate startDate) {
        List<Shift> shifts = new ArrayList<>();
        AtomicInteger shiftId = new AtomicInteger(0);
        
        String[] locations = {"Location A", "Location B", "Location C", "Location D"};
        LocalTime[] shiftTimes = {
            DAY_SHIFT_START_TIME,
            AFTERNOON_SHIFT_START_TIME,
            NIGHT_SHIFT_START_TIME
        };
        
        for (int day = 0; day < days; day++) {
            LocalDate date = startDate.plusDays(day);
            
            for (int i = 0; i < shiftsPerDay; i++) {
                String location = locations[ThreadLocalRandom.current().nextInt(locations.length)];
                LocalTime startTime = shiftTimes[ThreadLocalRandom.current().nextInt(shiftTimes.length)];
                
                LocalDateTime shiftStart = date.atTime(startTime);
                LocalDateTime shiftEnd = shiftStart.plus(SHIFT_LENGTH);
                
                // Random task
                List<Task> taskList = createSmallDataSetTasks();
                Task task = pickRandom(taskList);
                
                Shift shift = new Shift(task, shiftStart, shiftEnd, location);
                shift.setId(String.valueOf(shiftId.getAndIncrement()));
                shifts.add(shift);
            }
        }
        
        return shifts;
    }
}