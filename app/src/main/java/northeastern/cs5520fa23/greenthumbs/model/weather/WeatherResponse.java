package northeastern.cs5520fa23.greenthumbs.model.weather;

public class WeatherResponse {
    private Properties properties;

    public String getForecastUrl() {
        return properties != null ? properties.forecastHourly : null;
    }

    private class Properties {
        private String forecastHourly;
    }
}

