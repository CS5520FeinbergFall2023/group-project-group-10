package northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.PlantRecommendations;

public class PlantItem {
    private String name;
    private int imageResourceId;

    public PlantItem(String name, int imageResourceId) {
        this.name = name;
        this.imageResourceId = imageResourceId;
    }

    public String getName() {
        return name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
