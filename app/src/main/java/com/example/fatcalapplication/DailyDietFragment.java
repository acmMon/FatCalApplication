package com.example.fatcalapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fatcalapplication.Entity.Appuser;
import com.example.fatcalapplication.Entity.Consumption;
import com.example.fatcalapplication.Entity.Food;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyDietFragment extends Fragment implements View.OnClickListener {
    private View vDailyDiet;
    private EditText etFoodItem;
    private TextView tvSearchResults;
    private Button btFoodSearch;
    private Button btAddFood;
    private ImageView iv;
    Bitmap bitmap;
    Spinner sCategory;
    Spinner sFoodItem;
    TextView descLabel;
    TextView nutrientLabel;
    Spinner addCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vDailyDiet = inflater.inflate(R.layout.fragment_daily_diet, container, false);
        etFoodItem = (EditText) vDailyDiet.findViewById(R.id.et_food_item);
        tvSearchResults = (TextView) vDailyDiet.findViewById(R.id.wiki_desc);
        iv= (ImageView) vDailyDiet.findViewById(R.id.searchFoodImage);
        btFoodSearch = (Button) vDailyDiet.findViewById(R.id.bt_food_search);
        sCategory= (Spinner) vDailyDiet.findViewById(R.id.fc_spinner);
        sFoodItem = (Spinner)vDailyDiet.findViewById(R.id.fi_spinner);
        descLabel = (TextView)vDailyDiet.findViewById(R.id.desc_label);
        nutrientLabel = (TextView)vDailyDiet.findViewById(R.id.nut_label);
        addCategory= (Spinner) vDailyDiet.findViewById(R.id.fc_spinner_add);

        populateCategorySpinner();

        sCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory =
                        parent.getItemAtPosition(position).toString();
                if(selectedCategory != null || !"".equals(selectedCategory))
                {
                    FoodInfoFetchAsyncTask foodFetch = new FoodInfoFetchAsyncTask();
                    foodFetch.execute(selectedCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        addCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        sFoodItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String foodCategory = sCategory.getSelectedItem().toString();
                String selectedFoodItem =
                        parent.getItemAtPosition(position).toString();
                if(selectedFoodItem != null || !"".equals(selectedFoodItem))
                {
                    AddConsumptionAsync addConsumptionAsync = new AddConsumptionAsync();
                    addConsumptionAsync.execute(foodCategory,selectedFoodItem);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        btFoodSearch.setOnClickListener(this);
       /* btAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodItem = etFoodItem.getText().toString();
                // Validate user input
                if (foodItem.isEmpty()) {
                    etFoodItem.setError("Food item is required!");
                    return;
                }
            }
        });*/
        return vDailyDiet;
    }

    private void populateCategorySpinner() {
        List<String> categoryList = new ArrayList<String>();
        categoryList.add("Meat");
        categoryList.add("Fruit");
        categoryList.add("Dairy");
        categoryList.add("Vegetable");
        categoryList.add("Salad");

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(vDailyDiet.getContext()
                ,android.R.layout.simple_spinner_item, categoryList){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View v = null;

                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                }
                else {

                    v = super.getDropDownView(position, null, parent);
                }

                parent.setVerticalScrollBarEnabled(false);
                return v;
            }

        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCategory.setAdapter(spinnerAdapter);
        addCategory.setAdapter(spinnerAdapter);


    }

    @Override
    public void onClick(View v) {
        String foodItem = etFoodItem.getText().toString();
        String foodCategory = addCategory.getSelectedItem().toString();
        // Validate user input
        if (foodItem.isEmpty()) {
            etFoodItem.setError("Food item is required!");
            return;
        }

        else if(foodCategory.isEmpty())
        {
            Toast.makeText(vDailyDiet.getContext(),"Please select food category",Toast.LENGTH_LONG).show();
            return;
        }

        SearchAsyncTask searchAsyncTask=new SearchAsyncTask();
        searchAsyncTask.execute(foodItem);

        SearchImageAsync searchImageAsync = new SearchImageAsync();
        searchImageAsync.execute(foodItem);

        SearchFromNDBAPI searchFromNDBAPI = new SearchFromNDBAPI();
        searchFromNDBAPI.execute(foodItem);

    }



    private class SearchAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return SearchGoogleAPI.search(params[0], new String[]{"num"}, new
                    String[]{"1"});
        }
        @Override
        protected void onPostExecute(String result) {
            descLabel.setText("About the food");
            TextView tv= (TextView)vDailyDiet.findViewById(R.id.wiki_desc);
            tv.setText(SearchGoogleAPI.getSnippet(result));
            String foodItem = sFoodItem.getSelectedItem().toString();
            if(foodItem!=null || !"".equals(foodItem))
            {
                SearchImageAsync searchImageAsync=new SearchImageAsync();
                searchImageAsync.execute(foodItem);
            }

        }
    }

    private class SearchImageAsync extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params) {
            return SearchGoogleAPI.search(params[0], new String[]{"fileType","num","searchType"}, new
                    String[]{"jpg","1","image"});
        }
        @Override
        protected void onPostExecute(String result) {

            String searchLink = SearchGoogleAPI.getImageURL(result);
            DisplayImageAsync displayImageAsync = new DisplayImageAsync();
            displayImageAsync.execute(searchLink);

        }
    }

    private class DisplayImageAsync extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            int file_length = 0;
            try {
                InputStream srt = new URL(params[0]).openStream();
                bitmap = BitmapFactory.decodeStream(srt);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            iv.setImageResource(0);
            iv.setImageBitmap(bitmap);

        }
    }

    private class SearchFromNDBAPI extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params) {
            return SearchNDBAPI.search(false,new String[]{"q","format","max","offset","ds"}, new
                    String[]{params[0],"json","1","0","Standard Reference"});
        }
        @Override
        protected void onPostExecute(String result) {

            String searchLink = SearchNDBAPI.getNdbNo(result);
            DisplayNutrientInformation displayNutrientInformation = new DisplayNutrientInformation();
            displayNutrientInformation.execute(searchLink);

        }
    }

    private class DisplayNutrientInformation extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {
            return SearchNDBAPI.search(true,new String[]{"ndbno","type","format"}, new
                    String[]{params[0],"b","json"});
        }

        @Override
        protected void onPostExecute(String result) {

           HashMap<String,String> nutrientValueMap = SearchNDBAPI.getNutrients(result);
            List<HashMap<String, String>> nutrientListArray;
            SimpleAdapter myListAdapter;
            ListView nutrientList;

            String[] colHEAD = new String[] {"NUTRIENT NAME","NUTRIENT VALUE"};
            int[] dataCell = new int[] {R.id.nutrient_name,R.id.nutrient_value};
            nutrientList = vDailyDiet.findViewById(R.id.list_view);
            nutrientListArray = new
                    ArrayList<HashMap<String, String>>();
            if(!nutrientValueMap.isEmpty())
            {
                for (Map.Entry<String,String> entry : nutrientValueMap.entrySet())
                {
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("NUTRIENT NAME",entry.getKey());
                    map.put("NUTRIENT VALUE",entry.getValue());
                    nutrientListArray.add(map);
                }

            }

            if(nutrientListArray.size()>0)
            {
                nutrientLabel.setText("Nutrient Info");

                myListAdapter = new
                        SimpleAdapter(vDailyDiet.getContext(),nutrientListArray,R.layout.list_view,colHEAD,dataCell);
                nutrientList.setAdapter(myListAdapter);

                PostAsyncTask postAsyncTask = new PostAsyncTask();
                postAsyncTask.execute(nutrientValueMap);


            }

            else
            {
                Toast.makeText(vDailyDiet.getContext(),"No nutrient info available",Toast.LENGTH_SHORT).show();
            }

            //To-do make list view and show nutrient details

        }
    }

    private class FoodInfoFetchAsyncTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {
            return RestClient.findFoodByCategory(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            List<String> foodItemList = new ArrayList<String>();
            try{
                JSONArray jsonArray = new JSONArray(result);
                if(jsonArray != null && jsonArray.length() > 0) {
                    for(int i = 0;i<jsonArray.length();i++)
                    {
                        foodItemList.add(jsonArray.getJSONObject(i).getString("name"));
                    }

                    final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(vDailyDiet.getContext()
                            ,android.R.layout.simple_spinner_item, foodItemList){
                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {

                            View v = null;

                            if (position == 0) {
                                TextView tv = new TextView(getContext());
                                tv.setHeight(0);
                                tv.setVisibility(View.GONE);
                                v = tv;
                            }
                            else {

                                v = super.getDropDownView(position, null, parent);
                            }

                            parent.setVerticalScrollBarEnabled(false);
                            return v;
                        }

                    };

                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sFoodItem.setAdapter(spinnerAdapter);
                }
            }catch (Exception e){
                e.printStackTrace();

            }

        }
    }

    private class PostAsyncTask extends AsyncTask<HashMap<String,String>, Void, String>
    {
        @Override
        protected String doInBackground(HashMap<String,String>... params) {
            Food food=new Food();
            String selectedFoodCategory = addCategory.getSelectedItem().toString();
            food.setName(etFoodItem.getText().toString());
            food.setCategory(selectedFoodCategory);
            for (Map.Entry<String,String> entry : params[0].entrySet())
            {
                if("Energy".equals(entry.getKey()))

                {

                    food.setCalorieamount(new BigDecimal(entry.getValue().split(" ")[0]));
                }

                else if("Total lipid (fat)".equals(entry.getKey()))
                {
                    food.setFat(new BigDecimal(entry.getValue().split(" ")[0]));
                }

            }

            switch(selectedFoodCategory)
            {
                case "Meat":
                    food.setServingunit("oz");
                    break;
                case "Fruit":
                    food.setServingunit("cup");
                    break;
                case "Dairy":
                    food.setServingunit("ml");
                    break;
                case "Vegetable":
                    food.setServingunit("g");
                    break;
                case "Salad":
                    food.setServingunit("cup");
            }
            food.setServingamount(BigDecimal.valueOf(1));
            RestClient.createFood(food);
            return "Food was added";
        }
        @Override
        protected void onPostExecute(String response) {

            Toast.makeText(vDailyDiet.getContext(),"Food has been added to our database",Toast.LENGTH_LONG).show();
        }
    }

    private class AddConsumptionAsync extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params) {
            Consumption consumption = createConsumptionRecord(params[0],params[1]);
            RestClient.addConsumption(consumption);
            return "Your consumption has been recorded";
        }
        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(vDailyDiet.getContext(),response,Toast.LENGTH_SHORT).show();
        }
    }

    private Consumption createConsumptionRecord(String foodCategory, String foodItem) {
        Consumption consumption = new Consumption();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        consumption.setConsumptiondate(formattedDate);
        consumption.setQuantityservings(1);
        consumption.setFoodid(new Food());
        consumption.setUserid(new Appuser());
        consumption.getFoodid().setCategory(foodCategory);
        consumption.getFoodid().setName(foodItem);
        SharedPreferences userDetails =
                getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        int userid= userDetails.getInt("userid",0);
        consumption.getUserid().setUserid(userid);

        return consumption;

    }


}
