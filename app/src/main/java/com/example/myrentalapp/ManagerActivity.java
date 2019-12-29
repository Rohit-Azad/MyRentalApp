package com.example.myrentalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class ManagerActivity extends AppCompatActivity {
    Map<String, ArrayList<AutomobileDB>> map;
    ArrayList<AutomobileDB> list;
    ArrayList<AutomobileDB> value;
    ArrayList<AutomobileDB> carList=MainActivity.myRentalList;
    String COLLECTION_NAME2="VehicleInventory";
    Button btnManageCustomers;
    Button btnManageBookings;


    private FirebaseFirestore db;
   // TextView tv;
    ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        initialize();
        sv=(ScrollView)findViewById(R.id.theScroller);
        Log.d( "onCreate: ",carList.size()+"");

        btnManageBookings=(Button)findViewById(R.id.btnManageBookings);
        btnManageCustomers=(Button)findViewById(R.id.btnCustomerInfo);

        btnManageCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ManagerActivity.this,ManageCustomerActivity.class);
                startActivity(i);
            }
        });


        list=new ArrayList<>();
        map = new HashMap<String, ArrayList<AutomobileDB>>();

        Button btnShow=(Button) findViewById(R.id.btnShow);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ManagerActivity.this,ReadAllData.class);
                startActivity(i);
            }
        });

        btnManageBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ManagerActivity.this,ManageBookingsActivity.class);
                startActivity(i);
            }
        });

        Button btnUpdate=(Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ManagerActivity.this,InventoryManagementActivity.class);
                startActivity(i);
            }
        });

        Button btnAdd=(Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionReference carRentalDB = db.collection(COLLECTION_NAME2);
                carRentalDB.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null ){
                            Log.d("ERROR", e.getMessage());
                            return;
                        }
                        if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() >0){

                            List<DocumentChange> ldoc = queryDocumentSnapshots.getDocumentChanges();
                            Log.d("OnEvent", ldoc.size() + "");

                            // index 0 is used. We are assuming only 1 change was made
                            Log.d("OnEvent", ldoc.get(0).getDocument().getId() + "");

                            Log.d("OnEvent", ldoc.get(0).getDocument().getData() + "");
                            Map <String,Object> m = ldoc.get(0).getDocument().getData();

                            Toast.makeText(ManagerActivity.this, "Current data:" + ldoc.size(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                Map<String, Object> data;
                Map<String,String> data2;

                for(int i=0;i<carList.size();i++) {
                    data = new HashMap<>();
                    data2=new HashMap<>(0);
                    Log.d("onClick: ",carList.get(i).model);
                    data.put("ID",i);
                    data.put("Type", carList.get(i).type);
                    data.put("Make", carList.get(i).make);
                    data.put("Model", carList.get(i).model);
                    data.put("Seats", Integer.parseInt(carList.get(i).seats));
                    data.put("Quantity", Integer.parseInt(carList.get(i).quantity));
                    data.put("Hourly Price", Integer.parseInt(carList.get(i).pricePerHour));
                    data.put("Daily Price", Integer.parseInt(carList.get(i).pricePerDay));
                    data.put("Color", Arrays.asList(carList.get(i).colors));
                   carRentalDB.document(String.valueOf(i)+" - " +carList.get(i).make).set(data);
                }


            }
        });
    }
    private void initialize() {

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }
}
