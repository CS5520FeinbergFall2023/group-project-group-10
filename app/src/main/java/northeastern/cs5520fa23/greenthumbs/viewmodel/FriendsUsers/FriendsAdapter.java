package northeastern.cs5520fa23.greenthumbs.viewmodel.FriendsUsers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Profile.Friend;

public class FriendsAdapter extends RecyclerView.Adapter<FriendViewHolder> {
    private List<Friend> friendList;
    private Context context;

    public FriendsAdapter(List<Friend> friendList, Context context) {
        this.friendList = friendList;
        this.context = context;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Friend f = new Friend(parent.getContext());
        return new FriendViewHolder(LayoutInflater.from(context).inflate(R.layout.user_activity_friend_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend f = friendList.get(position);
        if (f != null) {
            String fName = f.getFriend_username();
            String fId = f.getFriend_id();
            String status = f.getStatus();
            if (fName != null) {
                holder.setFriendUsername(fName);
                holder.getUsernameText().setText(fName);
            }
            if (fId != null) {
                holder.setFriendUserId(fId);
            }
            if (status != null) {
                holder.getIsFriendText().setText(status);
                if (status.toLowerCase().contains("friends")) {
                    holder.getAddFriendButton().setImageResource(R.drawable.baseline_check_24);
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }
}
