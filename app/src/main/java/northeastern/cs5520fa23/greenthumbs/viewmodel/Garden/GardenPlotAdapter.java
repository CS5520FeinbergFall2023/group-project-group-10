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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.GrowingChart.Plant;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.Chat.ChatActivity;

public class GardenPlotAdapter extends RecyclerView.Adapter<GardenPlotViewHolder> {
    private List<GardenPlotPlant> plotPlants;
    private Context context;
    private HashMap<String, Integer> plantIds;
    private Integer count;
    private HashMap<String, Integer> growTimes;
    private FirebaseDatabase db;
    private FirebaseUser currUser;

    public interface PlotPlantDragCallback {
        boolean dragPlotPlant(GardenPlotPlant plant, ImageView plotImage);
    }

    private PlotPlantDragCallback plotPlantDragCallback;
    public GardenPlotAdapter(List<GardenPlotPlant> plotPlants, Context context, PlotPlantDragCallback plotPlantDragCallback) {
        this.db = FirebaseDatabase.getInstance();
        this.currUser = FirebaseAuth.getInstance().getCurrentUser();
        this.plotPlants = plotPlants;
        this.context = context;
        this.plotPlantDragCallback = plotPlantDragCallback;
        this.count = 1;

        this.growTimes = new HashMap<>();
        this.growTimes.put("tomato", 100);
        this.growTimes.put("eggplant", 85);
        this.growTimes.put("lettuce", 53);
        this.growTimes.put("pepper", 78);
        this.growTimes.put("carrot", 68);
        this.growTimes.put("potato", 70);
        this.growTimes.put("broccoli", 70);
        this.growTimes.put("onion", 105);
        this.growTimes.put("cucumber", 60);
        this.growTimes.put("peas", 60);

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

                 else {
                    plant.setPlant_type("empty");
                    plant.setResId(plantIds.get(plant.getPlant_type().toLowerCase()));
                    holder.getPlotImage().setImageResource(R.drawable.baseline_crop_square_24);
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (plant.getExpected_finish() != null) {
                        Date finish = formatter.parse(plant.getExpected_finish());
                        //Date today = formatter.parse(expectedFinish);
                        Date today = new Date();
                        if (!today.before(finish)) {
                            Drawable background = context.getDrawable(R.drawable.rounded_corners_background_dg);
                            holder.getPlotImage().setBackground(background);
                            //plant.getHolderView().setBackground(background);
                        }
                    }
                    else {
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

            holder.getPlotImage().setOnDragListener((view, event) -> {

                switch(event.getAction()) {

                    case DragEvent.ACTION_DRAG_STARTED:

                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                            if (plant.getPlant_id() == null) {
                                //Log.d("PLANT ID LOG", "onBindViewHolder: no image at" + position);
                                ((ImageView)view).setColorFilter(Color.parseColor("#4B7DFA"));
                                view.invalidate();
                                return true;
                            } else {

                                return false;

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
                        if (plant.getPlant_id() == null && plant.getPlant_type() == "empty") {
                            ClipData.Item item = event.getClipData().getItemAt(0);
                            CharSequence dragData = item.getText();
                            ClipDescription plantDescription = event.getClipData().getDescription();
                            CharSequence plantName = plantDescription.getLabel();
                            String plantNameString = (String) plantName;
                            if (plantNameString.length() >= 5) {
                                if (plantNameString.contains("menu_")) {
                                    plantNameString = plantNameString.toLowerCase();
                                    plantNameString = plantNameString.substring(5);
                                    addPlantToDatabase(plant, plantNameString);
                                    count++;
                                    String newId = plantName + "_" + count;
                                    plant.setPlant_id(newId);
                                    holder.setPlantId(newId);
                                    Log.d("ON DROP NAME", "plant name: " + plantNameString);
                                    plant.setPlant_type(plantNameString);

                                    Picasso.get().load(plantIds.get(plantNameString)).resize(60,60).into((ImageView) view);

                                }
                            }

                            ((ImageView)view).clearColorFilter();

                            view.invalidate();
                            return true;
                        }
                        else {
                            return false;
                        }

                    case DragEvent.ACTION_DRAG_ENDED:
                        ((ImageView)view).clearColorFilter();
                        view.invalidate();

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

                    if (plant.getPlant_type() != null && plantIds.containsKey(plant.getPlant_type().toLowerCase()) && plant.getPlant_type() != "empty") { // don't drag empty plot
                        plotPlantDragCallback.dragPlotPlant(plant, holder.getPlotImage());
                    }
                    else {
                        //plotPlantDragCallback.dragPlotPlant(plant, holder.getPlotImage());

                    }

                    return true;
                }
            });
        }
    }

    public void harvestPlant(GardenPlotPlant plant) {
        try {
            Tasks.await(updateHarvestDatabasePlants(plant));
        } catch (Exception e) {

        }


    }

    public void deletePlant(GardenPlotPlant plant) {
        updateDeleteDatabasePlants(plant);

    }

    private void addPlantToDatabase(GardenPlotPlant plant, String plantNameString) {
        //plant.clearPlant(); // might not need here - do for harvest and delete though probably
        if (this.growTimes.containsKey(plantNameString)) {
            Map<String, Object> newPlant = new HashMap<>();
            Calendar calendar = Calendar.getInstance();
            Integer plantGrowTime = this.growTimes.get(plantNameString);
            calendar.add(Calendar.DATE, plantGrowTime);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();
            String date_planted = formatter.format(today);
            Date expectedFinish = calendar.getTime();
            String expected_finish = formatter.format(expectedFinish);

            Boolean is_growing = true;
            Integer plantPostion = plant.getPosition();
            DatabaseReference userPlantsRef = db.getReference("users").child(currUser.getUid()).child("plants").child("growing").child(plantNameString);
            userPlantsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(context, "Unable to upload plant to user's plants", Toast.LENGTH_LONG);
                    } else {
                        String newPlantId = userPlantsRef.push().getKey();
                        newPlant.put("date_planted", date_planted);
                        newPlant.put("expected_finish", expected_finish);
                        newPlant.put("plant_id", newPlantId);
                        newPlant.put("is_growing", is_growing);
                        newPlant.put("plant_type", plantNameString);
                        userPlantsRef.child(newPlantId).setValue(newPlant).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(context, "Unable to upload plant to user's plants", Toast.LENGTH_LONG);
                                } else {
                                    addPlantToGarden(newPlant, newPlantId, plantPostion);
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void addPlantToGarden(Map<String, Object> newPlant, String newPlantId, Integer position) {
        newPlant.put("position", position);

        //plotPlants.clear();
        //notifyDataSetChanged();
        DatabaseReference gardenRef = db.getReference("users").child(currUser.getUid()).child("plants").child("garden");
        gardenRef.child(newPlantId).updateChildren(newPlant).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(context, "Unable to upload plant to user's garden", Toast.LENGTH_LONG);
                } else {
                }
            }
        });
    }

