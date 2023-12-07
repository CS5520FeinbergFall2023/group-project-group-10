package northeastern.cs5520fa23.greenthumbs.viewmodel.Garden;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
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

            // When this is is dragged onto / dropped onto
            holder.getPlotImage().setOnDragListener((v, e) -> {

                switch(e.getAction()) {

                    case DragEvent.ACTION_DRAG_STARTED:

                        // Determine whether this View can accept the dragged data.
                        if (e.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                            // keep the blue stuff from bryan
                            //int id = holder.getPlotImage().getId();
                            if (plant.getPlant_id() == null) {
                                Log.d("PLANT ID LOG", "onBindViewHolder: no image at" + position);
                                ((ImageView)v).setColorFilter(Color.parseColor("#4B7DFA"));
                                v.invalidate();
                                return true;
                            } else {
                                Log.d("PLANT ID LOG", "IMG AT " + position);
                                Log.d("PLANT ID LOG", "HOLDER ID " + holder.getPlantId());
                                Log.d("PLANT ID LOG", "PLANT ID  " + plant.getPlant_id());

                            }
                        }
                        return false;

                    case DragEvent.ACTION_DRAG_ENTERED:

                        ((ImageView)v).setColorFilter(Color.parseColor("#49A36A"));
                        v.invalidate();
                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION: // doesn't matter
                        return true;

                    case DragEvent.ACTION_DRAG_EXITED:
                        ((ImageView)v).setColorFilter(Color.parseColor("#4B7DFA"));
                        v.invalidate();
                        return true;

                    case DragEvent.ACTION_DROP:

                        // Get the item containing the dragged data.
                        ClipData.Item item = e.getClipData().getItemAt(0);
                        CharSequence dragData = item.getText();
                        plant.setPlant_id((String) dragData);
                        holder.setPlantId((String) dragData);

                        // Set imageView in garden plot to display new image based on clipboard data
                        ((ImageView) v).setImageResource(Integer.parseInt((String) dragData));
                        ((ImageView)v).clearColorFilter();

                        v.invalidate();
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        ((ImageView)v).clearColorFilter();
                        v.invalidate();

                        // potentially ad this back in later
                        //Drawable d = context.getResources().getDrawable(R.drawable.rounded_corners_orange);
                        //v.setBackground(d);

                        // Return true. The value is ignored.
                        return true;
                    default:
                        break;
                }
                return false;
            });

            // When this is dragged
            holder.getPlotImage().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    // create data we want to send in drag event
                    // TODO: use tags for imageviews to get resourceID, no function to get resourceID.
                    if (plant.getPlant_type() != null && plantIds.containsKey(plant.getPlant_type().toLowerCase())) {
                        ClipData.Item item = new ClipData.Item(String.valueOf(plantIds.get(plant.getPlant_type().toLowerCase())));
                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                        // Create a new ClipData using "Lettuce" as a label.
                        ClipData dragData = new ClipData("Lettuce", mimeTypes, item);
                        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(holder.getPlotImage());
                        // Start the drag.
                        v.startDragAndDrop(dragData, myShadow, null, 0);
                    }
                    else {
                        ClipData.Item item = new ClipData.Item(String.valueOf(R.drawable.baseline_nature_24));
                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                        // Create a new ClipData using "Lettuce" as a label.
                        ClipData dragData = new ClipData("Lettuce", mimeTypes, item);
                        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(holder.getPlotImage());
                        // Start the drag.
                        v.startDragAndDrop(dragData, myShadow, null, 0);
                    }


//                img.setVisibility(View.INVISIBLE);
                    return true; // Indicate that the long-click is handled.
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return plotPlants.size();
    }

}
