package northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.FriendRequests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import northeastern.cs5520fa23.greenthumbs.R;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestViewHolder> {
    private List<FriendRequest> friendRequests;
    private Context context;
    private FirebaseDatabase db;
    private FirebaseUser currUser;
    public interface FriendRequestCallback {
        void removeRequestCallback(int position);
    }
    private FriendRequestCallback friendRequestCallback;


    public FriendRequestAdapter(List<FriendRequest> friendRequests, Context context, FriendRequestCallback friendRequestCallback) {
        this.friendRequests = friendRequests;
        this.context = context;
        this.friendRequestCallback = friendRequestCallback;
        this.db = FirebaseDatabase.getInstance();
        this.currUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_request_layout, parent, false);
        return new FriendRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        FriendRequest fr = friendRequests.get(position);
        if (fr != null) {
            String approved = fr.getApproved();
            String fromUid = fr.getFrom_uid();
            String fromUsername = fr.getFrom_username();
            if (approved != null) {

            }
            if (fromUid != null) {

            }
            if (fromUsername != null) {
                holder.getRequestUsername().setText(fromUsername);
            }
            holder.getAcceptButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    approveRequest(fromUsername, fromUid, holder.getAdapterPosition());
                }
            });
            holder.getDenyButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    denyRequest(fromUid, holder.getAdapterPosition());
                }
            });
         }
    }
    private void denyRequest(String fromUid, int position) {
        DatabaseReference frRef = db.getReference("users").child(currUser.getUid()).child("friend_requests").child(fromUid);
        frRef.removeValue();
        DatabaseReference othersFriendRef = db.getReference("users").child(fromUid).child("friends").child(currUser.getUid());
        othersFriendRef.removeValue();
        friendRequestCallback.removeRequestCallback(position);
    }
    private void approveRequest(String fromUsername, String fromUid, int position) {
        DatabaseReference frRef = db.getReference("users").child(currUser.getUid()).child("friend_requests").child(fromUid);
        frRef.removeValue();
        DatabaseReference othersFriendRef = db.getReference("users").child(fromUid).child("friends").child(currUser.getUid());
        Map<String, Object> friendUpdate = new HashMap<>();
        friendUpdate.put("status", "friends");
        othersFriendRef.updateChildren(friendUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(context, "Unable to approve request", Toast.LENGTH_LONG).show();
                } else {
                    DatabaseReference fRef = db.getReference("users").child(currUser.getUid()).child("friends").child(fromUid);
                    Map<String, Object> update = new HashMap<>();
                    update.put("friend_id", fromUid);
                    update.put("friend_username", fromUsername);
                    update.put("status", "friends");
                    fRef.setValue(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(context, "Unable to add friend", Toast.LENGTH_LONG).show();
                            } else {
                                friendRequestCallback.removeRequestCallback(position);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendRequests.size();
    }
}
