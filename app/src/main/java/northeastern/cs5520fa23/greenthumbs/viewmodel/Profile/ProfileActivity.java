package northeastern.cs5520fa23.greenthumbs.viewmodel.Profile;

import static android.content.ContentValues.TAG;
import static northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.CreatePostDialog.REQUEST_GET_IMAGE;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.Chat.ChatActivity;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.ImgPost;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.Like;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialAdapter;
import northeastern.cs5520fa23.greenthumbs.viewmodel.User;

public class ProfileActivity extends AppCompatActivity implements SocialAdapter.UsernameCallback {

    FirebaseDatabase db;
    private String username;
    private String profUid;
    private EditText usernameView;
    private EditText userBioView;
    private boolean isEditing;
    private boolean canEdit;
    private boolean isUsersProfile;
    private Button editProfileButton;
    private Button addFriendButton;
    private Button sendMsgButton;
    private FirebaseUser currUser;
    private RecyclerView socialRecyclerView;
    private SocialAdapter socialAdapter;
    private List<ImgPost> postList;
    private ImageView headerImage;
    private ImageView profilePicture;
    //private ActivityResultLauncher<PickVisualMediaRequest> headerImgSelect;
    //private ActivityResultLauncher<PickVisualMediaRequest> profPicSelect;
    private Uri profPicUri;
    private Uri headerUri;
    private Map<String, String> friends;
    private boolean isFriend;
    private boolean isRequested;
    private boolean headerEdited;
    private boolean profilePictureEdited;
    private boolean bioEdited;
    Friend userFriend;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ArrayList<String> profileInfo = getIntent().getBundleExtra("profile_info").getStringArrayList("user_info");
        username = profileInfo.get(0);
        profUid = profileInfo.get(1);

        currUser = FirebaseAuth.getInstance().getCurrentUser();

        // determine if this is the users own profile
        if (profUid.equals(currUser.getUid())) {
            isUsersProfile = true;
        } else {
            isUsersProfile = false;
        }
        this.db = FirebaseDatabase.getInstance();
        this.profilePictureEdited = false;
        this.headerEdited = false;
        this.bioEdited = false;

