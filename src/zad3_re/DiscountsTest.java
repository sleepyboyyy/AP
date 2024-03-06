package zad3_re;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Discounts
 */

class Item implements Comparable<Item>{
    private final int normalPrice;
    private final int discountedPrice;

    public Item(int normalPrice, int discountedPrice) {
        this.normalPrice = normalPrice;
        this.discountedPrice = discountedPrice;
    }

    public int getNormalPrice() {
        return normalPrice;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    public int getDiscountPercentage() {
        return (int) Math.floor(Math.abs(100 - (discountedPrice * 100.0) / normalPrice));
    }

    public int getAbsoluteDiscount() {
        return normalPrice - discountedPrice;
    }

    @Override
    public int compareTo(Item o) {
        return Comparator.comparing(Item::getDiscountPercentage)
                .thenComparing(Item::getAbsoluteDiscount)
                .reversed()
                .compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("%d%% %d/%d", getDiscountPercentage(), getDiscountedPrice(), getNormalPrice());
    }
}

class Store {
    private final String name;
    private final Set<Item> items;

    public Store(String name, Set<Item> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public double getDiscountAvg() {
        return items.stream().mapToDouble(Item::getDiscountPercentage).average().orElse(0.0);
    }

    public int getAbsoluteDiscountSum() {
        return items.stream().mapToInt(Item::getAbsoluteDiscount).sum();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(String.format("%s\n", getName()));
        str.append(String.format("Average discount: %.1f%%\nTotal discount: %d\n", getDiscountAvg(), getAbsoluteDiscountSum()));
        String listedItems = items.stream().sorted().map(Item::toString).collect(Collectors.joining("\n"));
        str.append(listedItems);

        return str.toString();
    }
}

class Discounts {
    Set<Store> stores;

    public Discounts() {
        stores = new HashSet<>();
    }

    public int readStores(InputStream inputStream) {
        //Desigual 5967:9115  5519:9378  3978:5563  7319:13092  8558:10541
        Scanner reader = new Scanner(inputStream);


        while (reader.hasNextLine()) {
            String [] data = reader.nextLine().split(" ");
            Set<Item> items = new HashSet<>();
            for(int i = 1; i < data.length; i++) {
                if (data[i].equals("")) continue;

                String [] priceData = data[i].split(":");
                int discPrice = Integer.parseInt(priceData[0]);
                int regPrice = Integer.parseInt(priceData[1]);

                items.add(new Item(regPrice, discPrice));
            }

            stores.add(new Store(data[0], items));
        }

        return stores.size();
    }

    public List<Store> byAverageDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::getDiscountAvg)
                        .thenComparing(Store::getName).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Store> byTotalDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::getAbsoluteDiscountSum)
                        .thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toList());
    }

}

public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

// Vashiot kod ovde