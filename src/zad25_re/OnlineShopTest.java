package zad25_re;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
    private int quantitySold;
    private double totalProductRevenue;

    public Product(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;

        this.quantitySold = 0;
        this.totalProductRevenue = 0;
    }

    static Comparator<Product> productComparator(COMPARATOR_TYPE type) {
        switch(type) {
            case NEWEST_FIRST:
                    return Comparator.comparing(Product::getCreatedAt).reversed();
            case OLDEST_FIRST:
                    return Comparator.comparing(Product::getCreatedAt);
            case LOWEST_PRICE_FIRST:
                return Comparator.comparing(Product::getPrice);
            case HIGHEST_PRICE_FIRST:
                return Comparator.comparing(Product::getPrice).reversed();
            case LEAST_SOLD_FIRST:
                return Comparator.comparing(Product::getQuantitySold);
            case MOST_SOLD_FIRST:
                return Comparator.comparing(Product::getQuantitySold).reversed();
        }

        return productComparator(COMPARATOR_TYPE.HIGHEST_PRICE_FIRST);
    }

    double buyProduct(int quantity) {
        quantitySold += quantity;
        totalProductRevenue += quantity * price;
        return quantity * price;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getTotalProductRevenue() {
        return totalProductRevenue;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        //Product{id='1718af53', name='product7', createdAt=2018-09-22T10:13:57.715710, price=2832.58, quantitySold=5}
        String decFormat = new DecimalFormat("#.##").format(price);
        return String.format("Product{id='%s', name='%s', createdAt=%s, price=%s, quantitySold=%d}", id, name, createdAt, decFormat, quantitySold);
    }
}


class OnlineShop {
    private Map<String, Product> products;

    OnlineShop() {
        products = new HashMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price){
        products.put(id, new Product(category, id, name, createdAt, price));
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException{
        if (!products.containsKey(id)) throw new ProductNotFoundException(String.format("Product with id %s does not exist in the online shop!", id));
        else return products.get(id).buyProduct(quantity);
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<List<Product>> result = new ArrayList<>();
        result.add(new ArrayList<>());

        List<Product> sortedProducts;
        if (category == null) {
            sortedProducts = products.values().stream()
                    .sorted(Product.productComparator(comparatorType))
                    .collect(Collectors.toList());
        } else {
            sortedProducts = products.values().stream()
                    .filter(product -> product.getCategory().equals(category))
                    .sorted(Product.productComparator(comparatorType))
                    .collect(Collectors.toList());
        }

        int idx = 0;
        for (Product product : sortedProducts) {
            if (result.get(idx).size() == pageSize) {
                result.add(new ArrayList<>());
                idx++;
            }
            result.get(idx).add(product);
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

