package com.example.skrij.gravytruck;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.skrij.gravytruck.helpers.SystemPrefsHelper;

import org.json.JSONObject;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Set the content
        setContentView(R.layout.activity_sign_in);

        // Give a custom typography to the title
        TextView title = (TextView) findViewById(R.id.app_title_textview);
        Typeface oaf = Typeface.createFromAsset(getAssets(), "fonts/OAF.otf");
        title.setTypeface(oaf);

        // Get the EditViews and the button
        final EditText usernameEditText = (EditText) findViewById(R.id.signin_edittext_username);
        final EditText passwordEditText = (EditText) findViewById(R.id.signin_edittext_password);
        final Button sign_in_button = (Button) findViewById(R.id.sign_in_button);

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                boolean result = false;

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    final String username = usernameEditText.getText().toString();
                    final String password = passwordEditText.getText().toString();

                    RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
                    String url = getResources().getString(R.string.api_address) + "/login?key=" + getResources().getString(R.string.api_key);

                    // Conditions
                    if (username.length() < 2 || password.length() < 5) {
                        Toast.makeText(SignInActivity.this, "Nom (2) ou mot de passe (5) trop courts !", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    // Get data
                    HashMap<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("password", password);

                    JsonObjectRequest request_json = new JsonObjectRequest(url, new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        // Set the aaccess_token
                                        SystemPrefsHelper.getInstance(SignInActivity.this).setSystemPrefString("hash", response.getString("hash"));
                                        SystemPrefsHelper.getInstance(SignInActivity.this).setSystemPrefString("username", username);
                                        // You pass
                                        Toast.makeText(SignInActivity.this, "It's been a long time o/", Toast.LENGTH_SHORT).show();
                                        // Go to MapsActivity
                                        Intent sign_in_intent = new Intent(SignInActivity.this, MapsActivity.class);
                                        startActivity(sign_in_intent);
                                    } catch (Exception e) {
                                        Toast.makeText(SignInActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SignInActivity.this, "Nom ou mot de passe invalides", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // Add the request object to the queue to be executed
                    queue.add(request_json);

                    result = true;
                }
                return result;
            }
        });

        sign_in_button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

                switch (arg1.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        sign_in_button.setBackground(ContextCompat.getDrawable(SignInActivity.this, R.drawable.gradient_button_clicked));

                        final String username = usernameEditText.getText().toString();
                        final String password = passwordEditText.getText().toString();

                        RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
                        String url = getResources().getString(R.string.api_address) + "/login?key=" + getResources().getString(R.string.api_key);

                        // Conditions
                        if (username.length() < 2 || password.length() < 5) {
                            Toast.makeText(SignInActivity.this, "Nom (2) ou mot de passe (5) trop courts !", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        // Get data
                        HashMap<String, String> params = new HashMap<>();
                        params.put("username", username);
                        params.put("password", password);

                        JsonObjectRequest request_json = new JsonObjectRequest(url, new JSONObject(params),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            // You pass
                                            Toast.makeText(SignInActivity.this, "It's been a long time o/", Toast.LENGTH_SHORT).show();
                                            // Set the aaccess_token
                                            SystemPrefsHelper.getInstance(SignInActivity.this).setSystemPrefString("hash", response.getString("hash"));
                                            SystemPrefsHelper.getInstance(SignInActivity.this).setSystemPrefString("username", username);
                                            // Go to MapsActivity
                                            Intent sign_in_intent = new Intent(SignInActivity.this, MapsActivity.class);
                                            startActivity(sign_in_intent);
                                        } catch (Exception e) {
                                            Toast.makeText(SignInActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(SignInActivity.this, "Nom ou mot de passe invalides !", Toast.LENGTH_SHORT).show();
                            }
                        });
                        // Add the request object to the queue to be executed
                        queue.add(request_json);
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        sign_in_button.setBackground(ContextCompat.getDrawable(SignInActivity.this, R.drawable.button_background));
                        break;

                    case MotionEvent.ACTION_UP:
                        sign_in_button.setBackground(ContextCompat.getDrawable(SignInActivity.this, R.drawable.button_background));
                        break;
                }
                return true;
            }

        });

        // Hide the keyboard when the user clicks outside
        usernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        // Hide the keyboard when the user clicks outside
        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
