package com.example.myrentalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<AutomobileDB> myRentalList;
    TextView txt;
    Button btnManager;
    Button btnClient;
    Button btnSales;
    DBConnection dbc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbc = new DBConnection();

        myRentalList=processData();
        btnClient=(Button)findViewById(R.id.btnClient);
        btnSales=(Button)findViewById(R.id.btnSales);
        btnManager=(Button)findViewById(R.id.btnManager);

        btnClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,ClientActivity.class);
                startActivity(i);
            }
        });
        btnManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,ManagerActivity.class);
                startActivity(i);
            }
        });
        btnSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,SalesActivity.class);
                startActivity(i);
            }
        });

    }

    private ArrayList<AutomobileDB> processData() {
        String json = null;
        try {
            InputStream is = getAssets().open("auto_3.json");


            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        ArrayList<AutomobileDB> temp = new ArrayList<>();
       // ArrayList<String> color;
        String[] colors;
        try {

            JSONArray ar = new JSONArray(json);
            JSONObject element;
            AutomobileDB autoDB;

            for (int i=0 ; i < ar.length(); i++) {
                element = ar.getJSONObject(i);
                autoDB = new AutomobileDB();
                colors=new String[4];
               // color=new ArrayList<>();
               // autoDB.id = element.getString("id");
                autoDB.type = element.getString("vehicle_type");
                autoDB.make = element.getString("car_make");
                autoDB.model = element.getString("car_model");
                autoDB.quantity = element.getString("seats");
                autoDB.seats = element.getString("quantity");
                autoDB.pricePerHour = element.getString("price_hour");
                autoDB.pricePerDay= element.getString("price_day");
                for(int j=0;j<4;j++) {
                    colors[j]=element.getJSONArray("color").getString(j);
                }
                autoDB.colors=colors;
                temp.add(autoDB);
            }
            return temp;
        } catch (JSONException e) {
            Log.d("MainActivity", e.getMessage());
        }

        return null;
    }
}
