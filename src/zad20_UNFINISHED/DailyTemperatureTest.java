//START FROM SCRATCH BOT

/*package zad20;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.IntStream;

class Temperature {
    private double value;
    private String type;

    public Temperature(double value, String type) {
        this.value = value;
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}

class DailyTemperatures {
    Map<String, List<Temperature>> temperatures;

    public DailyTemperatures() {
        temperatures = new HashMap<>();
    }

    //(val - 32) * 5) / 9 : (val * 9) / 5 + 32; <-- Conversion
    public void readTemperatures(InputStream inputStream) {
        Scanner reader = new Scanner(inputStream);

        while (reader.hasNextLine()) {
            String [] data = reader.nextLine().split(" ");
            temperatures.computeIfAbsent(data[0], x -> new ArrayList<>());
            IntStream.range(1, data.length).forEach(i -> temperatures.get(data[0]).add(new Temperature(Double.parseDouble(data[i].substring(0, data[i].length() - 1)), data[i].substring(data[i].length() - 1))));
        }
    }

    public void writeDailyStats(OutputStream outputStream, char scale) {

    }

    public double minimumTempValue() {
        return 0;
    }

    public double maximumTempValue() {
        return 0;
    }

    public double avgTempValue() {
        return 0;
    }

    public
}

public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}

 */
