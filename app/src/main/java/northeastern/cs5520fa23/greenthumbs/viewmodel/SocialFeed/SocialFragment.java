package northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Profile.Friend;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Profile.ProfileFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SocialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SocialFragment extends Fragment implements SocialAdapter.UsernameCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SocialAdapter socialAdapter;
    private List<ImgPost> postList;
    private Switch friendsSwitch;
    private SwipeRefreshLayout swipeRefreshLayout;
    FirebaseUser currUser;

    public SocialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SocialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SocialFragment newInstance(String param1, String param2) {
        SocialFragment fragment = new SocialFragment();
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
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
        postList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_social, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        this.friendsSwitch = view.findViewById(R.id.friends_switch);
        this.swipeRefreshLayout = view.findViewById(R.id.social_swipe_refresh);
        this.swipeRefreshLayout.setOnRefreshListener(this::addPosts);
        RecyclerView socialRecyclerView = view.findViewById(R.id.social_recycler_view);
        socialRecyclerView.setHasFixedSize(true);
        socialRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        socialAdapter = new SocialAdapter(postList, getContext(), this);
        socialRecyclerView.setAdapter(socialAdapter);
        FloatingActionButton fab = view.findViewById(R.id.post_fab);
        fab.setOnClickListener(v -> openCreatePostDialog());
        addPosts();
        friendsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> addPosts());
    }

    private void addPosts() {
        postList.clear();
        socialAdapter.notifyDataSetChanged();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("posts");
        if (friendsSwitch.isChecked()) {
            List<String> friend_ids = new ArrayList<>();
            DatabaseReference friendRef = FirebaseDatabase.getInstance().getReference("users").child(currUser.getUid()).child("friends");
            Query query = friendRef.orderByChild("friend_id").limitToLast(100);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Friend user_friend = dataSnapshot.getValue(Friend.class);
                        if (user_friend.getStatus().equals("friends")) {
                            friend_ids.add(user_friend.getFriend_id());
                        }
                        //socialAdapter.notifyDataSetChanged();
                    }
                    Query query = dbRef.orderByChild("timestamp").limitToLast(100);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                ImgPost currPost = dataSnapshot.getValue(ImgPost.class);
                                if (friend_ids.contains(currPost.getUid())) {
                                    postList.add(currPost);
                                    socialAdapter.notifyDataSetChanged();
                                }
                            }

                            Collections.reverse(postList);
                            socialAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Query query = dbRef.orderByChild("timestamp").limitToLast(100);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ImgPost currPost = dataSnapshot.getValue(ImgPost.class);
                        postList.add(currPost);
                        socialAdapter.notifyDataSetChanged();

                    }

                    Collections.reverse(postList);
                    socialAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void openCreatePostDialog() {
        DialogFragment createPostDialog = new CreatePostDialog();
        createPostDialog.show(getActivity().getSupportFragmentManager(), "Post");
    }

    @Override
    public void openProfileCallback(String username, String posterId) {
        //Bundle args = new Bundle();
        //args.putString("ARG_USERNAME", username);
        Fragment profileFragment = ProfileFragment.newInstance(username, posterId);
        //profileFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, profileFragment).commit();
    }
}