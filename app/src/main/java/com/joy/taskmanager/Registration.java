package com.joy.taskmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Registration extends AppCompatActivity {

    TextView gotologin;
    EditText nameInput, emailInput, passwordInput;
    Button submitButton;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        gotologin = findViewById(R.id.gotologin);
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        submitButton = findViewById(R.id.submitButton);

        progressDialog = new ProgressDialog(Registration.this);
        progressDialog.setMessage("Please Wait....."); // Set the message
        progressDialog.setCancelable(false); // Make it non-cancelable

        submitButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (validateInputs(name, email, password)) {
                progressDialog.show();
                sendRegistrationRequest(name, email, password);
            }
        });

        gotologin.setOnClickListener(v -> {
            startActivity(new Intent(Registration.this, Sing_in.class));
        });
    }

    private boolean validateInputs(String name, String email, String password) {
        if (name.isEmpty()) {
            nameInput.setError("Name is required");
            return false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Valid email is required");
            return false;
        }

        if (password.isEmpty() || password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters long");
            return false;
        }

        return true;
    }

    private void sendRegistrationRequest(String name, String email, String password) {
        String url = "https://nazmulofficial24.com/Task_app/registration.php?name=" + name + "&email=" + email + "&pwd=" + password;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            progressDialog.dismiss();
            if ("Registration successful!".equals(response)) {
                Toast.makeText(Registration.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Registration.this, Sing_in.class));
            } else {
                Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(Registration.this, "Failed to register!" + error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(Registration.this);
        requestQueue.add(stringRequest);
    }
}
