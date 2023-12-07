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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.GrowingChart.Plant;

public class GardenPlotAdapter extends RecyclerView.Adapter<GardenPlotViewHolder> {
    private List<GardenPlotPlant> plotPlants;
    private Context context;
    private HashMap<String, Integer> plantIds;
    private Integer count;

    public interface PlotPlantDragCallback {
        boolean dragPlotPlant(GardenPlotPlant plant, ImageView plotImage);
    }

    private PlotPlantDragCallback plotPlantDragCallback;
    public GardenPlotAdapter(List<GardenPlotPlant> plotPlants, Context context, PlotPlantDragCallback plotPlantDragCallback) {
        this.plotPlants = plotPlants;
        this.context = context;
        this.plotPlantDragCallback = plotPlantDragCallback;
        this.count = 1;

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
        this.plantIds.put("default", R.drawable.baseline_nature_24);
        this.plantIds.put("empty", R.drawable.baseline_crop_square_24);
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
                plant.setViewHolder(holder);
                plant.setHolderView(holder.getPlotImage());
                if (plantType != null) {
                    if (plantIds.containsKey(plantType)) {
                        plant.setResId(plantIds.get(plantType.toLowerCase()));
                        Picasso.get().load(plantIds.get(plantType.toLowerCase())).resize(60,60).into(holder.getPlotImage());
                        plant.setHolderView(holder.getPlotImage());

                    } else {
                        plant.setPlant_type("default");
                        plant.setResId(plantIds.get(plantType.toLowerCase()));
                        holder.getPlotImage().setImageResource(R.drawable.baseline_nature_24);
                        plant.setHolderView(holder.getPlotImage());

                    }
                }
                /*else {
                    plant.setPlant_type("default");
                    plant.setResId(plantIds.get(plantType.toLowerCase()));
                    holder.getPlotImage().setImageResource(R.drawable.baseline_nature_24);
                    plant.setHolderView(holder.getPlotImage());
                  }
                 */
                 else {
                    plant.setPlant_type("empty");
                    plant.setResId(plantIds.get(plant.getPlant_type().toLowerCase()));
                    holder.getPlotImage().setImageResource(R.drawable.baseline_crop_square_24);
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date finish = formatter.parse(plant.getExpected_finish());
                    //Date today = formatter.parse(expectedFinish);
                    Date today = new Date();
                    if (!today.before(finish)) {
                        Drawable background = context.getDrawable(R.drawable.rounded_corners_background_dg);
                        holder.getPlotImage().setBackground(background);
                    } else {
                        holder.getPlotImage().setBackgroundResource(0);
                    }
                } catch (ParseException e) {

                }
            } else {
                plant.setViewHolder(holder);
                plant.setHolderView(holder.getPlotImage());
                plant.setPlant_type("empty");
                plant.setResId(plantIds.get(plant.getPlant_type().toLowerCase()));
                holder.getPlotImage().setImageResource(R.drawable.baseline_crop_square_24);
            }

            // When this is is dragged onto / dropped onto
            holder.getPlotImage().setOnDragListener((view, event) -> {

                switch(event.getAction()) {

                    case DragEvent.ACTION_DRAG_STARTED:

                        // Determine whether this View can accept the dragged data.
                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            //int id = holder.getPlotImage().getId();
                            if (plant.getPlant_id() == null) {
                                Log.d("PLANT ID LOG", "onBindViewHolder: no image at" + position);
                                ((ImageView)view).setColorFilter(Color.parseColor("#4B7DFA"));
                                view.invalidate();
                                return true;
                            } else {
                                Log.d("PLANT ID LOG", "IMG AT " + position);
                                Log.d("PLANT ID LOG", "HOLDER ID " + holder.getPlantId());
                                Log.d("PLANT ID LOG", "PLANT ID  " + plant.getPlant_id());

                            }
                        }
                        return false;

                    case DragEvent.ACTION_DRAG_ENTERED:

                        ((ImageView)view).setColorFilter(Color.parseColor("#49A36A"));
                        view.invalidate();
                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION: // doesn't matter
                        return true;

                    case DragEvent.ACTION_DRAG_EXITED:
                        ((ImageView)view).setColorFilter(Color.parseColor("#4B7DFA"));
                        view.invalidate();
                        return true;

                    case DragEvent.ACTION_DROP:

                        // Get the item containing the dragged data.
                        ClipData.Item item = event.getClipData().getItemAt(0);
                        CharSequence dragData = item.getText();
                        ClipDescription plantDescription = event.getClipData().getDescription();
                        CharSequence plantName = plantDescription.getLabel();
                        String plantNameString = (String) plantName;
                        plantNameString = plantNameString.toLowerCase();
                        count++;
                        String newId = plantName + "_" + count;
                        plant.setPlant_id(newId);
                        holder.setPlantId(newId);
                        Log.d("ON DROP NAME", "plant name: " + plantNameString);
                        plant.setPlant_type(plantNameString);

                        // Set imageView in garden plot to display new image based on clipboard data
                        //((ImageView) view).setImageResource(Integer.parseInt((String) dragData));
                        //((ImageView) view).setImageResource();
                        Picasso.get().load(plantIds.get(plantNameString)).resize(60,60).into((ImageView) view);

                        ((ImageView)view).clearColorFilter();

                        view.invalidate();
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        ((ImageView)view).clearColorFilter();
                        view.invalidate();

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
                        plotPlantDragCallback.dragPlotPlant(plant, holder.getPlotImage());
                        /*
                        ClipData.Item item = new ClipData.Item(String.valueOf(plantIds.get(plant.getPlant_type().toLowerCase())));
                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                        // Create a new ClipData using "Lettuce" as a label.
                        ClipData dragData = new ClipData("Lettuce", mimeTypes, item);
                        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(holder.getPlotImage());
                        // Start the drag.
                        v.startDragAndDrop(dragData, myShadow, null, 0);

                         */
                    }
                    else {
                        plotPlantDragCallback.dragPlotPlant(plant, holder.getPlotImage());
                        /*
                        ClipData.Item item = new ClipData.Item(String.valueOf(R.drawable.baseline_nature_24));
                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                        // Create a new ClipData using "Lettuce" as a label.
                        ClipData dragData = new ClipData("Lettuce", mimeTypes, item);
                        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(holder.getPlotImage());
                        // Start the drag.
                        v.startDragAndDrop(dragData, myShadow, null, 0);

                         */
                    }


//                img.setVisibility(View.INVISIBLE);
                    return true; // Indicate that the long-click is handled.
                }
            });
        }
    }

    public void harvestPlant(GardenPlotPlant plant) {
        plant.getHolderView().setImageResource(plantIds.get("empty"));
        plant.getViewHolder().getPlotImage().setImageResource(plantIds.get("empty"));
        plant.getHolderView().setBackgroundResource(0);
        plant.getViewHolder().getPlotImage().setBackgroundResource(0);
        plant.setResId(plantIds.get("empty"));
        plant.setPlant_type(null);
        plant.setPlant_id(null);

    }

    @Override
    public int getItemCount() {
        return plotPlants.size();
    }

}
