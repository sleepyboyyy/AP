package zad11;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

//RETRY

class DeadlineNotValidException extends Exception{
    public DeadlineNotValidException(LocalDateTime deadline) {
        super("The deadline " + deadline + " has already passed");
    }
}

class Task {
    private final String taskName;
    private final String description;
    private boolean hasDeadline;
    private boolean hasPriority;
    private LocalDateTime deadline;
    private int priority;

    public Task(String taskName, String description) {
        this.description = description;
        this.taskName = taskName;

        this.deadline = LocalDateTime.now();
        this.hasDeadline = false;
        this.hasPriority = false;
    }

    public void setDeadline(LocalDateTime timestamp) throws DeadlineNotValidException {
        LocalDateTime validDate = LocalDateTime.parse("2020-06-02T00:00:00");
        if (timestamp.isBefore(validDate)) {
            throw new DeadlineNotValidException(timestamp);
        } else {
            this.hasDeadline = true;
            this.deadline = timestamp;
        }
    }

    public void setPriority(int priorityLevel) {
        this.hasPriority = true;
        this.priority = priorityLevel;
    }

    public boolean isPriority() {
        return hasPriority;
    }

    public int getPriority() {
        return priority;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(String.format("Task{name='%s', description='%s'", taskName, description));

        if(hasDeadline) str.append(", deadline=").append(deadline);
        if(hasPriority) str.append(", priority=").append(priority);

        str.append("}\n");

        return str.toString();
    }
}

class TaskManager {
    Map<String, List<Task>> tasks;

    public TaskManager() {tasks = new HashMap<>();}

    public void readTasks(InputStream inputStream) {
        Scanner reader = new Scanner(inputStream);

        while (reader.hasNextLine()) {
            String [] data = reader.nextLine().split(",");

            tasks.computeIfAbsent(data[0], x -> new ArrayList<>());
            Task t = new Task(data[1], data[2]);

            if (data.length > 3) {
                if(isValidDate(data[3])) {
                    try {
                        t.setDeadline(LocalDateTime.parse(data[3]));
                    } catch(Exception e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                    if (data.length == 5) t.setPriority(Integer.parseInt(data[4]));
                } else {
                    t.setPriority(Integer.parseInt(data[3]));
                }
            }

            tasks.computeIfPresent(data[0], (k, v) -> {v.add(t); return v;});
        }
    }

    public void printTasks(OutputStream os, boolean includePriority, boolean includeCategory) {
        PrintWriter printer = new PrintWriter((os));

        Comparator<Task> priorityComparator = Comparator.comparing(Task::isPriority).reversed().thenComparing(Task::getPriority)
                .thenComparing(task -> Duration.between(LocalDateTime.now(), task.getDeadline()));
        Comparator<Task> durationComparator = Comparator.comparing(task -> Duration.between(LocalDateTime.now(), task.getDeadline()));

        if (includeCategory) {
            tasks.forEach((category, tasks) -> {
               printer.println(category.toUpperCase());
               tasks.stream()
                       .sorted(includePriority ? priorityComparator : durationComparator)
                       .forEach(printer::print);
            });
        } else {
            tasks.values().stream()
                    .flatMap(Collection::stream)
                    .sorted(includePriority ? priorityComparator : durationComparator)
                    .forEach(printer::print);
        }

        printer.flush();
    }

    public boolean isValidDate(String date) {
        try {
            LocalDateTime.parse(date);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

public class TasksManagerTest {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        System.out.println("Tasks reading");
        manager.readTasks(System.in);
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
        System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");

    }
}
