package com.cloudydino.pushups;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        background = (ConstraintLayout) findViewById(R.id.constraintLayout_background);
        counter = (TextView) findViewById(R.id.textView_counter);
        incrementMessage = (TextView) findViewById(R.id.textView_incrementMessage);
        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.floatingActionButton_add);

        numberOfPushups = 0;
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
                                Toast.makeText(getApplicationContext(), "Number is too large", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            dialogBuilder.create().show();
        });
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
            Toast.makeText(getApplicationContext(), "Pushup counter can not go higher", Toast.LENGTH_SHORT).show();
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
