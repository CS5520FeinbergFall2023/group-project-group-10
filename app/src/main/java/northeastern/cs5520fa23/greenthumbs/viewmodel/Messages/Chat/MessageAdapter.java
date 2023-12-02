package northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;


public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private List<ChatMessage> messages;
    private Context context;

    public MessageAdapter(List<ChatMessage> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getViewType() == ChatMessage.Sent_View) {
            return 1;
        } else {
            return 0;
        }
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new SentMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.sent_message, null));
        } else {
            return new SentMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.received_message, null));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage msg = messages.get(position);
        if (msg != null) {
            String timestamp = msg.getTimestamp();
            String msgContent = msg.getMessageContent();

            if (timestamp != null) {
                holder.getTimestamp().setText(timestamp);
            }

            if (msgContent != null) {
                holder.getMessageContent().setText(msgContent);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
