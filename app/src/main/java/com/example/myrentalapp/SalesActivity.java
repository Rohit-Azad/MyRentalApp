package com.example.myrentalapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SalesActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private final String COLLECTION_NAME = "AnotherTest";
    private String UID ;
    private final String TAG = "SalesActivity" ;

    Button btnFind;
    Button btnReturn;
    Button btnShowAllReservations;
    Button btnSearchByUniqueID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        btnFind=(Button)findViewById(R.id.btnFind);
        btnReturn=(Button)findViewById(R.id.btnReturn);
        btnSearchByUniqueID=(Button)findViewById(R.id.btnSearchbyUniqueID);
        btnShowAllReservations=(Button)findViewById(R.id.btnShowAllReservations);


        initialize();
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SalesActivity.this,ClientActivity.class);
                startActivity(i);

            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SalesActivity.this,ReturnToInventoryActivity.class);
                startActivity(i);

            }
        });

        btnShowAllReservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SalesActivity.this,ReadBookingsDataActivity.class);
                startActivity(i);

            }
        });

        btnSearchByUniqueID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SalesActivity.this,SalesPersonReservationSearchActivity.class);
                startActivity(i);
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

//    private void readAllData() {
//        db.collection(COLLECTION_NAME)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            TextView tv = (TextView) findViewById(R.id.tvRee);
//                            tv.setText("");
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                tv.append(document.getId() + " => " + document.getData() +"\n" );
//                                tv.append("\t\t" + "ID is " + document.getData().get("ID") +"\n" );
//                            }
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//    }
//
//    private void search() {
//        // Create a reference
//        CollectionReference ref = db.collection(COLLECTION_NAME);
//
//        // Create a query against the collection.
//        Query query = ref.whereEqualTo("ID", et.getText().toString());
//        UID = "" ;
//        query.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            TextView tv = (TextView) findViewById(R.id.tvRee);
//                            tv.setText("");
////                            EditText etDPrice = (EditText)findViewById(R.id.etAge);
////                            EditText etQuantity = (EditText)findViewById(R.id.etGender);
//
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                tv.append(document.getId() + " => " + document.getData() +"\n" );
//                                tv.append("\t\t" + "ID is " + document.getData().get("ID") +"\n" );
//
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//
//                                // getting the last document
//                            UID = document.getId();
////                                etDPrice.setText(document.getData().get(key[1])+"");
////                                etQuantity.setText(document.getData().get(key[2])+"");
//                            }
//
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//    }

}
