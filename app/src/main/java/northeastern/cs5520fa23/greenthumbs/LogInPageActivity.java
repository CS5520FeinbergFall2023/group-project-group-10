package northeastern.cs5520fa23.greenthumbs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LogInPageActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button logInButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_page);
        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.edit_text_email_login);
        passwordEditText = findViewById(R.id.edit_text_pass_login);
        logInButton = findViewById(R.id.enter_log_in);
        progressBar = findViewById(R.id.progressBar);

        if (savedInstanceState != null) {
            emailEditText.setText(savedInstanceState.getString("email", ""));
            passwordEditText.setText(savedInstanceState.getString("password", ""));
        }

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if(validateInput(email, password)) {
                    signInUser(email, password);
                }
            }
        }
        );
    }

    private void signInUser(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Intent i = new Intent(LogInPageActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                });
    }

    private boolean validateInput(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("email", emailEditText.getText().toString());
        outState.putString("password", passwordEditText.getText().toString());
    }

}
