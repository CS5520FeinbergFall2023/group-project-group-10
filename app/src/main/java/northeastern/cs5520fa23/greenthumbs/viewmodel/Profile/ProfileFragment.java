package northeastern.cs5520fa23.greenthumbs.viewmodel.Profile;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import static northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.CreatePostDialog.REQUEST_GET_IMAGE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
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

import northeastern.cs5520fa23.greenthumbs.MainActivity;
import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.FriendsUsers.UsersActivity;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.Chat.ChatActivity;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.CreatePostDialog;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.ImgPost;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialAdapter;
import northeastern.cs5520fa23.greenthumbs.viewmodel.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements SocialAdapter.UsernameCallback {

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


    }

    @Override
    public void openProfileCallback(String username, String posterId) {
        //Fragment profileFragment = ProfileFragment.newInstance(username, posterId);
        //getActivity().getSupportFragmentManager().beginTransaction().replace(this.getId(), profileFragment).addToBackStack(null).commit();
    }

    @Override
    public void addLikeCallback(ImgPost post) {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*
        OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStackImmediate();





            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), backPressedCallback);
        */
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
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                                headerImage.setImageBitmap(bitmap);
                                headerEdited = true;
                            } catch (Exception e) {
                                Toast.makeText(getContext(), "Unable to get image", Toast.LENGTH_SHORT).show();
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
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                                profilePicture.setImageBitmap(bitmap);
                                profilePictureEdited = true;
                            } catch (Exception e) {
                                Toast.makeText(getContext(), "Unable to get image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });

        this.usernameView = view.findViewById(R.id.profile_name);
        this.headerImage = view.findViewById(R.id.profile_header_image);
        this.profilePicture = view.findViewById(R.id.profile_prof_pic);
        this.isEditing = false;
        this.friends = new HashMap<>();
        getFriends();
        this.usernameView.setText(this.username);
        this.usernameView.setEnabled(false);
        this.userBioView = view.findViewById(R.id.profile_user_bio);
        this.userBioView.setEnabled(false);
        this.usernameView.setTextColor(getResources().getColor(R.color.black));
        this.userBioView.setTextColor(getResources().getColor(R.color.black));
        this.sendMsgButton = view.findViewById(R.id.profile_msg_button);
        this.sendMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ChatActivity.class);
                i.putExtra("other_username", username);
                getActivity().startActivity(i);

            }
        });
        this.addFriendButton = view.findViewById(R.id.profile_friend_button);
        this.addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFriendClick();
            }
        });
        this.editProfileButton = view.findViewById(R.id.profile_edit_button);
        socialRecyclerView = view.findViewById(R.id.profile_posts_rv);
        socialRecyclerView.setHasFixedSize(true);
        socialRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.postList = new ArrayList<>();
        socialAdapter = new SocialAdapter(postList, getContext(), null);
        socialRecyclerView.setAdapter(socialAdapter);
        /*
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

         */
        //uploadProfilePic();

        // hide message and add friend button if this is their own profile and enable edit profile
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
                        saveProfileUpdates();
                        editProfileButton.setText("Edit Profile");
                        isEditing = false;
                    }
                }
            });
            this.headerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEditing) {
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GET_IMAGE);
                            Toast.makeText(getContext(), "Need photo access", Toast.LENGTH_SHORT).show();
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
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GET_IMAGE);
                            Toast.makeText(getContext(), "Need photo access", Toast.LENGTH_SHORT).show();
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

    //private void getImg(ActivityResultLauncher<PickVisualMediaRequest> request) {
    //    request.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
    //}
    private void saveProfileUpdates() {
        //uploadProfilePic();
        //uploadHeaderImage();

        Map<String, Object> updates = new HashMap<>();
        uploadProfilePic(updates);
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
                    Toast.makeText(getContext(), "Unable to update profile", Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(getContext(), "Unable to send friend request", Toast.LENGTH_LONG).show();
                                } else {
                                    Map<String, Object> userFriendUpdate = new HashMap<>();
                                    userFriendUpdate.put("status", "requested");
                                    userFriendUpdate.put("friend_id", profUid);
                                    userFriendUpdate.put("friend_username", username);
                                    db.getReference("users").child(currUser.getUid()).child("friends").child(profUid).updateChildren(userFriendUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Unable to send friend request", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(getContext(), "Friend Request Sent", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getContext(), "Unfriended", Toast.LENGTH_SHORT).show();
            }
        }
    }
}