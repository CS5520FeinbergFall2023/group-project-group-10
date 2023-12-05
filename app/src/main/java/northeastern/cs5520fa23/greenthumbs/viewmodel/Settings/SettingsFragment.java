package northeastern.cs5520fa23.greenthumbs.viewmodel.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import northeastern.cs5520fa23.greenthumbs.LogInActivity;
import northeastern.cs5520fa23.greenthumbs.R;

public class SettingsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public SettingsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View settingFragmentView = inflater.inflate(R.layout.fragment_settings, container, false);

        Button accountSettingsButton = settingFragmentView.findViewById(R.id.AccountSettingsbtn);
        accountSettingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UserSettingsActivity.class);
            startActivity(intent);
        });

        Button logoutBtn = settingFragmentView.findViewById(R.id.LogOutBtn);
        logoutBtn.setOnClickListener(v -> logOutUser());
        return settingFragmentView;
    }

    private void logOutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        if(getActivity() != null){
            getActivity().finish();

        }
    }
}