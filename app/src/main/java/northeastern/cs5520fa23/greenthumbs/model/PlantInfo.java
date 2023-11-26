package northeastern.cs5520fa23.greenthumbs.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public class PlantInfo {

    private static final Map<String, List<String>> zonePlants = new HashMap<>();

    static {
        zonePlants.put("3", Arrays.asList("Spinach", "Peas", "Radishes", "Rhubarb", "Apple Trees", "Spruce Trees", "Lilac Shrubs"));

        zonePlants.put("4", Arrays.asList("Lettuce", "Swiss Chard", "Carrots", "Cherry Trees", "Plum Trees", "Hydrangea Shrubs", "Juniper Shrubs"));

        zonePlants.put("5", Arrays.asList("Broccoli", "Cauliflower", "Garlic", "Peach Trees", "Pear Trees", "Rose Bushes", "Weigela"));

        zonePlants.put("6", Arrays.asList("Tomatoes", "Bell Peppers", "Cucumbers", "Grapes", "Fig Trees", "Dogwood Trees", "Azalea Shrubs"));

        zonePlants.put("6b", Arrays.asList("Cabbage", "Beans", "Zucchini", "Blackberries", "Raspberries", "Japanese Maple Trees", "Boxwood Shrubs"));

        zonePlants.put("7", Arrays.asList("Corn", "Sweet Potatoes", "Okra", "Apple Trees", "Cherry Trees", "Crape Myrtle Trees", "Camellia Shrubs"));

        zonePlants.put("8", Arrays.asList("Cucumbers", "Peppers", "Melons", "Citrus Trees", "Pecan Trees", "Oleander Shrubs", "Palm Trees"));

        zonePlants.put("9", Arrays.asList("Tomatoes", "Eggplant", "Squash", "Fig Trees", "Olive Trees", "Bougainvillea", "Hibiscus Shrubs"));

        zonePlants.put("10", Arrays.asList("Peppers", "Tomatoes", "Avocado Trees", "Mango Trees", "Bamboo", "Bird of Paradise", "Orchid Trees"));

        zonePlants.put("11", Arrays.asList("Papaya", "Guava", "Pineapple", "Palm Trees", "Banana Trees", "Hibiscus", "Bougainvillea"));

        // Add more zones and their corresponding plants as needed
    }

    public static List<String> getPlantsForZone(String zone) {
        return zonePlants.getOrDefault(zone, Arrays.asList("No plants available for this zone"));
    }
}

