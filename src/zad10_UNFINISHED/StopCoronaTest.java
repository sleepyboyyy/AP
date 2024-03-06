package zad10_UNFINISHED;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

//UNFINISHED

class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException (String msg) {
        super(msg);
    }
}

class User {
    private String name;
    private String id;
    private List<ILocation> iLocations;
    private LocalDateTime dettectionDate;

    public User(String name, String id) {
        this.name = name;
        this.id = id;

        iLocations = new ArrayList<>();
    }

    public void addILocations(List<ILocation> locationsList) {
        this.iLocations.addAll(locationsList);
    }

    public void registerDettectionDate(LocalDateTime timestamp) {
        dettectionDate = timestamp;
    }
}

class StopCoronaApp {
    Map<String, User> users;

    public StopCoronaApp() {
        users = new HashMap<>();
    }

    public void addUser(String name, String id) throws UserAlreadyExistException {
        if (users.containsKey(id)) {
            throw new UserAlreadyExistException(String.format("User with id %s already exists", id));
        } else {
            users.put(id, new User(name, id));
        }
    }

    public void addLocations (String id, List<ILocation> iLocations) {
        users.get(id).addILocations(iLocations);
    }

    public void detectNewCase(String id, LocalDateTime timestamp) {
        users.get(id).registerDettectionDate(timestamp);
    }

    public Map<User, Integer> getDirectContacts (User u) {
        return null;
    }

    public Collection<User> getIndirectContacts (User u) {
        return null;
    }

    public void createReport () {

    }
}

interface ILocation{
    double getLongitude();

    double getLatitude();

    LocalDateTime getTimestamp();
}

public class StopCoronaTest {

    public static double timeBetweenInSeconds(ILocation location1, ILocation location2) {
        return Math.abs(Duration.between(location1.getTimestamp(), location2.getTimestamp()).getSeconds());
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StopCoronaApp stopCoronaApp = new StopCoronaApp();

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            switch (parts[0]) {
                case "REG": //register
                    String name = parts[1];
                    String id = parts[2];
                    try {
                        stopCoronaApp.addUser(name, id);
                    } catch (UserAlreadyExistException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "LOC": //add locations
                    id = parts[1];
                    List<ILocation> locations = new ArrayList<>();
                    for (int i = 2; i < parts.length; i += 3) {
                        locations.add(createLocationObject(parts[i], parts[i + 1], parts[i + 2]));
                    }
                    stopCoronaApp.addLocations(id, locations);

                    break;
                case "DET": //detect new cases
                    id = parts[1];
                    LocalDateTime timestamp = LocalDateTime.parse(parts[2]);
                    stopCoronaApp.detectNewCase(id, timestamp);

                    break;
                case "REP": //print report
                    stopCoronaApp.createReport();
                    break;
                default:
                    break;
            }
        }
    }

    private static ILocation createLocationObject(String lon, String lat, String timestamp) {
        return new ILocation() {
            @Override
            public double getLongitude() {
                return Double.parseDouble(lon);
            }

            @Override
            public double getLatitude() {
                return Double.parseDouble(lat);
            }

            @Override
            public LocalDateTime getTimestamp() {
                return LocalDateTime.parse(timestamp);
            }
        };
    }
}
