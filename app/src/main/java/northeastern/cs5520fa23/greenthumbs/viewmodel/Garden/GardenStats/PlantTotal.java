package northeastern.cs5520fa23.greenthumbs.viewmodel.Garden.GardenStats;

public class PlantTotal {
    private String plantName;
    private Integer total;

    public PlantTotal() {
    }

    public PlantTotal(String plantName, Integer total) {
        this.plantName = plantName;
        this.total = total;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
