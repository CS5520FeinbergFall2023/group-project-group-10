package northeastern.cs5520fa23.greenthumbs.viewmodel.Garden;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;

public class GardenAdapter extends RecyclerView.Adapter<GardenViewHolder> {
    private List<GardenMenuItem> plantsItemList;
    private List<String> plantNames;
    public GardenAdapter(List<GardenMenuItem> plantsItemList, Context context) {
        this.plantsItemList = plantsItemList;
        this.plantNames = new ArrayList<>();
        this.plantNames.add("Tomato");
        this.plantNames.add("Eggplant");
        this.plantNames.add("Cucumber");
        this.plantNames.add("Carrot");
        this.plantNames.add("Lettuce");
        this.plantNames.add("Broccoli");
        this.plantNames.add("Onion");
        this.plantNames.add("Peas");
        this.plantNames.add("Pepper");
        this.plantNames.add("Potato");
    }



    @NonNull
    @Override
    public GardenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull GardenViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
