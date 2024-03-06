package zad15_re;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class Flight {
    private final String from;
    private final String to;
    private LocalTime time;
    private final int duration;
    private final String type;

    public Flight(String from, String to, int time, int duration, String type) {
        this.from = from;
        this.to = to;
        this.duration = duration;
        this.type = type;

        this.time = LocalTime.of(0, 0, 0);
        this.time = this.time.plusMinutes(time);
    }

    public static Comparator<Flight> COMPARE = Comparator.comparing(Flight::getTo)
            .thenComparing(Flight::getTime).thenComparing(Flight::getDuration);

    public String getTo() {
        return to;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }

    public String getFrom() {
        return from;
    }

    @Override
    public String toString() {
        return String.format("%s-%s %s-%s %s", from, to, time,time.plusMinutes(duration), durationFormat(time, duration));
    }

    public String durationFormat(LocalTime time, int duration) {
        StringBuilder str = new StringBuilder();
        int overallTime = time.getHour() * 60 + time.getMinute() + duration;
        if (overallTime >= 1440) str.append(String.format("+%dd ", overallTime/1440));

        int durationHours = duration / 60;
        str.append(String.format("%dh%02dm", durationHours, duration - (durationHours*60)));

        return str.toString();
    }
}

class Airport {
    private final String name;
    private final String country;
    private final String code;
    private final int passengers;
    private final Set<Flight> flights;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;

        flights = new TreeSet<>(Flight.COMPARE);
    }

    public void addFlight(String from, String to, int time, int duration, String type) {
        flights.add(new Flight(from, to, time, duration, type));
    }

    public Set<Flight> getFlights() {
        return flights;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)\n%s\n%s", name, code, country, passengers);
    }
}

class Airports {
    private final Map<String, Airport> airports;

    public Airports() {
        airports = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        airports.put(code, new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration) {
        airports.get(from).addFlight(from, to, time, duration, "DEPARTURE");
        airports.get(to).addFlight(from, to, time, duration, "ARRIVAL");
    }

    public void showFlightsFromAirport(String code) {
        System.out.println(airports.get(code));
        AtomicInteger idx = new AtomicInteger(1);
        airports.get(code).getFlights().stream().filter(flight -> flight.getFrom().equals(code)).forEach(f -> System.out.printf("%d. %s\n", idx.getAndIncrement(), f));
    }

    public void showDirectFlightsFromTo(String from, String to) {
        List<Flight> flightsFromTo = airports.get(from).getFlights().stream()
                .filter(flight -> flight.getType().equals("DEPARTURE") && flight.getTo().equals(to))
                .collect(Collectors.toList());

        if (flightsFromTo.isEmpty()) System.out.printf("No flights from %s to %s\n", from, to);
        else flightsFromTo.forEach(System.out::println);
    }

    public void showDirectFlightsTo(String to) {
        airports.get(to).getFlights().stream()
                .filter(flight -> flight.getType().equals("ARRIVAL"))
                .forEach(System.out::println);
    }
}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde

