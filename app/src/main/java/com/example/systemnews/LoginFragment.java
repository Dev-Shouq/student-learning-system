package com.example.systemnews;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

/**
 * LoginFragment: Handles user authentication, credential validation, 
 * and an automatic lockout system for security.
 */
public class LoginFragment extends Fragment {

    // Hardcoded credentials for demonstration
    private static final String CORRECT_USERNAME = "shouq";
    private static final String CORRECT_PASSWORD = "12345";
    
    // Security settings: maximum failed attempts and lockout time (30 seconds)
    private static final int MAX_ATTEMPTS = 3; 
    private static final long LOCKOUT_DURATION = 30000; 

    // Current state: number of failed attempts and when the current lockout expires
    private int failedAttempts = 0;
    private long lockoutEndTime = 0;
    private TextView tvError;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Bind UI components to variables
        EditText etUsername = view.findViewById(R.id.et_username);
        EditText etPassword = view.findViewById(R.id.et_password);
        tvError = view.findViewById(R.id.tv_login_error);
        Button btnLogin = view.findViewById(R.id.btn_login);

        // Setup click listener for the login button
        btnLogin.setOnClickListener(v -> {
            // Check if the user is currently locked out
            if (System.currentTimeMillis() < lockoutEndTime) return;

            String user = etUsername.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            // Validate the entered credentials
            if (user.equals(CORRECT_USERNAME) && pass.equals(CORRECT_PASSWORD)) {
                // Success: reset attempts and navigate to the resources screen
                failedAttempts = 0; 
                tvError.setVisibility(View.GONE);
                Navigation.findNavController(v).navigate(R.id.action_LoginFragment_to_ResourcesFragment);
            } else {
                // Failure: increment attempt counter and handle lockout if needed
                handleFailedAttempt();
            }
        });
        return view;
    }

    /**
     * handleFailedAttempt: Increments the failure counter and triggers 
     * the lockout timer if the threshold is reached.
     */
    private void handleFailedAttempt() {
        failedAttempts++;
        if (failedAttempts >= MAX_ATTEMPTS) {
            // Start the lockout period
            lockoutEndTime = System.currentTimeMillis() + LOCKOUT_DURATION;
            failedAttempts = 0; // Reset counter for the next cycle
            
            // Create a countdown timer to update the UI during the lockout
            new CountDownTimer(LOCKOUT_DURATION, 1000) {
                public void onTick(long millisUntilFinished) {
                    // Update the error message with remaining seconds
                    tvError.setText("System blocked! Try again in " + (millisUntilFinished / 1000) + " seconds.");
                    tvError.setVisibility(View.VISIBLE);
                }
                public void onFinish() {
                    // Lockout expired: clear error message and reset time
                    tvError.setVisibility(View.GONE);
                    lockoutEndTime = 0;
                }
            }.start();
        } else {
            // Show number of remaining attempts before a lockout occurs
            tvError.setText("Wrong credentials! " + (MAX_ATTEMPTS - failedAttempts) + " attempts remaining.");
            tvError.setVisibility(View.VISIBLE);
        }
    }
}
