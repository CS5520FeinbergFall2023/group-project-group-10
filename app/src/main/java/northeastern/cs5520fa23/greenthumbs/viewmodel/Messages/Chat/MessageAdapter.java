package northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            return new SentMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.sent_message, parent, false));
        } else {
            return new SentMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.received_message, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage msg = messages.get(position);
        if (msg != null) {
            String timestampFull = msg.getTimestamp();
            String timestamp;
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(timestampFull);
                timestamp = new SimpleDateFormat("yyyy-MM-dd").format(date);

            } catch (Exception e) {
                timestamp = timestampFull;
            }

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

    @Override
    public long getItemId(int position) {
        return position;
    }
}
