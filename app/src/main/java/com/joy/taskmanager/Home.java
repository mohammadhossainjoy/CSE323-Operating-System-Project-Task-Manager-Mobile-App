package com.joy.taskmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class Home extends Fragment {

    SharedPreferences sharedPreferences;

    ArrayList<HashMap<String,String> > arrayList= new ArrayList<>();
    HashMap<String,String> hashMap;

    GridView gridView;

    ProgressBar progressBar;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_home, container, false);

        CardView addBtn = myview.findViewById(R.id.addBtn);
        gridView= myview.findViewById(R.id.gridView);
        progressBar = myview.findViewById(R.id.progressbar);

        // Set click listener to show custom dialog
        addBtn.setOnClickListener(v -> showCustomAlertDialog());


        Data();

        return myview;
    }

    private void showCustomAlertDialog() {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait....."); // Set the message
        progressDialog.setCancelable(false); // Make it non-cancelable





        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.custom_dialog, null); // custom_dialog.xml ইনফ্লেট করুন

        // Initialize views inside the custom dialog layout
        EditText titleInput = dialogView.findViewById(R.id.titleInput);
        EditText descriptionInput = dialogView.findViewById(R.id.descriptionInput);
        EditText timeInput = dialogView.findViewById(R.id.timeInput);
        Spinner prioritySpinner = dialogView.findViewById(R.id.prioritySpinner);
        EditText locationInput = dialogView.findViewById(R.id.locationInput);
        Button submitButton = dialogView.findViewById(R.id.submitButton);

        // Handle DateTime Picker for `timeInput`
        timeInput.setOnClickListener(v -> openDateTimePicker(timeInput));

        // Get Spinner selected item
        final String[] selectedPriority = new String[1];
        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPriority[0] = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPriority[0] = "Medium"; // Default value
            }
        });

        // Build AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        // Set click listener for Submit button
        submitButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString();
            String description = descriptionInput.getText().toString();
            String time = timeInput.getText().toString();
            String priority = selectedPriority[0];
            String location = locationInput.getText().toString();

            // Validate inputs
            if (title.isEmpty() || description.isEmpty() || time.isEmpty() || location.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }


            // Initialize SharedPreferences
            sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String userId = sharedPreferences.getString("user_id", null);


            progressDialog.show();
            // Your server URL
            String url = "https://nazmulofficial24.com/Task_app/insert_Task.php?title=" + title +
                    "&description=" + description +
                    "&time=" + time +
                    "&priority=" + priority +
                    "&user_id=" + userId +
                    "&location=" + location;

            // Create StringRequest
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("Inset Successfully")) {
                        Toast.makeText(getActivity(), "Task added successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        // Close the dialog
                        alertDialog.dismiss();
                        Data();

                    } else {
                        Toast.makeText(getActivity(), "Failed to add task", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        // Close the dialog
                        alertDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    // Close the dialog
                    alertDialog.dismiss();
                }
            });

            // Add the request to the RequestQueue
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);


        });

        // Show the dialog
        alertDialog.show();
    }

    private void openDateTimePicker(EditText timeInput) {
        // Initialize Calendar
        final Calendar calendar = Calendar.getInstance();

        // DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Set the selected date
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Open TimePickerDialog after selecting the date
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                            (timeView, hourOfDay, minute) -> {
                                // Set the selected time
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                // Format the selected date and time
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                String formattedDateTime = sdf.format(calendar.getTime());

                                // Set the formatted date and time in EditText
                                timeInput.setText(formattedDateTime);
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true);
                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }




    public void Data(){

        arrayList = new ArrayList<>();
        // Initialize SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", null);





        String url = "https://nazmulofficial24.com/Task_app/pending.php?id="+userId;
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                progressBar.setVisibility(View.GONE);


                for (int x = 0; x<response.length(); x++){

                    JSONObject jsonObject = response.optJSONObject(x);
                    String id = jsonObject.optString("ID");
                    String title = jsonObject.optString("title");
                    String description = jsonObject.optString("description");
                    String time = jsonObject.optString("time");
                    String priority = jsonObject.optString("priority");
                    String location = jsonObject.optString("location");

                    hashMap = new HashMap<>();
                    hashMap.put("title",title);
                    hashMap.put("id",id);
                    hashMap.put("description",description);
                    hashMap.put("time",time);
                    hashMap.put("priority",priority);
                    hashMap.put("location",location);
                    arrayList.add(hashMap);

                }

                Myadapter myadapter = new Myadapter();
                gridView.setAdapter(myadapter);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(arrayRequest);

    }


    public class Myadapter  extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View myview = getLayoutInflater().inflate(R.layout.task_list,null);




            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please Wait....."); // Set the message
            progressDialog.setCancelable(false); // Make it non-cancelable




            TextView title = myview.findViewById(R.id.title);
            TextView description = myview.findViewById(R.id.description);
            TextView priority = myview.findViewById(R.id.priority);
            TextView time = myview.findViewById(R.id.time);
            TextView location = myview.findViewById(R.id.location);

            ImageView completeBtn = myview.findViewById(R.id.completeBtn);
            ImageView editBtn = myview.findViewById(R.id.editBtn);
            ImageView deleteBtn = myview.findViewById(R.id.deleteBtn);


            hashMap = arrayList.get(position);
            String id = hashMap.get("id");
            String title1 = hashMap.get("title");
            String description1 = hashMap.get("description");
            String priority1 = hashMap.get("priority");
            String time1 = hashMap.get("time");
            String location1 = hashMap.get("location");


            title.setText(title1);
            description.setText(description1);
            priority.setText(priority1);
            time.setText(time1);
            location.setText(location1);


            if (priority1.equals("High")) {
                priority.setTextColor(Color.RED); // টেক্সটের রং লাল হবে
            } else if (priority1.equals("Medium")) {
                priority.setTextColor(Color.GREEN); // টেক্সটের রং হলুদ হবে
            } else {
                priority.setTextColor(Color.parseColor("#FFA500"));
            }





            completeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    progressDialog.show();

                    String url = "https://nazmulofficial24.com/Task_app/completed.php?id="+id;
                    StringRequest stringRequest =  new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            progressDialog.dismiss();
                            if (response.equals("Update Successfully")){
                                Toast.makeText(getActivity(), "Completed", Toast.LENGTH_SHORT).show();
                                //getActivity().recreate();
                                Data();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            progressDialog.dismiss();
                        }
                    });

                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    requestQueue.add(stringRequest);


                }
            });








            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    showCustomAlertDialogForEdit(id,title1,description1,time1,priority1,location1);


                }
            });






           deleteBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {


                   ProgressDialog progressDialog = new ProgressDialog(getActivity());
                   progressDialog.setMessage("Please Wait....."); // Set the message
                   progressDialog.setCancelable(false); // Make it non-cancelable




                   new AlertDialog.Builder(getActivity())
                          .setTitle("Delete Task")
                          .setMessage("Are you sure you want to delete this task?")
                          .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {


                                  progressDialog.show();

                                  String url = "https://nazmulofficial24.com/Task_app/delete.php?id="+id;
                                  StringRequest stringRequest  = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                      @Override
                                      public void onResponse(String response) {

                                          progressDialog.dismiss();
                                          if (response.equals("Deleted Successfully")){
                                              Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                                              //getActivity().recreate();
                                              Data();
                                          }


                                      }
                                  }, new Response.ErrorListener() {
                                      @Override
                                      public void onErrorResponse(VolleyError error) {

                                          progressDialog.dismiss();
                                      }
                                  });


                                  RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                  requestQueue.add(stringRequest);
                              }
                          })
                          .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  dialog.dismiss();
                              }
                          })
                          .show();

               }
           });



            return myview;
        }
    }

    private void showCustomAlertDialogForEdit( String id,String title_form_db,String description_form_db,String time_form_db,String priority_form_db,String location_form_db) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait....."); // Set the message
        progressDialog.setCancelable(false); // Make it non-cancelable





        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.custom_dialog, null); // custom_dialog.xml ইনফ্লেট করুন

        // Initialize views inside the custom dialog layout
        EditText titleInput = dialogView.findViewById(R.id.titleInput);
        EditText descriptionInput = dialogView.findViewById(R.id.descriptionInput);
        EditText timeInput = dialogView.findViewById(R.id.timeInput);
        Spinner prioritySpinner = dialogView.findViewById(R.id.prioritySpinner);
        EditText locationInput = dialogView.findViewById(R.id.locationInput);

        titleInput.setText(title_form_db);
        descriptionInput.setText(description_form_db);
        prioritySpinner.setSelection(priority_form_db.equals("High") ? 0 : priority_form_db.equals("Medium") ? 1 : 2);
        timeInput.setText(time_form_db);
        locationInput.setText(location_form_db);



        


        Button submitButton = dialogView.findViewById(R.id.submitButton);

        // Handle DateTime Picker for `timeInput`
        timeInput.setOnClickListener(v -> openDateTimePicker(timeInput));

        // Get Spinner selected item
        final String[] selectedPriority = new String[1];
        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPriority[0] = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPriority[0] = "Medium"; // Default value
            }
        });

        // Build AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        // Set click listener for Submit button
        submitButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString();
            String description = descriptionInput.getText().toString();
            String time = timeInput.getText().toString();
            String priority = selectedPriority[0];
            String location = locationInput.getText().toString();



            // Validate inputs
            if (title.isEmpty() || description.isEmpty() || time.isEmpty() || location.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }


            // Initialize SharedPreferences
            sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String userId = sharedPreferences.getString("user_id", null);


            progressDialog.show();
            // Your server URL
            String url = "https://nazmulofficial24.com/Task_app/update.php?title=" + title +
                    "&description=" + description +
                    "&time=" + time +
                    "&priority=" + priority +
                    "&location=" + location+
                    "&id=" +id;




            // Create StringRequest
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("Update Successfully")) {
                        Toast.makeText(getActivity(), "Task Update Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        // Close the dialog
                        alertDialog.dismiss();
                        Data();

                    } else {
                        Toast.makeText(getActivity(), "Failed to Update task", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        // Close the dialog
                        alertDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    // Close the dialog
                    alertDialog.dismiss();
                }
            });

            // Add the request to the RequestQueue
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);



        });

        // Show the dialog
        alertDialog.show();
    }

    private void openDateTimePickerForEdit(EditText timeInput) {
        // Initialize Calendar
        final Calendar calendar = Calendar.getInstance();

        // DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Set the selected date
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Open TimePickerDialog after selecting the date
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                            (timeView, hourOfDay, minute) -> {
                                // Set the selected time
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                // Format the selected date and time
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                String formattedDateTime = sdf.format(calendar.getTime());

                                // Set the formatted date and time in EditText
                                timeInput.setText(formattedDateTime);
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true);
                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }




}

