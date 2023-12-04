package northeastern.cs5520fa23.greenthumbs.viewmodel.FriendsUsers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Profile.Friend;
import northeastern.cs5520fa23.greenthumbs.viewmodel.User;

public class UsersActivity extends AppCompatActivity {
    private SearchView userSearch;
    private RecyclerView userRV;
    private List<Friend> friendObjList;
    private List<User> friendList;
    private List<User> usersList;
    private FirebaseUser currUser;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        this.currUser = FirebaseAuth.getInstance().getCurrentUser();
        this.db = FirebaseDatabase.getInstance();
        this.friendList = new ArrayList<>();
        this.usersList = new ArrayList<>();
        this.friendObjList = new ArrayList<>();
        this.userSearch = findViewById(R.id.users_activity_search);
        this.userRV = findViewById(R.id.users_activity_rv);
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

        loadFriends();
    }

    private void loadFriends() {
        
    }

    private void filterUsers(String userQuery) {

    }
}