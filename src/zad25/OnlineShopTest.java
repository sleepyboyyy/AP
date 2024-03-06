package zad25;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(message);
    }
}


class Product {
    private String category;
    private String id;
    private String name;
    private LocalDateTime createdAt;
    private double price;
    private double productRevenue;
    private int quantitySold;

    public Product(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;

        this.productRevenue = 0;
        this.quantitySold = 0;
    }

    public static Comparator<Product> productComparator(COMPARATOR_TYPE type) {
        switch(type) {
            case OLDEST_FIRST:
                return Comparator.comparing(Product::getCreatedAt);
            case NEWEST_FIRST:
                return Comparator.comparing(Product::getCreatedAt).reversed();
            case LOWEST_PRICE_FIRST:
                return Comparator.comparing(Product::getPrice);
            case HIGHEST_PRICE_FIRST:
                return Comparator.comparing(Product::getPrice).reversed();
            case LEAST_SOLD_FIRST:
                return Comparator.comparing(Product::getQuantitySold);
            case MOST_SOLD_FIRST:
                return Comparator.comparing(Product::getQuantitySold).reversed();
        }

        return productComparator(COMPARATOR_TYPE.NEWEST_FIRST);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public double purchaseProduct(int quantity) {
        updateRevenueNQuantity(quantity);
        return price * quantity;
    }

    public void updateRevenueNQuantity(int quantity) {
        quantitySold += quantity;
        productRevenue += (quantity * price);
    }

    @Override
    public String toString() {
        String formated = new DecimalFormat("#.##").format(price);
        return String.format("Product{id='%s', name='%s', createdAt=%s, price=%s, quantitySold=%d}", id, name, createdAt, formated, quantitySold);
    }
}


class OnlineShop {
    Map<String, Product> products;

    OnlineShop() {
        products = new HashMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price){
        products.put(id, new Product(category, id, name, createdAt, price));
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException{
        if (!products.containsKey(id)) throw new ProductNotFoundException("Product with id \" + id + \" does not exist in the online shop!");

        return products.get(id).purchaseProduct(quantity);
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<List<Product>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        List<Product> sortedList;

        if (category == null) {
            sortedList = products.values().stream()
                    .sorted(Product.productComparator(comparatorType))
                    .collect(Collectors.toList());
        } else {
            sortedList = products.values().stream()
                    .filter(x -> x.getCategory().equalsIgnoreCase(category))
                    .sorted(Product.productComparator(comparatorType))
                    .collect(Collectors.toList());
        }

        int pIdx = 0;
        for (Product p : sortedList) {
            if (result.get(pIdx).size() == pageSize) {
                result.add(new ArrayList<>());
                pIdx++;
            }

            result.get(pIdx).add(p);
        }

        return result;
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category=null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}


