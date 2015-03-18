package com.gmail.slybarrack.practicelog.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by natalie on 6/20/14.
 */
public class UpdatePracticeActivity extends Activity{

        MyLog passLog = new MyLog();
        TextView dateTextView = null;
        Chronometer mChronometer = null;
        Button startButt = null;
        Button stopButt = null;
        Button doneButt = null;
        Button deleteButton = null;
        long timeWhenStopped = 0;
        EditText mText = null;
        MySQLiteHelper db = new MySQLiteHelper(this);
        String chronoText;
        String chronoInstanceText;
        int stoppedMilliseconds = 0;



    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.update_practice);


            final int value = (int) getIntent().getExtras().getLong("id");

            Log.v("ID", String.valueOf(value));

            passLog = db.getLog(value);

            final String date = passLog.getDate();
            String notes = passLog.getNotes();
            String timer =  passLog.getTimer();

            db.getTimerData();

            //Date

            dateTextView = (TextView)findViewById(R.id.dateText);

            dateTextView.setText(date);


            //EditText

            mText = (EditText)findViewById(R.id.notes);

            mText.setText(notes);


            //Chronometer

            mChronometer = (Chronometer)findViewById(R.id.chrono);

            if(savedInstanceState != null){
                chronoInstanceText = savedInstanceState.getString("ChronoText");
                mChronometer.setText(chronoInstanceText);
                String array[] = chronoInstanceText.split(":");

                if(array.length == 2){
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000 + Integer.parseInt(array[1]) * 1000;
                }else{
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000 + Integer.parseInt(array[1]) * 60 * 1000 + Integer.parseInt(array[2]) * 1000;
                }

                mChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
                mChronometer.start();
            }
            else {
                mChronometer.setText(timer);
            }

            startButt = (Button)findViewById(R.id.startButt);
            stopButt = (Button)findViewById(R.id.stopButt);
            timeWhenStopped = mChronometer.getBase() - SystemClock.elapsedRealtime();


            stopButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timeWhenStopped = mChronometer.getBase() - SystemClock.elapsedRealtime();
                    mChronometer.stop();
                }
            });



            startButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    chronoText = mChronometer.getText().toString();

                    String array[] = chronoText.split(":");

                    if(array.length == 2){
                        stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000 + Integer.parseInt(array[1]) * 1000;
                    }else{
                        stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000 + Integer.parseInt(array[1]) * 60 * 1000 + Integer.parseInt(array[2]) * 1000;
                    }

                    mChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
                    mChronometer.start();
                }



            });


            mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer cArg) {
                    long t = SystemClock.elapsedRealtime() - cArg.getBase();
                    int h = (int) (t / 3600000);
                    int m = (int) (t - h * 3600000) / 60000;
                    int s = (int) (t - h * 3600000 - m * 60000) / 1000;
                    String hh = h < 10 ? "0" + h : h + "";
                    String mm = m < 10 ? "0" + m : m + "";
                    String ss = s < 10 ? "0" + s : s + "";
                    cArg.setText(hh + ":" + mm + ":" + ss);
                }
            });



            //Update Button

            doneButt = (Button)findViewById(R.id.doneButt);

            doneButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String notes = mText.getText().toString();

                    String timer = mChronometer.getText().toString();

                    db.updateLog(passLog, notes, timer);




                    Intent i = new Intent(getApplicationContext(), PreviousPractices.class);

                    startActivity(i);

                }
            });

            deleteButton = (Button)findViewById(R.id.deleteButt);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.deleteLog(passLog);
                    Intent i = new Intent(getApplicationContext(), PreviousPractices.class);
                    startActivity(i);
                }
            });


        }

        //Code to hide keyboard from Daniel @ Stack Overflow

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {

            View v = getCurrentFocus();
            boolean ret = super.dispatchTouchEvent(event);

            if (v instanceof EditText) {
                View w = getCurrentFocus();
                int scrcoords[] = new int[2];
                w.getLocationOnScreen(scrcoords);
                float x = event.getRawX() + w.getLeft() - scrcoords[0];
                float y = event.getRawY() + w.getTop() - scrcoords[1];

                Log.d("Activity", "Touch event " + event.getRawX() + "," + event.getRawY() + " " + x + "," + y + " rect " + w.getLeft() + "," + w.getTop() + "," + w.getRight() + "," + w.getBottom() + " coords " + scrcoords[0] + "," + scrcoords[1]);
                if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())  ) {

                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                }
            }
            return ret;
        }



        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()) {
                // Respond to the action bar's Up/Home button
                case android.R.id.home:
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("ChronoText", mChronometer.getText().toString());
        super.onSaveInstanceState(outState);
    }
}



