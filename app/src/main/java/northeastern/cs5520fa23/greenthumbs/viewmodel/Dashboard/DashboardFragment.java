package northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.FriendRequests.FriendRequest;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.FriendRequests.FriendRequestAdapter;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.GrowingChart.GrowingChartAdapter;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.GrowingChart.Plant;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.MessageHistoryAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment implements FriendRequestAdapter.FriendRequestCallback {

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance();
        friendRequestList = new ArrayList<>();
        plantList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.frRecyclerView = view.findViewById(R.id.dash_notification_rv);
        this.frRecyclerView.setHasFixedSize(true);
        this.frRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.friendRequestAdapter = new FriendRequestAdapter(friendRequestList, getContext(), this);
        this.frRecyclerView.setAdapter(friendRequestAdapter);
        getFriendRequests();
        this.growTimes = new HashMap<>();
        this.growTimes.put("tomato", 50);
        this.growingChartRecyclerView = view.findViewById(R.id.dashboard_progress_bars_rv);
        this.growingChartRecyclerView.setHasFixedSize(true);
        this.growingChartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.growingChartAdapter = new GrowingChartAdapter(plantList, getContext());
        this.growingChartRecyclerView.setAdapter(this.growingChartAdapter);
        getPlants();

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
        frQuery.addListenerForSingleValueEvent(new ValueEventListener() {
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
}