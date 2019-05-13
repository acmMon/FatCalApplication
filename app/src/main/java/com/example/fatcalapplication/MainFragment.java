package com.example.fatcalapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainFragment extends Fragment {
    View vMain;
    private TextView welcomeUser;
    private TextView currentDate;
    private EditText setGoal;
    private Button setGoalBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vMain = inflater.inflate(R.layout.fragment_main, container, false);
        welcomeUser = (TextView)vMain.findViewById(R.id.welcomeUser);
        currentDate = (TextView)vMain.findViewById(R.id.currentDate);
        setGoal = (EditText) vMain.findViewById(R.id.et_calorieGoal);
        setGoalBtn = (Button) vMain.findViewById(R.id.bt_setGoal);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String formattedDate = df.format(c);
        SharedPreferences userDetails =
                getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        String username= userDetails.getString("name",null);
        float calorieGoal = userDetails.getFloat("usergoal",0);

        welcomeUser.setText(welcomeUser.getText().toString() + username);
        currentDate.setText(formattedDate);
        setGoal.setText(String.valueOf(calorieGoal));

        setGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences userDetails =
                        getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor eUserDetails = userDetails.edit();
                eUserDetails.putFloat("usergoal",Float.parseFloat(setGoal.getText().toString()));
                eUserDetails.apply();
            }
        });


        return vMain;
    }

}
