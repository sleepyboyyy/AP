package zad1;

import java.util.*;

class Candidate implements Comparable<Candidate> {
    private String name;
    private String code;
    private String city;
    private int age;

    public Candidate(String name, String code, String city, int age) {
        this.name = name;
        this.code = code;
        this.city = city;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getCode() {
        return code;
    }

    public int getAge() {
        return age;
    }

    @Override
    public int compareTo(Candidate o) {
        return Comparator.comparing(Candidate::getName)
                .thenComparing(Candidate::getAge)
                .thenComparing(Candidate::getCode)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }
}

class Audition {
    private Map<String, List<Candidate>> candidates;

    public Audition() {
        candidates = new HashMap<>();
    }

    public void addParticpant(String city, String code, String name, int age) {
        candidates.computeIfAbsent(city, x -> new ArrayList<>());
        if(doesCodeExist(city, code)) return;

        candidates.get(city).add(new Candidate(name, code, city, age));
    }

    public void listByCity(String city) {
        candidates.get(city).stream()
                .sorted()
                .forEach(System.out::println);
    }

    public boolean doesCodeExist(String city, String code) {
        return candidates.get(city).stream()
                .anyMatch(x -> x.getCode().equals(code));
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}
