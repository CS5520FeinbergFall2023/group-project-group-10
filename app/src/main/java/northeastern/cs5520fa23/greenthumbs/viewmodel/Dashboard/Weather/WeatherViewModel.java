package northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.Weather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

public class WeatherViewModel extends ViewModel {
    // LiveData for weather forecasts
    private final MutableLiveData<ArrayList<String>> forecasts = new MutableLiveData<>();

    // Method to get the forecasts as LiveData
    public LiveData<ArrayList<String>> getForecasts() {
        return forecasts;
    }

    // Method to update the forecasts
    public void setForecasts(ArrayList<String> newForecasts) {
        forecasts.setValue(newForecasts);
    }
}

