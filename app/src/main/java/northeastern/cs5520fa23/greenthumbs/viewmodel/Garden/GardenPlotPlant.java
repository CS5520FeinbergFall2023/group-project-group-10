package northeastern.cs5520fa23.greenthumbs.viewmodel.Garden;

import android.widget.ImageView;

public class GardenPlotPlant {
    private String date_planted;
    private String expected_finish;
    private Boolean is_growing;
    private String plant_id;
    private String plant_type;
    private Integer position;
    private Integer resId;
    private ImageView holderView;
    private GardenPlotViewHolder viewHolder;

    public GardenPlotPlant() {
    }

    public GardenPlotPlant(String date_planted, String expected_finish, Boolean is_growing, String plant_id, String plant_type, Integer position, Integer resId, ImageView holderView, GardenPlotViewHolder viewHolder) {
        this.date_planted = date_planted;
        this.expected_finish = expected_finish;
        this.is_growing = is_growing;
        this.plant_id = plant_id;
        this.plant_type = plant_type;
        this.position = position;
        this.resId = resId;
        this.holderView = holderView;
        this.viewHolder = viewHolder;
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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public ImageView getHolderView() {
        return holderView;
    }

    public void setHolderView(ImageView holderView) {
        this.holderView = holderView;
    }

    public GardenPlotViewHolder getViewHolder() {
        return viewHolder;
    }

    public void setViewHolder(GardenPlotViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    public void clearPlant() {
        this.setPlant_id(null);
        this.setPlant_type("empty");
        this.setDate_planted(null);
        this.setExpected_finish(null);
        this.setIs_growing(false);
    }
}
