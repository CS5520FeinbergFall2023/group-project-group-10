package northeastern.cs5520fa23.greenthumbs.viewmodel.Settings;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import northeastern.cs5520fa23.greenthumbs.LogInActivity;
import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SetLocationFragment;

public class SettingsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private View settingFragmentView;
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

        settingFragmentView = inflater.inflate(R.layout.fragment_settings, container, false);

        Button accountSettingsButton = settingFragmentView.findViewById(R.id.AccountSettingsbtn);
        accountSettingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UserSettingsActivity.class);
            startActivity(intent);
        });

        Button btnShowSetLocation = settingFragmentView.findViewById(R.id.SetLocationbtn);
        btnShowSetLocation.setOnClickListener(view -> showSetLocationFragment());

        Button btnShowInformation = settingFragmentView.findViewById(R.id.Informationbtn);
        btnShowInformation.setOnClickListener(view -> showInformation());

        Button logoutBtn = settingFragmentView.findViewById(R.id.LogOutBtn);
        logoutBtn.setOnClickListener(v -> logOutUser());

        if (savedInstanceState != null) {
            settingFragmentView.findViewById(R.id.AccountSettingsbtn).setVisibility(savedInstanceState.getInt("accountSettingsVisibility", View.VISIBLE));
            settingFragmentView.findViewById(R.id.SetLocationbtn).setVisibility(savedInstanceState.getInt("setLocationVisibility", View.VISIBLE));
        }
        return settingFragmentView;
    }

    private void showInformation() {
        Intent intent = new Intent(getActivity(), InformationActivity.class);
        startActivity(intent);
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
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("accountSettingsVisibility", settingFragmentView.findViewById(R.id.AccountSettingsbtn).getVisibility());
        outState.putInt("setLocationVisibility", settingFragmentView.findViewById(R.id.SetLocationbtn).getVisibility());
    }

    private void showSetLocationFragment() {
        SetLocationFragment setLocationFragment = new SetLocationFragment();
        setLocationFragment.show(getParentFragmentManager(), "setLocationFragment");
    }
}
