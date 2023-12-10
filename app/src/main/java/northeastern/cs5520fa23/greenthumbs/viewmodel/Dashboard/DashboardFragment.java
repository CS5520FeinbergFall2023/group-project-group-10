package northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard;

import static androidx.core.content.ContextCompat.registerReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.model.plant_recommendations.PlantRecommendationReceiver;
import northeastern.cs5520fa23.greenthumbs.model.services.PlantRecommendationService;
import northeastern.cs5520fa23.greenthumbs.model.services.WeatherService;
import northeastern.cs5520fa23.greenthumbs.model.weather.WeatherUpdateReceiver;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.FriendRequests.FriendRequest;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.FriendRequests.FriendRequestAdapter;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.GrowingChart.GrowingChartAdapter;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.GrowingChart.Plant;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.PlantRecommendations.PlantRecommendationAdapter;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.PlantRecommendations.PlantViewModel;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.Posts.PostAdapter;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.Weather.WeatherViewModel;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.MessageHistoryAdapter;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Profile.Friend;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Profile.ProfileActivity;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.ImgPost;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.Like;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment implements FriendRequestAdapter.FriendRequestCallback, PostAdapter.DashboardPostCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView frRecyclerView;
    private FriendRequestAdapter friendRequestAdapter;
    private List<FriendRequest> friendRequestList;
    private RecyclerView growingChartRecyclerView;
    private GrowingChartAdapter growingChartAdapter;
    private FirebaseUser currUser;
    private FirebaseDatabase db;
    private HashMap<String, Integer> growTimes;
    private List<Plant> plantList;
    private WeatherUpdateReceiver weatherUpdateReceiver;
    private WeatherViewModel weatherViewModel;
    private PlantRecommendationReceiver plantRecommendationReceiver;
    private PlantViewModel plantViewModel;
    private RecyclerView recyclerView;
    private PlantRecommendationAdapter adapter;
    private RecyclerView dashPostsRecyclerView;
    private PostAdapter postAdapter;
    private List<ImgPost> postList;
    private SocialAdapter socialAdapter;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance();
        friendRequestList = new ArrayList<>();
        plantList = new ArrayList<>();
        weatherViewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
        plantViewModel = new ViewModelProvider(requireActivity()).get(PlantViewModel.class);
        startWeatherService();
        weatherUpdateReceiver = new WeatherUpdateReceiver(weatherViewModel);
        plantRecommendationReceiver = new PlantRecommendationReceiver(plantViewModel);

         */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance();
        friendRequestList = new ArrayList<>();
        plantList = new ArrayList<>();
        weatherViewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
        plantViewModel = new ViewModelProvider(requireActivity()).get(PlantViewModel.class);
        startWeatherService();
        weatherUpdateReceiver = new WeatherUpdateReceiver(weatherViewModel);
        plantRecommendationReceiver = new PlantRecommendationReceiver(plantViewModel);

        this.frRecyclerView = view.findViewById(R.id.dash_notification_rv);
//        this.frRecyclerView.setHasFixedSize(true);
        this.frRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.friendRequestAdapter = new FriendRequestAdapter(friendRequestList, getContext(), this);
        this.frRecyclerView.setAdapter(friendRequestAdapter);
        getFriendRequests();
        this.growTimes = new HashMap<>();
        this.growTimes.put("tomato", 50);
        this.growingChartRecyclerView = view.findViewById(R.id.dashboard_progress_bars_rv);
