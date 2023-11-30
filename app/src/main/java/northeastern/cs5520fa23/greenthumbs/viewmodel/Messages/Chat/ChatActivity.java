package northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;

public class ChatActivity extends AppCompatActivity {
    private final int SENT_MESSAGE_TYPE = 0;
    private final int RECEIVED_MESSAGE_TYPE = 1;
    private RecyclerView msgRecyclerView;
    private MessageAdapter msgAdapter;
    private List<ChatMessage> msgList;
    EditText msgInput;
    ImageButton sendMsgButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        msgList = new ArrayList<>();
        msgInput = findViewById(R.id.send_msg_input);
        sendMsgButton = findViewById(R.id.send_msg_button);
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
        msgRecyclerView.setAdapter(msgAdapter);
    }

    private void sendMsg() {
        String msgText = msgInput.getText().toString();
        return;
    }
}