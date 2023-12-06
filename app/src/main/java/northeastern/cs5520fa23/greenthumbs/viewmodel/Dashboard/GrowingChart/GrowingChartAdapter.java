package northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.GrowingChart;

import android.content.Context;
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
        this.growTimes.put("tomato", 50);
        this.plantResIds = new HashMap<>();
        this.plantResIds.put("tomato", R.drawable.tomato);
    }

    @NonNull
    @Override
    public GrowingChartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
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
                                long timeDif = end.getTime() - start.getTime();
                                Integer dayDif = (int) (timeDif / (1000 * 60 * 60 * 24) ) % 365;
                                Integer growTime = growTimes.get(plantType.toLowerCase());
                                Integer daysGrown = growTime - dayDif;
                                Integer percentGrown = daysGrown / growTime;
                                holder.getProgressBar().setProgress(percentGrown);
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
        this.plantList.clear();
        this.plantList.addAll(plantList);
        notifyDataSetChanged();
    }
}
