package northeastern.cs5520fa23.greenthumbs.viewmodel.Garden;

public class GardenMenuItem {
    private int resId;
    private String plantName;

    public GardenMenuItem() {
    }

    public GardenMenuItem(String plantName, int resId) {
        this.resId = resId;
        this.plantName = plantName;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }
}
