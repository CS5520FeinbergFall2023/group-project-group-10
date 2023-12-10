package northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import northeastern.cs5520fa23.greenthumbs.R;

public class CreatePostDialog extends DialogFragment {
    Button postButton;
    Button cancelButton;
    private Button addImgButton;
    private ImageView postImage;

    private ImageView loadingImageView;


    boolean hasImage;
    private TextView postText;
    private TextView postTags;
    private ActivityResultLauncher<PickVisualMediaRequest> imgSelect;
    private FirebaseDatabase db;
    private Map<String, Object> post;
    public static final int REQUEST_GET_IMAGE = 1;
    FirebaseUser currUser;
    DatabaseReference dbRef;

    ProgressBar progress_bar;


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
        progress_bar = view.findViewById(R.id.progress_bar);
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
        ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == RESULT_OK) {
                    if (o.getData() != null) {
                        Intent data = o.getData();
                        if (data.getData() != null) {
                            Uri uri = data.getData();
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                                postImage.setImageBitmap(bitmap);
                                postImage.setVisibility(View.VISIBLE);
                                hasImage = true;
                            } catch (Exception e) {
                                Toast.makeText(getContext(), "Unable to get image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });

        postImage.setVisibility(View.GONE);
        addImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GET_IMAGE);
                    Toast.makeText(getContext(), "Need photo access", Toast.LENGTH_SHORT).show();
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryLauncher.launch(galleryIntent);
                }
            }
        });
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelButton.setEnabled(false);
                addImgButton.setEnabled(false);
                postButton.setEnabled(false);
                progress_bar.setVisibility(View.VISIBLE);

                String postId = dbRef.child("posts").push().getKey();
                SimpleDateFormat currentTimestampSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                currentTimestampSDF.setTimeZone(TimeZone.getTimeZone("EST"));
                String currentTimestamp = currentTimestampSDF.format(new Date());
                post.put("_id", postId);
                post.put("num_likes", 0);
                post.put("timestamp", currentTimestamp);
                post.put("num_comments", 0);
                post.put("tags", postTags.getText().toString());
                post.put("post_text", postText.getText().toString());
                post.put("uid", currUser.getUid().toString());
                post.put("geo_location", getCurrentLocation());
                if (post.get("_id") == null) {
                    Toast.makeText(getContext(), "Unable to create post", Toast.LENGTH_SHORT).show();
                    CreatePostDialog.this.getDialog().cancel();
                }
                dbRef.child("users").child(currUser.getUid()).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Unable to fetch data", Toast.LENGTH_LONG);
                            progress_bar.setVisibility(View.GONE);
                        } else {
                            post.put("username", String.valueOf(task.getResult().getValue()));
                            uploadPost(postId);
                            progress_bar.setVisibility(View.GONE);
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
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GET_IMAGE);
            Toast.makeText(getContext(), "Need photo access", Toast.LENGTH_SHORT).show();
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //galleryLauncher.launch(galleryIntent);
        }
        //imgSelect.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
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
                    Objects.requireNonNull(CreatePostDialog.this.getDialog()).cancel();
                }
            });
        }
    }

    private String getCurrentLocation() {
        Geocoder geocoder = new Geocoder(this.requireContext(), Locale.getDefault());
        SharedPreferences sharedPreferences = this.requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        float latitude = sharedPreferences.getFloat("latitude", 0);
        float longitude = sharedPreferences.getFloat("longitude", 0);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String cityName = addresses.get(0).getLocality();
                String stateName = addresses.get(0).getAdminArea();
                return cityName + ", " + stateName;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
        return "";
    }
}
