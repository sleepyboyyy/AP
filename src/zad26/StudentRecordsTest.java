package zad26;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Student implements Comparable<Student>{
    private String id;
    private String major;
    private List<Integer> grades;

    public Student(String id, String major, List<Integer> grades) {
        this.id = id;
        this.major = major;
        this.grades = grades;
    }

    public String getId() {
        return id;
    }

    public String getMajor() {
        return major;
    }

    public List<Integer> getGrades() {
        return grades;
    }

    public int get6Grades() {
        return (int) grades.stream().filter(g -> g == 6).mapToInt(g -> g).count();
    }

    public int get7Grades() {
        return (int) grades.stream().filter(g -> g == 7).mapToInt(x -> x).count();
    }

    public int get8Grades() {
        return (int) grades.stream().filter(g -> g == 8).mapToInt(x -> x).count();
    }

    public int get9Grades() {
        return (int) grades.stream().filter(g -> g == 9).mapToInt(x -> x).count();
    }

    public int get10Grades() {
        return (int) grades.stream().filter(g -> g == 10).mapToInt(x -> x).count();
    }

    public double gradesAvg() {
        return grades.stream().mapToDouble(x -> x).average().orElse(0.0);
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", getId(), gradesAvg());
    }

    @Override
    public int compareTo(Student o) {
        return Comparator.comparing(Student::gradesAvg).reversed()
                .thenComparing(Student::getId)
                .compare(this, o);
    }
}

class StudentRecords {
    private Map<String, List<Student>> students;

    public StudentRecords() {
        students = new HashMap<>();
    }

    public int readRecords(InputStream inputStream) {
        Scanner reader = new Scanner(inputStream);

        while (reader.hasNextLine()) {
            String [] data = reader.nextLine().split(" ");
            students.computeIfAbsent(data[1], x -> new ArrayList<>());

            List<Integer> studentGrades = new ArrayList<>();
            IntStream.range(2, data.length).forEach(i -> studentGrades.add(Integer.parseInt(data[i])));

            students.get(data[1]).add(new Student(data[0], data[1], studentGrades));
        }

        return students.values().stream().mapToInt(List::size).sum();
    }

    public void writeTable(OutputStream outputStream) {
        PrintWriter printer = new PrintWriter(outputStream);
        students.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String major = entry.getKey();
                    List<Student> studentList = entry.getValue();

                    printer.println(major);
                    studentList.stream()
                            .sorted()
                            .forEach(printer::println);
                });

        printer.flush();
    }

    void writeDistribution(OutputStream outputStream) {
        PrintWriter printer = new PrintWriter(outputStream);

        List<String> sortedKeysDesc = students.keySet().stream()
                .sorted(Comparator.comparingInt(key ->
                        students.get(key).stream()
                                .mapToInt(Student::get10Grades)
                                .sum()
                ).reversed())
                .collect(Collectors.toList());

        sortedKeysDesc.forEach(key -> {
            printer.println(key);
            int counting6 = students.get(key).stream().mapToInt(Student::get6Grades).sum();
            int counting7 = students.get(key).stream().mapToInt(Student::get7Grades).sum();
            int counting8 = students.get(key).stream().mapToInt(Student::get8Grades).sum();
            int counting9 = students.get(key).stream().mapToInt(Student::get9Grades).sum();
            int counting10 = students.get(key).stream().mapToInt(Student::get10Grades).sum();

            printer.println(String.format("%2d | %s(%d)", 6, "*".repeat((int) Math.ceil((double) counting6 / 10)), counting6));
            printer.println(String.format("%2d | %s(%d)", 7, "*".repeat((int) Math.ceil((double) counting7 / 10)), counting7));
            printer.println(String.format("%2d | %s(%d)", 8, "*".repeat((int) Math.ceil((double) counting8 / 10)), counting8));
            printer.println(String.format("%2d | %s(%d)", 9, "*".repeat((int) Math.ceil((double) counting9 / 10)), counting9));
            printer.println(String.format("%2d | %s(%d)", 10, "*".repeat((int) Math.ceil((double) counting10 / 10)), counting10));
        });

        printer.flush();
    }
}




public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}

