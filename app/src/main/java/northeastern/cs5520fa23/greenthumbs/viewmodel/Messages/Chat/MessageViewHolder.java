package northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.Chat;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import northeastern.cs5520fa23.greenthumbs.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    private TextView timestamp;
    private TextView messageContent;
    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        timestamp = itemView.findViewById(R.id.msg_timestamp);
        messageContent = itemView.findViewById(R.id.msg_content);
    }

    public TextView getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(TextView timestamp) {
        this.timestamp = timestamp;
    }

    public TextView getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(TextView messageContent) {
        this.messageContent = messageContent;
    }
}
