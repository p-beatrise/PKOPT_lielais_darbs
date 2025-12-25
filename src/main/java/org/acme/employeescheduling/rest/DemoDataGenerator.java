package org.acme.employeescheduling.rest;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.enterprise.context.ApplicationScoped;

import org.acme.employeescheduling.domain.Employee;
import org.acme.employeescheduling.domain.EmployeeSchedule;
import org.acme.employeescheduling.domain.Shift;
import org.acme.employeescheduling.domain.Skill;
import org.acme.employeescheduling.domain.Task;

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

    private static final String[] FIRST_NAMES = { "Amy", "Beth", "Carl", "Dan", "Elsa", "Flo", "Gus", "Hugo", "Ivy", "Jay" };
    private static final String[] LAST_NAMES = { "Cole", "Fox", "Green", "Jones", "King", "Li", "Poe", "Rye", "Smith", "Watt" };
    private static final Duration SHIFT_LENGTH = Duration.ofHours(8);
    private static final LocalTime MORNING_SHIFT_START_TIME = LocalTime.of(6, 0);
    private static final LocalTime DAY_SHIFT_START_TIME = LocalTime.of(9, 0);
    private static final LocalTime AFTERNOON_SHIFT_START_TIME = LocalTime.of(14, 0);
    private static final LocalTime NIGHT_SHIFT_START_TIME = LocalTime.of(22, 0);

    static final LocalTime[][] SHIFT_START_TIMES_COMBOS = {
            { MORNING_SHIFT_START_TIME, AFTERNOON_SHIFT_START_TIME },
            { MORNING_SHIFT_START_TIME, AFTERNOON_SHIFT_START_TIME, NIGHT_SHIFT_START_TIME },
            { MORNING_SHIFT_START_TIME, DAY_SHIFT_START_TIME, AFTERNOON_SHIFT_START_TIME, NIGHT_SHIFT_START_TIME },
    };

    Map<String, List<LocalTime>> locationToShiftStartTimeListMap = new HashMap<>();

    public EmployeeSchedule generateDemoData(DemoData demoData) {
        return generateDemoData();
    }
    ;
    public EmployeeSchedule generateDemoData() {
    	return generateSmallDataSet();
    }
    
    private EmployeeSchedule generateSmallDataSet() {
        EmployeeSchedule employeeSchedule = new EmployeeSchedule();
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));

        // Create employees with predefined skills and availabilities
        List<Employee> employees = createSmallDataSetEmployees(startDate);
        employeeSchedule.setEmployees(employees);

        // Create shifts for 14 days
        List<Shift> shifts = createSmallDataSetShifts(startDate);
        employeeSchedule.setShifts(shifts);

        return employeeSchedule;
    }
    
    private List<Employee> createSmallDataSetEmployees(LocalDate startDate) {
        List<Employee> employees = new ArrayList<>();

        // Employee 1: Amy Cole - Doctor with Anaesthetics
        employees.add(new Employee(
                "Amy Cole",
                Set.of(Skill.SKILL1),
                new LinkedHashSet<>(List.of(startDate.plusDays(3), startDate.plusDays(7))), // unavailable
                new LinkedHashSet<>(List.of(startDate.plusDays(10))), // undesired
                new LinkedHashSet<>(List.of(startDate.plusDays(1), startDate.plusDays(5))) // desired
        ));

        // Employee 2: Beth Fox - Nurse with Cardiology
        employees.add(new Employee(
                "Beth Fox",
                Set.of(Skill.SKILL2),
                new LinkedHashSet<>(List.of(startDate.plusDays(2))), // unavailable
                new LinkedHashSet<>(List.of(startDate.plusDays(8), startDate.plusDays(12))), // undesired
                new LinkedHashSet<>(List.of(startDate.plusDays(4), startDate.plusDays(9))) // desired
        ));

        // Employee 3: Carl Green - Doctor only
        employees.add(new Employee(
                "Carl Green",
                Set.of(Skill.SKILL3),
                new LinkedHashSet<>(List.of(startDate.plusDays(6))), // unavailable
                new LinkedHashSet<>(List.of(startDate.plusDays(11))), // undesired
                new LinkedHashSet<>(List.of(startDate.plusDays(2), startDate.plusDays(13))) // desired
        ));

        // Employee 4: Dan Jones - Nurse with Anaesthetics
        employees.add(new Employee(
                "Dan Jones",
                Set.of(Skill.SKILL1),
                new LinkedHashSet<>(), // unavailable
                new LinkedHashSet<>(List.of(startDate.plusDays(5), startDate.plusDays(9))), // undesired
                new LinkedHashSet<>(List.of(startDate.plusDays(7))) // desired
        ));

        // Employee 5: Elsa King - Doctor with Cardiology
        employees.add(new Employee(
                "Elsa King",
                Set.of(Skill.SKILL2),
                new LinkedHashSet<>(List.of(startDate.plusDays(4), startDate.plusDays(8))), // unavailable
                new LinkedHashSet<>(List.of(startDate.plusDays(1))), // undesired
                new LinkedHashSet<>(List.of(startDate.plusDays(10), startDate.plusDays(12))) // desired
        ));

        // Employee 6: Flo Li - Nurse only
        employees.add(new Employee(
                "Flo Li",
                Set.of(Skill.SKILL3),
                new LinkedHashSet<>(List.of(startDate.plusDays(9))), // unavailable
                new LinkedHashSet<>(List.of(startDate.plusDays(6), startDate.plusDays(13))), // undesired
                new LinkedHashSet<>(List.of(startDate.plusDays(3))) // desired
        ));

        // Employee 7: Gus Poe - Doctor with Anaesthetics and Cardiology
        employees.add(new Employee(
                "Gus Poe",
                Set.of(Skill.SKILL1),
                new LinkedHashSet<>(), // unavailable
                new LinkedHashSet<>(List.of(startDate.plusDays(2), startDate.plusDays(7))), // undesired
                new LinkedHashSet<>(List.of(startDate.plusDays(6), startDate.plusDays(11))) // desired
        ));

        // Employee 8: Hugo Rye - Nurse with Cardiology
        employees.add(new Employee(
                "Hugo Rye",
                Set.of(Skill.SKILL2),
                new LinkedHashSet<>(List.of(startDate.plusDays(5))), // unavailable
                new LinkedHashSet<>(List.of(startDate.plusDays(10))), // undesired
                new LinkedHashSet<>(List.of(startDate.plusDays(0), startDate.plusDays(8))) // desired
        ));

        // Employee 9: Ivy Smith - Doctor only
        employees.add(new Employee(
                "Ivy Smith",
                Set.of(Skill.SKILL3),
                new LinkedHashSet<>(List.of(startDate.plusDays(1), startDate.plusDays(11))), // unavailable
                new LinkedHashSet<>(List.of(startDate.plusDays(4))), // undesired
                new LinkedHashSet<>(List.of(startDate.plusDays(9))) // desired
        ));

        // Employee 10: Jay Watt - Nurse with Anaesthetics
        employees.add(new Employee(
                "Jay Watt",
                Set.of(Skill.SKILL1),
                new LinkedHashSet<>(), // unavailable
                new LinkedHashSet<>(List.of(startDate.plusDays(3), startDate.plusDays(12))), // undesired
                new LinkedHashSet<>(List.of(startDate.plusDays(5), startDate.plusDays(13))) // desired
        ));

        // Employee 11: Ann Cole - Doctor with Cardiology
        employees.add(new Employee(
                "Ann Cole",
                Set.of(Skill.SKILL2),
                new LinkedHashSet<>(List.of(startDate.plusDays(10))), // unavailable
                new LinkedHashSet<>(List.of(startDate.plusDays(7))), // undesired
                new LinkedHashSet<>(List.of(startDate.plusDays(2))) // desired
        ));

        // Employee 12: Ben Fox - Nurse only
        employees.add(new Employee(
                "Ben Fox",
                Set.of(Skill.SKILL3),
                new LinkedHashSet<>(List.of(startDate.plusDays(12))), // unavailable
                new LinkedHashSet<>(List.of(startDate.plusDays(9))), // undesired
                new LinkedHashSet<>(List.of(startDate.plusDays(4), startDate.plusDays(11))) // desired
        ));

        // Employee 13: Cara Green - Doctor with Anaesthetics
        employees.add(new Employee(
                "Cara Green",
                Set.of(Skill.SKILL1),
                new LinkedHashSet<>(), // unavailable
                new LinkedHashSet<>(List.of(startDate.plusDays(0), startDate.plusDays(13))), // undesired
                new LinkedHashSet<>(List.of(startDate.plusDays(6), startDate.plusDays(8))) // desired
        ));

        // Employee 14: Dave Jones - Nurse with Cardiology
        employees.add(new Employee(
                "Dave Jones",
                Set.of(Skill.SKILL2),
                new LinkedHashSet<>(List.of(startDate.plusDays(13))), // unavailable
                new LinkedHashSet<>(List.of(startDate.plusDays(11))), // undesired
                new LinkedHashSet<>(List.of(startDate.plusDays(1), startDate.plusDays(7))) // desired
        ));

        // Employee 15: Emma King - Doctor only
        employees.add(new Employee(
                "Emma King",
                Set.of(Skill.SKILL3),
                new LinkedHashSet<>(List.of(startDate.plusDays(8))), // unavailable
                new LinkedHashSet<>(List.of(startDate.plusDays(5))), // undesired
                new LinkedHashSet<>(List.of(startDate.plusDays(10))) // desired
        ));

        return employees;
    }
    
    private List<Task> createSmallDataSetTasks() {
        List<Task> tasks = new ArrayList<>();

       
        tasks.add(new Task(
                "T1",
                12,
                Set.of(Skill.SKILL3)
        ));

        tasks.add(new Task(
                "T2",
                11,
                Set.of(Skill.SKILL1)
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
                16,
                Set.of(Skill.SKILL3)
        ));

        tasks.add(new Task(
                "T8",
                16,
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
                16,
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
        String[] locations = {"Ambulatory care", "Critical care", "Pediatric care"};
        LocalTime[][] shiftPatterns = {
                {MORNING_SHIFT_START_TIME, AFTERNOON_SHIFT_START_TIME},
                {MORNING_SHIFT_START_TIME, AFTERNOON_SHIFT_START_TIME, NIGHT_SHIFT_START_TIME},
                {MORNING_SHIFT_START_TIME, DAY_SHIFT_START_TIME, AFTERNOON_SHIFT_START_TIME, NIGHT_SHIFT_START_TIME}
        };

        // Generate shifts for 14 days
        for (int day = 0; day < 14; day++) {
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
                        String requiredSkill = getRequiredSkillForShift(day, locIdx, i);
                        Shift shift = new Shift(randomTasks,shiftStart, shiftEnd, location);
                        shift.setId(String.valueOf(shiftId.getAndIncrement()));
                        shifts.add(shift);
                    }
                }
            }
        }

        return shifts;
    }
    
    private String getRequiredSkillForShift(int day, int locationIndex, int shiftIndex) {
        // Alternate between required skills in a predictable pattern
        String[][] skillOptions = {
                {"Doctor", "Nurse"},
                {"Doctor", "Anaesthetics"},
                {"Nurse", "Cardiology"}
        };
        
        int patternIndex = (day + locationIndex + shiftIndex) % skillOptions.length;
        int skillIndex = (day + shiftIndex) % skillOptions[patternIndex].length;
        
        return skillOptions[patternIndex][skillIndex];
    }

    public EmployeeSchedule generateDemoData(DemoDataParameters parameters) {
        EmployeeSchedule employeeSchedule = new EmployeeSchedule();

        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));

        Random random = new Random(parameters.randomSeed);

        int shiftTemplateIndex = 0;
        for (String location : parameters.locations) {
            locationToShiftStartTimeListMap.put(location, List.of(SHIFT_START_TIMES_COMBOS[shiftTemplateIndex]));
            shiftTemplateIndex = (shiftTemplateIndex + 1) % SHIFT_START_TIMES_COMBOS.length;
        }

        List<String> namePermutations = joinAllCombinations(FIRST_NAMES, LAST_NAMES);
        Collections.shuffle(namePermutations, random);

        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < parameters.employeeCount; i++) {
            Set<String> skills = pickSubset(parameters.optionalSkills, random, parameters.optionalSkillDistribution);
            skills.add(pickRandom(parameters.requiredSkills, random));
           // Employee employee = new Employee(namePermutations.get(i), skills, new LinkedHashSet<>(), new LinkedHashSet<>(), new LinkedHashSet<>());
           // employees.add(employee);
        }
        employeeSchedule.setEmployees(employees);

        List<Shift> shifts = new LinkedList<>();
        for (int i = 0; i < parameters.daysInSchedule; i++) {
            Set<Employee> employeesWithAvailabilitiesOnDay = pickSubset(employees, random,
                    parameters.availabilityCountDistribution);
            LocalDate date = startDate.plusDays(i);
            for (Employee employee : employeesWithAvailabilitiesOnDay) {
                switch (random.nextInt(3)) {
                    case 0 -> employee.getUnavailableDates().add(date);
                    case 1 -> employee.getUndesiredDates().add(date);
                    case 2 -> employee.getDesiredDates().add(date);
                }
            }
            shifts.addAll(generateShiftsForDay(parameters, date, random));
        }
        AtomicInteger countShift = new AtomicInteger();
        shifts.forEach(s -> s.setId(Integer.toString(countShift.getAndIncrement())));
        employeeSchedule.setShifts(shifts);

        return employeeSchedule;
    }

    private List<Shift> generateShiftsForDay(DemoDataParameters parameters, LocalDate date, Random random) {
        List<Shift> shifts = new LinkedList<>();
        for (String location : parameters.locations) {
            List<LocalTime> shiftStartTimes = locationToShiftStartTimeListMap.get(location);
            for (LocalTime shiftStartTime : shiftStartTimes) {
                LocalDateTime shiftStartDateTime = date.atTime(shiftStartTime);
                LocalDateTime shiftEndDateTime = shiftStartDateTime.plus(SHIFT_LENGTH);
                shifts.addAll(generateShiftForTimeslot(parameters, shiftStartDateTime, shiftEndDateTime, location, random));
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

    private List<Shift> generateShiftForTimeslot(DemoDataParameters parameters,
            LocalDateTime timeslotStart, LocalDateTime timeslotEnd, String location,
            Random random) {
        var shiftCount = pickCount(random, parameters.shiftCountDistribution);

        List<Shift> shifts = new LinkedList<>();
        for (int i = 0; i < shiftCount; i++) {
            String requiredSkill;
            if (random.nextBoolean()) {
                requiredSkill = pickRandom(parameters.requiredSkills, random);
            } else {
                requiredSkill = pickRandom(parameters.optionalSkills, random);
            }
            List<Task> taskList = createSmallDataSetTasks();
            Task randomTasks = pickRandom(taskList);
            shifts.add(new Shift(randomTasks,timeslotStart, timeslotEnd, location));
        }
        return shifts;
    }

    private <T> T pickRandom(List<T> source, Random random) {
        return source.get(random.nextInt(source.size()));
    }

    private int pickCount(Random random, List<CountDistribution> countDistribution) {
        double probabilitySum = 0;
        for (var possibility : countDistribution) {
            probabilitySum += possibility.weight;
        }
        var choice = random.nextDouble(probabilitySum);
        int numOfItems = 0;
        while (choice >= countDistribution.get(numOfItems).weight) {
            choice -= countDistribution.get(numOfItems).weight;
            numOfItems++;
        }
        return countDistribution.get(numOfItems).count;
    }

    private <T> Set<T> pickSubset(List<T> sourceSet, Random random, List<CountDistribution> countDistribution) {
        var count = pickCount(random, countDistribution);
        List<T> items = new ArrayList<>(sourceSet);
        Collections.shuffle(items, random);
        return new HashSet<>(items.subList(0, count));
    }

    private List<String> joinAllCombinations(String[]... partArrays) {
        int size = 1;
        for (String[] partArray : partArrays) {
            size *= partArray.length;
        }
        List<String> out = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            StringBuilder item = new StringBuilder();
            int sizePerIncrement = 1;
            for (String[] partArray : partArrays) {
                item.append(' ');
                item.append(partArray[(i / sizePerIncrement) % partArray.length]);
                sizePerIncrement *= partArray.length;
            }
            item.delete(0, 1);
            out.add(item.toString());
        }
        return out;
    }
}
