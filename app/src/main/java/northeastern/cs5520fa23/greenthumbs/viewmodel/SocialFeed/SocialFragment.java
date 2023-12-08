package northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed;

import static android.content.ContentValues.TAG;
import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import northeastern.cs5520fa23.greenthumbs.MainActivity;
import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.model.SocialAlgo;
import northeastern.cs5520fa23.greenthumbs.viewmodel.FriendsUsers.UsersActivity;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.Chat.ChatActivity;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Profile.Friend;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Profile.ProfileActivity;
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView socialRecyclerView;
    private SocialAdapter socialAdapter;
    private List<ImgPost> postList;
    private List<ImgPost> originalPosts;
    private FloatingActionButton fab;
    private Switch friendsSwitch;
    private SearchView userSearch;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView usersIcon;
    private FirebaseUser currUser;
    private List<String> friendIds;
    private HashMap<String, Integer> numPlants;
    private List<ImgPost> friendPosts;

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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_social, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.postList = new ArrayList<>();
        this.originalPosts = new ArrayList<>();
        this.friendIds = new ArrayList<>();
        this.numPlants = new HashMap<>();
        this.friendPosts = new ArrayList<>();

        currUser = FirebaseAuth.getInstance().getCurrentUser();
        this.usersIcon = view.findViewById(R.id.social_search_icon);
        this.usersIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), UsersActivity.class);
                getActivity().startActivity(i);
            }
        });
        this.userSearch = view.findViewById(R.id.social_search_view);
        this.userSearch.setQueryHint("Filter by user/content");
        this.userSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (friendsSwitch.isChecked()) {
                    filterPosts(newText, true);
                    return true;
                } else {
                    filterPosts(newText, false);
                    return true;
                }
            }

        });
        this.friendsSwitch = view.findViewById(R.id.friends_switch);
        this.swipeRefreshLayout = view.findViewById(R.id.social_swipe_refresh);
        this.swipeRefreshLayout.setOnRefreshListener(() -> {
            if (friendsSwitch.isChecked()) {
                addPosts(true);
            } else {
                addPosts(false);
            }
        });
        socialRecyclerView = view.findViewById(R.id.social_recycler_view);
        socialRecyclerView.setHasFixedSize(true);
        socialRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        socialAdapter = new SocialAdapter(postList, getContext(), this);
        socialRecyclerView.setAdapter(socialAdapter);
        fab = view.findViewById(R.id.post_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreatePostDialog();
            }
        });
        addPosts(false);
        friendsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //addPosts();
                if (isChecked) {
                    filterFriendsPosts();
                    Log.d("PoST FILTER SIZE", "" + socialAdapter.getItemCount());
                } else {
                    filterAllPosts();
                }
                // change to filter friends posts
            }
        });
    }

    /*
    private void addPosts() {
        postList.clear();
        originalPosts.clear();
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
                            friendIds.add(user_friend.getFriend_id());
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
                                    originalPosts.add(currPost);
                                    socialAdapter.notifyDataSetChanged();
                                }
                            }

                            Collections.reverse(postList);
                            Collections.reverse(originalPosts);
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
                        originalPosts.add(currPost);
                        socialAdapter.notifyDataSetChanged();

                    }

                    Collections.reverse(postList);
                    Collections.reverse(originalPosts);
                    socialAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

     */

    private void addPosts(boolean refreshOnFriends) {
        postList.clear();
        originalPosts.clear();
        friendIds.clear();
        socialAdapter.notifyDataSetChanged();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("posts");

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
                        friendIds.add(user_friend.getFriend_id());
                    }
                }
                Query query = dbRef.orderByChild("timestamp").limitToLast(100);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ImgPost currPost = dataSnapshot.getValue(ImgPost.class);
                                postList.add(currPost);
                                originalPosts.add(currPost);

                        }
                        getUserTopPlants(refreshOnFriends);
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

    }

    private void openCreatePostDialog() {
        DialogFragment createPostDialog = new CreatePostDialog();
        createPostDialog.show(getActivity().getSupportFragmentManager(), "Post");
    }

    @Override
    public void openProfileCallback(String username, String posterId) {
        //Fragment profileFragment = ProfileFragment.newInstance(username, posterId);
        //getActivity().getSupportFragmentManager().beginTransaction().replace(this.getId(), profileFragment).addToBackStack(null).commit();
        Intent i = new Intent(getContext(), ProfileActivity.class);
        Bundle extras = new Bundle();
        ArrayList<String> userInfo = new ArrayList<>();
        userInfo.add(username);
        userInfo.add(posterId);
        extras.putStringArrayList("user_info", userInfo);
        i.putExtra("profile_info", extras);
        this.startActivity(i);
    }

    @Override
    public void addLikeCallback(ImgPost post) {
        String postId = post.get_id();
        if (!post.isLiked()) {
            Like newLike = new Like(currUser.getUid(), currUser.getUid());
            FirebaseDatabase.getInstance().getReference("posts").child(postId).child("likes").child(currUser.getUid()).setValue(newLike).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getContext(), "Unable to add like", Toast.LENGTH_LONG).show();
                    } else {
                        post.setLiked(true);
                        post.setNum_likes(post.getNum_likes() + 1);
                    }
                }
            });
        }
        /*
        FirebaseDatabase.getInstance().getReference("posts").child(postId).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                ImgPost updatePost = currentData.getValue(ImgPost.class);
                if (updatePost == null) {
                    return Transaction.success(currentData);
                }
                updatePost.setNum_likes(updatePost.getNum_likes() + 1);
                currentData.setValue(updatePost);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

            }
        });

         */
    }

    private boolean inFilter(ImgPost post, String filterQuery) {
        if (post.getPost_text().toLowerCase().contains(filterQuery.toLowerCase())) {
            return true;
        } else if (post.getUsername().toLowerCase().contains(filterQuery.toLowerCase())) {
            return true;
        } else if (post.getTags().toLowerCase().contains(filterQuery.toLowerCase())) { // fix tags
            return true;
        } else {
            return false;
        }
    }
    private void filterPosts(String filterQuery, boolean friendsOnly) {

        List<ImgPost> filteredPosts = new ArrayList<>();

        for (ImgPost post : originalPosts) {
            if (inFilter(post, filterQuery) == true) {
                if (friendsOnly) {
                    if (friendIds.contains(post.getUid())) {
                        filteredPosts.add(post);
                    }
                } else {
                    filteredPosts.add(post);

                }
            }
        }
        socialAdapter.setPosts(filteredPosts);
    }

    private void filterFriendsPosts() {
        List<ImgPost> filteredPosts = new ArrayList<>();
        for (int i = postList.size() - 1; i >= 0; i--) {
            if (friendIds.contains(postList.get(i).getUid())) {
                filteredPosts.add(postList.get(i));
                Log.d("FILTER_FRIENDS", postList.get(i).getUid());
            }
        }
        Collections.reverse(filteredPosts);
        socialAdapter.setPosts(filteredPosts);
    }
    private void filterAllPosts() {
        socialAdapter.setPosts(originalPosts);
    }

    private void getUserTopPlants(boolean refreshOnFriends) {
        DatabaseReference plantsRef = FirebaseDatabase.getInstance().getReference("users").child(currUser.getUid()).child("plants");
        plantsRef.child("growing").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot plant: snapshot.getChildren()) {
                    numPlants.put(plant.getKey(), Math.toIntExact(plant.getChildrenCount()));
                    Log.d("plant", plant.getKey() + ":" + Math.toIntExact(plant.getChildrenCount()));
                }

                //Collections.reverse(postList); dont need to reverse when using algo
                //Collections.reverse(originalPosts);
                Log.d("plants", numPlants.keySet().contains("tomato") + " ");
                List<ImgPost> sortedPosts = SocialAlgo.sortPostAlgo(postList, numPlants);
                originalPosts = SocialAlgo.sortPostAlgo(postList, numPlants);
                //socialAdapter.notifyDataSetChanged();
                //if (refreshOnFriends) {filterFriendsPosts();}
                postList = sortedPosts;
                socialAdapter.setPosts(postList);
                for (ImgPost currPost : postList) {
                    if (friendIds.contains(currPost.getUid())) {
                        friendPosts.add(currPost);
                    }
                }
                if (refreshOnFriends) {
                    filterFriendsPosts();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}