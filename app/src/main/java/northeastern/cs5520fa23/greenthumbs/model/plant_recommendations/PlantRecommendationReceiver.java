package northeastern.cs5520fa23.greenthumbs.model.plant_recommendations;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.PlantRecommendations.PlantItem;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.PlantRecommendations.PlantViewModel;

public class PlantRecommendationReceiver extends BroadcastReceiver {
    private final PlantViewModel viewModel;
    public static final String EXTRA_PLANT_DATA = "com.example.greenthumbs.EXTRA_PLANT_DATA";

    public PlantRecommendationReceiver(PlantViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<String> plantRecommendations = intent.getStringArrayListExtra(EXTRA_PLANT_DATA);
        if (plantRecommendations != null && !plantRecommendations.isEmpty()) {
            List<PlantItem> plantItems = new ArrayList<>();
            for (String plantName : plantRecommendations) {
                int imageResourceId = getImageResourceIdForPlant(plantName);
                plantItems.add(new PlantItem(plantName, imageResourceId));
            }
            viewModel.setPlantRecommendations(plantItems);
        }
    }

    private int getImageResourceIdForPlant(String plantName) {
        switch (plantName.toLowerCase()) {
            case "tomato":
                return R.drawable.tomato;
            case "eggplant":
                return R.drawable.eggplant;
            case "cucumber":
                return R.drawable.cucumber;
        }
        return R.drawable.tomato;
    }

}
