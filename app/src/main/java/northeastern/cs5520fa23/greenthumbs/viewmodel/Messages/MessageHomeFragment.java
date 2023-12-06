package northeastern.cs5520fa23.greenthumbs.viewmodel.Messages;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import northeastern.cs5520fa23.greenthumbs.MainActivity;
import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.Chat.ChatActivity;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialAdapter;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialPostDetails.Comment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.User;

public class MessageHomeFragment extends Fragment implements MessageHistoryAdapter.MessageHistoryCallback {
    private RecyclerView msgHistoryRV;
    private MessageHistoryAdapter msgHistoryAdapter;
    private List<MessageHistoryItem> chats;
    //private FloatingActionButton fab;
    private DatabaseReference userChatRef;
    private List<String> otherUsers;
    private SearchView msgUserSearch;
    private ImageButton msgUserButton;
    private HashMap<String, String> nameIdMap;

    public MessageHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chats = new ArrayList<>();
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        String currUsername = currUser.getUid();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference allChatRef = db.getReference().child("chats");
        userChatRef = db.getReference().child("users").child(currUsername).child("chats");
        otherUsers = new ArrayList<>();
        nameIdMap = new HashMap<>();
        populateOtherUsers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView msgHistoryRV = view.findViewById(R.id.message_recycler_view);
        msgHistoryRV.setHasFixedSize(true);
        msgHistoryRV.setLayoutManager(new LinearLayoutManager(getContext()));
        msgHistoryAdapter = new MessageHistoryAdapter(chats, getContext(), this, nameIdMap);
        msgHistoryRV.setAdapter(msgHistoryAdapter);
        //fab = view.findViewById(R.id.post_fab);
        msgUserSearch = view.findViewById(R.id.msg_user_search);
        msgUserSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (otherUsers.contains(query)) {
                    openChatCallback(query);
                    return true;
                }
                else {
                    Toast.makeText(getActivity(), "No user found with that username!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        msgUserButton = view.findViewById(R.id.msg_history_start_chat);
        msgUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgUserSearch.setQuery(msgUserSearch.getQuery(), true);
            }
        });
        addChats();
        //msgHistoryAdapter.notifyItemInserted(0);
    }

    private void addChats() {
        //this.chats.add(new MessageHistoryItem("garden_star", "Isn't that plant great :O ?!"));
        userChatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    chats.clear();
                    for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                        MessageHistoryItem chat = chatSnapshot.getValue(MessageHistoryItem.class);
                        chats.add(chat);
                        msgHistoryAdapter.notifyDataSetChanged();
                        nameIdMap.put(chat.getOther_username(), chatSnapshot.getKey());
                    }
                    msgHistoryAdapter.notifyDataSetChanged();
                    //populateProfilePics(ids);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("CommentsError", "Failed to get comments.", error.toException());
            }
        });
    }


    private void populateOtherUsers() {
        DatabaseReference usersRef = db.getReference().child("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User currUser = dataSnapshot.getValue(User.class);
                    //if (friend_ids.contains(currPost.getUid())) {
                    otherUsers.add(currUser.getUsername());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void openChatCallback(String username) {
        Intent i = new Intent(getActivity(), ChatActivity.class);
        i.putExtra("other_username", username);
        getActivity().startActivity(i);
    }
}