//        this.growingChartRecyclerView.setHasFixedSize(true);
        this.growingChartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.growingChartAdapter = new GrowingChartAdapter(plantList, getContext());
        this.growingChartRecyclerView.setAdapter(this.growingChartAdapter);
        getPlants();

        weatherViewModel.getForecasts().observe(getViewLifecycleOwner(), this::updateUIWithWeatherData);

        recyclerView = view.findViewById(R.id.plant_recommendations_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        plantViewModel.getPlantRecommendations().observe(getViewLifecycleOwner(), newPlantRecommendations -> {
            if (newPlantRecommendations != null && !newPlantRecommendations.isEmpty()) {
                if (adapter == null) {
                    adapter = new PlantRecommendationAdapter(newPlantRecommendations);
                }
                recyclerView.setAdapter(adapter);
            }
        });

        dashPostsRecyclerView = view.findViewById(R.id.dash_posts_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        dashPostsRecyclerView.setLayoutManager(layoutManager);

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList, getContext(), this);

        dashPostsRecyclerView.setAdapter(postAdapter);

        addPosts();

    }


    private void getPlants() {
        DatabaseReference plantRef = db.getReference("users").child(currUser.getUid()).child("plants").child("growing");
        plantRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                plantList.clear();
                growingChartAdapter.notifyDataSetChanged();
                for (DataSnapshot plantsSnapshot: snapshot.getChildren()) {
                    //HashMap<String, Object> children = plantsSnapshot.getChildren();
                    Log.d("plant charts", plantsSnapshot.toString());

                    for (DataSnapshot plantSnapshot : plantsSnapshot.getChildren()) {
                        Log.d("plant charts", plantSnapshot.toString());
                        try {
                            Plant plant = plantSnapshot.getValue(Plant.class);
                            if (plant != null) {
                                if (plant.isIs_growing() != null && plant.isIs_growing() == true) {
                                    plantList.add(plant);
                                    growingChartAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (Error e) {
                            Log.d("plant charts error", plantSnapshot.toString());
                        }
                        //for (DataSnapshot plantSnapshot : plantTypeSnapshot.getChildren()) {

                        //}


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    private void getFriendRequests() {
        DatabaseReference frRef = db.getReference("users").child(currUser.getUid());
        Query frQuery = frRef.child("friend_requests").orderByChild("approved").equalTo("false");
        frQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FriendRequest fr = dataSnapshot.getValue(FriendRequest.class);
                    if (fr != null) {
                        //Toast.makeText(getActivity(), "HERE", Toast.LENGTH_LONG).show();
                        friendRequestList.add(fr);
                        friendRequestAdapter.notifyDataSetChanged();
                    }
                }
                friendRequestAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void removeRequestCallback(int position) {
        friendRequestList.remove(position);
        friendRequestAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(WeatherService.ACTION_WEATHER_UPDATE);
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(weatherUpdateReceiver, filter);
        IntentFilter filter_plant = new IntentFilter(PlantRecommendationService.ACTION_PLANT_RECOMMENDATIONS_UPDATE);
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(plantRecommendationReceiver, filter_plant);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(weatherUpdateReceiver);
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(plantRecommendationReceiver);
    }

    private void updateUIWithWeatherData(ArrayList<String> forecasts) {
        ImageView imgToday = this.requireView().findViewById(R.id.img_weather_today);
        TextView txtToday = this.requireView().findViewById(R.id.txt_weather_today);
        ImageView imgTomorrow = this.requireView().findViewById(R.id.img_weather_tomorrow);
        TextView txtTomorrow = this.requireView().findViewById(R.id.txt_weather_tomorrow);
        ImageView imgDayAfter = this.requireView().findViewById(R.id.img_weather_day_after);
        TextView txtDayAfter = this.requireView().findViewById(R.id.txt_weather_day_after);

        // Update the UI components with the new data
        updateWeatherView(imgToday, txtToday, forecasts.get(0));
        updateWeatherView(imgTomorrow, txtTomorrow, forecasts.get(1));
        updateWeatherView(imgDayAfter, txtDayAfter, forecasts.get(2));
    }

    private void updateWeatherView(ImageView weatherIcon, TextView weatherText, String forecast) {
        int resourceIcon = getWeatherIconResource(forecast);
        weatherIcon.setImageResource(resourceIcon);
        weatherText.setText(forecast);
    }

    private int getWeatherIconResource(String forecast) {
        switch (forecast.toLowerCase()) {
            case "sun":
            case "clear":
                return R.drawable.sunny_24;
            case "cloud":
                return R.drawable.cloud_24;
            case "rain":
                return R.drawable.water_drop_24;
            case "snow":
            case "frost":
                return R.drawable.snow_24;
            case "fog":
                return R.drawable.fog_24;
            default:
                return R.drawable.baseline_question_mark_24;
        }
    }

    private void startWeatherService() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        float latitude = sharedPreferences.getFloat("latitude", 0);
        float longitude = sharedPreferences.getFloat("longitude", 0);
        if (latitude != 0 && longitude != 0) {
            Intent serviceIntent = new Intent(this.getContext(), WeatherService.class);
            serviceIntent.putExtra(WeatherService.latitude, latitude);
            serviceIntent.putExtra(WeatherService.longitude, longitude);
            requireContext().startService(serviceIntent);
        } else {
            //
        }
    }

    private void addPosts() {
        postList.clear();
        postAdapter.notifyDataSetChanged();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("posts");
        DatabaseReference friendRef = FirebaseDatabase.getInstance().getReference("users").child(currUser.getUid()).child("friends");

        // Fetch friends
        friendRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> friendIds = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Friend userFriend = dataSnapshot.getValue(Friend.class);
                    if (userFriend != null && "friends".equals(userFriend.getStatus())) {
                        friendIds.add(userFriend.getFriend_id());
                    }
                }

                // Fetch posts
                dbRef.orderByChild("timestamp").limitToLast(100).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<ImgPost> tempPosts = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ImgPost currPost = dataSnapshot.getValue(ImgPost.class);
                            if (currPost != null && friendIds.contains(currPost.getUid())) {
                                tempPosts.add(currPost);
                            }
                        }

                        // Sort and take top 3 posts
                        Collections.sort(tempPosts, (p1, p2) -> p2.getTimestamp().compareTo(p1.getTimestamp()));
                        for (int i = 0; i < Math.min(3, tempPosts.size()); i++) {
                            postList.add(tempPosts.get(i));
                        }

                        postAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("DashboardFragment", "Failed to read posts", error.toException());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DashboardFragment", "Failed to read friends list", error.toException());
            }
        });
    }


    @Override
    public void openProfileCallback(String username, String posterId) {
        Intent i = new Intent(getContext(), ProfileActivity.class);
        Bundle extras = new Bundle();
        ArrayList<String> userInfo = new ArrayList<>();
        userInfo.add(username);
        userInfo.add(posterId);
        extras.putStringArrayList("user_info", userInfo);
        i.putExtra("profile_info", extras);
        this.startActivity(i);
    }


}