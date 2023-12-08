package northeastern.cs5520fa23.greenthumbs.viewmodel.Garden.GardenStats;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import northeastern.cs5520fa23.greenthumbs.R;

public class StatsViewHolder extends RecyclerView.ViewHolder {
    private ImageView plantIcon;
    private TextView totalText;
    public StatsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.totalText = itemView.findViewById(R.id.plant_stat_card_text);
        this.plantIcon = itemView.findViewById(R.id.plant_stat_image);
    }

    public ImageView getPlantIcon() {
        return plantIcon;
    }

    public void setPlantIcon(ImageView plantIcon) {
        this.plantIcon = plantIcon;
    }

    public TextView getTotalText() {
        return totalText;
    }

    public void setTotalText(TextView totalText) {
        this.totalText = totalText;
    }
}
