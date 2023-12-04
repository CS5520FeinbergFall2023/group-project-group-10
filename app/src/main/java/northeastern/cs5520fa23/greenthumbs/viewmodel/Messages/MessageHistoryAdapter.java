package northeastern.cs5520fa23.greenthumbs.viewmodel.Messages;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.Chat.ChatActivity;

public class MessageHistoryAdapter extends RecyclerView.Adapter<MessageHistoryViewHolder> {
    private List<MessageHistoryItem> chats;
    private Context context;
    public interface MessageHistoryCallback {
        void openChatCallback(String username);
    }
    private MessageHistoryCallback messageHistoryCallback;
    public MessageHistoryAdapter(List<MessageHistoryItem> chats, Context context, MessageHistoryCallback messageHistoryCallback) {
        this.chats = chats;
        this.context = context;
        this.messageHistoryCallback = messageHistoryCallback;
    }

    @NonNull
    @Override
    public MessageHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_history_card, parent, false);
        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ChatActivity.class);
                context.startActivity(i);
            }
        });

         */
        return new MessageHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHistoryViewHolder holder, int position) {
        MessageHistoryItem chat = chats.get(position);

        if (chat != null) {
            String username = chat.getOther_username();
            Toast.makeText(context, username, Toast.LENGTH_SHORT).show();
            String lastMessage = chat.getLast_message();
            if (username != null) {
                holder.getOther_username().setText(username);
            }
            if (lastMessage != null) {
                holder.getLast_message().setText(lastMessage);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent i = new Intent(context, ChatActivity.class);
                    i.putExtra("other_username", username);
                    context.startActivity(i);

                     */
                    Log.d("OTHER_UN", username + "!!!!");
                    messageHistoryCallback.openChatCallback(username);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}
