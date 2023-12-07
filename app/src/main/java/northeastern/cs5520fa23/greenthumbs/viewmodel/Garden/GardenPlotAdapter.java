package northeastern.cs5520fa23.greenthumbs.viewmodel.Garden;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;

public class GardenPlotAdapter extends RecyclerView.Adapter<GardenPlotViewHolder> {
    private List<GardenPlotPlant> plotPlants;
    private Context context;
    private HashMap<String, Integer> plantIds;
    public GardenPlotAdapter(List<GardenPlotPlant> plotPlants, Context context) {
        this.plotPlants = plotPlants;
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
    public GardenPlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GardenPlotViewHolder(LayoutInflater.from(context).inflate(R.layout.garden_cell, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GardenPlotViewHolder holder, int position) {
        GardenPlotPlant plant = plotPlants.get(position);
        if (plant != null) {
            if (plant.getPlant_id() != null) {
                String plantId = plant.getPlant_id();
                String datePlanted = plant.getDate_planted();
                String expectedFinish = plant.getExpected_finish();
                Boolean isGrowing = plant.getIs_growing();
                String plantType = plant.getPlant_type();
                Integer p = plant.getPosition();
                if (plantType != null) {
                    if (plantIds.containsKey(plantType)) {
                        Picasso.get().load(plantIds.get(plantType.toLowerCase())).resize(60,60).into(holder.getPlotImage());
                    } else {
                        holder.getPlotImage().setImageResource(R.drawable.baseline_nature_24);
                    }
                }
            }
            holder.getPlotImage().setOnDragListener((v, e) -> {

                // Handle each of the expected events.
                switch(e.getAction()) {

                    case DragEvent.ACTION_DRAG_STARTED:

                        // Determine whether this View can accept the dragged data.
                        if (e.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                            // keep the blue stuff from bryan
                            ((ImageView)v).setColorFilter(Color.BLUE);
                            v.invalidate();
                            return true;
                        }
                        return false;

                    case DragEvent.ACTION_DRAG_ENTERED:

                        ((ImageView)v).setColorFilter(Color.GREEN);
                        v.invalidate();
                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION: // doesn't matter
                        return true;

                    case DragEvent.ACTION_DRAG_EXITED:
                        ((ImageView)v).setColorFilter(Color.BLUE);
                        v.invalidate();
                        return true;

                    case DragEvent.ACTION_DROP:

                        // Get the item containing the dragged data.
                        ClipData.Item item = e.getClipData().getItemAt(0);
                        CharSequence dragData = item.getText();

                        // Set imageView in garden plot to display new image based on clipboard data
                        ((ImageView) v).setImageResource(Integer.parseInt((String) dragData));
                        ((ImageView)v).clearColorFilter();

                        v.invalidate();
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:

                        // Turn off color tinting.
                        ((ImageView)v).clearColorFilter();

                        // Invalidate the view to force a redraw.
                        v.invalidate();
                        //Drawable d = context.getResources().getDrawable(R.drawable.rounded_corners_orange);
                        //v.setBackground(d);

                        // Return true. The value is ignored.
                        return true;
                    // An unknown action type is received.
                    default:
                        break;
                }
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return plotPlants.size();
    }

}
