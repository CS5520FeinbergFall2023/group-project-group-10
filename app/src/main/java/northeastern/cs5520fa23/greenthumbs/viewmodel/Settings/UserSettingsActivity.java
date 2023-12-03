package northeastern.cs5520fa23.greenthumbs.viewmodel.Settings;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import northeastern.cs5520fa23.greenthumbs.R;

public class UserSettingsActivity extends AppCompatActivity {
    private EditText usernameSettingsET;
    private EditText firstNameSettingsET;
    private EditText lastNameSettingsET;
    private EditText emailSettingsET;
    private EditText passwordSettingsET;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fdb;
    private DatabaseReference dbReg;
    private FirebaseUser currentUser;

    protected void OnCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountsettings);
    }
}
