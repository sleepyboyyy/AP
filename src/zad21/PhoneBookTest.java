package zad21;

import java.util.*;
import java.util.stream.Collectors;

class DuplicateNumberException extends Exception {
    public DuplicateNumberException(String number) {
        super("Duplicate number: " + number);
    }
}

class Contact implements Comparable<Contact>{
    private final String name;
    private final String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return String.format("%s %s", name, number);
    }

    @Override
    public int compareTo(Contact o) {
        return Comparator.comparing(Contact::getName)
                .thenComparing(Contact::getNumber)
                .compare(this, o);
    }
}

class PhoneBook {
    private final Map<String, Contact> contacts;

    public PhoneBook() {
        contacts = new HashMap<>();
    }

    public void addContact(String name, String number) throws DuplicateNumberException {
        if (contacts.containsKey(number)) throw new DuplicateNumberException(number);
        contacts.put(number, new Contact(name, number));
    }

    public void contactsByNumber(String number) {
        List<Contact> elements = contacts.values().stream()
                .filter(contact -> contact.getNumber().contains(number))
                .sorted()
                .collect(Collectors.toList());

        if (!elements.isEmpty()) elements.forEach(System.out::println);
        else System.out.println("NOT FOUND");
    }

    public void contactsByName(String name) {
        List<Contact> elements = contacts.values().stream()
                .filter(contact -> contact.getName().equals(name))
                .sorted()
                .collect(Collectors.toList());

        if (!elements.isEmpty()) elements.forEach(System.out::println);
        else System.out.println("NOT FOUND");
    }
}

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}



