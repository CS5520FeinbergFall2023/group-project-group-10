package northeastern.cs5520fa23.greenthumbs.viewmodel.Messages;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.Chat.ChatActivity;

public class MessageHistoryAdapter extends RecyclerView.Adapter<MessageHistoryViewHolder> {
    List<MessageHistoryItem> chats;
    Context context;
    public MessageHistoryAdapter(List<MessageHistoryItem> chats, Context context) {
        this.chats = chats;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.message_history_card, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHistoryViewHolder holder, int position) {
        MessageHistoryItem chat = chats.get(position);
        holder.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ChatActivity.class);
                context.startActivity(i);
            }
        });
        if (chat != null) {
            String username = chat.getUsername();
            String lastMessage = chat.getLastMessage();
            if (username != null) {
                holder.getUsername().setText(username);
            }
            if (lastMessage != null) {
                holder.getLastMsg().setText(lastMessage);
            }

        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}
