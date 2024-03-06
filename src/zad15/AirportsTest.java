package zad15;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//RETRY

class Flight {
    private final String from;
    private final String to;
    private LocalTime time;
    private final int duration;
    private String flightType;

    public Flight(String from, String to, int time, int duration, String flightType) {
        this.from = from;
        this.duration = duration;
        this.to = to;
        this.flightType = flightType;

        this.time = LocalTime.of(0, 0, 0);
        this.time = this.time.plusMinutes(time);
    }

    public static Comparator<Flight> COMPARATOR = Comparator.comparing(Flight::getTo)
            .thenComparing(Flight::getTime).thenComparing(Flight::getDuration);

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    public String getFlightType() {
        return flightType;
    }

    public String formatTime(LocalTime departureTime, int flightDuration) {
        StringBuilder str = new StringBuilder();

        int totalTime = departureTime.getHour() * 60 + time.getMinute() + flightDuration;
        if (totalTime > 1440) {
            str.append(String.format("+%dd ", totalTime/1440));
        }
        int hours = flightDuration/60;
        str.append(String.format("%dh%02dm", hours, flightDuration - (hours*60)));
        return str.toString();
    }

    @Override
    public String toString() {
        return String.format("%s-%s %s-%s %s", getFrom(), getTo(), time, time.plusMinutes(duration), formatTime(time, duration));
    }
}

class Airport {
    private String name;
    private String country;
    private String code;
    private int passengers;
    private Set<Flight> flights;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;

        flights = new TreeSet<>(Flight.COMPARATOR);
    }

    public void addFlight(String from, String to, int time, int duration, String type) {
        flights.add(new Flight(from, to, time, duration, type));
    }

    public Set<Flight> getFlights() {
        return flights;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)\n%s\n%d", name, code, country, passengers);
    }
}

class Airports {
    Map<String, Airport> airports;

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
        Airport a = airports.get(code);
        System.out.println(a);

        List<Flight> flightsFrom = airports.get(code).getFlights().stream()
                .filter(flight -> flight.getFlightType().equalsIgnoreCase("DEPARTURE"))
                .collect(Collectors.toList());

        IntStream.range(0, flightsFrom.size()).forEach(count -> System.out.println((count + 1) + ". " + flightsFrom.get(count)));
    }

    public void showDirectFlightsFromTo(String from, String to) {
        Airport a = airports.get(from);
        List<Flight> flightsFromTo = a.getFlights().stream()
                .filter(flight -> flight.getTo().equalsIgnoreCase(to))
                .collect(Collectors.toList());

        if(flightsFromTo.isEmpty()) {
            System.out.println("No flights from " + from + " to " + to);
            return;
        }

        flightsFromTo.forEach(System.out::println);
    }

    public void showDirectFlightsTo(String to) {
        airports.get(to).getFlights().stream()
                .filter(flight -> flight.getFlightType().equalsIgnoreCase("ARRIVAL"))
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

