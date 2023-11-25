package northeastern.cs5520fa23.greenthumbs.viewmodel.Messages;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import northeastern.cs5520fa23.greenthumbs.R;

public class MessageHistoryViewHolder extends RecyclerView.ViewHolder {
    private TextView username;
    private TextView lastMsg;
    private ImageView profPic;

    public MessageHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        this.username = itemView.findViewById(R.id.message_history_username);
        this.lastMsg = itemView.findViewById(R.id.message_history_last_message);
        this.profPic = itemView.findViewById(R.id.msg_history_profile_pic);
    }

    public TextView getUsername() {
        return username;
    }

    public void setUsername(TextView username) {
        this.username = username;
    }

    public TextView getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(TextView lastMsg) {
        this.lastMsg = lastMsg;
    }

    public ImageView getProfPic() {
        return profPic;
    }

    public void setProfPic(ImageView profPic) {
        this.profPic = profPic;
    }

}
