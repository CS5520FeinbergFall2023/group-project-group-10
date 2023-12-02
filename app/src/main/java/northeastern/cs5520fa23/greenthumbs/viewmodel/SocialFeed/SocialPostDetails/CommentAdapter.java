package northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialPostDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
   private List<Comment> commentList;
   private Context context;

    public CommentAdapter(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(context).inflate(R.layout.comment_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        if (comment != null) {
            String username = comment.getUsername();
            String commentContent = comment.getComment_text();
            Integer numLikes = comment.getComment_likes();
            if (username != null) {
                holder.getCommentUsername().setText(username);
            }
            if (commentContent != null) {
                holder.getCommentContent().setText(commentContent);
            }
            if (numLikes != null) {
                //holder.getNumLikes().setText(numLikes);
            }
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
