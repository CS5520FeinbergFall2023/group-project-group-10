package northeastern.cs5520fa23.greenthumbs.viewmodel.Messages;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import northeastern.cs5520fa23.greenthumbs.R;

public class MessageHistoryViewHolder extends RecyclerView.ViewHolder {
    private TextView other_username;
    private TextView last_message;
    private ImageView profPic;

    public MessageHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        this.other_username = itemView.findViewById(R.id.message_history_username);
        this.last_message = itemView.findViewById(R.id.message_history_last_message);
        this.profPic = itemView.findViewById(R.id.msg_history_profile_pic);
    }

    public TextView getOther_username() {
        return other_username;
    }

    public void setOther_username(TextView other_username) {
        this.other_username = other_username;
    }

    public TextView getLast_message() {
        return last_message;
    }

    public void setLast_message(TextView last_message) {
        this.last_message = last_message;
    }

    public ImageView getProfPic() {
        return profPic;
    }

    public void setProfPic(ImageView profPic) {
        this.profPic = profPic;
    }


}
