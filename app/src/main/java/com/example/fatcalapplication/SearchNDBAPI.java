package com.example.fatcalapplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class SearchNDBAPI {

    private static final String API_KEY = "kroU2ucUEb995bZiJeik9eXYdMN0tGTSOsui7782";
    public static String search(boolean isReport,String[] params, String[] values) {
        String path = "";
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        String query_parameter="";
        if (params!=null && values!=null){
            for (int i =0; i < params.length; i ++){
                if(i!=0)
                    query_parameter += "&";
                query_parameter += params[i];
                query_parameter += "=";
                query_parameter += values[i];
            }
        }
        try {
            if(isReport)
               path =  "https://api.nal.usda.gov/ndb/reports/?";
            else
                path = "https://api.nal.usda.gov/ndb/search/?";
            url = new URL(path +
                   query_parameter + "&api_key=" + API_KEY );
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            connection.disconnect();
        }
        return textResult;
    }

    public static String getNdbNo(String result){
        String snippet = null;
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONObject("list").getJSONArray("item");
            if(jsonArray != null && jsonArray.length() > 0) {
                snippet = jsonArray.getJSONObject(0).getString("ndbno");
            }
        }catch (Exception e){
            e.printStackTrace();
            snippet = "NO INFO FOUND";
        }
        return snippet;
    }


    public static HashMap getNutrients(String result){
        HashMap<String,String> nutrientValueMap = new HashMap<String,String>();
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONObject("report").getJSONObject("food").getJSONArray("nutrients");
            JSONArray jsonArrayMeasure;
            if(jsonArray != null && jsonArray.length() > 0) {

                //Getting calorie information
                String nutrient = jsonArray.getJSONObject(1).getString("name");
                String nutrientValue = jsonArray.getJSONObject(1).getString("value") + " " +jsonArray.getJSONObject(1).getString("unit");
                nutrientValueMap.put(nutrient,nutrientValue);

                //Getting Fat information
                nutrient = jsonArray.getJSONObject(3).getString("name");
                nutrientValue = jsonArray.getJSONObject(3).getString("value") + " " +  jsonArray.getJSONObject(3).getString("unit");
                nutrientValueMap.put(nutrient,nutrientValue);


            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return nutrientValueMap;
    }


}
