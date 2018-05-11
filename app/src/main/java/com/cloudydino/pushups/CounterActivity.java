package com.cloudydino.pushups;

import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CounterActivity extends AppCompatActivity {

    private static int numberOfPushups;
    private static TextView counter;
    private static TextView incrementMessage;
    private static ConstraintLayout background;
    private static Toast toast;

    public static final String MY_GLOBAL_PREFS = "my_global_prefs";
    public static final String PUSHUPS_KEY = "number_of_pushups";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        background = (ConstraintLayout) findViewById(R.id.constraintLayout_background);
        counter = (TextView) findViewById(R.id.textView_counter);
        incrementMessage = (TextView) findViewById(R.id.textView_incrementMessage);
        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.floatingActionButton_add);
        toast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);

        SharedPreferences preferences = getSharedPreferences(MY_GLOBAL_PREFS, MODE_PRIVATE);
        numberOfPushups = preferences.getInt(PUSHUPS_KEY, 0);
        counter.setText(String.valueOf(numberOfPushups));

        background.setOnClickListener(v -> changePushups(1));

        addButton.setOnClickListener(v -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_multiple, null);

            dialogBuilder.setView(dialogView)
                    .setTitle("Add how many push-ups?")
                    .setNegativeButton("Cancel", (dI, i) -> {})
                    .setPositiveButton("Add", (dI, i) -> {
                        EditText editTextNumberOfPushups = (EditText) dialogView.findViewById(R.id.editText_numberOfPushups);
                        String changeValue = editTextNumberOfPushups.getText().toString();
                        try {
                            changePushups(Integer.parseInt(changeValue));
                        } catch (NumberFormatException e) {
                            if (changeValue.length() > 0) {
                                toast.setText("Number is too large");
                                toast.show();
                            }
                        }
                    });

            dialogBuilder.create().show();
        });
    }

    @Override
    protected void onPause() {
            super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences(MY_GLOBAL_PREFS, MODE_PRIVATE).edit();
        editor.putInt(PUSHUPS_KEY, numberOfPushups);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_counter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.button_reset:
                numberOfPushups = 0;
                updatePushups();
                return true;
            case R.id.button_subtract:
                changePushups(-1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changePushups(int change) {
        if (change > 0 && numberOfPushups + change < 0) {
            numberOfPushups = Integer.MAX_VALUE;
            toast.setText("Pushup counter can not go higher");
            toast.show();
        } else {
            numberOfPushups += change;
            if (numberOfPushups < 0) {
                numberOfPushups = 0;
            }
        }
        updatePushups();
    }

    private void updatePushups() {
        counter.setText(String.valueOf(numberOfPushups));
        if (numberOfPushups <= 0) {
            incrementMessage.setVisibility(View.VISIBLE);
        } else {
            incrementMessage.setVisibility(View.INVISIBLE);
        }
    }
}
