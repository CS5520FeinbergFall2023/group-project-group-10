package northeastern.cs5520fa23.greenthumbs.SocialFeed;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import northeastern.cs5520fa23.greenthumbs.R;

public class CreatePostDialog extends DialogFragment {
    Button postButton;
    Button cancelButton;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.fragment_create_post, null);
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

        postButton = view.findViewById(R.id.create_post_button);
        cancelButton = view.findViewById(R.id.cancel_create_post_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePostDialog.this.getDialog().cancel();
            }
        });
        return builder.create();
    }
}
