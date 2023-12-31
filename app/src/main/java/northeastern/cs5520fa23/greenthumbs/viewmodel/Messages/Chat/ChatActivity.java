package northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.Chat;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import northeastern.cs5520fa23.greenthumbs.MainActivity;
import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialPostDetails.SocialPostDetailsActivity;
import northeastern.cs5520fa23.greenthumbs.viewmodel.User;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView msgRecyclerView;
    private MessageAdapter msgAdapter;
    private List<ChatMessage> msgList;
    private EditText msgInput;
    private ImageButton sendMsgButton;
    private TextView otherUsernameHeader;
    private FirebaseDatabase db;
    private FirebaseUser currUser;
    private String otherUserID;
    private String currUsername;
    private String otherUsername;
    private String chatID;
    private Bundle extras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.db = FirebaseDatabase.getInstance();
        this.currUser = FirebaseAuth.getInstance().getCurrentUser();
        extras = getIntent().getExtras();
        this.otherUsername = extras.getString("other_username");
        Log.d("OTHER_UN", this.otherUsername + "!!!!");
        //Log.d("OTHER_UN", gettypethis.otherUsername + "!!!!");

        //setChatID(otherUsername);
        getUsersInfo();

        //Toast.makeText(ChatActivity.this, otherUserID, Toast.LENGTH_LONG).show();


        msgList = new ArrayList<>();
        msgInput = findViewById(R.id.send_msg_input);
        sendMsgButton = findViewById(R.id.send_msg_button);
        otherUsernameHeader = findViewById(R.id.chat_activity_header_username);
        otherUsernameHeader.setText(this.otherUsername);
        if (savedInstanceState != null) {
            msgInput.setText(savedInstanceState.getString("messageInput", ""));
        }
        sendMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg();
            }
        });
        msgRecyclerView = findViewById(R.id.chat_recycler_view);
        msgRecyclerView.setHasFixedSize(true);
        msgRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        msgAdapter = new MessageAdapter(msgList, this);
        msgAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        msgRecyclerView.setAdapter(msgAdapter);


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("messageInput", msgInput.getText().toString());

    }


    private void getMessages() {
        setChatID(otherUsername);
        Log.d("Chat_ID", chatID);
        DatabaseReference msgRef = db.getReference("chats").child(chatID).child("messages");
        msgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    msgList.clear();
                    for (DataSnapshot msgSnapshot : snapshot.getChildren()) {
                        ChatMessage msg = msgSnapshot.getValue(ChatMessage.class);
                        if (msg.getSenderId().equals(currUser.getUid())) {
                            msg.setViewType(1);
                        } else {
                            msg.setViewType(0);
                        }
                        msgList.add(msg);
                    }
                    msgAdapter.notifyDataSetChanged();
                    msgRecyclerView.smoothScrollToPosition(msgList.size());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MessagesError", "Failed to get messages.", error.toException());
            }
        });
    }
    private void setChatID (String u2) {
        if (compareUsernames(currUsername, u2) < 0) {
            this.chatID = currUsername + "_" + u2;
        } else {
            this.chatID = otherUsername + "_" + currUsername;
        }
    }
    private void sendMsg() {

        String messageContent = msgInput.getText().toString();
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("messageContent", messageContent);
        msgMap.put("chatId", chatID);
        msgMap.put("senderId", currUser.getUid());
        msgMap.put("userId", currUser.getUid());
        msgMap.put("receiverId", otherUserID);
        //String currentTimestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(new Date());
        SimpleDateFormat currentTimestampSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        currentTimestampSDF.setTimeZone(TimeZone.getTimeZone("EST"));
        String currentTimestamp = currentTimestampSDF.format(new Date());
        msgMap.put("timestamp", currentTimestamp);

        uploadMessage(msgMap);
    }

    private void uploadMessage(Map<String, Object> msgMap) {
        DatabaseReference msgRef = db.getReference("chats").child(chatID).child("messages");
        msgRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(ChatActivity.this, "Unable to fetch messages", Toast.LENGTH_LONG);
                } else {
                    String msgId = msgRef.push().getKey();
                    msgRef.child(msgId).setValue(msgMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(ChatActivity.this, "Unable to upload chat", Toast.LENGTH_LONG);
                            } else {
                                msgInput.setText("");
                                hideKeyboard();
                                updateUserChats(msgMap);
                                //msgRecyclerView.smoothScrollToPosition(msgList.size());
                            }
                        }
                    });
                }
            }
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private int compareUsernames(String u1, String u2) {
        return u1.compareToIgnoreCase(u2);
    }

    private void getUsersInfo() {
        db.getReference().child("users").child(currUser.getUid()).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(ChatActivity.this, "Unable to get username", Toast.LENGTH_SHORT).show();
                } else {
                    currUsername = String.valueOf(task.getResult().getValue());
                    //Toast.makeText(ChatActivity.this, currUsername, Toast.LENGTH_SHORT).show();

                    DatabaseReference dbRef = db.getReference("users");
                    dbRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            for (DataSnapshot userSnapshot: task.getResult().getChildren()) {
                                User other = userSnapshot.getValue(User.class);
                                Boolean t = other.getUsername().equals(otherUsername);
                                //Toast.makeText(ChatActivity.this, other.getUsername().toString() + " " + otherUsername.toString(), Toast.LENGTH_SHORT).show();

                                if (other.getUsername().contains(otherUsername)) {
                                    otherUserID = other.getUser_id();
                                    getMessages();
                                    Log.d("other_uid", otherUserID);



                                    //Toast.makeText(ChatActivity.this, otherUserID, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });




                }
            }
        });
    }

    private void updateUserChats(Map<String, Object> msgMap) {
        Map<String, Object> updatesOne = new HashMap<>();
        updatesOne.put("timestamp", msgMap.get("timestamp"));
        updatesOne.put("last_message", msgMap.get("messageContent"));
        updatesOne.put("other_username", otherUsername);

        db.getReference("users").child(currUser.getUid()).child("chats").child(otherUserID).updateChildren(updatesOne).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Map<String, Object> updatesTwo = new HashMap<>();
                updatesTwo.put("timestamp", msgMap.get("timestamp"));
                updatesTwo.put("last_message", msgMap.get("messageContent"));
                updatesTwo.put("other_username", currUsername);
                db.getReference("users").child(otherUserID).child("chats").child(currUser.getUid()).updateChildren(updatesTwo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //getMessages();
                        updateChatsHistory(msgMap);
                    }
                });
            }
        });

    }
    private void updateChatsHistory(Map<String, Object> msgMap) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("last_message", msgMap.get("messageContent"));
        updates.put("timestamp", msgMap.get("timestamp"));
        db.getReference("chats").child(chatID).updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                getMessages();
            }
        });
    }

}