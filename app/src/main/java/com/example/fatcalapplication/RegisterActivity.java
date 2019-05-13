package com.example.fatcalapplication;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fatcalapplication.Entity.Appuser;
import com.example.fatcalapplication.Entity.Credential;
import com.example.fatcalapplication.Entity.Food;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText mDisplayDate;
    private EditText emailAddr;
    private EditText address;
    private EditText password;
    private EditText userName;
    private EditText firstName;
    private EditText surname;
    private EditText height;
    private EditText weight;
    private EditText stepsPerMile;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Spinner levelOfActivitySpinner;
    private TextView backToLoginLink;
    private Button registerUser;
    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    final String passwordPattern = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,8}$";
    final String namePattern = "^[A-Za-z]+$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Edit Texts
        mDisplayDate = (EditText) findViewById(R.id.et_dob);
        emailAddr = (EditText) findViewById(R.id.et_email);
        address = (EditText) findViewById(R.id.et_address);
        userName = (EditText) findViewById(R.id.et_userName);
        password = (EditText) findViewById(R.id.et_password);
        firstName = (EditText) findViewById(R.id.et_firstName);
        surname = (EditText) findViewById(R.id.et_surname);
        height = (EditText) findViewById(R.id.et_height);
        weight = (EditText) findViewById(R.id.et_weight);
        stepsPerMile = (EditText) findViewById(R.id.et_spm);
        registerUser = (Button)findViewById(R.id.bt_add_user);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);


        EditText etDob = (EditText) findViewById(R.id.et_dob);
        levelOfActivitySpinner = (Spinner)findViewById(R.id.sp_loa);
        backToLoginLink  = (TextView)findViewById(R.id.tv_back_login);
        etDob.setFocusable(false);
        etDob.setClickable(true);
        List<String> levelOfActivity = new ArrayList<String>();
        levelOfActivity.add("1");
        levelOfActivity.add("2");
        levelOfActivity.add("3");
        levelOfActivity.add("4");
        levelOfActivity.add("5");
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this
                ,android.R.layout.simple_spinner_item, levelOfActivity);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelOfActivitySpinner.setAdapter(spinnerAdapter);


        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int currentYear = cal.get(Calendar.YEAR);
                int currentMonth = cal.get(Calendar.MONTH);
                int currentDay = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
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

                if(String.valueOf(month).length()==1)
                    monthVar =  String.format("%01d", month);

                if(String.valueOf(day).length()==1)
                    dayVar = String.format("%01d", day);

                month = month + 1;
                String date = year + "-" + monthVar + "-" +dayVar;
                 mDisplayDate.setText(date);


            }
        };

        backToLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,
                        LoginActivity.class);
                startActivity(intent);
            }
        });

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFormFields())
                {
                     VerifyUserAsync verifyUserAsync = new VerifyUserAsync();
                     verifyUserAsync.execute(userName.getText().toString(),emailAddr.getText().toString());
                }
            }
        });
    }

    private boolean validateFormFields() {
        boolean isFormValid = true;

        if(firstName.getText().toString().isEmpty()||!(firstName.getText().toString().matches(namePattern))
            ||firstName.getText().toString().length()<2 || firstName.getText().toString().length()>25)
        {
            isFormValid = false;
            firstName.setError("First name cannot be left empty and should consist of only alphabets with length" +
                    " greater than 2 and less than 26 characters");
            firstName.requestFocus();
        }
       else if(surname.getText().toString().isEmpty()||!(surname.getText().toString().matches(namePattern))
                ||surname.getText().toString().length()<2 || surname.getText().toString().length()>25)
        {
            isFormValid = false;
            surname.setError("Surname cannot be left empty and should consist of only alphabets with length" +
                    " greater than 2 and less than 26 characters");
            surname.requestFocus();
        }
        else if(height.getText().toString().isEmpty())
        {
            isFormValid = false;
            height.setError("Height cannot be left empty!");
            height.requestFocus();
        }
        else if(weight.getText().toString().isEmpty())
        {
            isFormValid = false;
            weight.setError("Weight cannot be left empty!");
            weight.requestFocus();
        }
        else if(stepsPerMile.getText().toString().isEmpty())
        {
            isFormValid = false;
            stepsPerMile.setError("Steps per mile cannot be left empty!");
            stepsPerMile.requestFocus();
        }
        else if(surname.getText().toString().isEmpty()||!(surname.getText().toString().matches(namePattern))
                ||surname.getText().toString().length()<2 || surname.getText().toString().length()>25)
        {
            isFormValid = false;
            surname.setError("Surname cannot be left empty and should consist of only alphabets with length" +
                    " greater than 2 and less than 26 characters");
            surname.requestFocus();
        }
        else if(surname.getText().toString().isEmpty()||!(surname.getText().toString().matches(namePattern))
                ||surname.getText().toString().length()<2 || surname.getText().toString().length()>25)
        {
            isFormValid = false;
            surname.setError("Surname cannot be left empty and should consist of only alphabets with length" +
                    " greater than 2 and less than 26 characters");
            surname.requestFocus();
        }
        else if(emailAddr.getText().toString().isEmpty() ||
                !(emailAddr.getText().toString().matches(emailPattern)))
        {
            isFormValid = false;
            emailAddr.setError("Email Address is invalid!");
            emailAddr.requestFocus();
        }
        else if(address.getText().toString().isEmpty()||
                address.getText().toString().length() <= 10 || address.getText().toString().length() > 250)
        {
            isFormValid = false;
            address.setError("The Address cannot be left empty and cannot be less than 10 and greater than 250 characters");
            address.requestFocus();
        }

        else if(password.getText().toString().isEmpty()||
                !(password.getText().toString().matches(passwordPattern)) ||
                !(password.getText().toString().length() > 0))
        {
            isFormValid = false;
            password.setError("The Password is Invalid! Password must be at least 4 characters, no more than 8 characters, and must include at least one upper case letter, one lower case letter, and one numeric digit.");
            password.requestFocus();
        }

        else if(userName.getText().toString().isEmpty()||
                userName.getText().toString().length() > 10 ||userName.getText().toString().length() < 2 )
        {
            isFormValid = false;
            userName.setError("The User Name cannot be less than 2 characters and  greater than 10 characters!");
            userName.requestFocus();
        }


        return isFormValid;
    }


    private class VerifyUserAsync extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params) {

            return RestClient.findIfUserExists(params[0],params[1]);

        }
        @Override
        protected void onPostExecute(String response) {
            if(!("[]".equals(response)))
                Toast.makeText(getApplicationContext(),"User Already Exists!",Toast.LENGTH_SHORT).show();
            else
            {
                Appuser registeredUser = createUserObject();
                CreateUserAsync createUserAsync = new CreateUserAsync();
                createUserAsync.execute(registeredUser);
            }

        }
    }

    private Appuser createUserObject() {
        Appuser appuser = new Appuser();
        appuser.setName(firstName.getText().toString());
        appuser.setSurname(surname.getText().toString());
        appuser.setEmail(emailAddr.getText().toString());
        appuser.setDob(mDisplayDate.getText().toString());
        appuser.setHeight(new BigDecimal(height.getText().toString()));
        appuser.setWeight(new BigDecimal(weight.getText().toString()));
        int selectedId = radioSexGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioSexButton = (RadioButton) findViewById(selectedId);
        appuser.setGender(radioSexButton.getText().toString());
        appuser.setAddress(address.getText().toString());
        appuser.setLevelofactivity(Integer.parseInt(levelOfActivitySpinner.getSelectedItem().toString()));
        appuser.setStepspermile(new BigDecimal(stepsPerMile.getText().toString()));

        return appuser;
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

    private class CreateUserAsync extends AsyncTask<Appuser, Void, String>
    {
        @Override
        protected String doInBackground(Appuser... params) {
            RestClient.createUser(params[0]);
            return "User Created Successfully";
        }
        @Override
        protected void onPostExecute(String response) {

            Credential userCredential = new Credential();
            Appuser registeredUser = createUserObject();

            String passwordHash = generateHashForPassword(password.getText().toString());
            userCredential.setUsername(userName.getText().toString());
            userCredential.setHash(passwordHash);
            userCredential.setPassword(password.getText().toString());
            userCredential.setUserid(registeredUser);
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c);
            userCredential.setSignupdate(formattedDate);

            RegisterUserAsync registerUserAsync = new RegisterUserAsync();
            registerUserAsync.execute(userCredential);

        }
    }
    private class RegisterUserAsync extends AsyncTask<Credential, Void, String>
    {
        @Override
        protected String doInBackground(Credential... params) {
            RestClient.registerUser(params[0]);
            return "User Registered Successfully!";
        }
        @Override
        protected void onPostExecute(String response) {

            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this,
                    LoginActivity.class);
            startActivity(intent);
        }
    }
}
