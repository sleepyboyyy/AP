package zad18_re;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * January 2016 Exam problem 2
 */

//RETRY AGAIN

interface iIdentifier {
    long getId();
    double getDistance(Object o);
}

class Cluster<T extends iIdentifier> {
    Map<Long, T> elements;

    public Cluster() {
        elements = new HashMap<>();
    }

    void addItem(T element) {
        elements.put(element.getId(), element);
    }

    void near(long id, int top) {
        AtomicInteger index = new AtomicInteger(1);

        elements.entrySet().stream()
                .filter(x -> x.getKey() != id)
                .sorted(Comparator.comparing(x -> x.getValue().getDistance(elements.get(id))))
                .limit(top)
                .forEach(x -> System.out.println(String.format("%d. %d -> %.3f", index.getAndIncrement(), x.getValue().getId(), x.getValue().getDistance(elements.get(id)))));
    }
}

class Point2D implements iIdentifier{
    private long id;
    private float x;
    private float y;

    public Point2D(long id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public double getDistance(Object o) {
        Point2D other = (Point2D) o;
        return Math.sqrt(Math.pow(x - other.getX(), 2) + Math.pow(y - other.getY(), 2));
    }
}

public class ClusterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}

// your code here
