package northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import northeastern.cs5520fa23.greenthumbs.R;

/**
THIS IS AN OLD CLASS AND CAN PROBABLY BE DELETED
 */
public class CreatePostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText postText;
    private EditText postTags;
    private Button addImgButton;
    private ImageView postImage;
    private Button postButton;
    private final int PERMISSION_REQUEST_READ_MEDIA_IMAGES = 1;

    public CreatePostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreatePost.
     */
    // TODO: Rename and change types and number of parameters
    public static CreatePostFragment newInstance(String param1, String param2) {
        CreatePostFragment fragment = new CreatePostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);
        postText = view.findViewById(R.id.create_post_text);
        postTags = view.findViewById(R.id.create_post_tags);
        addImgButton = view.findViewById(R.id.create_post_add_image_button);
        postImage = view.findViewById(R.id.create_post_image);
        postImage.setVisibility(View.GONE);
        addImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireActivity(),
                        Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                            PERMISSION_REQUEST_READ_MEDIA_IMAGES);
                } else {
                    // Permission has already been granted, proceed with getting image
                    getImg();
                }
            }
        });
        //postButton = view.findViewById(R.id.create_post_button);

        return view;
    }

    private void getImg() {
        Toast.makeText(getContext(), "HERE", Toast.LENGTH_LONG).show();
        ActivityResultLauncher<PickVisualMediaRequest> imgSelect = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                postImage.setImageURI(uri);
                postImage.setVisibility(View.VISIBLE);
            } else {

            }
        });

        imgSelect.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
    }
}