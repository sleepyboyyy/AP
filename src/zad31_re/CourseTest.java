package zad31_re;

//package mk.ukim.finki.midterm;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Student implements Comparable<Student>{
    private String index;
    private String name;
    private int midterm1Points;
    private int midterm2Points;
    private int labPoints;

    public Student(String index, String name) {
        this.index = index;
        this.name = name;

        this.midterm1Points = 0;
        this.midterm2Points = 0;
        this.labPoints = 0;
    }

    public String getIndex() {
        return index;
    }

    public double getSummaryPoints() {
        //midterm1 * 0.45 + midterm2 * 0.45 + lab
        return (midterm1Points * 0.45) + (midterm2Points * 0.45) + labPoints;
    }

    public void updateStudent(String activity, int points) {
        switch (activity) {
            case "midterm1" : midterm1Points += points; break;
            case "midterm2" : midterm2Points += points; break;
            case "labs" : labPoints += points; break;
        }
    }

    public int getStudentGrade() {
        return getSummaryPoints() < 51 ? 5 : (int) Math.ceil(getSummaryPoints() / 10);
    }

    @Override
    public String toString() {
        //ID: 151020 Name: Stefan First midterm: 78 Second midterm 80 Labs: 8 Summary points: 79.10 Grade: 8
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points: %.2f Grade: %d",
                                index, name, midterm1Points, midterm2Points, labPoints, getSummaryPoints(), getStudentGrade());
    }

    @Override
    public int compareTo(Student o) {
        return Comparator.comparing(Student::getSummaryPoints)
                .reversed()
                .compare(this, o);
    }
}

class AdvancedProgrammingCourse {
    private Map<String, Student> students;

    public AdvancedProgrammingCourse() {
        students = new HashMap<>();
    }

    public void addStudent (Student s) {
        students.put(s.getIndex(), s);
    }

    public void updateStudent (String idNumber, String activity, int points) {
        students.get(idNumber).updateStudent(activity, points);
    }

    public List<Student> getFirstNStudents (int n) {
        return students.values().stream()
                .sorted()
                .limit(n)
                .collect(Collectors.toList());
    }

    public Map<Integer,Integer> getGradeDistribution() { // STUDY THIS
        return IntStream.rangeClosed(5, 10)
                .boxed()
                .collect(Collectors.toMap(
                        grade -> grade,
                        grade -> (int) students.values().stream()
                                .filter(student -> student.getStudentGrade() == grade)
                                .count()
                ));
    }

    public void printStatistics() {
        DoubleSummaryStatistics summaryStats = students.values().stream().filter(student -> student.getStudentGrade() != 5).mapToDouble(Student::getSummaryPoints).summaryStatistics();
        //Count: 1 Min: 79.10 Average: 79.10 Max: 79.10
        System.out.printf("Count: %d Min: %.2f Average: %.2f Max: %.2f\n", summaryStats.getCount(), summaryStats.getMin(), summaryStats.getAverage(), summaryStats.getMax());
    }
}


public class CourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                advancedProgrammingCourse.updateStudent(idNumber, activity, points);
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}
