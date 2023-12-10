package northeastern.cs5520fa23.greenthumbs.viewmodel.Garden.GardenStats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Garden.GardenMenuItem;

public class StatsAdapter extends RecyclerView.Adapter<StatsViewHolder> {
    private HashMap<String, Integer> plantIds;
    private List<PlantTotal> plantTotalList;
    private Context context;
    public StatsAdapter(List<PlantTotal> plantTotalList, Context context) {
        this.plantTotalList = plantTotalList;
        this.context = context;
        this.plantIds = new HashMap<>();
        this.plantIds.put("tomato", R.drawable.tomato);
        this.plantIds.put("eggplant", R.drawable.eggplant);
        this.plantIds.put("cucumber", R.drawable.cucumber);
        this.plantIds.put("carrot", R.drawable.carrot);
        this.plantIds.put("lettuce", R.drawable.lettuce);
        this.plantIds.put("broccoli", R.drawable.broccoli);
        this.plantIds.put("onion", R.drawable.onion);
        this.plantIds.put("peas", R.drawable.peas);
        this.plantIds.put("pepper", R.drawable.pepper);
        this.plantIds.put("potato", R.drawable.potato);
    }

    @NonNull
    @Override
    public StatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StatsViewHolder(LayoutInflater.from(context).inflate(R.layout.plant_stat_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StatsViewHolder holder, int position) {
        PlantTotal plantTotal = plantTotalList.get(position);
        if (plantTotal != null) {
            String plantName = plantTotal.getPlantName();
            String numPlants = plantTotal.getTotal().toString();
            if (plantName != null && numPlants != null) {
                holder.getTotalText().setText(plantName + ": " + numPlants);
                if (plantIds.containsKey(plantName.toLowerCase())) {
                    Integer resId = plantIds.get(plantName.toLowerCase());
                    holder.getPlantIcon().setImageResource(resId);
                } else {
                    holder.getPlantIcon().setImageResource(R.drawable.baseline_nature_24);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return plantTotalList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
