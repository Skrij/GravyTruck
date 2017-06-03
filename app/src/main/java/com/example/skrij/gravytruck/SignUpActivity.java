package com.example.skrij.gravytruck;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class SignUpActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Set the content
        setContentView(R.layout.activity_sign_up);

        // Give a custom typography to the title
        TextView title = (TextView) findViewById(R.id.app_title_textview);
        Typeface oaf = Typeface.createFromAsset(getAssets(), "fonts/OAF.otf");
        title.setTypeface(oaf);

        // Get the EditViews and the button
        final EditText usernameEditText = (EditText) findViewById(R.id.singup_edittext_username);
        final EditText passwordEditText = (EditText) findViewById(R.id.signup_edittext_password);
        final Button sign_up_button = (Button) findViewById(R.id.sign_up_button);

        // Save
        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        passwordEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                boolean result = false;

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    final String username = usernameEditText.getText().toString();
                    final String password = passwordEditText.getText().toString();

                    RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
                    String url = getResources().getString(R.string.api_address) + "/register?key=" + getResources().getString(R.string.api_key);

                    // Conditions
                    if (username.length() < 2 || password.length() < 5) {
                        Toast.makeText(SignUpActivity.this, "Nom (2) ou mot de passe (5) trop courts !", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(SignUpActivity.this, "Welcome on board o/", Toast.LENGTH_SHORT).show();
                                        // Set the hash_token
                                        SystemPrefsHelper.getInstance(SignUpActivity.this).setSystemPrefString("hash", response.getString("hash"));
                                        SystemPrefsHelper.getInstance(SignUpActivity.this).setSystemPrefString("username", username);
                                        //Go to MapsActivity
                                        Intent sign_up_intent = new Intent(SignUpActivity.this, MapsActivity.class);
                                        startActivity(sign_up_intent);
                                    } catch (Exception e) {
                                        Toast.makeText(SignUpActivity.this, "Something went wrong :( : ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SignUpActivity.this, "Cet utilisateur existe déjà !", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // Add the request object to the queue to be executed
                    queue.add(request_json);

                    result = true;
                }
                return result;
            }
        });

        sign_up_button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

                switch (arg1.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        sign_up_button.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.gradient_button_clicked));

                        final String username = usernameEditText.getText().toString();
                        final String password = passwordEditText.getText().toString();

                        RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
                        String url = getResources().getString(R.string.api_address) + "/register?key=" + getResources().getString(R.string.api_key);

                        // Conditions
                        if (username.length() < 2 || password.length() < 5) {
                            Toast.makeText(SignUpActivity.this, "Nom (2) ou mot de passe (5) trop courts !", Toast.LENGTH_SHORT).show();
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
                                            // Set the hash_token
                                            SystemPrefsHelper.getInstance(SignUpActivity.this).setSystemPrefString("hash", response.getString("hash"));
                                            SystemPrefsHelper.getInstance(SignUpActivity.this).setSystemPrefString("username", username);
                                            Toast.makeText(SignUpActivity.this, "Welcome on board o/", Toast.LENGTH_SHORT).show();
                                            //Go to MapsActivity
                                            Intent sign_up_intent = new Intent(SignUpActivity.this, MapsActivity.class);
                                            startActivity(sign_up_intent);
                                        } catch (Exception e) {
                                            Toast.makeText(SignUpActivity.this, "Something went wrong :( ", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(SignUpActivity.this, "Cet utilisateur existe déjà !", Toast.LENGTH_SHORT).show();
                            }
                        });
                        // Add the request object to the queue to be executed
                        queue.add(request_json);
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        sign_up_button.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.button_background));
                        break;

                    case MotionEvent.ACTION_UP:
                        sign_up_button.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.button_background));
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


