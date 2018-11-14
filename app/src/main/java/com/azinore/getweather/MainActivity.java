package com.azinore.getweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    DownLoader task;
    EditText location;
    TextView userInput;


    public class DownLoader extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... urls) {

            URL url;
            HttpURLConnection connection;
            String result = "";

            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();

                while(data!= -1){

                    char current = (char)data;
                    result+=current;
                    data = reader.read();

                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject jsonObject = new JSONObject(s);

                String weather = jsonObject.getString("weather");

                //Log.i("Weather info", weather);

                JSONArray jsonArray = new JSONArray(weather);
                String mess="";

                for(int i=0;i<jsonArray.length();i++){

                    JSONObject jsonPart = null;
                    try {
                        jsonPart = jsonArray.getJSONObject(i);

                        Log.i("main", jsonPart.getString("main"));

                        String main = jsonPart.getString("main");
                        String des =  jsonPart.getString("description");
                        if(!main.equals("")&& !des.equals("")) {
                            mess += main + ": " + des + "\r\n";
                        }
                        if(!mess.equals("")){
                            userInput.setText(mess);
                        }


                    } catch (JSONException e) {

                        e.printStackTrace();

                    }


                }




            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public void buttonClick(View view){

        userInput.setText("No such Location");
        Log.i("Button", "Clicked");
        task = new DownLoader();

        task.execute("http:openweathermap.org/data/2.5/weather?q="+location.getText().toString()+"&appid=b6907d289e10d714a6e88b30761fae22");

        InputMethodManager mnr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mnr.hideSoftInputFromWindow(userInput.getWindowToken(),0);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInput = findViewById(R.id.textView);
        location = findViewById(R.id.location);



    }
}
