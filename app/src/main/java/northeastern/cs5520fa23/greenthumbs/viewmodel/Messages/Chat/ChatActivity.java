package northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;

public class ChatActivity extends AppCompatActivity {
    private final int SENT_MESSAGE_TYPE = 0;
    private final int RECEIVED_MESSAGE_TYPE = 1;
    private RecyclerView msgRecyclerView;
    private MessageAdapter msgAdapter;
    private List<ChatMessage> msgList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        msgList = new ArrayList<>();
        msgRecyclerView = findViewById(R.id.chat_recycler_view);
        msgRecyclerView.setHasFixedSize(true);
        msgRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        msgAdapter = new MessageAdapter(msgList, this);
        msgRecyclerView.setAdapter(msgAdapter);
    }
}