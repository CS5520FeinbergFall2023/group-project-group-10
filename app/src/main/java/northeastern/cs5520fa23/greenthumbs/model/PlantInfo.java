package northeastern.cs5520fa23.greenthumbs.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public class PlantInfo {

    private static final Map<String, List<String>> zonePlants = new HashMap<>();

    static {
        zonePlants.put("3a", Arrays.asList("Peas")); // Hardier vegetables like peas can survive in colder zones

        zonePlants.put("3b", Arrays.asList("Peas", "Lettuce"));

        zonePlants.put("4a", Arrays.asList("Peas", "Carrot", "Lettuce"));

        zonePlants.put("4b", Arrays.asList("Peas", "Carrot", "Lettuce"));

        zonePlants.put("5a", Arrays.asList("Broccoli", "Carrot", "Lettuce", "Peas", "Potato"));

        zonePlants.put("5b", Arrays.asList("Broccoli", "Carrot", "Lettuce", "Peas", "Potato"));

        zonePlants.put("6a", Arrays.asList("Broccoli", "Carrot", "Cucumber", "Lettuce", "Onion", "Peas", "Pepper", "Potato"));

        zonePlants.put("6b", Arrays.asList("Broccoli", "Carrot", "Cucumber", "Lettuce", "Onion", "Peas", "Pepper", "Potato", "Tomato"));

        zonePlants.put("7a", Arrays.asList("Broccoli", "Cucumber", "Eggplant", "Lettuce", "Peas", "Pepper", "Potato", "Tomato"));

        zonePlants.put("7b", Arrays.asList("Broccoli", "Carrot", "Lettuce", "Onion", "Peas", "Pepper", "Potato"));

        zonePlants.put("8a", Arrays.asList("Broccoli", "Carrot", "Cucumber", "Eggplant", "Lettuce", "Onion", "Pepper"));

        zonePlants.put("8b", Arrays.asList("Broccoli", "Carrot", "Cucumber", "Lettuce", "Onion", "Pepper", "Potato", "Tomato"));

        zonePlants.put("9a", Arrays.asList("Broccoli", "Carrot", "Cucumber", "Eggplant", "Lettuce", "Pepper", "Potato", "Tomato"));

        zonePlants.put("9b", Arrays.asList("Broccoli", "Carrot", "Cucumber", "Eggplant", "Lettuce", "Onion", "Tomato"));

        zonePlants.put("10a", Arrays.asList("Broccoli", "Carrot", "Cucumber", "Eggplant", "Lettuce", "Onion", "Pepper", "Tomato"));

        zonePlants.put("10b", Arrays.asList("Broccoli", "Carrot", "Cucumber", "Eggplant", "Lettuce", "Onion", "Pepper", "Tomato"));

        zonePlants.put("11a", Arrays.asList("Cucumber", "Eggplant", "Pepper"));

        zonePlants.put("11b", Arrays.asList("Cucumber", "Eggplant", "Pepper", "Tomato"));
    }

    public static List<String> getPlantsForZone(String zone) {
        return zonePlants.getOrDefault(zone, Arrays.asList("No plants available for this zone"));
    }
}

