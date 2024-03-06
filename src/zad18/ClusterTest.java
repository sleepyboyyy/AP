package zad18;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

//ANALYZE AND REDO

interface IIdentifier {
    long getId();
    double getDistance(Object o);
};

class Cluster<T extends IIdentifier> {
    Map<Long, T> clusters;

    public Cluster() {
        clusters = new HashMap<>();
    }

    void addItem(T element) {
        clusters.put(element.getId(), element);
    }

    void near(long id, int top) {
        AtomicInteger idx = new AtomicInteger(1);
        clusters.entrySet().stream()
                .filter(x -> x.getKey() != id)
                .sorted(Comparator.comparing(x -> x.getValue().getDistance(clusters.get(id)))).limit(top)
                .forEach(x -> System.out.println(idx.getAndIncrement() + ". " + format(x.getKey(), x.getValue(), id)));
    }

    private String format(long id, T element, long targetId) {

        return String.format("%d -> %.3f", id, element.getDistance(clusters.get(targetId)));
    }
}

class Point2D implements IIdentifier{
    long id;
    float x;
    float y;

    public Point2D(long id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public double getDistance(Object o) {
        Point2D other = (Point2D)o;
        return Math.sqrt(Math.pow((x - other.x), 2) + Math.pow((y - other.y), 2));
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
