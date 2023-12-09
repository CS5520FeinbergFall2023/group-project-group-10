package northeastern.cs5520fa23.greenthumbs.model;

import android.util.Log;

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
        //Log.d("sortPostAlgo", "sortPostAlgo: first"+ posts.get(0));

        Map<ImgPost, Double> sortedPostsMap = new HashMap<>();
        for (int i = posts.size() - 1; i >= 0; i--) {
            Double totalWeight = 0.0;
            Double timeWeight = i/100.0; // time weight is just the order of the post since they are already sorted by time
            totalWeight += timeWeight;
            int likes = posts.get(i).getNum_likes();
            Double likesWeight = likes * 0.002; // add weight for how much it was liked
            totalWeight += likesWeight;
            String tags = posts.get(i).getTags();
            if (tags != null) {
                String[] splitTags = tags.split(",");
                List<String> topPlants = plantCounts.entrySet().stream() // get the top 3 plants from the user and see if the post has any tags for the plant
                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .limit(3).map(Map.Entry::getKey).collect(Collectors.toList());
                //Log.d("sortPostAlgo", "sortPostAlgo: "+ plantCounts.get("tomato"));

                Double plantWeight = 0.0;
                for (String tag: splitTags) {
                    tag.replace(" ", "");
                    if (topPlants.contains(tag)) {
                        plantWeight += .015; // if the tag is in the users top plants add weight
                    }
                }
                totalWeight += plantWeight;
            }



            //Log.d("sortPostAlgo", "sortPostAlgo:" + posts.get(i).getPost_text() + " totalWeight" + totalWeight);

            sortedPostsMap.put(posts.get(i), totalWeight); // add to the map of posts to weight
        }
        List<ImgPost> sortedPosts = sortedPostsMap.entrySet().stream() // sort the posts by their weight
                .sorted(Map.Entry.<ImgPost, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey).collect(Collectors.toList());
        //Log.d("sortPostAlgo", "sortPostAlgo: "+ sortedPosts.get(0).getTags());
        return sortedPosts; // return the sorted posts
    }

}
