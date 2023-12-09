package northeastern.cs5520fa23.greenthumbs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import northeastern.cs5520fa23.greenthumbs.model.AuthHelper;

public class LogInPageActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button logInButton;

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_page);
        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.edit_text_email_login);
        passwordEditText = findViewById(R.id.edit_text_pass_login);
        logInButton = findViewById(R.id.enter_log_in);

        if (savedInstanceState != null) {
            emailEditText.setText(savedInstanceState.getString("email", ""));
            passwordEditText.setText(savedInstanceState.getString("password", ""));
        }

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (validateInput(email, password)) {

                    progressDialog = new ProgressDialog(LogInPageActivity.this);
                    progressDialog.setMessage("Logging in...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    signInUser(email, password);
                }
            }
        }
        );
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        timerHandler.removeCallbacks(timerRunnable);
                        Intent i = new Intent(LogInPageActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        AuthHelper.storeUserDetails(this);
                    } else {
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });

        startLoginTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(LogInPageActivity.this, "Login is taking longer than expected", Toast.LENGTH_SHORT).show();
        }
    };

    private void startLoginTimer() {
        timerHandler.postDelayed(timerRunnable, 5000);
    }

}
