package northeastern.cs5520fa23.greenthumbs.viewmodel.Garden;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import northeastern.cs5520fa23.greenthumbs.viewmodel.Garden.GardenStats.GardenPlotPlant;

public class GardenPlotAdapter extends RecyclerView.Adapter<GardenPlotViewHolder> {
    private List<GardenPlotPlant> plotPlants;
    private Context context;
    public GardenPlotAdapter(List<GardenPlotPlant> plotPlants, Context context) {
        this.plotPlants = plotPlants;
        this.context = context;
    }

    @NonNull
    @Override
    public GardenPlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull GardenPlotViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
