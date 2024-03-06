package zad35;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String msg) {
        super(msg);
    }
}

class Item implements Comparable<Item>{
    private String itemName;
    private int itemPrice;

    public Item(String itemName, int itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    @Override
    public int compareTo(Item o) {
        return Comparator.comparing(Item::getItemPrice)
                .thenComparing(Item::getItemName)
                .reversed()
                .compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("%s %d", itemName, itemPrice);
    }
}

class Student {
    private String index;
    private Map<String, Item> itemsWithPrices;

    public Student(String index) {
        this.index = index;

        itemsWithPrices = new HashMap<>();
    }

    public String getIndex() {
        return index;
    }

    public void addPayedItem(String itemDesc, int itemPrice) {
        itemsWithPrices.put(itemDesc, new Item(itemDesc, itemPrice));
    }

    public int getItemsSum() {
        return itemsWithPrices.values().stream()
                .mapToInt(Item::getItemPrice)
                .sum();
    }

    public int getBankProvision() {
        int provision = (int) Math.round((getItemsSum() * 1.14) / 100);
        return (provision >= 3 && provision <= 300) ? provision : (provision < 3) ? 3 : 300;
    }

    public int getOverallSum() {
        return getItemsSum() + getBankProvision();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        //Student: 151020 Net: 13150 Fee: 150 Total: 13300
        str.append(String.format("Student: %s Net: %d Fee: %d Total: %d\nItems:\n", getIndex(), getItemsSum(), getBankProvision(), getOverallSum()));
        AtomicInteger idx = new AtomicInteger(1);
        itemsWithPrices.values().stream()
                .sorted()
                .forEach(item -> str.append(String.format("%d. %s\n", idx.getAndIncrement(), item)));
        return str.toString();
    }
}

class OnlinePayments {
    private Map<String, Student> students;

    public OnlinePayments() {
        students = new HashMap<>();
    }

    public void readItems (InputStream is) {
        Scanner reader = new Scanner(is);

        //151020;Школарина за летен семестар 2022/2023;12300
        while (reader.hasNextLine()) {
            String [] data = reader.nextLine().split(";");
            students.computeIfAbsent(data[0], x -> new Student(data[0]));

            students.get(data[0]).addPayedItem(data[1], Integer.parseInt(data[2]));
        }
    }

    public void printStudentReport (String index, OutputStream os) throws StudentNotFoundException {
        //Student 151021 not found!
        if(!students.containsKey(index)) throw new StudentNotFoundException(String.format("Student %s not found!", index));
        PrintWriter printer = new PrintWriter(os);
        printer.print(students.get(index));
        printer.flush();
    }
}

public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> {
            try {
                onlinePayments.printStudentReport(id, System.out);
            } catch (StudentNotFoundException e) {
                System.out.println(e.getMessage());
            }
        });
    }
}