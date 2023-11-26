package northeastern.cs5520fa23.greenthumbs.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public class PlantInfo {

    private static final Map<String, List<String>> zonePlants = new HashMap<>();

    static {
        zonePlants.put("3a", Arrays.asList("Kale", "Leeks", "Raspberries", "Blue Spruce", "Juniper"));

        zonePlants.put("3b", Arrays.asList("Brussels Sprouts", "Rhubarb", "Strawberries", "Aspen", "Dogwood"));

        zonePlants.put("4a", Arrays.asList("Spinach", "Parsnips", "Plums", "Birch Trees", "Lilac Shrubs"));

        zonePlants.put("4b", Arrays.asList("Lettuce", "Beets", "Cherries", "Maple Trees", "Rose Bushes"));

        zonePlants.put("5a", Arrays.asList("Broccoli", "Carrots", "Apples", "Oak Trees", "Azalea Shrubs"));

        zonePlants.put("5b", Arrays.asList("Cauliflower", "Swiss Chard", "Pears", "Elm Trees", "Holly Bushes"));

        zonePlants.put("6a", Arrays.asList("Peas", "Garlic", "Peaches", "Cedar Trees", "Rhododendrons"));

        zonePlants.put("6b", Arrays.asList("Tomatoes", "Peppers", "Grapes", "Japanese Maple", "Hydrangea"));

        zonePlants.put("7a", Arrays.asList("Cucumbers", "Squash", "Figs", "Pine Trees", "Magnolia"));

        zonePlants.put("7b", Arrays.asList("Sweet Corn", "Okra", "Kiwi", "Cypress Trees", "Camellia Shrubs"));

        zonePlants.put("8a", Arrays.asList("Green Beans", "Melons", "Citrus", "Olive Trees", "Palm Trees"));

        zonePlants.put("8b", Arrays.asList("Eggplant", "Sweet Potatoes", "Pomegranates", "Bamboo", "Bougainvillea"));

        zonePlants.put("9a", Arrays.asList("Bell Peppers", "Tomatillos", "Avocados", "Oak Trees", "Hibiscus"));

        zonePlants.put("9b", Arrays.asList("Chili Peppers", "Lemons", "Mangoes", "Palm Trees", "Bird of Paradise"));

        zonePlants.put("10a", Arrays.asList("Cucumbers", "Papayas", "Guavas", "Bamboo", "Orchids"));

        zonePlants.put("10b", Arrays.asList("Tomatoes", "Bananas", "Coconuts", "Palm Trees", "Flowering Hibiscus"));

        zonePlants.put("11a", Arrays.asList("Sweet Potatoes", "Pineapples", "Mango Trees", "Ficus Trees", "Bougainvillea"));

        zonePlants.put("11b", Arrays.asList("Peppers", "Dragon Fruit", "Palm Trees", "Bird of Paradise", "Tropical Hibiscus"));

        // Add more zones and their corresponding plants as needed
    }

    public static List<String> getPlantsForZone(String zone) {
        return zonePlants.getOrDefault(zone, Arrays.asList("No plants available for this zone"));
    }
}

