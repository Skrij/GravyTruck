package com.example.skrij.gravytruck;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ThanksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);

        TextView title = (TextView) findViewById(R.id.thanks_title_textview);
        Typeface oaf = Typeface.createFromAsset(getAssets(), "fonts/OAF.otf");
        title.setTypeface(oaf);

        final Button thanks_button = (Button) findViewById(R.id.thanks_button);

        thanks_button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                switch (arg1.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        thanks_button.setBackground(ContextCompat.getDrawable(ThanksActivity.this, R.drawable.gradient_button_clicked));
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        thanks_button.setBackground(ContextCompat.getDrawable(ThanksActivity.this, R.drawable.button_background));
                        break;

                    case MotionEvent.ACTION_UP:
                        thanks_button.setBackground(ContextCompat.getDrawable(ThanksActivity.this, R.drawable.button_background));
                        Intent maps_intent = new Intent(ThanksActivity.this, MapsActivity.class);
                        startActivity(maps_intent);
                        break;
                }
                return true;
            }
        });
    }
}
