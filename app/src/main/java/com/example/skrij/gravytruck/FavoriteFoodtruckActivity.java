package com.example.skrij.gravytruck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.skrij.gravytruck.adapters.FavoriteFoodtruckAdapter;
import com.example.skrij.gravytruck.helpers.SystemPrefsHelper;
import com.example.skrij.gravytruck.informations.FavoriteFoodtruckInformation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFoodtruckActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FavoriteFoodtruckAdapter mAdapter;
    private String systemPrefUserHash;
    private JsonObjectRequest request_json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_foodtrucks);

        // Get the user's hash
        systemPrefUserHash = SystemPrefsHelper.getInstance(FavoriteFoodtruckActivity.this).getSystemPrefString("hash");

        mRecyclerView = (RecyclerView) findViewById(R.id.favorite_recycler_view);
        Update();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public ArrayList<FavoriteFoodtruckInformation> getData() {

        final ArrayList<FavoriteFoodtruckInformation> data = new ArrayList<>();

        // Will have to rework this. Pretty bad request in request in order to just get a truck's name
        RequestQueue queue = Volley.newRequestQueue(FavoriteFoodtruckActivity.this);
        String url = getResources().getString(R.string.api_address) + "/user/" + systemPrefUserHash + "/likes?key=" + getResources().getString(R.string.api_key);

        request_json = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Get the user's likes
                            JSONArray likes = response.getJSONArray("likes");
                            for (int i = 0; i < likes.length(); i++) {
                                JSONObject obj = likes.getJSONObject(i);
                                String truck_hash = obj.getString("truck_hash");

                                // Get the truck hash to access its informations
                                RequestQueue queue_2 = Volley.newRequestQueue(FavoriteFoodtruckActivity.this);
                                String url_2 = getResources().getString(R.string.api_address) + "/truck/" + truck_hash + "?key=" + getResources().getString(R.string.api_key);

                                request_json = new JsonObjectRequest(url_2, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    // Get the name
                                                    String truck_name = response.getString("name");
                                                    FavoriteFoodtruckInformation favInfo = new FavoriteFoodtruckInformation(truck_name);
                                                    data.add(favInfo);
                                                    mAdapter = new FavoriteFoodtruckAdapter(FavoriteFoodtruckActivity.this, data);
                                                    mRecyclerView.setAdapter(mAdapter);
                                                    mRecyclerView.invalidate();
                                                } catch (Exception e) {
                                                    Toast.makeText(FavoriteFoodtruckActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        VolleyLog.e("Error: ", error.getMessage());
                                    }
                                });
                                // Add the request object to the queue to be executed
                                queue_2.add(request_json);
                            }
                        } catch (Exception e) {
                            Toast.makeText(FavoriteFoodtruckActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        // Add the request object to the queue to be executed
        queue.add(request_json);

        return data;
    }

    void Update() {
        List<FavoriteFoodtruckInformation> mi = getData();
        mAdapter = new FavoriteFoodtruckAdapter(this, mi);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.invalidate();
    }

}
