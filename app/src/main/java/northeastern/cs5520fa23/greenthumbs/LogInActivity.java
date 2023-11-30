package northeastern.cs5520fa23.greenthumbs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {

    private Button goToSignUpButton;
    private Button goToLogInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_or_log_in);
        goToLogInButton = findViewById(R.id.log_in_start_button);
        goToSignUpButton = findViewById(R.id.sign_up_page_start_button);

        goToLogInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(LogInActivity.this, LogInPageActivity.class);
                startActivity(i);
            }
        });

        goToSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LogInActivity.this, SignUpPageActivity.class);
                startActivity(i);
            }
        });
    }
}