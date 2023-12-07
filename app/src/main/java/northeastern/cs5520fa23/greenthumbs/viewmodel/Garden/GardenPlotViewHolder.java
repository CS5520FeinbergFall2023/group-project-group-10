package northeastern.cs5520fa23.greenthumbs.viewmodel.Garden;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import northeastern.cs5520fa23.greenthumbs.R;

public class GardenPlotViewHolder extends RecyclerView.ViewHolder {
    ImageView plotImage;
    public GardenPlotViewHolder(@NonNull View itemView) {
        super(itemView);
        this.plotImage = itemView.findViewById(R.id.garden_cell_img);
    }

    public ImageView getPlotImage() {
        return plotImage;
    }

    public void setPlotImage(ImageView plotImage) {
        this.plotImage = plotImage;
    }
}
