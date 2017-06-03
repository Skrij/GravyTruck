package com.example.skrij.gravytruck.fragments;

/**
 * Created by skrij on 24/05/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.example.skrij.gravytruck.R;
import com.example.skrij.gravytruck.helpers.JsonObjectRequestWithNull;
import com.example.skrij.gravytruck.helpers.SystemPrefsHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class InformationFragment extends Fragment {

    TextView foodTruckName;
    TextView foodTruckAddress;
    TextView foodTruckLikes;
    TextView foodTruckFood;
    TextView foodTruckHour;
    TextView foodTruckDistance;
    ImageButton fav;
    ImageButton comment;
    ImageButton phone;

    private String truck_hash;
    private String truck_phone;
    private int likes;

    public static final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences settings;

    public boolean isLiked = false;
    public String likeHash;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();

        // Get the shared preferences
        settings = getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        View view = lf.inflate(R.layout.fragment_information, container, false);

        foodTruckName = (TextView) view.findViewById(R.id.foodtuck_name_textview);
        foodTruckAddress = (TextView) view.findViewById(R.id.foodtuck_address_textview);
        foodTruckLikes = (TextView) view.findViewById(R.id.foodtuck_likes_textview);
        foodTruckFood = (TextView) view.findViewById(R.id.foodtuck_food_textview);
        foodTruckHour = (TextView) view.findViewById(R.id.foodtuck_hours_textview);
        foodTruckDistance = (TextView) view.findViewById(R.id.foodtuck_distance_textview);
        fav = (ImageButton) view.findViewById(R.id.fav_button);
        comment = (ImageButton) view.findViewById(R.id.comment_button);
        phone = (ImageButton) view.findViewById(R.id.phone_button);

        // When the user clicks on the fav button --> like or dislike the truck
        fav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isLiked) {

                    RequestQueue queue = Volley.newRequestQueue(InformationFragment.this.getContext());
                    String url = getResources().getString(R.string.api_address) + "/likes?key=" + getResources().getString(R.string.api_key);

                    // Get data
                    HashMap<String, String> params = new HashMap<>();
                    params.put("user_hash", SystemPrefsHelper.getInstance(InformationFragment.this.getContext()).getSystemPrefString("hash"));
                    params.put("truck_hash", truck_hash);

                    JsonObjectRequestWithNull request_json = new JsonObjectRequestWithNull(url, new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        likeHash = response.getString("hash");
                                        likes = likes + 1;
                                        foodTruckLikes.setText(String.valueOf(likes));
                                        UpdateLike(!isLiked, likeHash);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e("Error: ", error.getMessage());
                        }
                    });
                    queue.add(request_json);
                } else {

                    RequestQueue queue = Volley.newRequestQueue(InformationFragment.this.getContext());
                    String url = getResources().getString(R.string.api_address) + "/like/" + likeHash + "?key=" + getResources().getString(R.string.api_key);

                    // Get data
                    HashMap<String, String> params = new HashMap<>();
                    params.put("user_hash", SystemPrefsHelper.getInstance(InformationFragment.this.getContext()).getSystemPrefString("hash"));
                    params.put("truck_hash", truck_hash);

                    JsonObjectRequestWithNull request_json = new JsonObjectRequestWithNull(Request.Method.DELETE, url, new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    likes = likes - 1;
                                    foodTruckLikes.setText(String.valueOf(likes));
                                    UpdateLike(!isLiked, likeHash);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e("Error: ", error.getMessage());
                        }
                    });
                    queue.add(request_json);
                }
            }
        });

        // When the user clicks to the comment button
        // Go to the comment child fragment
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentFragment parentFrag = ((ParentFragment) InformationFragment.this.getParentFragment());
                parentFrag.ChangeToCommandChildFragment();
            }
        });

        // When the user clicks to the phone button
        // Open the phone application with the foodtruck's number
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use intent.ACTION_CALL to call directly
                Intent telephone_intent = new Intent(Intent.ACTION_DIAL);
                telephone_intent.setData(Uri.parse("tel:" + truck_phone));
                startActivity(telephone_intent);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    // Update the informations fragment with all the data
    void Update(JSONObject jsonData, String _truckDistance) {
        if (jsonData == null)
            return;
        try {
            truck_hash = jsonData.getString("hash");
            truck_phone = jsonData.getString("phone");
            if (_truckDistance != null) {
                foodTruckDistance.setText(_truckDistance);
            }
            if (foodTruckName != null) {
                foodTruckName.setText(jsonData.getString("name"));
            }
            if (foodTruckAddress != null) {
                String[] separatedAddress_1 = jsonData.getString("address").split(",");
                String[] separatedAddress_2 = separatedAddress_1[1].split(" ");
                foodTruckAddress.setText(separatedAddress_1[0] + " - " + separatedAddress_2[1]);
            }
            if (foodTruckLikes != null) {
                likes = jsonData.getInt("likes");
                foodTruckLikes.setText(jsonData.getString("likes"));
            }
            if (foodTruckFood != null) {
                String upperString = jsonData.getString(("food"));
                String food = upperString.substring(0, 1).toUpperCase() + upperString.substring(1);
                foodTruckFood.setText(food);
            }
            if (foodTruckHour != null) {
                foodTruckHour.setText(jsonData.getString("opening_time") + " - " + jsonData.getString("closing_time"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void UpdateLike(boolean _isLiked, String _likeHash) {
        if (_likeHash != null)
            likeHash = _likeHash;
        isLiked = _isLiked;
        if (_isLiked)
            fav.setImageResource(R.drawable.fav_full);
        else
            fav.setImageResource(R.drawable.fav_stroke);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnFragmentInteractionListener)) {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromChildFragment(Uri uri);
    }

}