package zad23_re;

import java.util.*;
import java.util.stream.Collectors;

class Student {
    private String index;
    private List<Integer> labPoints;

    public Student(String index, List<Integer> labPoints) {
        this.index = index;
        this.labPoints = labPoints;
    }

    public String getIndex() {
        return index;
    }

    public double getSummaryLabsPoints() {
        return labPoints.stream().mapToInt(x->x).sum() / 10.0;
    }

    public String isSignature() {
        return labPoints.size() < 8 ? "NO" : "YES";
    }

    public int getYearOfStudies() {
        return 20 - Integer.parseInt(getIndex().substring(0, 2));
    }

    @Override
    public String toString() {
        return String.format("%s %s %.2f", index, isSignature(), getSummaryLabsPoints());
    }
}

class LabExercises {
    private Set<Student> students;

    public LabExercises() {
        students = new HashSet<>();
    }

    public void addStudent (Student student) {
        students.add(student);
    }

    public void printByAveragePoints (boolean ascending, int n) {
        Comparator<Student> comp = Comparator.comparing(Student::getSummaryLabsPoints).thenComparing(Student::getIndex);
        students.stream()
                .sorted(ascending ? comp : comp.reversed())
                .limit(n)
                .forEach(System.out::println);
    }

    public List<Student> failedStudents () {
        return students.stream()
                .filter(student -> student.isSignature().equals("NO"))
                .sorted(Comparator.comparing(Student::getIndex).thenComparing(Student::getSummaryLabsPoints))
                .collect(Collectors.toList());
    }

    public Map<Integer,Double> getStatisticsByYear() {
        return students.stream()
                .filter(student -> student.isSignature().equals("YES"))
                .collect(Collectors.groupingBy(Student::getYearOfStudies, Collectors.averagingDouble(Student::getSummaryLabsPoints)));
    }
}

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}