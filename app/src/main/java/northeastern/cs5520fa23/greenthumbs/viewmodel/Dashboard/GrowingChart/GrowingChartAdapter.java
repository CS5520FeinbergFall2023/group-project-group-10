package northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.GrowingChart;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;

public class GrowingChartAdapter extends RecyclerView.Adapter<GrowingChartViewHolder> {
    private List<Plant> plantList;
    private Context context;
    private HashMap<String, Integer> growTimes;
    private HashMap<String, Integer> plantResIds;

    public GrowingChartAdapter(List<Plant> plantList, Context context) {
        this.plantList = plantList;
        this.context = context;
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

        this.plantResIds = new HashMap<>();
        this.plantResIds.put("tomato", R.drawable.tomato);
        this.plantResIds.put("eggplant", R.drawable.eggplant);
        this.plantResIds.put("cucumber", R.drawable.cucumber);
        this.plantResIds.put("carrot", R.drawable.carrot);
        this.plantResIds.put("lettuce", R.drawable.lettuce);
        this.plantResIds.put("broccoli", R.drawable.broccoli);
        this.plantResIds.put("onion", R.drawable.onion);
        this.plantResIds.put("peas", R.drawable.peas);
        this.plantResIds.put("pepper", R.drawable.pepper);
        this.plantResIds.put("potato", R.drawable.potato);
        this.plantResIds.put("default", R.drawable.baseline_nature_24);
    }

    @NonNull
    @Override
    public GrowingChartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //GardenViewHolder(LayoutInflater.from(context).inflate(R.layout.garden_plant_item, parent, false))
        return new GrowingChartViewHolder(LayoutInflater.from(context).inflate(R.layout.plant_progress_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GrowingChartViewHolder holder, int position) {
        Plant plant = plantList.get(position);
        if (plant != null) {
            Boolean isGrowing = plant.isIs_growing();
            String expectedFinish = plant.getExpected_finish();
            String datePlanted = plant.getDate_planted();
            String plantType = plant.getPlant_type();
            if (isGrowing != null) {
                if (isGrowing == true) {
                    if (expectedFinish != null) {
                        holder.getExpectedFinishText().setText(expectedFinish);
                    }
                    if (datePlanted != null) {
                        holder.getDatePlantedText().setText(datePlanted);
                    }
                    if (plantType != null) {
                        if (plantResIds.containsKey(plantType.toLowerCase())) {
                            Integer resId = plantResIds.get(plantType.toLowerCase());
                            Picasso.get().load(resId).resize(30, 30).into(holder.getPlantImage());
                        } else {
                            Picasso.get().load(R.drawable.baseline_nature_24).resize(30,30).into(holder.getPlantImage());
                        }
                        if (growTimes.containsKey(plantType.toLowerCase())) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date start = formatter.parse(datePlanted);
                                Date end = formatter.parse(expectedFinish);
                                Date today = new Date();
                                long timeDif = today.getTime() - start.getTime();
                                Integer dayDif = (int) (timeDif / (1000 * 60 * 60 * 24) ) % 365;
                                Integer growTime = growTimes.get(plantType.toLowerCase());
                                //Integer daysGrown = growTime - dayDif;
                                double percentGrownPercent = dayDif / (growTime + 0.0);
                                Integer percentGrown = (int)(percentGrownPercent * 100);
                                holder.getProgressBar().setProgress(percentGrown);
                                if (percentGrown < 34) {
                                    holder.getProgressBar().setProgressTintList(ColorStateList.valueOf(Color.parseColor("#F0AF6C")));
                                } else if (percentGrown > 33 && percentGrown < 67) {
                                    holder.getProgressBar().setProgressTintList(ColorStateList.valueOf(Color.parseColor("#F0CC6C")));
                                } else if (percentGrown > 66 && percentGrown < 100) {
                                    holder.getProgressBar().setProgressTintList(ColorStateList.valueOf(Color.parseColor("#49A36A")));
                                } else {
                                    holder.getProgressBar().setProgressTintList(ColorStateList.valueOf(Color.parseColor("#356859")));
                                }
                            } catch (ParseException e) {

                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<Plant> getPlantList() {
        return plantList;
    }

    public void setPlantList(List<Plant> plantList) {
        this.plantList = plantList;
    }
}
