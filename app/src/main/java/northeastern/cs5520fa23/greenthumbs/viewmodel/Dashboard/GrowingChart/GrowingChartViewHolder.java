package northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.GrowingChart;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import northeastern.cs5520fa23.greenthumbs.R;

public class GrowingChartViewHolder extends RecyclerView.ViewHolder {
    private String plant_id;
    private String plant_type;
    private String date_planted;
    private String expected_finish;
    private Boolean is_growing;
    private TextView datePlantedText;
    private TextView expectedFinishText;
    private ProgressBar progressBar;
    private ImageView plantImage;
    public GrowingChartViewHolder(@NonNull View itemView) {
        super(itemView);
        this.datePlantedText = itemView.findViewById(R.id.plant_progress_item_date_planted);
        this.expectedFinishText = itemView.findViewById(R.id.plant_progress_item_date_finished);
        this.progressBar = itemView.findViewById(R.id.plant_progress_progress_bar);
        this.plantImage = itemView.findViewById(R.id.plant_progress_item_image);
    }

    public String getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(String plant_id) {
        this.plant_id = plant_id;
    }

    public String getPlant_type() {
        return plant_type;
    }

    public void setPlant_type(String plant_type) {
        this.plant_type = plant_type;
    }

    public String getDate_planted() {
        return date_planted;
    }

    public void setDate_planted(String date_planted) {
        this.date_planted = date_planted;
    }

    public String getExpected_finish() {
        return expected_finish;
    }

    public void setExpected_finish(String expected_finish) {
        this.expected_finish = expected_finish;
    }

    public Boolean getIs_growing() {
        return is_growing;
    }

    public void setIs_growing(Boolean is_growing) {
        this.is_growing = is_growing;
    }

    public TextView getDatePlantedText() {
        return datePlantedText;
    }

    public void setDatePlantedText(TextView datePlantedText) {
        this.datePlantedText = datePlantedText;
    }

    public TextView getExpectedFinishText() {
        return expectedFinishText;
    }

    public void setExpectedFinishText(TextView expectedFinishText) {
        this.expectedFinishText = expectedFinishText;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public ImageView getPlantImage() {
        return plantImage;
    }

    public void setPlantImage(ImageView plantImage) {
        this.plantImage = plantImage;
    }
}
