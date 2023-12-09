package northeastern.cs5520fa23.greenthumbs.viewmodel.FriendsUsers;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import northeastern.cs5520fa23.greenthumbs.MainActivity;
import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.Chat.ChatActivity;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Profile.Friend;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Profile.ProfileActivity;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Profile.ProfileFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.ImgPost;
import northeastern.cs5520fa23.greenthumbs.viewmodel.User;

public class UsersActivity extends AppCompatActivity implements FriendsAdapter.ProfileCallback {
    private SearchView userSearch;
    private RecyclerView userRV;
    private TextView titleText;
    private List<Friend> friendObjList;
    private List<User> friendUserList;
    private List<String> friendIDList;
    private List<User> usersList;
    private List<User> originalUsers;
    private FirebaseUser currUser;
    private FirebaseDatabase db;
    private RecyclerView friendRV;
    private FriendsAdapter friendsAdapter;
    private List<User> originalFriendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        this.titleText = findViewById(R.id.friends_title);
        this.friendRV = findViewById(R.id.users_activity_rv);
        this.friendRV.setHasFixedSize(true);
        this.friendRV.setLayoutManager(new LinearLayoutManager(this));
        this.currUser = FirebaseAuth.getInstance().getCurrentUser();
        this.db = FirebaseDatabase.getInstance();
        this.friendIDList = new ArrayList<>();
        this.usersList = new ArrayList<>();
        this.originalUsers = new ArrayList<>();
        this.friendObjList = new ArrayList<>();
        this.friendUserList = new ArrayList<>();
        this.originalFriendsList = new ArrayList<>();
        this.friendsAdapter = new FriendsAdapter(friendUserList, this, friendObjList, this);
        this.friendRV.setAdapter(friendsAdapter);
        this.userSearch = findViewById(R.id.users_activity_search);
        this.userRV = findViewById(R.id.users_activity_rv);
        this.titleText = findViewById(R.id.friends_title);

        if (savedInstanceState != null) {
            String savedSearchQuery = savedInstanceState.getString("searchQuery", "");
            userSearch.setQuery(savedSearchQuery, false);
        }
        this.userSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                /*
                if (query.length() >= 5) {
                    filterUsers(query);
                } else {

                    friendUserList.clear();
                    friendsAdapter.notifyDataSetChanged();
                    friendUserList.addAll(originalFriendsList);
                    friendsAdapter.notifyDataSetChanged();
                }

                 */
                filterUsers(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 5) {
                    titleText.setText("Showing All Users");
                    filterUsers(newText);
                } else {
                    titleText.setText("Friends");
                    resetFriends();
                }
                return true;
            }

        });

        loadUsers();
    }

    private void loadUsers() {
        DatabaseReference usersRef = db.getReference().child("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User currUser = dataSnapshot.getValue(User.class);
                    //if (friend_ids.contains(currPost.getUid())) {
                    usersList.add(currUser);
                    originalUsers.add(currUser);
                }
                loadFriends();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void loadFriends() {
        DatabaseReference friendRef = FirebaseDatabase.getInstance().getReference("users").child(currUser.getUid()).child("friends");
        friendRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Friend f = dataSnapshot.getValue(Friend.class);
                    friendObjList.add(f);
                    friendIDList.add(f.getFriend_id());

                }
                for (User u : usersList) {
                    if (friendIDList.contains(u.getUser_id())) {
                        friendUserList.add(u);
                        originalFriendsList.add(u);
                        friendsAdapter.notifyDataSetChanged();
                    }
                }
                friendsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void filterUsers(String userQuery) {
        List<User> filteredUsers = new ArrayList<>();
        for (User u : originalUsers) {

            if (u.getUsername().contains(userQuery)) {
                filteredUsers.add(u);
            }
        }
        for (int i = friendsAdapter.getItemCount() - 1; i >= 0; i--) {
            friendUserList.remove(i);
            friendsAdapter.notifyItemRemoved(i);
        }
        for (int i = 0; i < filteredUsers.size(); i++) {
            friendUserList.add(filteredUsers.get(i));
            friendsAdapter.notifyItemInserted(i);
        }

        //friendUserList.clear();
        //friendsAdapter.notifyDataSetChanged();
        //friendUserList.addAll(filteredUsers);
        //friendsAdapter.notifyDataSetChanged();


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("searchQuery", userSearch.getQuery().toString());
    }

    private void resetFriends() {
        friendUserList.clear();
        friendsAdapter.notifyDataSetChanged();
        friendUserList.addAll(originalFriendsList);
        friendsAdapter.notifyDataSetChanged();
    }

    @Override
    public void openProfileCallback(String username, String userId) {

        /*
        Intent i = new Intent(this, MainActivity.class);
        Bundle extras = new Bundle();
        ArrayList<String> userInfo = new ArrayList<>();
        userInfo.add(username);
        userInfo.add(userId);
        extras.putStringArrayList("user_info", userInfo);
        i.putExtra("profile_info", extras);
        this.startActivity(i);

         */
        Intent i = new Intent(this, ProfileActivity.class);
        Bundle extras = new Bundle();
        ArrayList<String> userInfo = new ArrayList<>();
        userInfo.add(username);
        userInfo.add(userId);
        extras.putStringArrayList("user_info", userInfo);
        i.putExtra("profile_info", extras);
        this.startActivity(i);

    }

}