package zad17;

import java.util.*;

// RETRY 100%

class ForecastDisplay implements Display {

    private float previousPressure;

    public ForecastDisplay(WeatherDispatcher weatherDispatcher) {
        weatherDispatcher.register(this);
        previousPressure = 0;
    }

    @Override
    public void setMeasurements(float temperature, float humidity, float pressure) {
        String forecastState = (pressure > previousPressure) ? "Improving" : (pressure < previousPressure) ? "Cooler" : "Same" ;
        System.out.printf("Forecast: %s\n", forecastState);
        previousPressure = pressure;
    }

    @Override
    public int priority() {
        return 1;
    }
}

class CurrentConditionsDisplay implements Display{

    public CurrentConditionsDisplay(WeatherDispatcher weatherDispatcher) {
        weatherDispatcher.register(this);
    }

    @Override
    public void setMeasurements(float temperature, float humidity, float pressure) {
        System.out.printf("Temperature: %.1fF\nHumidity: %.1f%%\n", temperature, humidity);
    }

    @Override
    public int priority() {
        return 0;
    }
}

interface Display {
    void setMeasurements(float temperature, float humidity, float pressure); // for updates
    int priority(); // for print
}

class WeatherDispatcher {
    private float temperature;
    private float humidity;
    private float pressure;
    List<Display> displays;

    public WeatherDispatcher() {
        displays = new ArrayList<>();
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        displays.stream()
                .sorted(Comparator.comparing(Display::priority))
                .forEach(x -> x.setMeasurements(temperature, humidity, pressure));
        System.out.println();
    }

    public void register(Display d) {
        displays.add(d);
    }

    public void remove(Display d) {
        displays.remove(d);
    }
}

public class WeatherApplication {

    public static void main(String[] args) {
        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            if(parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if(operation==1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if(operation==2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if(operation==3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if(operation==4) {
                    weatherDispatcher.register(currentConditions);
                }

            }
        }
    }
}