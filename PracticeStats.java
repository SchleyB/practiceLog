package com.gmail.slybarrack.practicelog.app;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;

/**
 * Created by natalie on 6/25/14.
 */
public class PracticeStats extends Activity {


    GraphicalView mChart;

    XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

    XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    ArrayList<Integer> myTimes;

    MySQLiteHelper db = new MySQLiteHelper(this);

    XYSeries mCurrentSeries;

    XYSeriesRenderer mCurrentRenderer;

    ArrayList<Integer> times;

    ArrayList<String> myDates;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_stats);


        mCurrentSeries = new XYSeries("");
        mDataset.addSeries(mCurrentSeries);
        mCurrentRenderer = new XYSeriesRenderer();
        mCurrentRenderer.setFillPoints(true);
        mCurrentRenderer.setColor(Color.parseColor("#99CC00"));
        mCurrentRenderer.setDisplayChartValues(false);
        mRenderer.addSeriesRenderer(mCurrentRenderer);
        mRenderer.setZoomButtonsVisible(false);
        mRenderer.setBarWidth(30);
        mRenderer.setBackgroundColor(Color.parseColor("#FDFDFD"));
        mRenderer.setBarSpacing(0);
        mRenderer.setYAxisMin(0);
        mRenderer.setXLabels(0);
        mRenderer.setYLabels(0);
        mRenderer.setXAxisMin(0);
        mRenderer.setXLabelsAngle(45);
        mRenderer.setXLabelsAlign(Paint.Align.LEFT);
        mRenderer.setXLabelsPadding(10);
        mRenderer.setLabelsTextSize(16);
        mRenderer.setDisplayValues(false);
        mRenderer.setShowLegend(false);
        mRenderer.setMargins(new int[]{80, 80, 80, 80});
        myTimes = db.getTimerData();
        myDates = db.getDateData();

        for(int i = 0; i < myTimes.size(); i++){
            mCurrentSeries.add(i, myTimes.get(i));
        }

        for(int i = 0; i < myDates.size(); i++){
            mRenderer.addXTextLabel(i, myDates.get(i));
        }

        TextView avgText;

        avgText = (TextView)findViewById(R.id.avgText);

        times = db.getTimerData();

        double avg = Math.round(getAverage(times));

        avgText.setText(statsText(avg));
    }

    public String statsText(double i){

        int minutes;
        int hours;
        int seconds = (int)i / 1000;
        if(seconds >= 60){
            minutes = Math.round(seconds / 60);
            seconds = seconds % 60;
        }
        else {
            minutes = 0;
        }
        if(minutes >= 60){
            hours = Math.round(minutes/60);
            minutes = minutes % 60;
        }
        else {
            hours = 0;
        }
        return "Your average practice time is: " + hours + " Hours, " + minutes + " Minutes " + seconds + " Seconds.";
    }

    public double getAverage(ArrayList<Integer> list){

        Integer sum = 0;
        double avg;

        for(Integer i = 0; i <list.size(); i++){
            sum = sum + list.get(i);
        }

        avg = (double) sum / list.size();

        return avg;
    }


        protected void onResume() {
            super.onResume();
            LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
            if (mChart == null) {
                mChart = ChartFactory.getBarChartView(this, mDataset, mRenderer, BarChart.Type.STACKED);
                layout.addView(mChart);
            } else {
                mChart.repaint();
            }
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


}
