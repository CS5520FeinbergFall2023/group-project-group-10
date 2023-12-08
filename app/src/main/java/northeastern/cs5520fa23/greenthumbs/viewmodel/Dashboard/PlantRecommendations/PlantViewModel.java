package northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.PlantRecommendations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class PlantViewModel extends ViewModel {
    private final MutableLiveData<List<PlantItem>> plantRecommendations = new MutableLiveData<>();

    public LiveData<List<PlantItem>> getPlantRecommendations() {
        return plantRecommendations;
    }

    public void setPlantRecommendations(List<PlantItem> newPlantRecommendations) {
        plantRecommendations.setValue(newPlantRecommendations);
    }
}

