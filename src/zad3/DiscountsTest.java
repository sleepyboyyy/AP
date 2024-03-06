package zad3;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

//Да се имплементира класа Discounts за обработка на информации за цени и цени на попуст на одредени производи во неколку продавници (објекти од класа Store). Потребно е да се имплементираат следните методи:
//
//public int readStores(InputStream inputStream) - метод за вчитување на податоците за продавниците и цените на производите. Податоците за секоја продавница се во посебен ред во формат [ime] [cena_na_popust1:cena1] [cena_na_popust2:cena2] ... (погледнете пример). Методот враќа колку продавници се вчитани.
//public List<Store> byAverageDiscount() - метод кој враќа листа од 3-те продавници со најголем просечен попуст (просечна вредност на попустот за секој производ од таа продавница). Попустот (намалувањето на цената) е изразен во цел број (проценти) и треба да се пресмета од намалената цена и оригиналната цена. Ако две продавници имаат ист попуст, се подредуваат според името лексикографски.
//public List<Store> byTotalDiscount() - метод кој враќа листа од 3-те продавници со намал вкупен попуст (сума на апсолутен попуст од сите производи). Апсолутен попуст е разликата од цената и цената на попуст. Ако две продавници имаат ист попуст, се подредуваат според името лексикографски.
//Дополнително за класата Store да се имплементира стринг репрезентација, односно методот:
//
//public String toString() кој ќе враќа репрезентација во следниот формат:
//
//[Store_name]
//Average discount: [заокружена вредност со едно децимално место]%
//Total discount: [вкупен апсолутен попуст]
//[процент во две места]% [цена на попуст]/[цена]
//...
//при што продуктите се подредени според процентот на попуст (ако е ист, според апсолутниот попуст) во опаѓачки редослед.

//RETRY

class Item {
    private final int discountedPrice;
    private final int originalPrice;

    public Item(int discountedPrice, int originalPrice) {
        this.discountedPrice = discountedPrice;
        this.originalPrice = originalPrice;
    }

    public int getPercent() {
        return (int)Math.floor(100.0 - (100.0 / originalPrice * discountedPrice));
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    @Override
    public String toString() {
        return String.format("%d%% %d/%d", getPercent(), discountedPrice, originalPrice);
    }
}
class Store {
    private String name;
    private List<Item> items;

    public Store(String name) {
        this.name = name;
        items = new ArrayList<>();
    }

    public void addItem(String data) {
        String [] dataArr = data.split(":");
        items.add(new Item(Integer.parseInt(dataArr[0]), Integer.parseInt(dataArr[1])));
    }

    public double averageDiscount() {
        return items.stream()
                .mapToInt(Item::getPercent)
                .average()
                .orElse(0);
    }

    public int absSum() {
        return items.stream()
                .mapToInt(item -> Math.abs(item.getOriginalPrice() - item.getDiscountedPrice()))
                .sum();
    }

    @Override
    public String toString() {
        String printedItems = items.stream()
                .sorted(Comparator.comparing(Item::getPercent).thenComparing(Item::getOriginalPrice).reversed())
                .map(Item::toString)
                .collect(Collectors.joining("\n"));
        return String.format("%s\nAverage Discount: %.1f%%\nTotal Discount: %d\n%s", name, averageDiscount(), absSum(), printedItems);
    }
}

class Discounts {
    private List<Store> stores;

    public Discounts() {stores = new ArrayList<>();
    }
    public int readStores(InputStream inputStream) {
        Scanner input = new Scanner(inputStream);

        while (input.hasNextLine()) {

            String [] data = input.nextLine().split(" ");
            Store s = new Store(data[0]);
            for (int i = 1; i < data.length; i++) {
                if(data[i].isEmpty()) continue;
                s.addItem(data[i]);
            }

            stores.add(s);
        }

        return stores.size();
    }

    public List<Store> byAverageDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::averageDiscount).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Store> byTotalDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::absSum))
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