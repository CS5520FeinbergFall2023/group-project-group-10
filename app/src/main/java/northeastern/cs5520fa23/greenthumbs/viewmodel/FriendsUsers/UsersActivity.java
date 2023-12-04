package northeastern.cs5520fa23.greenthumbs.viewmodel.FriendsUsers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Profile.Friend;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.ImgPost;
import northeastern.cs5520fa23.greenthumbs.viewmodel.User;

public class UsersActivity extends AppCompatActivity {
    private SearchView userSearch;
    private RecyclerView userRV;
    private TextView titleText;
    private List<Friend> friendObjList;
    private List<User> friendList;
    private List<User> usersList;
    private FirebaseUser currUser;
    private FirebaseDatabase db;
    private RecyclerView friendRV;
    private FriendsAdapter friendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        this.friendRV = findViewById(R.id.users_activity_rv);
        this.friendRV.setHasFixedSize(true);
        this.friendRV.setLayoutManager(new LinearLayoutManager(this));
        this.currUser = FirebaseAuth.getInstance().getCurrentUser();
        this.db = FirebaseDatabase.getInstance();
        this.friendList = new ArrayList<>();
        this.usersList = new ArrayList<>();
        this.friendObjList = new ArrayList<>();
        this.friendsAdapter = new FriendsAdapter(friendObjList, this);
        this.friendRV.setAdapter(friendsAdapter);
        this.userSearch = findViewById(R.id.users_activity_search);
        this.userRV = findViewById(R.id.users_activity_rv);
        this.titleText = findViewById(R.id.friends_title);
        this.userSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 5) {
                    filterUsers(newText);
                }
                return true;
            }

        });

        loadUsers();
    }

    private void loadUsers() {
        DatabaseReference usersRef = db.getReference("users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User currUser = dataSnapshot.getValue(User.class);
                    //if (friend_ids.contains(currPost.getUid())) {
                    usersList.add(currUser);
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
        friendRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    friendObjList.add(dataSnapshot.getValue(Friend.class));
                }
                friendsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filterUsers(String userQuery) {

    }
}