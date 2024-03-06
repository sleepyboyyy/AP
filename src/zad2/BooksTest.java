package zad2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

//Да се напише класа за книга Book во која се чува:
//
//наслов
//категорија
//цена.
//Да се имплементира конструктор со следните аргументи Book(String title, String category, float price).
//
//Потоа да се напише класа BookCollection во која се чува колекција од книги. Во оваа класа треба да се имплментираат следните методи:
//
//public void addBook(Book book) - додавање книга во колекцијата
//public void printByCategory(String category) - ги печати сите книги од проследената категорија (се споредува стрингот без разлика на мали и големи букви), сортирани според насловот на книгата (ако насловот е ист, се сортираат според цената).
//public List<Book> getCheapestN(int n) - враќа листа на најевтините N книги (ако има помалку од N книги во колекцијата, ги враќа сите).

class Book {
    private final String title;
    private final String category;
    private final float price;

    public Book(String title, String category, float price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public float getPrice() {
        return price;
    }



    @Override
    public String toString() {
        return String.format("%s (%s) %.2f", getTitle(), getCategory(), getPrice());
    }
}

class BookCollection {
    List<Book> books;

    public BookCollection() {books = new ArrayList<>();}

    public void addBook(Book book) {
        books.add(book);
    }

    public void printByCategory(String category) {
        books.stream()
                .filter(book -> book.getCategory().equals(category))
                .sorted(Comparator.comparing(Book::getCategory).thenComparing(Book::getTitle).thenComparing(Book::getPrice))
                .forEach(System.out::println);
    }

    public List<Book> getCheapestN(int n) {
        return books.stream()
                .sorted(Comparator.comparing(Book::getPrice).thenComparing(Book::getTitle))
                .limit(n)
                .collect(Collectors.toList());
    }
}

public class BooksTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}

// Вашиот код овде