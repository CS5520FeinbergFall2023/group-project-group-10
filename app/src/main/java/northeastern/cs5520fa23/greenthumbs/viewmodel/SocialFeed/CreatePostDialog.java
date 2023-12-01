package northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import northeastern.cs5520fa23.greenthumbs.R;

/*
Notes:

 */

public class CreatePostDialog extends DialogFragment {
    Button postButton;
    Button cancelButton;
    private Button addImgButton;
    private ImageView postImage;
    private ActivityResultLauncher<PickVisualMediaRequest> imgSelect;
    private FirebaseDatabase db;
   //private FirebaseFirestore  fsDB;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.fragment_create_post, null);
        db = FirebaseDatabase.getInstance();
        //fsDB = FirebaseFirestore.getInstance();
        postButton = view.findViewById(R.id.create_post_button);
        cancelButton = view.findViewById(R.id.cancel_create_post_button);
        addImgButton = view.findViewById(R.id.create_post_add_image_button);
        postImage = view.findViewById(R.id.create_post_image);
        imgSelect = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                postImage.setImageURI(uri);
                postImage.setVisibility(View.VISIBLE);
            } else {

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
                Map<String, Object> post = new HashMap<>();
                //post.put("")
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePostDialog.this.getDialog().cancel();
            }
        });
        builder.setView(view);
                /*
                .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CreatePostDialog.this.getDialog().cancel();
                    }
                })


                 */


        return builder.create();
    }

    private void getImg() {

        /*Toast.makeText(getContext(), "HERE", Toast.LENGTH_LONG).show();
        ActivityResultLauncher<PickVisualMediaRequest> imgSelect = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                postImage.setImageURI(uri);
                postImage.setVisibility(View.VISIBLE);
            } else {

            }
        });

         */

        imgSelect.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
    }
}
