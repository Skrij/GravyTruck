package com.example.skrij.gravytruck;

import android.app.Activity;
import android.content.Intent;
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

import com.example.skrij.gravytruck.helpers.SystemPrefsHelper;

public class AddFoodtruckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_foodtruck);

        final EditText foodtruckNameSuggestionEditText = (EditText) findViewById(R.id.foodtruck_name_suggestion_edittext);
        final EditText foodtrucAddressSuggestionEditText = (EditText) findViewById(R.id.foodtruck_address_suggestion_edittext);
        final EditText foodtrucOtherSuggestionEditText = (EditText) findViewById(R.id.foodtruck_other_suggestion_edittext);
        final Button send_suggestion_button = (Button) findViewById(R.id.send_suggestion_button);

        foodtrucOtherSuggestionEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                boolean result = false;

                if (actionId == EditorInfo.IME_ACTION_DONE) {

//                    final String foodtruckNameSuggestion = foodtruckNameSuggestionEditText.getText().toString();
//                    final String foodtrucAddressSuggestion = foodtrucAddressSuggestionEditText.getText().toString();
//                    final String foodtrucOtherSuggestion = foodtrucOtherSuggestionEditText.getText().toString();
//                    final String systemPrefUserName = SystemPrefsHelper.getInstance(AddFoodtruckActivity.this).getSystemPrefString("username");

                    // Need to create an email client to it directly

//                    Intent mEmail = new Intent(Intent.ACTION_SEND);
//                    mEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
//                    mEmail.putExtra(Intent.EXTRA_SUBJECT, "Nouveau foodtruck : " + foodtruckNameSuggestion);
//                    mEmail.putExtra(Intent.EXTRA_TEXT, "Nom : " + foodtruckNameSuggestion
//                            + "\nAdresse : " + foodtrucAddressSuggestion
//                            + "\nOther : " + foodtrucOtherSuggestion
//                            + "\nDe la part de : " + systemPrefUserName);
//                    mEmail.setType("message/rfc822");
//                    startActivity(mEmail);
//
//                    System.out.println("Envoyé o/");

                    Intent thanks_intent = new Intent(AddFoodtruckActivity.this, ThanksActivity.class);
                    startActivity(thanks_intent);

                    result = true;
                }
                return result;
            }
        });


        send_suggestion_button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

                switch (arg1.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        send_suggestion_button.setBackground(ContextCompat.getDrawable(AddFoodtruckActivity.this, R.drawable.gradient_button_clicked));

//                        final String foodtruckNameSuggestion = foodtruckNameSuggestionEditText.getText().toString();
//                        final String foodtrucAddressSuggestion = foodtrucAddressSuggestionEditText.getText().toString();
//                        final String foodtrucOtherSuggestion = foodtrucOtherSuggestionEditText.getText().toString();
//                        final String systemPrefUserName = SystemPrefsHelper.getInstance(AddFoodtruckActivity.this).getSystemPrefString("username");

                        // Need to create an email client to it directly

//                        Intent mEmail = new Intent(Intent.ACTION_SEND);
//                        mEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{ "maelle.gaonac-h@hetic.net"});
//                        mEmail.putExtra(Intent.EXTRA_SUBJECT, "Nouveau foodtruck : " + foodtruckNameSuggestion);
//                        mEmail.putExtra(Intent.EXTRA_TEXT, "Nom : " + foodtruckNameSuggestion
//                                + "\nAdresse : " + foodtrucAddressSuggestion
//                                + "\nOther : " + foodtrucOtherSuggestion
//                                + "\nDe la part de : " + systemPrefUserName);
//                        mEmail.setType("message/rfc822");
//                        startActivity(mEmail);

                        Intent thanks_intent = new Intent(AddFoodtruckActivity.this, ThanksActivity.class);
                        startActivity(thanks_intent);

                    case MotionEvent.ACTION_CANCEL:
                        send_suggestion_button.setBackground(ContextCompat.getDrawable(AddFoodtruckActivity.this, R.drawable.button_background));
                        break;

                    case MotionEvent.ACTION_UP:
                        send_suggestion_button.setBackground(ContextCompat.getDrawable(AddFoodtruckActivity.this, R.drawable.button_background));
                        break;
                }
                return true;
            }

        });

        // Hide the keyboard when the user clicks outside
        foodtruckNameSuggestionEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        // Hide the keyboard when the user clicks outside
        foodtrucAddressSuggestionEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        // Hide the keyboard when the user clicks outside
        foodtrucOtherSuggestionEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
