package zad6;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Employee implements Comparable<Employee>{
    private final String typeEmployee;
    private final String id;
    private final String level;
    private final double rate;

    public Employee(String typeEmployee, String id, String level, double rate) {
        this.typeEmployee = typeEmployee;
        this.id = id;
        this.level = level;
        this.rate = rate;
    }

    public double getRate() {
        return rate;
    }

    public String getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }

    public double getSalary() {
        return 0;
    }

    @Override
    public int compareTo(Employee o) {
        return Comparator.comparing(Employee::getSalary)
                .thenComparing(Employee::getLevel)
                .compare(this, o);
    }
}

class HourlyEmployee extends Employee {
    private final double hours;

    public HourlyEmployee(String typeEmployee, String id, String level, double rate, double hours) {
        super(typeEmployee, id, level, rate);
        this.hours = hours;
    }

    public double getRegularHours() {
        return hours >= 40 ? 40 : hours;
    }

    public double getOvertimeHours() {
        return hours >= 40 ? hours-40 : 0;
    }

    @Override
    public double getSalary() {
        double normalRate = (hours >= 40) ? 40 * getRate() : hours * getRate();
        double bonusRate = (hours >= 40) ? (hours - 40) * (getRate() * 1.5) : 0;

        return normalRate + bonusRate;
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f",
                getId(), getLevel(), getSalary(), getRegularHours(), getOvertimeHours());
    }
}

class FreelanceEmployee extends Employee {
    private final List<Integer> tickets;

    public FreelanceEmployee(String typeEmployee, String id, String level, double rate, List<Integer> tickets) {
        super(typeEmployee, id, level, rate);
        this.tickets = tickets;
    }

    public int getTicketSum() {
        return tickets.stream().mapToInt(i -> i).sum();
    }

    @Override
    public double getSalary() {
        return getTicketSum() * getRate();
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d",
                getId(), getLevel(), getSalary(), tickets.size(), getTicketSum());
    }
}

class PayrollSystem {
    List<Employee> employees;
    private final Map<String, Double> hourlyRateByLevel;
    private final Map<String, Double> ticketRateByLevel;

    public PayrollSystem(Map<String,Double> hourlyRateByLevel, Map<String,Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;

        employees = new ArrayList<>();
    }

    public void readEmployees (InputStream is) {
        Scanner reader = new Scanner(is);

        while (reader.hasNextLine()) {
            String [] data = reader.nextLine().split(";");
            String type = data[0];
            if (type.equals("F")) {
                List<Integer> ticketPoints = new ArrayList<>();
                IntStream.range(3, data.length).forEach(i -> ticketPoints.add(Integer.parseInt(data[i])));
                Employee employee = new FreelanceEmployee(data[0], data[1], data[2], ticketRateByLevel.get(data[2]), ticketPoints);

                employees.add(employee);
            } else {
                Employee employee = new HourlyEmployee(type, data[1], data[2], hourlyRateByLevel.get(data[2]), Double.parseDouble(data[3]));
                employees.add(employee);
            }
        }
    }

    public Map<String, Set<Employee>> printEmployeesByLevels (OutputStream os, Set<String> levels) {
        Map<String, Set<Employee>> result = new LinkedHashMap<>();

        levels.stream().sorted().forEach(level -> employees.stream().filter(x -> x.getLevel().equals(level))
                .forEach(employee -> {
                    result.computeIfAbsent(level, x -> new HashSet<>());
                    result.put(level, employees.stream()
                            .filter(x -> x.getLevel().equals(level))
                            .sorted()
                            .collect(Collectors.toCollection(LinkedHashSet::new)));
                }));

        return result;
    }
}

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i=5;i<=10;i++) {
            levels.add("level"+i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: "+ level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });


    }
}