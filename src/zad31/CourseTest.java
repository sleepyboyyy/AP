package zad31;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//RETRY BECAUSE OF LAST METHOD USING WEIRD CLASS (LEARN IT)

class Student implements Comparable<Student>{
    private String index;
    private String name;
    private int pointsFirstExams;
    private int pointsSecondExams;
    private int pointsLaboratoryExercises;

    public Student(String index, String name) {
        this.index = index;
        this.name = name;

        this.pointsFirstExams = 0;
        this.pointsSecondExams = 0;
        this.pointsLaboratoryExercises = 0;
    }

    public String getIndex() {
        return index;
    }

    public void updatePoints(String activity, int points) {
        switch(activity) {
            case "midterm1":
                pointsFirstExams += points;
                break;
            case "midterm2":
                pointsSecondExams += points;
                break;
            case "labs":
                pointsLaboratoryExercises += points;
                break;
        }
    }

    public double getTotalPoints() {
        return (pointsFirstExams * 0.45) + (pointsSecondExams * 0.45) + pointsLaboratoryExercises;
    }

    public int getStudentGrade() {
        int finalGrade = (int) Math.ceil(getTotalPoints() / 10);
        return (finalGrade >= 6) ? finalGrade : 5;
    }

    @Override
    public int compareTo(Student o) {
        return Comparator.comparing(Student::getTotalPoints).reversed()
                .compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points %.2f Grade: %d",
                index, name, pointsFirstExams, pointsSecondExams, pointsLaboratoryExercises, getTotalPoints(), getStudentGrade());
    }
}

class AdvancedProgrammingCourse {
    Map<String, Student> students;

    public AdvancedProgrammingCourse() {
        students = new HashMap<>();
    }

    public void addStudent (Student s) {
        students.put(s.getIndex(), s);
    }

    public void updateStudent (String idNumber, String activity, int points) {
        students.get(idNumber).updatePoints(activity, points);
    }

    public List<Student> getFirstNStudents (int n) {
        return students.values().stream()
                .sorted()
                .limit(n)
                .collect(Collectors.toList());
    }

    public Map<Integer,Integer> getGradeDistribution() {
        Map<Integer, Integer> result = new HashMap<>();

        IntStream.range(5, 11).forEach(i -> result.put(i, 0));
        students.values().forEach(student -> result.computeIfPresent(student.getStudentGrade(), (k, v) -> v+1));

        return result;
    }

    public void printStatistics() {
        List<Student> passedStudents = students.values().stream()
                .filter(x -> x.getStudentGrade() >= 6)
                .collect(Collectors.toList());

        DoubleSummaryStatistics ds = passedStudents.stream().mapToDouble(Student::getTotalPoints).summaryStatistics();

        System.out.println(String.format("Count: %d Min: %.2f Average: %.2f Max: %.2f\n", passedStudents.size(), ds.getMin(), ds.getAverage(), ds.getMax()));
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