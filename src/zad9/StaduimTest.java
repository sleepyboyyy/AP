package zad9;

import java.util.*;
import java.util.stream.IntStream;

//RETRY BUT LOW PRIORITY

class SeatTakenException extends Exception {
    public SeatTakenException(String msg) {
        super(msg);
    }
}

class SeatNotAllowedException extends Exception {
    public SeatNotAllowedException(String msg) {
        super(msg);
    }
}

class Sector implements Comparable<Sector>{
    private String sectorName;
    private int availableSeats;
    private Map<Integer, Integer> seatsAvailability;

    public Sector(String sectorName, int availableSeats) {
        this.sectorName = sectorName;
        this.availableSeats = availableSeats;

        seatsAvailability = new HashMap<>();
    }

    public String getSectorName() {
        return sectorName;
    }

    public void buySeat(int seatNumber, int buyType) {
        seatsAvailability.put(seatNumber, buyType);
    }

    public int nonTakenSeats() {
        return availableSeats - seatsAvailability.size();
    }

    public Map<Integer, Integer> getSeatsAvailability() {
        return seatsAvailability;
    }

    @Override
    public int compareTo(Sector o) {
        return Comparator.comparing(Sector::nonTakenSeats)
                .thenComparing(Sector::getSectorName).reversed()
                .compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%", getSectorName(), nonTakenSeats(), availableSeats, (seatsAvailability.size()) * 100.0/availableSeats);
        //return String.format("%s        %d/%d  %.f%%", getSectorName(), seatsAvailability.size(), availableSeats, (seatsAvailability.size())*100.0/availableSeats);
    }
}

class Stadium {
    Map<String, Sector> sectors;

    private String name;
    public Stadium(String name) {
        this.name = name;

        sectors = new HashMap<>();
    }

    public void createSectors(String[] sectorNames, int[] sizes) {
        IntStream.range(0, sectorNames.length).forEach(i -> sectors.computeIfAbsent(sectorNames[i], x -> new Sector(sectorNames[i], sizes[i])));
    }

    public void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        int typeValue = -1;
        if (type == 1) typeValue = 2;
        else if (type == 2) typeValue = 1;

        if (sectors.get(sectorName).getSeatsAvailability().containsKey(seat)) throw new SeatTakenException("SeatTakenException");
        if (sectors.get(sectorName).getSeatsAvailability().containsValue(typeValue)) throw new SeatNotAllowedException("SeatNotAllowedException");

        sectors.get(sectorName).buySeat(seat, type);
    }

    public void showSectors() {
        sectors.values().stream().sorted().forEach(System.out::println);
    }
}

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}
