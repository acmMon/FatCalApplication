package com.example.fatcalapplication;

import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fatcalapplication.Entity.Appuser;
import com.example.fatcalapplication.Entity.Credential;
import com.example.fatcalapplication.Entity.Report;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StepsFragment extends Fragment {
    View vSteps;
    UserStepsDatabase db = null;
    List<HashMap<String, String>> stepsListArray;
    SimpleAdapter myListAdapter;
    HashMap<String,String> map = new HashMap<String,String>();
    private Button addSteps;
    private EditText et_StepsTaken;
    private EditText et_modify_Steps;
    private Spinner sp_steps_time;
    private TextView currentDate;
    private HashMap<String,String> steps_time_map;
    private Button testServices ;
    private Integer totalSteps;
    private BigDecimal calorieGoal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vSteps = inflater.inflate(R.layout.fragment_daily_steps, container, false);
        addSteps = (Button)vSteps.findViewById(R.id.bt_set_steps);
        testServices = (Button)vSteps.findViewById(R.id.bt_schedule_service);
        db = Room.databaseBuilder(vSteps.getContext(),
                UserStepsDatabase.class, "UserStepsDatabase")
                .fallbackToDestructiveMigration()
                .build();

        et_StepsTaken =(EditText)vSteps.findViewById(R.id.et_set_steps);
        currentDate = (TextView)vSteps.findViewById(R.id.tv_curr_date);
        sp_steps_time = (Spinner)vSteps.findViewById(R.id.sp_steps_time);
        et_modify_Steps =(EditText)vSteps.findViewById(R.id.et_steps_modify);
        totalSteps = 0;
        calorieGoal = BigDecimal.valueOf(0);

        steps_time_map = new HashMap<String,String>();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        currentDate.setText(formattedDate);


        sp_steps_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTime =
                        parent.getItemAtPosition(position).toString();
                if(!steps_time_map.isEmpty())
                {
                    if(steps_time_map.get(selectedTime)!=null)
                    et_modify_Steps.setText(steps_time_map.get(selectedTime));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        addSteps.setOnClickListener(new View.OnClickListener() {
            //including onClick() method
            public void onClick(View v) {
                InsertDatabase addDatabase = new InsertDatabase();
                addDatabase.execute();
            }
        });

        testServices.setOnClickListener(new View.OnClickListener() {
            //including onClick() method
            public void onClick(View v) {
                for (Map.Entry<String,String> entry : steps_time_map.entrySet())
                {
                    totalSteps = totalSteps +  Integer.parseInt(entry.getValue());

                }
                SharedPreferences userDetails =
                        getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);

                int userid = userDetails.getInt("userid",0);
                String loggedInDate = userDetails.getString("loggedInDate",null);
                if(!"".equals(loggedInDate)|| loggedInDate!=null)
                {
                    float calorieGoalSp = userDetails.getFloat("usergoal",0);
                    calorieGoal = BigDecimal.valueOf(calorieGoalSp);
                }

                CalculateCaloriesAsync calculateCaloriesAsync = new CalculateCaloriesAsync();
                calculateCaloriesAsync.execute(userid);

            }

        });


        loadAllSteps();


        return vSteps;
    }

    private void loadAllSteps() {
        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute();
    }


    private class InsertDatabase extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c);
            df = new SimpleDateFormat("hh:mm:ss");
            String formattedTime = df.format(c);

            SharedPreferences userDetails =
                    getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
            int userId = userDetails.getInt("userid", 0);
            String stepsTaken = et_StepsTaken.getText().toString();
            UserSteps userSteps = new UserSteps(userId, formattedDate, formattedTime, stepsTaken);
            long id = db.userStepsDao().insert(userSteps);
            return "Steps Added Successfully";
        }

        @Override
        protected void onPostExecute(String details) {
            loadAllSteps();
        }
    }
    private class ReadDatabase extends AsyncTask<Void,Void,String> {
            @Override
            protected String doInBackground(Void... params) {
                List<UserSteps> userSteps = db.userStepsDao().getAll();
                if (!(userSteps.isEmpty() || userSteps == null) ){
                    String userStepsStr = "";
                    for (UserSteps temp : userSteps) {
                        steps_time_map.put(temp.getStepsTime(),temp.getStepsTaken());
                    }

                }

                return "Database Read Successfully";
            }
            @Override
            protected void onPostExecute(String details) {

                List<String> stepsTimeList = new ArrayList<String>();

                for (Map.Entry<String,String> entry : steps_time_map.entrySet())
                {
                    stepsTimeList.add(entry.getKey());
                }

                final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(vSteps.getContext()
                        ,android.R.layout.simple_spinner_item, stepsTimeList);

                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_steps_time.setAdapter(spinnerAdapter);
            }
        }

    private class DeleteDatabase extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            db.userStepsDao().deleteAll();
            return null;
        }
        protected void onPostExecute(Void param) {
            steps_time_map.clear();
            sp_steps_time.setAdapter(null);
            et_modify_Steps.setText("");
        }
    }


    private class CalculateCaloriesAsync extends AsyncTask<Integer, Void, String>
    {
        @Override
        protected String doInBackground(Integer... params) {
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String loggedInDate = df.format(c);

            return RestClient.findCaloriesConsumedAndBurnt(params[0],loggedInDate,totalSteps);
        }
        @Override
        protected void onPostExecute(String response) {
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String loggedInDate = df.format(c);
            Report report = new Report();
            report.setCaloriegoal(calorieGoal);
            report.setTotalstepstaken(totalSteps);
            report.setReportdate(loggedInDate);
            try {
                JSONArray jsonarray = new JSONArray(response);
                JSONObject jsonobject = jsonarray.getJSONObject(0);
                if(jsonobject!=null)
                {
                    if(jsonobject.getString("totalcalburned")!=null ||
                            !"".equals(jsonobject.getString("totalcalburned")))
                        report.setTotalcalburned(new BigDecimal(jsonobject.getString("totalcalburned")));
                    if(jsonobject.getString("totalcalconsumed")!=null ||
                            !"".equals(jsonobject.getString("totalcalconsumed")))
                        report.setTotalcalconsumed(new BigDecimal(jsonobject.getString("totalcalconsumed")));
                }


                AddReportAsync addReportAsync = new AddReportAsync();
                addReportAsync.execute(report);


            } catch (JSONException e) {
                e.printStackTrace();
            }


            // resultTextView.setText(response);
        }
    }


    private class AddReportAsync extends AsyncTask<Report, Void, String>
    {
        @Override
        protected String doInBackground(Report... params) {
            SharedPreferences userDetails =
                    getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
            int userId = userDetails.getInt("userid", 0);
            params[0].setUserid(new Appuser());
            params[0].getUserid().setUserid(userId);
            RestClient.addReport(params[0]);
            return "Your calorie Report has been added!";
        }
        @Override
        protected void onPostExecute(String response) {

            Toast.makeText(vSteps.getContext(),response,Toast.LENGTH_SHORT).show();
            DeleteDatabase deleteDatabase = new DeleteDatabase();
            deleteDatabase.execute();
        }
    }



    }






