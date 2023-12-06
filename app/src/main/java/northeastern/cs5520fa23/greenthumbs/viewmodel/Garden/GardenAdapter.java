package northeastern.cs5520fa23.greenthumbs.viewmodel.Garden;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;

public class GardenAdapter extends RecyclerView.Adapter<GardenViewHolder> {
    private List<GardenMenuItem> plantsItemList;
    private List<String> plantNames;
    private Context context;

    public interface PlantDragCallback {
        boolean dragPlant(String plantName, int resId, ImageView plantImage);
    }
    private PlantDragCallback plantDragCallback;
    public GardenAdapter(List<GardenMenuItem> plantsItemList, Context context, PlantDragCallback plantDragCallback) {
        this.context = context;
        this.plantsItemList = plantsItemList;
        this.plantDragCallback = plantDragCallback;
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
        return new GardenViewHolder(LayoutInflater.from(context).inflate(R.layout.garden_plant_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GardenViewHolder holder, int position) {
        GardenMenuItem item = plantsItemList.get(position);
        if (item != null) {
            String plantName = item.getPlantName();
            int plantResId = item.getResId();
            if (plantName != null) {
                holder.getPlantName().setText(plantName);
            }
            try {
                holder.getPlantPicture().setImageResource(plantResId);
                holder.getPlantPicture().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return plantDragCallback.dragPlant(plantName, plantResId, holder.getPlantPicture());
                    }
                });
            } catch (Exception e) {
                holder.getPlantPicture().setImageResource(R.drawable.baseline_nature_24);
            }
        }
    }

    @Override
    public int getItemCount() {
        return plantsItemList.size();
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