        ActivityResultLauncher<Intent> headerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == RESULT_OK) {
                    if (o.getData() != null) {
                        Intent data = o.getData();
                        if (data.getData() != null) {
                            Uri uri = data.getData();
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                headerImage.setImageBitmap(bitmap);
                                headerEdited = true;
                            } catch (Exception e) {
                                Toast.makeText(ProfileActivity.this, "Unable to get image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });

        ActivityResultLauncher<Intent> profilePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == RESULT_OK) {
                    if (o.getData() != null) {
                        Intent data = o.getData();
                        if (data.getData() != null) {
                            Uri uri = data.getData();
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                profilePicture.setImageBitmap(bitmap);
                                profilePictureEdited = true;
                            } catch (Exception e) {
                                Toast.makeText(ProfileActivity.this, "Unable to get image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });

        this.usernameView = findViewById(R.id.profile_name_activity);
        this.headerImage = findViewById(R.id.profile_header_image_activity);
        this.profilePicture = findViewById(R.id.profile_prof_pic_activity);
        this.isEditing = false;
        this.friends = new HashMap<>();
        getFriends();
        this.usernameView.setText(this.username);
        this.usernameView.setEnabled(false);
        this.userBioView = findViewById(R.id.profile_user_bio_activity);
        this.userBioView.setEnabled(false);
        //this.usernameView.setTextColor(getResources().getColor(R.color.black));
        //this.userBioView.setTextColor(getResources().getColor(R.color.black));
        this.sendMsgButton = findViewById(R.id.profile_msg_button_activity);
        this.sendMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, ChatActivity.class);
                i.putExtra("other_username", username);
                startActivity(i);

            }
        });
        this.addFriendButton = findViewById(R.id.profile_friend_button_activity);
        this.addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFriendClick();
            }
        });
        this.editProfileButton = findViewById(R.id.profile_edit_button_activity);
        socialRecyclerView = findViewById(R.id.profile_posts_rv_activity);
        socialRecyclerView.setHasFixedSize(true);
        socialRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.postList = new ArrayList<>();
        socialAdapter = new SocialAdapter(postList, this, this);
        socialRecyclerView.setAdapter(socialAdapter);

        if (savedInstanceState != null) {
            loadData();
            usernameView.setText(savedInstanceState.getString("username", ""));
            userBioView.setText(savedInstanceState.getString("userBio", ""));
            isEditing = savedInstanceState.getBoolean("isEditing", false);
            headerEdited = savedInstanceState.getBoolean("headerEdited", false);
            profilePictureEdited = savedInstanceState.getBoolean("profilePictureEdited", false);

            if (headerEdited) {
                Bitmap headerBitmap = savedInstanceState.getParcelable("headerBitmap");
                if (headerBitmap != null) {
                    headerImage.setImageBitmap(headerBitmap);
                }
            }
            if (profilePictureEdited) {
                Bitmap profileBitmap = savedInstanceState.getParcelable("profileBitmap");
                if (profileBitmap != null) {
                    profilePicture.setImageBitmap(profileBitmap);
                }
            }

            if (isEditing) {
                enableEditingMode();
                userBioView.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(userBioView, InputMethodManager.SHOW_IMPLICIT);
            }
        }

        if (isUsersProfile) {
            this.sendMsgButton.setEnabled(false);
            this.sendMsgButton.setVisibility(View.GONE);
            this.sendMsgButton.setEnabled(false);
            this.addFriendButton.setVisibility(View.GONE);
            this.profilePicture.setEnabled(false);
            this.headerImage.setEnabled(false);
            this.editProfileButton.setEnabled(true);
            this.editProfileButton.setVisibility(View.VISIBLE);
            this.editProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isEditing) {
                        isEditing = true;
                        editProfileButton.setText("Finish");
                        //usernameView.setEnabled(true);
                        userBioView.setEnabled(true);
                        headerImage.setEnabled(true);
                        profilePicture.setEnabled(true);
                    } else {
                        if (userBioView.getText().length() > 40) {
                            Toast.makeText(ProfileActivity.this, "Bio cannot be more than 40 characters", Toast.LENGTH_LONG).show();
                        } else {
                            saveProfileUpdates();
                            editProfileButton.setText("Edit");
                            isEditing = false;
                        }
                    }
                }
            });
            this.headerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEditing) {
                        if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GET_IMAGE);
                            Toast.makeText(ProfileActivity.this, "Need photo access", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            headerLauncher.launch(galleryIntent);
                        }
                        //getImg(headerImgSelect);
                    }
                }
            });
            this.profilePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEditing) {
                        //getImg(profPicSelect);
                        if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GET_IMAGE);
                            Toast.makeText(ProfileActivity.this, "Need photo access", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            profilePictureLauncher.launch(galleryIntent);
                        }
                    }
                }
            });
        } else {
            this.sendMsgButton.setEnabled(true);
            this.sendMsgButton.setVisibility(View.VISIBLE);
            this.sendMsgButton.setEnabled(true);
            this.addFriendButton.setVisibility(View.VISIBLE);
            this.editProfileButton.setEnabled(false);
            this.editProfileButton.setVisibility(View.GONE);
            this.headerImage.setEnabled(false);
            this.profilePicture.setEnabled(false);
        }

        loadPosts();
        //Toast.makeText(getContext(), "post loaded not data", Toast.LENGTH_LONG).show();
        loadData();
    }

    private void enableEditingMode() {
        usernameView.setEnabled(true);
        userBioView.setEnabled(true);
        editProfileButton.setText(R.string.finish_editing);
        headerImage.setClickable(true);
        profilePicture.setClickable(true);
    }

    private void disableEditingMode() {
        usernameView.setEnabled(false);
        userBioView.setEnabled(false);
        headerImage.setClickable(false);
        profilePicture.setClickable(false);
        editProfileButton.setText(R.string.edit_profile);
    }

    private void saveProfileUpdates() {
        //uploadProfilePic();
        //uploadHeaderImage();

        Map<String, Object> updates = new HashMap<>();
        uploadProfilePic(updates);
        disableEditingMode();
        //isEditing = false;
        /*
        if (headerUri != null) {
            updates.put("header_image", headerUri.toString());
        }
        if (profPicUri != null) {
            updates.put("profile_pic", profPicUri.toString());
        }

         */
        //updates.put("username", usernameView.getText().toString());
    }
    private void loadData() {
        //Toast.makeText(getContext(), "post loaded not data", Toast.LENGTH_LONG).show();

        Query profileQuery = db.getReference("users").orderByChild("user_id").equalTo(profUid);
        profileQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getHeader_image() != null) {
                        //Toast.makeText(getActivity(), "HERE", Toast.LENGTH_LONG).show();

                        loadHeaderImage(user.getHeader_image());
                    }
                    if (user.getProfile_pic() != null) {
                        loadProfPic(user.getProfile_pic());
                    }
                    if (user.getUsername() != null) {
                        usernameView.setText(user.getUsername());
                        // should we change username too?
                    }
                    if (user.getUser_bio() != null) {
                        userBioView.setText(user.getUser_bio());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadPosts() {
        Query postQuery = db.getReference("posts").orderByChild("uid").equalTo(profUid);
        postQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ImgPost currPost = dataSnapshot.getValue(ImgPost.class);
                    Log.d(TAG, currPost.toString());
                    postList.add(currPost);
                    socialAdapter.notifyDataSetChanged();

                }
                Collections.reverse(postList);
                socialAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadHeaderImage(String headerImgUri) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        //StorageReference imgRef = storage.getReferenceFromUrl(headerImgUri);
        StorageReference imgRef = storage.getReference().child("header_images").child(profUid);
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                headerUri = uri;
                try {
                    Picasso.get().load(uri).resize(headerImage.getWidth(), headerImage.getHeight()).centerCrop().into(headerImage);
                } catch (IllegalArgumentException e) {
                    Log.d("ERROR IMG", "header" + " " + headerImage.getWidth() + " " + headerImage.getHeight());
                }
                //Glide.with(getContext()).load(uri).skipMemoryCache(true).into(headerImage);
                //headerImage.setImageURI(uri);
            }
        });
    }

    private void loadProfPic(String profPictureUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        //StorageReference imgRef = storage.getReferenceFromUrl(profPictureUri);
        StorageReference imgRef = storage.getReference().child("profile_pics").child(profUid);

        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                profPicUri = uri;
                try {
                    Picasso.get().load(uri).resize(profilePicture.getWidth(), profilePicture.getHeight()).centerCrop().into(profilePicture);
                } catch (IllegalArgumentException e) {
                    Log.d("ERROR IMG", "prof pic" + " " + profilePicture.getWidth() + " " + profilePicture.getHeight());
                }
                // Picasso.get().load(uri).resize(profilePicture.getWidth(), profilePicture.getHeight()).centerCrop().into(profilePicture);
                //Glide.with(getContext()).load(uri).into(profilePicture);
            }
        });
    }

    private void uploadProfilePic(Map<String, Object> updates) {
        if (profilePictureEdited) {
            if (((BitmapDrawable) profilePicture.getDrawable()).getBitmap() == null) {
                return;
            }
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            String imgPath = "profile_pics/" + profUid;
            StorageReference profPicStorageRef = storageReference.child(imgPath);
            profPicStorageRef.delete();
            //Boolean t = profPicStorageRef == null;
            //Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();
            Bitmap bitmap = ((BitmapDrawable) profilePicture.getDrawable()).getBitmap();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] data = outputStream.toByteArray();


            UploadTask uploadTask = profPicStorageRef.putBytes(data); // upload then on the upload get the download link for the post
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String link = uri.toString();
                            db.getReference("users").child(currUser.getUid()).child("profile_pic").setValue(link).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    if (headerEdited) {
                                        updates.put("profile_picture", link);
                                        uploadHeaderImage(updates);
                                    } else {
                                        finishProfileUpdates(updates);
                                    }
                                    //CreatePostDialog.this.getDialog().cancel();
                                }
                            });
                        }
                    });

                }
            });
        } else {
            uploadHeaderImage(updates);
            finishProfileUpdates(updates);
        }


    }

    private void uploadHeaderImage(Map<String, Object> updates) {
        if (headerEdited) {
            if (((BitmapDrawable) headerImage.getDrawable()) == null) {
                return;
            }
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            String imgPath = "header_images/" + profUid;
            StorageReference headerStorageRef = storageReference.child(imgPath);
            headerStorageRef.delete();
            //Boolean t = profPicStorageRef == null;
            //Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();
            Bitmap bitmap = ((BitmapDrawable) headerImage.getDrawable()).getBitmap();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] data = outputStream.toByteArray();


            UploadTask uploadTask = headerStorageRef.putBytes(data); // upload then on the upload get the download link for the post
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String link = uri.toString();
                            db.getReference("users").child(currUser.getUid()).child("header_image").setValue(link).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    updates.put("header_image", link);
                                    finishProfileUpdates(updates);
                                    //CreatePostDialog.this.getDialog().cancel();
                                }
                            });
                        }
                    });

                }
            });
        } else {
            finishProfileUpdates(updates);
        }
    }

    private void finishProfileUpdates(Map<String, Object> updates) {
        updates.put("user_bio", userBioView.getText().toString());
        db.getReference("users").child(currUser.getUid()).updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Unable to update profile", Toast.LENGTH_LONG).show();
                    isEditing = false;

                } else {
                    isEditing = false;
                    headerEdited = false;
                    profilePictureEdited = false;
                    //loadData();
                    //bioEdited = false;
                }
            }
        });
    }

    private void getFriends() {
        Query friendQuery = db.getReference("users").child(currUser.getUid()).child("friends").orderByKey().equalTo(profUid);
        friendQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Friend friend = dataSnapshot.getValue(Friend.class);
                    userFriend = friend;
                    if (friend.getStatus() != null) {
                        //Toast.makeText(getActivity(), "HERE", Toast.LENGTH_LONG).show();
                        if (friend.getStatus().equals("requested")) {
                            addFriendButton.setText("Requested");
                            addFriendButton.setEnabled(false);
                        } else {
                            addFriendButton.setText("Unfriend");
                            addFriendButton.setEnabled(true);
                            isFriend = true;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleFriendClick() {
        if (!isRequested && !isFriend) {
            //String otherId = userFriend.getFriend_id();
            Map<String, Object> request = new HashMap<>();
            request.put("approved", "false");
            request.put("from_uid", currUser.getUid());

            db.getReference("users").child(currUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {

                    } else {
                        User fromUser = task.getResult().getValue(User.class);
                        String fromUsername = fromUser.getUsername();
                        request.put("from_username", fromUsername);
                        db.getReference("users").child(profUid).child("friend_requests").child(currUser.getUid()).updateChildren(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(ProfileActivity.this, "Unable to send friend request", Toast.LENGTH_LONG).show();
                                } else {
                                    Map<String, Object> userFriendUpdate = new HashMap<>();
                                    userFriendUpdate.put("status", "requested");
                                    userFriendUpdate.put("friend_id", profUid);
                                    userFriendUpdate.put("friend_username", username);
                                    db.getReference("users").child(currUser.getUid()).child("friends").child(profUid).updateChildren(userFriendUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(ProfileActivity.this, "Unable to send friend request", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(ProfileActivity.this, "Friend Request Sent", Toast.LENGTH_LONG).show();
                                                addFriendButton.setText("Requested");
                                                addFriendButton.setEnabled(false);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });

        }
        else {
            if (isFriend) {
                DatabaseReference frRef = db.getReference("users").child(currUser.getUid()).child("friends").child(profUid);
                frRef.removeValue();
                DatabaseReference othersFriendRef = db.getReference("users").child(profUid).child("friends").child(currUser.getUid());
                othersFriendRef.removeValue();
                isFriend = false;
                addFriendButton.setText("Add Friend");
                Toast.makeText(ProfileActivity.this, "Unfriended", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("username", usernameView.getText().toString());
        outState.putString("userBio", userBioView.getText().toString());
        outState.putBoolean("isEditing", isEditing);
        outState.putBoolean("headerEdited", headerEdited);
        outState.putBoolean("profilePictureEdited", profilePictureEdited);

        if (headerEdited && headerImage.getDrawable() != null) {
            Bitmap headerBitmap = ((BitmapDrawable) headerImage.getDrawable()).getBitmap();
            outState.putParcelable("headerBitmap", headerBitmap);
        }
        if (profilePictureEdited && profilePicture.getDrawable() != null) {
            Bitmap profileBitmap = ((BitmapDrawable) profilePicture.getDrawable()).getBitmap();
            outState.putParcelable("profileBitmap", profileBitmap);
        }
    }



    @Override
    public void openProfileCallback(String username, String posterId) {
        Toast.makeText(ProfileActivity.this, "You're already on their profile!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addLikeCallback(ImgPost post) {
        String postId = post.get_id();
        if (!post.isLiked()) {
            Like newLike = new Like(currUser.getUid(), currUser.getUid());
            FirebaseDatabase.getInstance().getReference("posts").child(postId).child("likes").child(currUser.getUid()).setValue(newLike).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Unable to add like", Toast.LENGTH_LONG).show();
                    } else {
                        post.setLiked(true);
                        post.setNum_likes(post.getNum_likes() + 1);
                    }
                }
            });
        }
    }
}