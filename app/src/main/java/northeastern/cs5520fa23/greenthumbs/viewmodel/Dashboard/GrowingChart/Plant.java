package northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.GrowingChart;

public class Plant {
    private String plant_id;
    private String plant_type;
    private String date_planted;
    private String expected_finish;
    private Boolean is_growing;

    public Plant() {
    }

    public Plant(String plant_id, String plant_type, String date_planted, String expected_finish, Boolean is_growing) {
        this.plant_id = plant_id;
        this.plant_type = plant_type;
        this.date_planted = date_planted;
        this.expected_finish = expected_finish;
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

    public Boolean isIs_growing() {
        return is_growing;
    }

    public void setIs_growing(Boolean is_growing) {
        this.is_growing = is_growing;
    }
}