    private Task updateHarvestDatabasePlants(GardenPlotPlant plantHarvested) {
        Map<String, Object> plantHarvestedData = new HashMap<>();
        plantHarvestedData.put("plant_id",plantHarvested.getPlant_id());
        plantHarvestedData.put("plant_type", plantHarvested.getPlant_type());
        plantHarvestedData.put("expected_finish", plantHarvested.getExpected_finish());
        plantHarvestedData.put("date_planted", plantHarvested.getDate_planted());
        plantHarvestedData.put("is_growing", false);
        String plantHarvestedType = plantHarvested.getPlant_type();
        String plantHarvestedId = plantHarvested.getPlant_id();
        DatabaseReference userPlantsRef = db.getReference("users").child(currUser.getUid()).child("plants").child("growing").child(plantHarvestedType).child(plantHarvestedId);
        Task updateTask = userPlantsRef.updateChildren(plantHarvestedData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {

                } else {
                    try {
                        Tasks.await(updateHarvestDatabaseGarden(plantHarvested ,plantHarvestedId));
                    } catch (Exception e) {

                    }

                }
            }
        });

        return updateTask;
    }



    private Task updateHarvestDatabaseGarden(GardenPlotPlant plantHarvested,String plantHarvestedId) {
        DatabaseReference userGardenRef = db.getReference("users").child(currUser.getUid()).child("plants").child("garden").child(plantHarvestedId);

        Task removeTask = userGardenRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {

                } else {
                    plantHarvested.clearPlant();
                    plantHarvested.getHolderView().setImageResource(plantIds.get("empty"));
                    plantHarvested.getViewHolder().getPlotImage().setImageResource(plantIds.get("empty"));
                    plantHarvested.getHolderView().setBackgroundResource(0);
                    plantHarvested.getViewHolder().getPlotImage().setBackgroundResource(0);
                    plantHarvested.setResId(plantIds.get("empty"));
                    try {Thread.sleep(1000);}
                    catch (Exception e) {}
                }
            }
        });

        return removeTask;

    }


    private void updateDeleteDatabasePlants(GardenPlotPlant plantDeleting) {
        String plantDeletingId = plantDeleting.getPlant_id();
        String plantDeletingType = plantDeleting.getPlant_type();
        if (plantDeletingId != null) {
            DatabaseReference userPlantsRef = db.getReference("users").child(currUser.getUid()).child("plants").child("growing").child(plantDeletingType).child(plantDeletingId);

            userPlantsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {

                    } else {
                        updateDeleteDatabaseGarden(plantDeleting);
                    }
                }
            });
        }
    }

    private void updateDeleteDatabaseGarden(GardenPlotPlant plantDeleting) {
        String plantDeletingId = plantDeleting.getPlant_id();
        if (plantDeletingId != null) {
            DatabaseReference userGardenRef = db.getReference("users").child(currUser.getUid()).child("plants").child("garden").child(plantDeletingId);

            userGardenRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {

                    } else {
                        plantDeleting.clearPlant();
                        plantDeleting.getHolderView().setImageResource(plantIds.get("empty"));
                        plantDeleting.getViewHolder().getPlotImage().setImageResource(plantIds.get("empty"));
                        plantDeleting.getHolderView().setBackgroundResource(0);
                        plantDeleting.getViewHolder().getPlotImage().setBackgroundResource(0);
                        plantDeleting.setResId(plantIds.get("empty"));
                        try {Thread.sleep(1000);}
                        catch (Exception e) {}
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return plotPlants.size();
    }

}
