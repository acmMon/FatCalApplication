package com.example.fatcalapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

public class BarReportFragment extends Fragment
{
    private View vBarChart;
    private EditText mStartDate;
    private EditText mEndDate;
    private DatePickerDialog.OnDateSetListener mStartDateListener;
    private DatePickerDialog.OnDateSetListener mEndDateListener;
    private BarChart reportBarChart;
    private Button generateReport;
    private  ArrayList<BarEntry> calConsBarEntryList ;
    private  ArrayList<BarEntry> calBurntBarEntryList ;
    private String[] reportDates ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vBarChart = inflater.inflate(R.layout.bar_chart, container, false);
        mStartDate = (EditText) vBarChart.findViewById(R.id.et_report_startDate);
        mEndDate = (EditText) vBarChart.findViewById(R.id.et_report_endDate);
        reportBarChart = (BarChart) vBarChart.findViewById(R.id.reportBarChart);
        generateReport = (Button)vBarChart.findViewById(R.id.bt_generate_report);

        reportBarChart.setDrawBarShadow(false);
        reportBarChart.setDrawValueAboveBar(true);
        reportBarChart.setMaxVisibleValueCount(5);
        reportBarChart.setPinchZoom(false);
        reportBarChart.setDrawGridBackground(true);
        reportBarChart.setDragEnabled(true);

        generateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mStartDate.getText().toString()==null || "".equals(mStartDate.getText().toString()))
                {
                    Toast.makeText(vBarChart.getContext(),"Please select Start Date",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(mEndDate.getText().toString()==null || "".equals(mEndDate.getText().toString()))
                {
                    Toast.makeText(vBarChart.getContext(),"Please select End Date",Toast.LENGTH_LONG).show();
                    return;
                }

                else
                {
                    GenerateBarReportAsync generateBarReportAsync = new GenerateBarReportAsync();
                    generateBarReportAsync.execute(mStartDate.getText().toString(),mEndDate.getText().toString());
                }
            }
        });


        mStartDate.setFocusable(false);
        mStartDate.setClickable(true);

        mEndDate.setFocusable(false);
        mEndDate.setClickable(true);


        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int currentYear = cal.get(Calendar.YEAR);
                int currentMonth = cal.get(Calendar.MONTH);
                int currentDay = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mStartDateListener,
                        currentYear,currentMonth,currentDay);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mStartDateListener = new DatePickerDialog.OnDateSetListener() {

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
                mStartDate.setText(date);




            }
        };


        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int currentYear = cal.get(Calendar.YEAR);
                int currentMonth = cal.get(Calendar.MONTH);
                int currentDay = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mEndDateListener,
                        currentYear,currentMonth,currentDay);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mEndDateListener = new DatePickerDialog.OnDateSetListener() {

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
                mEndDate.setText(date);

            }
        };



        return vBarChart;
    }

    private class GenerateBarReportAsync extends AsyncTask<String, Void, String>
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
                float totalCalConsumed;
                float totalCalBurnt;
                if(jsonarray.length() > 0)
                {
                    reportDates = new String[jsonarray.length()];
                    calConsBarEntryList = new ArrayList<BarEntry>();
                    calBurntBarEntryList = new ArrayList<BarEntry>();
                    for(int count = 0; count <jsonarray.length(); count++ )
                    {
                        reportDates[count] = jsonarray.getJSONObject(count).getString("reportDate");
                        totalCalConsumed = Float.parseFloat(jsonarray.getJSONObject(count).getString("totalcalconsumed"));
                        totalCalBurnt = Float.parseFloat(jsonarray.getJSONObject(count).getString("totalcalburned"));

                        calConsBarEntryList.add(new BarEntry(count+1,totalCalConsumed));
                        calBurntBarEntryList.add(new BarEntry(count+1,totalCalBurnt));

                    }

                    BarDataSet totalCalConsDataSet = new BarDataSet(calConsBarEntryList,"Total Calorie Consumed");
                    totalCalConsDataSet.setColor(Color.RED);

                    BarDataSet totalCalBurntDataSet = new BarDataSet(calBurntBarEntryList,"Total Calories Burnt");
                    totalCalBurntDataSet.setColor(Color.YELLOW);

                    BarData multipleBarData = new BarData(totalCalConsDataSet,totalCalBurntDataSet);
                    reportBarChart.setData(multipleBarData);

                    float barSpace = 0.02f;
                    float groupSpace = 0.1f;
                    float barWidth = 0.43f;


                    multipleBarData.setBarWidth(barWidth);

                    reportBarChart.groupBars(0,groupSpace,barSpace);



                    XAxis reportDateAxis = reportBarChart.getXAxis();
                    IAxisValueFormatter formatter = new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
                            if(reportDates.length > (int) value)
                                 return reportDates[(int)value];
                            else
                                return "";
                        }
                    };
                    reportDateAxis.setValueFormatter(formatter);
                    reportDateAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    reportDateAxis.setGranularity(1);
                    reportDateAxis.setGranularityEnabled(true);
                    reportDateAxis.setCenterAxisLabels(true);
                   // reportDateAxis.setAxisMinimum(1);



                }

                else
                {
                    Toast.makeText(vBarChart.getContext(),"Sorry there are no graphs for this period",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
