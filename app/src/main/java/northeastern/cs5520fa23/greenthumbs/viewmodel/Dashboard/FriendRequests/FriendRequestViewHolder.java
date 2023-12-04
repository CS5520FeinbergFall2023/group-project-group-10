package northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.FriendRequests;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import northeastern.cs5520fa23.greenthumbs.R;


public class FriendRequestViewHolder extends RecyclerView.ViewHolder {
    private TextView requestUsername;
    private Button acceptButton;
    private Button denyButton;
    public FriendRequestViewHolder(@NonNull View itemView) {
        super(itemView);
        this.denyButton = itemView.findViewById(R.id.friend_request_deny_button);
        this.acceptButton = itemView.findViewById(R.id.friend_request_accept_button);
        this.requestUsername = itemView.findViewById(R.id.friend_request_username);
    }

    public TextView getRequestUsername() {
        return requestUsername;
    }

    public void setRequestUsername(TextView requestUsername) {
        this.requestUsername = requestUsername;
    }

    public Button getAcceptButton() {
        return acceptButton;
    }

    public void setAcceptButton(Button acceptButton) {
        this.acceptButton = acceptButton;
    }

    public Button getDenyButton() {
        return denyButton;
    }

    public void setDenyButton(Button denyButton) {
        this.denyButton = denyButton;
    }
}

