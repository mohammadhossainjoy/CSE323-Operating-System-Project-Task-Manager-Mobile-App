package com.joy.taskmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Completed extends Fragment {


    ArrayList<HashMap<String,String> >  arrayList = new ArrayList<>();
    HashMap<String,String> hashMap;

    GridView gridView;

    SharedPreferences sharedPreferences;

    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_completed, container, false);


        gridView= myview.findViewById(R.id.gridView);
        progressBar = myview.findViewById(R.id.progressbar);

        Date();



        return myview;
    }


    public void Date(){


        // Initialize SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", null);





        String url = "https://nazmulofficial24.com/Task_app/completed_Task.php?id="+userId;
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


    public class Myadapter extends BaseAdapter {

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



            View myview = getLayoutInflater().inflate(R.layout.conplete_task,null);

            TextView tvtitle = myview.findViewById(R.id.title);
            TextView tvdescription = myview.findViewById(R.id.description);
            TextView tvpriority = myview.findViewById(R.id.priority);
            TextView tvtime = myview.findViewById(R.id.time);
            TextView tvlocation = myview.findViewById(R.id.location);


            hashMap = arrayList.get(position);
            String id = hashMap.get("id");
            String title = hashMap.get("title");
            String description = hashMap.get("description");
            String priority = hashMap.get("priority");
            String time = hashMap.get("time");
            String location = hashMap.get("location");


            tvtitle.setText(title);
            tvdescription.setText(description);
            tvpriority.setText(priority);
            tvtime.setText(time);
            tvlocation.setText(location);



            return myview;
        }
    }

}