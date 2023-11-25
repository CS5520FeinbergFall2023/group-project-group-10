package northeastern.cs5520fa23.greenthumbs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {
    private Button toRegisterButton;
    private Button loginButton;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        toRegisterButton = findViewById(R.id.register_button_login_activity);
        loginButton = findViewById(R.id.login_button_login_activity);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        toRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailString = email.toString();
                String passwordString = password.toString();
                // log in user here and if log in successful start activity
                Intent i = new Intent(LogInActivity.this, MainActivity.class);
            }
        });
    }
}