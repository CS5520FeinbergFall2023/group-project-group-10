package northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.PlantRecommendations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlantRecommendationAdapter extends RecyclerView.Adapter<PlantRecommendationAdapter.ViewHolder> {

    private final List<PlantItem> plantItems;

    public PlantRecommendationAdapter(List<PlantItem> plantItems) {
        this.plantItems = plantItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_recommendation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlantItem item = plantItems.get(position);
        holder.plantTextView.setText(item.getName());
        holder.plantImageView.setImageResource(item.getImageResourceId());
    }

    @Override
    public int getItemCount() {
        return plantItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView plantTextView;
        final ImageView plantImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plantTextView = itemView.findViewById(R.id.plant_text_view);
            plantImageView = itemView.findViewById(R.id.plant_image_view);
        }
    }
}


