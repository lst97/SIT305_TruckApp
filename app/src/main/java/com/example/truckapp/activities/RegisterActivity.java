package com.example.truckapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.truckapp.R;
import com.example.truckapp.controllers.ServicesController;
import com.example.truckapp.models.user.Roles;
import com.example.truckapp.models.user.User;
import com.example.truckapp.services.authenticate.AuthenticateService;
import com.example.truckapp.services.authenticate.PasswordHasher;

import java.time.LocalDateTime;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullNameEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText phoneEditText;
    private TextView messageTextView;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // ThreadPolicy is needed to allow network requests on main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Find views by ID
        fullNameEditText = findViewById(R.id.register_full_name_input);
        usernameEditText = findViewById(R.id.register_username_input);
        passwordEditText = findViewById(R.id.register_password_input);
        confirmPasswordEditText = findViewById(R.id.register_confirm_password_input);
        phoneEditText = findViewById(R.id.register_phone_input);
        messageTextView = findViewById(R.id.register_message);
        registerButton = findViewById(R.id.register_create_account_button);

        // set click listener for register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();

                // show login activity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser() {
        // Get username and password from EditText fields
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String fullName = fullNameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        // Validate username and password (e.g. check if they are not empty)
        if (username.isEmpty()) {
            usernameEditText.setError("Username is required");
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            return;
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Confirm password is required");
            return;
        }

        if (fullName.isEmpty()) {
            fullNameEditText.setError("Full name is required");
            return;
        }

        if (phone.isEmpty()) {
            phoneEditText.setError("Phone is required");
            return;
        }

        // Validate password and confirm password
        if (!Objects.equals(password, confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return;
        }

        // Authenticate user (e.g. by sending login request to server)
        // use LoginService to login
        ServicesController servicesController = ServicesController.getInstance();
        AuthenticateService authenticateService = (AuthenticateService) servicesController.getService("AuthenticateService");
        if (authenticateService == null) {
            throw new NullPointerException("AuthenticateService not found");
        }

        // check if username is already taken
        if(authenticateService.isUserExist(username)){
            messageTextView.setText(R.string.register_error_username_exists);
            return;
        }

        // create customer user
        LocalDateTime createdDate = LocalDateTime.now();
        User user = new User (username, PasswordHasher.hashPassword(password), fullName, createdDate, createdDate, phone, Roles.CUSTOMER);

        // save user to database
        if (authenticateService.register(user)) {
            // if user is successfully registered
            // display success message
            messageTextView.setText(R.string.register_info_register_success);
            // redirect to login page
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else {
            // if user is not successfully registered
            // display error message
            messageTextView.setText("Error occurred while registering user");
        }
    }

}
