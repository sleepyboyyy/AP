package zad22;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//RETRY

class WrongDateException extends Exception {
    public WrongDateException(Date date) {
        super("Wrong date: " + date);
    }
}

class Event implements Comparable<Event>{
    private final String name;
    private final String location;
    private final Date date;

    public Event(String name, String location, Date date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public int returnDateMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(getDate());

        return c.get(Calendar.MONTH);
    }

    public int returnDateDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(getDate());

        return c.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public int compareTo(Event o) {
        return Comparator.comparing(Event::getDate)
                .thenComparing(Event::getName)
                .compare(this, o);
    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy HH:mm");
        return String.format("%s at %s, %s", dateFormat.format(date), location, name);
    }
}

class EventCalendar {
    private final int year;
    private final Set<Event> events;

    public EventCalendar(int year) {
        this.year = year;
        events = new HashSet<>();
    }

    public void addEvent(String name, String location, Date date) throws WrongDateException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (calendar.get(Calendar.YEAR) != this.year) {
            throw new WrongDateException(date);
        }

        events.add(new Event(name, location, date));
    }

    public void listEvents(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        List<Event> listedEvenets = events.stream()
                .filter(event -> event.returnDateMonth() == calendar.get(Calendar.MONTH) && event.returnDateDay() == calendar.get(Calendar.DAY_OF_MONTH))
                .sorted()
                .collect(Collectors.toList());

        if (!listedEvenets.isEmpty()) listedEvenets.forEach(System.out::println);
        else System.out.println("No events on this day!");
    }

    public void listByMonth() {
        IntStream.rangeClosed(1, 12).forEach(month -> {
            long eventCount = events.stream()
                    .filter(event -> event.returnDateMonth() + 1 == month)
                    .count();

            System.out.println(month + " : " + eventCount);
        });
    }
}

public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}

// vashiot kod ovde