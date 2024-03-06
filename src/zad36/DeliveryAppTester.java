package zad36;

import java.util.*;

// RETRY

class DeliveryPerson {
    private final String id;
    private final String name;
    private Location currentLocation;
    private final List<Double> earnings;

    public DeliveryPerson(String id, String name, Location currentLocation) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;

        this.earnings = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public List<Double> getEarnings() {
        return earnings;
    }

    public double getEarningsSum() {
        return earnings.stream().mapToDouble(i -> i).sum();
    }

    public double avgEarnings() {
        return earnings.stream().mapToDouble(i -> i).average().orElse(0.0);
    }

    public void addEarnings(int distance, Location location) {
        this.currentLocation = location;
        earnings.add((double) 90 + 10 * (distance / 10 ));
    }

    public int compareDeliveryPeopleDistance(DeliveryPerson other, Location restaurantLoc) {
        int currUserDist = this.currentLocation.distance(restaurantLoc);
        int otherUserDist = other.getCurrentLocation().distance(restaurantLoc);
        if (currUserDist == otherUserDist) {
            return Integer.compare(this.getEarnings().size(), other.getEarnings().size());
        } else return currUserDist - otherUserDist;
    }

    public void updateLocation(Location location) {

    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f", getId(), getName(), getEarnings().size(), getEarningsSum(), avgEarnings());
    }
}

class Restaurant {
    private final String id;
    private final String name;
    private final Location location;
    private final List<Float> sales;

    public Restaurant(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;

        this.sales = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void updateSales(float cost) {
        sales.add(cost);
    }

    public double sumSales() {
        return sales.stream().mapToDouble(i -> i).sum();
    }

    public double avgSales() {
        return sales.stream().mapToDouble(i -> i).average().orElse(0.0);
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f", getId(), getName(), sales.size(), sumSales(), avgSales());
    }
}

class User {
    private final String id;
    private final String name;
    private final Map<String, Location> addresses;
    private final List<Float> moneySpent;

    public User(String id, String name) {
        this.id = id;
        this.name = name;

        this.moneySpent = new ArrayList<>();
        this.addresses = new HashMap<>();
    }

    public void addAddress(String addressName, Location location) {
        addresses.put(addressName, location);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, Location> getAddresses() {
        return addresses;
    }

    public List<Float> getMoneySpent() {
        return moneySpent;
    }

    public double getSumSpent() {
        return moneySpent.stream().mapToDouble(i -> i).sum();
    }

    public double avgSpent() {
        return moneySpent.stream().mapToDouble(i -> i).average().orElse(0.0);
    }

    public void update(float cost) {
        moneySpent.add(cost);
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f", getId(), getName(), getMoneySpent().size(), getSumSpent(), avgSpent());
    }
}

class DeliveryApp {
    private String name;
    private final Map<String, DeliveryPerson> deliveryPeople;
    private final Map<String, Restaurant> restaurants;
    private final Map<String, User> users;

    public DeliveryApp (String name) {
        this.name = name;

        this.deliveryPeople = new HashMap<>();
        this.restaurants = new HashMap<>();
        this.users = new HashMap<>();
    }

    public void registerDeliveryPerson (String id, String name, Location currentLocation) {
        deliveryPeople.put(id, new DeliveryPerson(id, name, currentLocation));
    }

    public void addRestaurant (String id, String name, Location location) {
        restaurants.put(id, new Restaurant(id, name, location));
    }

    public void addUser (String id, String name) {
        users.put(id, new User(id, name));
    }

    public void addAddress (String id, String addressName, Location location) {
        users.get(id).addAddress(addressName, location);
    }

    public void orderFood(String userId, String userAddressName, String restaurantId, float cost) {
        User orderingUser = users.get(userId);
        Location orderingUserLocation = orderingUser.getAddresses().get(userAddressName);
        Restaurant targetRestaurant = restaurants.get(restaurantId);

        DeliveryPerson selectedDeliveryGuy = deliveryPeople.values().stream()
                .min((l, r) -> l.compareDeliveryPeopleDistance(r, targetRestaurant.getLocation())).get();

        orderingUser.update(cost);
        targetRestaurant.updateSales(cost);

        int distance = selectedDeliveryGuy.getCurrentLocation().distance(targetRestaurant.getLocation());
        selectedDeliveryGuy.addEarnings(distance, orderingUserLocation);
    }

    public void printUsers() {
        users.values().stream().sorted(Comparator.comparing(User::getSumSpent).thenComparing(User::getId).reversed()).forEach(System.out::println);
    }

    public void printRestaurants() {
        restaurants.values().stream().sorted(Comparator.comparing(Restaurant::avgSales).thenComparing(Restaurant::getId).reversed()).forEach(System.out::println);
    }

    public void printDeliveryPeople() {
        deliveryPeople.values().stream().sorted(Comparator.comparing(DeliveryPerson::getEarningsSum).thenComparing(DeliveryPerson::getId).reversed()).forEach(System.out::println);
    }
}

interface Location {
    int getX();

    int getY();

    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}

class LocationCreator {
    public static Location create(int x, int y) {

        return new Location() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }
        };
    }
}

public class DeliveryAppTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }

        }
    }
}
