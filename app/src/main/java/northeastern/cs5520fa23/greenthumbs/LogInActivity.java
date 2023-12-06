package northeastern.cs5520fa23.greenthumbs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class LogInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_or_log_in);
        Button goToLogInButton = findViewById(R.id.log_in_start_button);
        Button goToSignUpButton = findViewById(R.id.sign_up_page_start_button);

        goToLogInButton.setOnClickListener(v -> {
            Intent i = new Intent(LogInActivity.this, LogInPageActivity.class);
            startActivity(i);
        });

        goToSignUpButton.setOnClickListener(v -> {
            Intent i = new Intent(LogInActivity.this, SignUpPageActivity.class);
            startActivity(i);
        });
    }
}