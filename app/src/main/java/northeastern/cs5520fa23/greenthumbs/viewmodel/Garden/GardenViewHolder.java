package northeastern.cs5520fa23.greenthumbs.viewmodel.Garden;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import northeastern.cs5520fa23.greenthumbs.R;

public class GardenViewHolder extends RecyclerView.ViewHolder {
    private ImageView plantPicture;
    private TextView plantName;

    public GardenViewHolder(@NonNull View itemView) {
        super(itemView);
        this.plantName = itemView.findViewById(R.id.garden_plant_menu_label);
        this.plantPicture = itemView.findViewById(R.id.garden_plant_menu_image);
    }

    public ImageView getPlantPicture() {
        return plantPicture;
    }

    public void setPlantPicture(ImageView plantPicture) {
        this.plantPicture = plantPicture;
    }

    public TextView getPlantName() {
        return plantName;
    }

    public void setPlantName(TextView plantName) {
        this.plantName = plantName;
    }
}
