package com.example.fatcalapplication;

import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalorieTrackerFragment extends Fragment {
    private View vCalTracker;
    private TextView myGoalVal;
    private TextView myStepsVal;
    private TextView totalCalConsumed;
    private TextView totalCalBurned;

    UserStepsDatabase db = null;
    private Integer totalSteps = 0;
    private BigDecimal calorieGoal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        vCalTracker = inflater.inflate(R.layout.fragment_calories_tracker, container, false);
        myGoalVal = (TextView) vCalTracker.findViewById(R.id.tv_goal_value);
        myStepsVal = (TextView) vCalTracker.findViewById(R.id.tv_steps_value);

        totalCalConsumed = (TextView) vCalTracker.findViewById(R.id.tv_tot_cons_val);
        totalCalBurned = (TextView) vCalTracker.findViewById(R.id.tv_tot_burnt_val);

        db = Room.databaseBuilder(vCalTracker.getContext(),
                UserStepsDatabase.class, "UserStepsDatabase")
                .fallbackToDestructiveMigration()
                .build();

        SharedPreferences userDetails =
                getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);

        float calorieGoalSp = userDetails.getFloat("usergoal",0);

        myGoalVal.setText(String.valueOf(calorieGoalSp));

        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute();


        return vCalTracker;

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

            return "Read Database successfully";
        }
        @Override
        protected void onPostExecute(String response) {

            myStepsVal.setText(String.valueOf(totalSteps));
            SharedPreferences userDetails =
                    getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
            int userid = userDetails.getInt("userid",0);
            CalculateCaloriesAsync calculateCaloriesAsync = new CalculateCaloriesAsync();
            calculateCaloriesAsync.execute(userid);

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

            try {
                JSONArray jsonarray = new JSONArray(response);
                JSONObject jsonobject = jsonarray.getJSONObject(0);
                if(jsonobject!=null)
                {
                    if(jsonobject.getString("totalcalburned")!=null ||
                            !"".equals(jsonobject.getString("totalcalburned")))
                        totalCalBurned.setText(jsonobject.getString("totalcalburned"));
                    if(jsonobject.getString("totalcalconsumed")!=null ||
                            !"".equals(jsonobject.getString("totalcalconsumed")))
                        totalCalConsumed.setText(jsonobject.getString("totalcalconsumed"));
                }





            } catch (JSONException e) {
                e.printStackTrace();
            }


            // resultTextView.setText(response);
        }
    }
}
