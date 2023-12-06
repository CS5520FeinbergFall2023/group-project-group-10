package northeastern.cs5520fa23.greenthumbs.viewmodel.Messages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.Map;
import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.Chat.ChatActivity;

public class MessageHistoryAdapter extends RecyclerView.Adapter<MessageHistoryViewHolder> {
    private List<MessageHistoryItem> chats;
    private Context context;
    private Map<String, String> nameIdMap;
    public interface MessageHistoryCallback {
        void openChatCallback(String username);
    }
    private MessageHistoryCallback messageHistoryCallback;
    public MessageHistoryAdapter(List<MessageHistoryItem> chats, Context context, MessageHistoryCallback messageHistoryCallback, Map nameIdMap) {
        this.chats = chats;
        this.context = context;
        this.messageHistoryCallback = messageHistoryCallback;
        this.nameIdMap = nameIdMap;
    }

    @NonNull
    @Override
    public MessageHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_history_card, null);
        view.setOnClickListener(v -> {
            Intent i = new Intent(context, ChatActivity.class);
            context.startActivity(i);
        return new MessageHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHistoryViewHolder holder, int position) {
        MessageHistoryItem chat = chats.get(position);

        if (chat != null) {
            String username = chat.getOther_username();
            String lastMessage = chat.getLast_message();
            if (username != null) {
                holder.getOther_username().setText(username);
            }
            if (lastMessage != null) {
                holder.getLast_message().setText(lastMessage);
            }
            if (nameIdMap.containsKey(username)) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference profPicRef = storage.getReference("profile_pics/"+nameIdMap.get(username));
                profPicRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (!task.isSuccessful()) {
                            Log.d("HAS_NO_PIC", profPicRef.toString());
                            //Picasso.get().load(R.drawable.baseline_tag_faces_24).resize(40, 40).centerCrop().into(holder.getPostProfPic());
                        } else {
                            Uri uri = task.getResult();
                            //Picasso.get().load(uri).resize(40, 40).centerCrop().into(holder.getPostProfPic());
                            Picasso.get().load(uri).resize(30, 30).into(holder.getProfPic(), new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap bitmap = ((BitmapDrawable) holder.getProfPic().getDrawable()).getBitmap();
                                    RoundedBitmapDrawable round = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
                                    round.setCircular(true);
                                    round.setCornerRadius(30/2.0f);
                                    holder.getProfPic().setImageDrawable(round);
                                }

                                @Override
                                public void onError(Exception e) {
                                    //holder.getPostProfPic().setImageResource(R.drawable.baseline_tag_faces_24);
                                }
                            });

                        }
                    }
                });
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent i = new Intent(context, ChatActivity.class);
                    i.putExtra("other_username", username);
                    context.startActivity(i);

                     */
                    Log.d("OTHER_UN", username + "!!!!");
                    messageHistoryCallback.openChatCallback(username);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}
