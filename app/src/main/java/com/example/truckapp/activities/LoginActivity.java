package com.example.truckapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.truckapp.R;
import com.example.truckapp.handlers.ServicesHandler;
import com.example.truckapp.models.user.User;
import com.example.truckapp.services.authenticate.AuthenticateService;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView messageTextView;
    private Button registerButton;
    private Button loginButton;


    private void initViews() {
        usernameEditText = findViewById(R.id.login_usernameInput);
        passwordEditText = findViewById(R.id.login_passwordInput);
        messageTextView = findViewById(R.id.login_message);
        loginButton = findViewById(R.id.login_loginBtn);
        registerButton = findViewById(R.id.login_registerBtn);
    }

    private void initListeners() {
        // set click listener for register button
        loginButton.setOnClickListener(v -> {
            // Get username and password from EditText fields
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Validate username and password (e.g. check if they are not empty)
            if (username.isEmpty()) {
                usernameEditText.setError("Username is required");
                return;
            }

            if (password.isEmpty()) {
                passwordEditText.setError("Password is required");
                return;
            }

            // Authenticate user (e.g. by sending login request to server)
            // use AuthenticateService to login
            ServicesHandler servicesHandler = ServicesHandler.getInstance();
            AuthenticateService authenticateService = (AuthenticateService) servicesHandler.getService("AuthenticateService");
            if (authenticateService == null) {
                throw new NullPointerException("LoginService not found");
            }


            User userCredentials = authenticateService.login(new User(username, password));
            if (userCredentials != null) {
                // if the password is empty, it means that the password is not correct
                if (Objects.equals(userCredentials.getPassword(), "")) {
                    messageTextView.setText(R.string.login_error_incorrect_password);
                    return;
                }

                // start HomeActivity
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Prevents user from going back to LoginActivity by pressing back button

            } else {
                // login failed
                messageTextView.setText(R.string.login_error_user_does_not_exist);
            }
        });

        registerButton.setOnClickListener(v -> {
            // Show register activity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ThreadPolicy is needed to allow network requests on main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initViews();
        initListeners();
    }
}