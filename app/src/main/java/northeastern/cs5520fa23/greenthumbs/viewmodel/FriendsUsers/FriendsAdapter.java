package northeastern.cs5520fa23.greenthumbs.viewmodel.FriendsUsers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Profile.Friend;
import northeastern.cs5520fa23.greenthumbs.viewmodel.User;

public class FriendsAdapter extends RecyclerView.Adapter<FriendViewHolder> {
    private List<User> userList;
    private Context context;
    private List<Friend> friendList;
    private Map<String, Friend> friendIds;
    private String currUid;

    public FriendsAdapter(List<User> userList, Context context, List<Friend> friendList) {
        this.currUid = FirebaseAuth.getInstance().getUid();
        this.userList = userList;
        this.context = context;
        this.friendList = friendList;
        this.friendIds = new HashMap<>();
        for (Friend f : friendList) {
            friendIds.put(f.getFriend_id(), f);
        }

    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Friend f = new Friend(parent.getContext());
        return new FriendViewHolder(LayoutInflater.from(context).inflate(R.layout.user_activity_friend_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        try {
            User f = userList.get(position);
            if (f != null) {
                String fName = f.getUsername();
                String fId = f.getUser_id();
                //String status = f.get();
                if (fName != null) {
                    holder.setFriendUsername(fName);
                    holder.getUsernameText().setText(fName);
                }
                if (fId != null) {
                    holder.setFriendUserId(fId);
                }
                if (fId.equals(currUid)) {
                    holder.getIsFriendText().setText("YOU");
                    holder.getAddFriendButton().setImageResource(R.drawable.baseline_check_24);

                }
                if (friendIds.containsKey(fId)) {
                    Friend friend = friendIds.get(fId);
                    if (friend.getStatus() != null) {
                        holder.getIsFriendText().setText(friend.getStatus());
                    }
                    if (friend.getStatus().toLowerCase().contains("friends")) {
                        holder.getAddFriendButton().setImageResource(R.drawable.baseline_check_24);
                    }
                }

            }
        } catch (Exception e) {

        }

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
