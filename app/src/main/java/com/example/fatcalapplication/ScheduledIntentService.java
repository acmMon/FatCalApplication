package com.example.fatcalapplication;

import android.app.IntentService;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.fatcalapplication.Entity.Appuser;
import com.example.fatcalapplication.Entity.Report;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ScheduledIntentService extends BroadcastReceiver {
    UserStepsDatabase db = null;
    private Integer totalSteps;
    private BigDecimal calorieGoal;
    private Context applicationContext;

    public ScheduledIntentService() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        applicationContext = context;
        db = Room.databaseBuilder(context,
                UserStepsDatabase.class, "UserStepsDatabase")
                .fallbackToDestructiveMigration()
                .build();

        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute();
    }


    private class ReadDatabase extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... params) {
            List<UserSteps> userSteps = db.userStepsDao().getAll();
            if (!(userSteps.isEmpty() || userSteps == null) ){
                for (UserSteps temp : userSteps) {
                   totalSteps = totalSteps + Integer.parseInt(temp.getStepsTaken());
                }

            }

            return "Database Read Successfully";
        }
        @Override
        protected void onPostExecute(String details) {

            SharedPreferences userDetails =
                    applicationContext.getSharedPreferences("userDetails", Context.MODE_PRIVATE);
            int userid = userDetails.getInt("userid",0);
            String loggedInDate = userDetails.getString("loggedInDate",null);
            if(!"".equals(loggedInDate)|| loggedInDate!=null)
            {
                float calorieGoalSp = userDetails.getFloat("usergoal",0);
                calorieGoal = BigDecimal.valueOf(calorieGoalSp);
                CalculateCaloriesAsync calculateCaloriesAsync = new CalculateCaloriesAsync();
                calculateCaloriesAsync.execute(userid);
            }
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
                    applicationContext.getSharedPreferences("userDetails", Context.MODE_PRIVATE);
            int userId = userDetails.getInt("userid", 0);
            params[0].setUserid(new Appuser());
            params[0].getUserid().setUserid(userId);
            RestClient.addReport(params[0]);
            return "Your calorie Report has been added!";
        }
        @Override
        protected void onPostExecute(String response) {


            DeleteDatabase deleteDatabase = new DeleteDatabase();
            deleteDatabase.execute();
        }
    }

    private class DeleteDatabase extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            db.userStepsDao().deleteAll();
            return null;
        }
        protected void onPostExecute(Void param) {

        }
    }



}

