package northeastern.cs5520fa23.greenthumbs.viewmodel.Profile;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import northeastern.cs5520fa23.greenthumbs.MainActivity;
import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.CreatePostDialog;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.ImgPost;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialAdapter;
import northeastern.cs5520fa23.greenthumbs.viewmodel.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USERNAME = "username_param";
    private static final String ARG_CURRUID = "uid_param";

    // TODO: Rename and change types of parameters
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
    private ActivityResultLauncher<PickVisualMediaRequest> headerImgSelect;
    private ActivityResultLauncher<PickVisualMediaRequest> profPicSelect;
    private Uri profPicUri;
    private Uri headerUri;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, param1);
        args.putString(ARG_CURRUID, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
            profUid = getArguments().getString(ARG_CURRUID);
        }
        currUser = FirebaseAuth.getInstance().getCurrentUser();

        // determine if this is the users own profile
        if (profUid.equals(currUser.getUid())) {
            isUsersProfile = true;
        } else {
            isUsersProfile = false;
        }
        this.db = FirebaseDatabase.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.usernameView = view.findViewById(R.id.profile_name);
        this.headerImage = view.findViewById(R.id.profile_header_image);
        this.profilePicture = view.findViewById(R.id.profile_prof_pic);
        this.isEditing = false;
        this.usernameView.setText(this.username);
        this.usernameView.setEnabled(false);
        this.userBioView = view.findViewById(R.id.profile_user_bio);
        this.userBioView.setEnabled(false);
        this.usernameView.setTextColor(getResources().getColor(R.color.black));
        this.userBioView.setTextColor(getResources().getColor(R.color.black));
        this.sendMsgButton = view.findViewById(R.id.profile_msg_button);
        this.addFriendButton = view.findViewById(R.id.profile_friend_button);
        this.editProfileButton = view.findViewById(R.id.profile_edit_button);
        socialRecyclerView = view.findViewById(R.id.profile_posts_rv);
        socialRecyclerView.setHasFixedSize(true);
        socialRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.postList = new ArrayList<>();
        socialAdapter = new SocialAdapter(postList, getContext(), null);
        socialRecyclerView.setAdapter(socialAdapter);
        headerImgSelect = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                //headerImage.setImageURI(uri);
                headerUri = uri;
                headerImage.setImageURI(headerUri);
            } else {
                // something here like a toast or something
            }
        });
        profPicSelect = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                //profilePicture.setImageURI(uri);
                profPicUri = uri;
                profilePicture.setImageURI(profPicUri);
            } else {
                // something here like a toast or something
            }
        });
        loadData();
        //uploadProfilePic();

        // hide message and add friend button if this is their own profile and enable edit profile
        if (isUsersProfile) {
            this.sendMsgButton.setEnabled(false);
            this.sendMsgButton.setVisibility(View.GONE);
            this.sendMsgButton.setEnabled(false);
            this.addFriendButton.setVisibility(View.GONE);
            this.editProfileButton.setEnabled(true);
            this.editProfileButton.setVisibility(View.VISIBLE);
            this.editProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isEditing) {
                        isEditing = true;
                        editProfileButton.setText("Finish");
                        usernameView.setEnabled(true);
                        userBioView.setEnabled(true);
                    } else {
                        saveProfileUpdates();
                        editProfileButton.setText("Edit Profile");
                    }
                }
            });
            this.headerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getImg(headerImgSelect);

                }
            });
            this.profilePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getImg(profPicSelect);

                }
            });
            this.headerImage.setEnabled(true);
            this.profilePicture.setEnabled(true);
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
    }

    private void getImg(ActivityResultLauncher<PickVisualMediaRequest> request) {
        request.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
    }
    private void saveProfileUpdates() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("header_image", headerUri.toString());
        updates.put("profile_pic", profPicUri.toString());
        updates.put("username", usernameView.getText().toString());
        updates.put("user_bio", userBioView.getText().toString());
        db.getReference("users").child(currUser.getUid()).setValue(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getContext(), "Unable to update profile", Toast.LENGTH_LONG).show();
                } else {

                }
            }
        });
    }
    private void loadData() {
        Query profileQuery = db.getReference("users").orderByChild("user_id").equalTo(profUid);
        profileQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    //Boolean t = user.getHeader_image() == null; true
                    //Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();
                    if (user.getHeader_image() != null) {
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
        Query postQuery = db.getReference("posts").orderByChild("uid").equalTo(currUser.getUid());
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
        StorageReference imgRef = storage.getReferenceFromUrl(headerImgUri);
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                headerUri = uri;
                Glide.with(getContext()).load(uri).into(headerImage);
            }
        });
    }

    private void loadProfPic(String profPictureUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imgRef = storage.getReferenceFromUrl(profPictureUri);
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                profPicUri = uri;
                Glide.with(getContext()).load(uri).into(profilePicture);
            }
        });
    }

    private void uploadProfilePic() {
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
                        db.getReference("users").child(currUser.getUid()).child("profile_pic").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                saveProfileUpdates();
                                //CreatePostDialog.this.getDialog().cancel();
                            }
                        });
                    }
                });

            }
        });

    }

    private void uploadHeaderImage() {
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
                        db.getReference("users").child(currUser.getUid()).child("header_image").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                saveProfileUpdates();
                                //CreatePostDialog.this.getDialog().cancel();
                            }
                        });
                    }
                });

            }
        });

    }
}