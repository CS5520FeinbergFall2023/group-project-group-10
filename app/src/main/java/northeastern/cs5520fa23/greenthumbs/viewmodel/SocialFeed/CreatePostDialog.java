package northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import northeastern.cs5520fa23.greenthumbs.R;

public class CreatePostDialog extends DialogFragment {
    Button postButton;
    Button cancelButton;
    private Button addImgButton;
    private ImageView postImage;
    boolean hasImage;
    private TextView postText;
    private TextView postTags;
    private ActivityResultLauncher<PickVisualMediaRequest> imgSelect;
    private FirebaseDatabase db;
    private Map<String, Object> post;
    FirebaseUser currUser;
    DatabaseReference dbRef;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.fragment_create_post, null);
        hasImage = false;
        post = new HashMap<>();
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();
        //fsDB = FirebaseFirestore.getInstance();
        postButton = view.findViewById(R.id.create_post_button);
        postText = view.findViewById(R.id.create_post_text);
        postTags = view.findViewById(R.id.create_post_tags);
        cancelButton = view.findViewById(R.id.cancel_create_post_button);
        addImgButton = view.findViewById(R.id.create_post_add_image_button);
        postImage = view.findViewById(R.id.create_post_image);
        imgSelect = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                postImage.setImageURI(uri);
                postImage.setVisibility(View.VISIBLE);
                hasImage = true;
            } else {
                // something here like a toast or something
            }
        });
        postImage.setVisibility(View.GONE);
        addImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImg();
            }
        });
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String postId = dbRef.child("posts").push().getKey();
                String currentTimestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(new Date());
                post.put("num_likes", 0);
                post.put("timestamp", currentTimestamp);
                post.put("num_comments", 0);
                post.put("tags", postTags.getText().toString());
                post.put("post_text", postText.getText().toString());
                post.put("uid", currUser.getUid().toString());
                dbRef.child("users").child(currUser.getUid()).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Unable to fetch data", Toast.LENGTH_LONG);
                        } else {
                            post.put("username", String.valueOf(task.getResult().getValue()));
                            uploadPost(postId);
                        }
                    }
                });
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePostDialog.this.getDialog().cancel();
            }
        });
        builder.setView(view);
        return builder.create();
    }

    private void getImg() {
        imgSelect.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
    }

    private void uploadPost(String postId) {
        if (hasImage) {

            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            String imgPath = "posts/post_id_" + postId;
            StorageReference postStorageRef = storageReference.child(imgPath);
            Bitmap bitmap = ((BitmapDrawable) postImage.getDrawable()).getBitmap();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] data = outputStream.toByteArray();


            UploadTask uploadTask = postStorageRef.putBytes(data); // upload then on the upload get the download link for the post
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            post.put("has_img", true);
                            post.put("img_uri", uri.toString());
                            dbRef.child("posts").child(postId).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    CreatePostDialog.this.getDialog().cancel();
                                }
                            });
                        }
                    });

                }
            });

        }
        else {
            post.put("has_img", false);
            post.put("img_uri", null);
            dbRef.child("posts").child(postId).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    CreatePostDialog.this.getDialog().cancel();
                }
            });
        }
    }
}
