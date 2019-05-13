package com.example.fatcalapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fatcalapplication.Entity.Appuser;
import com.example.fatcalapplication.Entity.Credential;
import com.example.fatcalapplication.Entity.Food;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private TextView registerlink;
    private Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = (EditText)findViewById(R.id.etUserName);
        password = (EditText)findViewById(R.id.etPassword);
        registerlink = (TextView)findViewById(R.id.tvRegisterLink);
        Login = (Button)findViewById(R.id.btnLogin);

        registerlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                startActivity(intent);
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(validateUserLogin())
               {
                   String passwordHash = generateHashForPassword(password.getText().toString());
                   UserLoginAsync userLoginAsync = new UserLoginAsync();
                   userLoginAsync.execute(userName.getText().toString(), passwordHash);
               }

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String generateHashForPassword(String userPassword) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hashInBytes = md.digest(userPassword.getBytes(StandardCharsets.UTF_8));

        // bytes to hex
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private boolean validateUserLogin() {
        boolean isCredValid = true;
        if(userName.getText().toString().isEmpty())
        {
            isCredValid = false;
            userName.setError("The Username cannot be left empty!");
        }
        else if(password.getText().toString().isEmpty())
        {
            isCredValid = false;
            password.setError("The Password cannot be left empty");

        }

            return isCredValid;

    }

    private class UserLoginAsync extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params) {

           return RestClient.loginUser(params[0],params[1]);
        }
        @Override
        protected void onPostExecute(String response) {

            if("".equals(response))
            {
                Toast.makeText(getApplicationContext(),"User doesn't exist! Please register first!",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Appuser loggedInUser = getUserFromJson(response);
                SharedPreferences userDetails =
                        getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor eUserDetails = userDetails.edit();
                eUserDetails.putInt("userid",loggedInUser.getUserid());
                eUserDetails.putString("name",loggedInUser.getName());
                //eUserDetails.putFloat("usergoal",0);
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c);
                df = new SimpleDateFormat("hh:mm:ss");
                String formattedTime = df.format(c);
                eUserDetails.putString("loggedInDate",formattedDate);
                eUserDetails.putString("loggedInTime",formattedTime);
                eUserDetails.apply();
                Intent loginIntent = new Intent(LoginActivity.this,
                        MainActivity.class);
                startActivity(loginIntent);

            }

            // resultTextView.setText(response);
        }
    }

    private Appuser getUserFromJson(String response) {

       Appuser appuser = new Appuser();
       ObjectMapper objectMapper = new ObjectMapper();
        try {
            Credential credential = objectMapper.readValue(response, Credential.class);
            appuser = credential.getUserid();

        } catch (IOException e) {
            e.printStackTrace();
        }

       return appuser;

    }
}

