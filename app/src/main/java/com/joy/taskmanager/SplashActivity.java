package com.joy.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 3 সেকেন্ড পর MainActivity-তে যাওয়ার কোড
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, Sing_in.class);
            startActivity(intent);
            finish(); // SplashActivity বন্ধ করে দিন
        }, 3000); // সময় ৩ সেকেন্ড (৩০০০ মিলিসেকেন্ড)
    }
}