package northeastern.cs5520fa23.greenthumbs.viewmodel.Messages;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;

public class MessageHomeFragment extends Fragment {
    private MessageHistoryAdapter msgHistoryAdapter;
    private List<MessageHistoryItem> chats;

    private DatabaseReference userChatRef;
    private List<String> otherUsers;

    public MessageHomeFragment() {
        // Required empty public constructor
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
        msgHistoryAdapter = new MessageHistoryAdapter(chats, getContext());
        msgHistoryRV.setAdapter(msgHistoryAdapter);
        FloatingActionButton fab = view.findViewById(R.id.post_fab);

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
                    }
                    msgHistoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("CommentsError", "Failed to get comments.", error.toException());
            }
        });
    }

    private void populateOtherUsers() {

    }
}