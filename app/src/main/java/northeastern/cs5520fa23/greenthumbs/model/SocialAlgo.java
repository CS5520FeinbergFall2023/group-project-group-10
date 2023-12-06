package northeastern.cs5520fa23.greenthumbs.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.ImgPost;

public class SocialAlgo {
    public SocialAlgo() {
    }

    public static List<ImgPost> sortPostAlgo(List<ImgPost> posts, Map<String, Integer> plantCounts) {
        Map<ImgPost, Double> sortedPostsMap = new HashMap<>();
        for (int i = 0; i < posts.size(); i++) {
            double timeWeight = i; // time weight is just the order of the post since they are already sorted by time
            int likes = posts.get(i).getNum_likes();
            double likesWeight = likes * 0.2; // add weight for how much it was liked
            String tags = posts.get(i).getTags();
            String[] splitTags = tags.split(",");
            List<String> topPlants = plantCounts.entrySet().stream() // get the top 3 plants from the user and see if the post has any tags for the plant
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(3).map(Map.Entry::getKey).collect(Collectors.toList());
            double plantWeight = 0;
            for (String tag: splitTags) {
                tag.replace(" ", "");
                if (topPlants.contains(tag)) {
                    plantWeight += .05; // if the tag is in the users top plants add weight
                }
            }
            Double totalWeight = timeWeight + likesWeight + plantWeight;
            sortedPostsMap.put(posts.get(i), totalWeight); // add to the map of posts to weight
        }
        List<ImgPost> sortedPosts = sortedPostsMap.entrySet().stream() // sort the posts by their weight
                .sorted(Map.Entry.<ImgPost, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey).collect(Collectors.toList());
        return sortedPosts; // return the sorted posts
    }

}
