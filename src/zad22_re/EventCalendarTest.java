package zad22_re;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class WrongDateException extends Exception {
    public WrongDateException(String msg) {
        super(msg);
    }
}

class Event implements Comparable<Event>{
    private String name;
    private String location;
    private Date date;

    public Event(String name, String location, Date date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Date getDate() {
        return date;
    }

    public int returnMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate());

        return calendar.get(Calendar.MONTH);
    }

    @Override
    public String toString() {
        //19 Apr, 2012 15:30 at FINKI, Brucoshka Zabava
        DateFormat formattedDate = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        return String.format("%s at %s, %s", formattedDate.format(getDate()), getLocation(), getName());
    }

    @Override
    public int compareTo(Event o) {
        return Comparator.comparing(Event::getDate)
                .thenComparing(Event::getName)
                .compare(this, o);
    }
}

class EventCalendar {
    private int year;
    private Set<Event> events;


    public EventCalendar(int year) {
        this.year = year;

        events = new HashSet<>();
    }

    public void addEvent(String name, String location, Date date) throws WrongDateException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (year != calendar.get(Calendar.YEAR)) throw new WrongDateException("Wrong date: " + date);

        events.add(new Event(name, location, date));
    }

    public void listEvents(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Get dates
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //Get sorted list
        List<Event> sortedEvents = events.stream()
                .sorted()
                .filter(x -> {
                    Calendar eventCalendar = Calendar.getInstance();
                    eventCalendar.setTime(x.getDate());

                    return eventCalendar.get(Calendar.MONTH) == month && eventCalendar.get(Calendar.DAY_OF_MONTH) == day;
                })
                .collect(Collectors.toList());

        if (sortedEvents.isEmpty()) System.out.println("No events on this day!");
        else sortedEvents.forEach(System.out::println);
    }

    public void listByMonth() {
        IntStream.rangeClosed(1, 13).forEach(i -> {
            long monthlyEvents = events.stream()
                    .filter(event -> event.returnMonth() + 1 == i)
                    .count();

            System.out.printf("%d : %d\n", i, monthlyEvents);
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