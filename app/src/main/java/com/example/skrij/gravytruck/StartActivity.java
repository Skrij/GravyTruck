package com.example.skrij.gravytruck;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.widget.Button;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.skrij.gravytruck.helpers.SystemPrefsHelper;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String username = SystemPrefsHelper.getInstance(this).getSystemPrefString("hash");

        if (username != null) {
            // Go to MapsActivity
            Intent sign_in_intent = new Intent(StartActivity.this, MapsActivity.class);
            startActivity(sign_in_intent);
        }

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Set content
        setContentView(R.layout.activity_start);

        // Give a custom typography to the title
        TextView title = (TextView) findViewById(R.id.app_title_textview);
        Typeface oaf = Typeface.createFromAsset(getAssets(), "fonts/OAF.otf");
        title.setTypeface(oaf);

        // Find the buttons
        final Button sign_up_button = (Button) findViewById(R.id.sign_up_button);
        final Button sign_in_button = (Button) findViewById(R.id.sign_in_button);

        sign_up_button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                switch (arg1.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sign_up_button.setBackground(ContextCompat.getDrawable(StartActivity.this, R.drawable.gradient_button_clicked));
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        sign_up_button.setBackground(ContextCompat.getDrawable(StartActivity.this, R.drawable.button_background));
                        break;

                    case MotionEvent.ACTION_UP:
                        sign_up_button.setBackground(ContextCompat.getDrawable(StartActivity.this, R.drawable.button_background));
                        Intent sign_up_intent = new Intent(StartActivity.this, SignUpActivity.class);
                        startActivity(sign_up_intent);
                        break;
                }
                return true;
            }
        });

        sign_in_button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                switch (arg1.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sign_in_button.setBackground(ContextCompat.getDrawable(StartActivity.this, R.drawable.gradient_button_clicked));
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        sign_in_button.setBackground(ContextCompat.getDrawable(StartActivity.this, R.drawable.button_background));
                        break;

                    case MotionEvent.ACTION_UP:
                        sign_in_button.setBackground(ContextCompat.getDrawable(StartActivity.this, R.drawable.button_background));
                        Intent sign_in_intent = new Intent(StartActivity.this, SignInActivity.class);
                        startActivity(sign_in_intent);
                        break;
                }
                return true;
            }
        });
    }
}
