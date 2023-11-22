package northeastern.cs5520fa23.greenthumbs.model;

import java.util.List;

import java.util.List;

public class WeatherForecast {
    private Properties properties;

    public List<Period> getPeriods() {
        return properties != null ? properties.periods : null;
    }

    private class Properties {
        private List<Period> periods;
    }

    public class Period {
        private int number;
        private int temperature; // Assuming temperature is always an integer

        private String shortForecast;

        public int getNumber() {
            return number;
        }

        public int getTemperature() {
            return temperature;
        }

        public String getShortForecast() {
            return shortForecast;
        }
    }
}


