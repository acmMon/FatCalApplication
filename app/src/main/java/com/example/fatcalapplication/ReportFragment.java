package com.example.fatcalapplication;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fatcalapplication.Entity.Appuser;
import com.example.fatcalapplication.Entity.Consumption;
import com.example.fatcalapplication.Entity.Food;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReportFragment extends Fragment {

    private View vReport;
    private EditText mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private PieChart reportPieChart;

    private float totalCalConsumed;
    private float totalCalBurnt;
    private float remainingCalories;

    private float[] yData ;
    private String[] xData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vReport = inflater.inflate(R.layout.fragment_report, container, false);

        mDisplayDate = (EditText) vReport.findViewById(R.id.et_report_date);

        reportPieChart = (PieChart) vReport.findViewById(R.id.reportPieChart);

        yData = new float[3];
        xData = new String[3];

        //reportPieChart.setDescription("Calorie Information (In percentage)");
        reportPieChart.setRotationEnabled(true);
        reportPieChart.getDescription().setText("My Calorie Information");
        reportPieChart.setHoleRadius(25f);
        reportPieChart.setTransparentCircleAlpha(0);
        reportPieChart.setCenterText("Calorie Information");
        reportPieChart.setCenterTextSize(10);
        reportPieChart.setDrawEntryLabels(true);

        //addDataForReport();

        reportPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

        mDisplayDate.setFocusable(false);
        mDisplayDate.setClickable(true);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int currentYear = cal.get(Calendar.YEAR);
                int currentMonth = cal.get(Calendar.MONTH);
                int currentDay = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        currentYear,currentMonth,currentDay);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String monthVar = "";
                String dayVar = "";



                month = month + 1;

                if(String.valueOf(month).length()==1)
                    monthVar =  "0" + month;
                else
                    monthVar = String.valueOf(month);

                if(String.valueOf(day).length()==1)
                    dayVar = "0" + day;
                else
                    dayVar = String.valueOf(day);

                String date = year + "-" + monthVar + "-" +dayVar;
                mDisplayDate.setText(date);

                CreatePieReportAsync createPieReportAsync = new CreatePieReportAsync();
                createPieReportAsync.execute(date);


            }
        };




        return vReport;

    }

    private void addDataForReport() {

        ArrayList<PieEntry> yEntrys = new ArrayList<>();


        for(int i = 0; i< yData.length; i++)
        {
            yEntrys.add(new PieEntry(yData[i],xData[i]));
        }



        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.RED);
        colors.add(Color.MAGENTA);

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = reportPieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);


        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        reportPieChart.setData(pieData);
        reportPieChart.invalidate();
    }

    private class CreatePieReportAsync extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params) {
            SharedPreferences userDetails =
                    getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
            int userid = userDetails.getInt("userid",0);
            return RestClient.fetchBarReportValues(userid, params[0],params[1]);
        }
        @Override
        protected void onPostExecute(String response) {

            try {
                JSONArray jsonarray = new JSONArray(response);
                JSONObject jsonobject = jsonarray.getJSONObject(0);
                if(jsonobject!=null)
                {

                        totalCalConsumed = Float.parseFloat(jsonobject.getString("totalcalconsumed"));
                        totalCalBurnt = Float.parseFloat(jsonobject.getString("totalcalburned"));
                        remainingCalories = Float.parseFloat(jsonobject.getString("remainingCalorie"));
                        float totalCalConsPercent = (totalCalConsumed/(totalCalConsumed + totalCalBurnt + remainingCalories)) * 100;
                        yData[0]= totalCalConsPercent;
                        xData[0] = "Total Calories Consumed";

                        float totalCalBurntPercent = (totalCalBurnt/(totalCalConsumed + totalCalBurnt + remainingCalories)) * 100;
                        yData[1] = totalCalBurntPercent;
                        xData[1] = "Total Calories Burnt";

                        float remainingCalPercent = (remainingCalories/(totalCalConsumed + totalCalBurnt + remainingCalories)) * 100;
                        yData[2] = remainingCalPercent;
                        xData[2] = "Remaining Calories";


                        addDataForReport();


                }





            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
