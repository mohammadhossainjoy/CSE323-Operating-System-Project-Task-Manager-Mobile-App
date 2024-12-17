package com.joy.taskmanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Sing_in extends AppCompatActivity {

    TextView gotoregistration;
    EditText emailInput, passwordInput;
    Button submitButton;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);

        gotoregistration = findViewById(R.id.gotoregister);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        submitButton = findViewById(R.id.submitButton);

        progressDialog = new ProgressDialog(Sing_in.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Check if user is already logged in
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            // Redirect to MainActivity if already logged in
            startActivity(new Intent(Sing_in.this, MainActivity.class));
            finish();
        }

        submitButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (validateInputs(email, password)) {
                progressDialog.show();
                sendLoginRequest(email, password);
            }
        });

        gotoregistration.setOnClickListener(v -> {
            startActivity(new Intent(Sing_in.this, Registration.class));
        });
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Valid email is required");
            return false;
        }

        if (password.isEmpty()) {
            passwordInput.setError("Password is required");
            return false;
        }

        return true;
    }

    private void sendLoginRequest(String email, String password) {
        String url = "https://nazmulofficial24.com/Task_app/login.php?email=" + email + "&pwd=" + password;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            String userId = response.getString("user_id");
                            String userName = response.getString("name");
                            String userEmail = response.getString("email");

                            // Save details in SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("user_id", userId);
                            editor.putString("name", userName);
                            editor.putString("email", userEmail);
                            editor.apply();


                            Toast.makeText(Sing_in.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Sing_in.this, MainActivity.class));
                            finish();
                        } else {
                            String errorMessage = response.getString("message");
                            Toast.makeText(Sing_in.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(Sing_in.this, "JSON Parsing error!", Toast.LENGTH_SHORT).show();
                    } finally {
                        progressDialog.dismiss();
                    }
                },
                error -> {
                    Toast.makeText(Sing_in.this, "Failed to login! Please check your connection.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });

        RequestQueue requestQueue = Volley.newRequestQueue(Sing_in.this);
        requestQueue.add(jsonObjectRequest);
    }
}
