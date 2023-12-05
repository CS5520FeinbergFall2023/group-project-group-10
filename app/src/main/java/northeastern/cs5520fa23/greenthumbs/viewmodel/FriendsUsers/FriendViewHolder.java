package northeastern.cs5520fa23.greenthumbs.viewmodel.FriendsUsers;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import northeastern.cs5520fa23.greenthumbs.R;

public class FriendViewHolder extends RecyclerView.ViewHolder {
    private ImageView profilePicture;
    private TextView usernameText;
    private ImageButton addFriendButton;
    private TextView isFriendText;
    private ImageButton msgButton;
    private String friendUserId;
    private String friendUsername;

    public FriendViewHolder(@NonNull View itemView) {
        super(itemView);
        this.profilePicture = itemView.findViewById(R.id.user_card_prof_pic);
        this.usernameText = itemView.findViewById(R.id.user_activity_username_text);

        this.addFriendButton = itemView.findViewById(R.id.user_activity_add_friend);
        // set on click listener

        this.isFriendText = itemView.findViewById(R.id.user_activity_is_friend_text);

        this.msgButton = itemView.findViewById(R.id.user_acitivity_msg_button);
        // set on click listener
    }

    public ImageView getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(ImageView profilePicture) {
        this.profilePicture = profilePicture;
    }

    public TextView getUsernameText() {
        return usernameText;
    }

    public void setUsernameText(TextView usernameText) {
        this.usernameText = usernameText;
    }

    public ImageButton getAddFriendButton() {
        return addFriendButton;
    }

    public void setAddFriendButton(ImageButton addFriendButton) {
        this.addFriendButton = addFriendButton;
    }

    public TextView getIsFriendText() {
        return isFriendText;
    }

    public void setIsFriendText(TextView isFriendText) {
        this.isFriendText = isFriendText;
    }

    public ImageButton getMsgButton() {
        return msgButton;
    }

    public void setMsgButton(ImageButton msgButton) {
        this.msgButton = msgButton;
    }

    public String getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(String friendUserId) {
        this.friendUserId = friendUserId;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }
}
