package com.joy.taskmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Profile extends Fragment {

    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_profile, container, false);



        // Initialize SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", null);
        String name = sharedPreferences.getString("name", "Md.Joy");
        String email = sharedPreferences.getString("email", null);


        TextView tvname = myview.findViewById(R.id.profile_name);
        TextView tvemail = myview.findViewById(R.id.profile_email);




        tvname.setText(name);
        tvemail.setText(email);

        Button logoutButton = myview.findViewById(R.id.edit_profile_button);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Clear user details from SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            Toast.makeText(getActivity(), "Logged out successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), Sing_in.class));
                            getActivity().finish();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });




        return myview;
    }
}