package com.example.fatcalapplication;

import android.util.Log;

import com.example.fatcalapplication.Entity.Appuser;
import com.example.fatcalapplication.Entity.Consumption;
import com.example.fatcalapplication.Entity.Credential;
import com.example.fatcalapplication.Entity.Food;
import com.example.fatcalapplication.Entity.Report;
import com.google.gson.Gson;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RestClient
{
    //private static final String BASE_URL = "http://192.168.0.7:8080/FatCalWebProject/webresources/";

   private static final String BASE_URL = "http://118.139.39.96:8080/FatCalWebProject/webresources/";

    public static String findFoodByCategory(String category)
    {

            final String methodPath = "fatcal.food/findByCategory/" + category;
            //initialise
            URL url = null;
            HttpURLConnection conn = null;
            String textResult = "";
            try
            {
                url = new URL(BASE_URL + methodPath);
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                Scanner inStream = new Scanner(conn.getInputStream());

                while (inStream.hasNextLine()) {
                    textResult += inStream.nextLine();
                }


            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally {

            }

            return textResult;


    }



    public static void createFood(Food food){
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath="fatcal.food/";
        try {
            Gson gson =new Gson();
            String stringFoodJson=gson.toJson(food);
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to POST
            conn.setRequestMethod("POST");
            //set the output to true
            conn.setDoOutput(true);
            //set length of the data you want to send
            conn.setFixedLengthStreamingMode(stringFoodJson.getBytes().length);
            //add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
            //Send the POST out
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(stringFoodJson);
            out.close();
            Log.i("error",new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }

    public static void registerUser(Credential userCredential){
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath="fatcal.credential/registerUser";
        try {
            Gson gson =new Gson();
            String stringUserCredential=gson.toJson(userCredential);
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to POST
            conn.setRequestMethod("POST");
            //set the output to true
            conn.setDoOutput(true);
            //set length of the data you want to send
            conn.setFixedLengthStreamingMode(stringUserCredential.getBytes().length);
            //add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
            //Send the POST out
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(stringUserCredential);
            out.close();
            Log.i("error",new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }

    public static void addConsumption(Consumption consumption){
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath="fatcal.consumption/addConsumption";
        try {
            Gson gson =new Gson();
            String stringUserConsumption=gson.toJson(consumption);
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to POST
            conn.setRequestMethod("POST");
            //set the output to true
            conn.setDoOutput(true);
            //set length of the data you want to send
            conn.setFixedLengthStreamingMode(stringUserConsumption.getBytes().length);
            //add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
            //Send the POST out
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(stringUserConsumption);
            out.close();
            Log.i("error",new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }

    public static String findIfUserExists(String username, String email) {

        final String methodPath = "fatcal.credential/findByUserNameAndEmail/" + username + "/" + email;
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        try
        {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());

            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {

        }

        return textResult;


    }

    public static String loginUser(String username, String hash) {
        final String methodPath = "fatcal.credential/verifyUser/" + username + "/" + hash;
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        try
        {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());

            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {

        }

        return textResult;


    }

    public static void createUser(Appuser appuser) {

        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath="fatcal.appuser/";
        try {
            Gson gson =new Gson();
            String stringUserObject=gson.toJson(appuser);
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to POST
            conn.setRequestMethod("POST");
            //set the output to true
            conn.setDoOutput(true);
            //set length of the data you want to send
            conn.setFixedLengthStreamingMode(stringUserObject.getBytes().length);
            //add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
            //Send the POST out
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(stringUserObject);
            out.close();
            Log.i("error",new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }

    public static String findCaloriesConsumedAndBurnt(Integer userid, String consumptionDate, Integer totalSteps)
    {
        final String methodPath = "fatcal.appuser/findTotalCaloriesConsumedAndBurnt/" + userid + "/" + consumptionDate + "/" + totalSteps;
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        try
        {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());

            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {

        }

        return textResult;
    }

    public static void addReport(Report report) {
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath="fatcal.report/addReport";
        try {
            Gson gson =new Gson();
            String stringUserConsumption=gson.toJson(report);
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to POST
            conn.setRequestMethod("POST");
            //set the output to true
            conn.setDoOutput(true);
            //set length of the data you want to send
            conn.setFixedLengthStreamingMode(stringUserConsumption.getBytes().length);
            //add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
            //Send the POST out
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(stringUserConsumption);
            out.close();
            Log.i("error",new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

    }


    public static String fetchPieReportValues(Integer userid,String reportDate)
    {

        final String methodPath = "fatcal.report/findCalorieReportByUserAndDate/" + userid + "/" + reportDate;
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        try
        {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());

            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {

        }

        return textResult;


    }


    public static String fetchBarReportValues(Integer userid,String startDate,String endDate)
    {

        final String methodPath = "fatcal.report/findCalorieReportByUserAndPeriod/" + userid + "/" + startDate + "/" + endDate;
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        try
        {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());

            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {

        }

        return textResult;


    }
}
