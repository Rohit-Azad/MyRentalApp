package com.example.myrentalapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ManageCustomerActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private final String TAG = "ReadAllData";
    private String COLLECTION_NAME="CustomerInfo";
    private String UID2;

    EditText fullName;
    EditText suite;
    EditText street;
    EditText city;
    EditText zipCode;
    EditText eMail;
    EditText licenseNumber;
    EditText phoneNumber;
    EditText stateProvince;
    EditText ccType;
    EditText ccNumber;
    EditText uniqueID;
    EditText searchByID;
    EditText searchByName;

    Button btnSearchByID;
    Button btnSearchByName;
    Button btnShowAll;
    Button btnDelete;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_customer);

        initialize();

        fullName=(EditText)findViewById(R.id.etFName);
        licenseNumber=(EditText)findViewById(R.id.etLicense);
        street=(EditText)findViewById(R.id.etStreet);
        city=(EditText)findViewById(R.id.etCity);
        suite=(EditText)findViewById(R.id.etSuite);
        eMail=(EditText)findViewById(R.id.etEmail);
        zipCode=(EditText)findViewById(R.id.etZipCode);
        phoneNumber=(EditText)findViewById(R.id.etPhone);
        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        stateProvince=(EditText)findViewById(R.id.etState);
        ccNumber=(EditText)findViewById(R.id.etCardNumber);
        ccType=(EditText)findViewById(R.id.etCCType);
        uniqueID=(EditText)findViewById(R.id.etUniqueID);
        searchByID=(EditText)findViewById(R.id.etSearchValue);
        searchByName=(EditText)findViewById(R.id.etNameSearch);

        btnSearchByID=(Button)findViewById(R.id.btnSearch);
        btnSearchByName=(Button)findViewById(R.id.btnSearchByName);
        btnDelete=(Button) findViewById(R.id.btnDelete);
        btnUpdate=(Button)findViewById(R.id.btnUpdate);
        btnShowAll=(Button)findViewById(R.id.btnShowAll);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

        btnShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(ManageCustomerActivity.this,ReadCustomerData.class);
                startActivity(i);
            }
        });

        btnSearchByID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchByID();
            }
        });
        btnSearchByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchByName();
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

    private void SearchByID() {
        // Create a reference
        CollectionReference ref = db.collection(COLLECTION_NAME);

        // Create a query against the collection.
        Query query = ref.whereEqualTo("UID", searchByID.getText().toString());
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                Log.d(TAG, document.getId() + " => " + document.getData());
                                // getting the last document
                                UID2 = document.getId();

                                fullName.setText(document.getData().get("Name")+"");
                                uniqueID.setText(document.getData().get("UID")+"");
                                phoneNumber.setText(document.getData().get("Phone")+"");
                                eMail.setText(document.getData().get("EMail").toString());
                                licenseNumber.setText(document.getData().get("License")+"");
                                ccType.setText(document.getData().get("Card Type").toString());
                                ccNumber.setText(document.getData().get("Card Number").toString());
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        CollectionReference ref2=db.collection(COLLECTION_NAME)
                .document(searchByID.getText().toString().toUpperCase())
                .collection("Address");
        Query query2 = ref2.whereEqualTo("UID", searchByID.getText().toString());
        query2.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                Log.d(TAG, document.getId() + " => " + document.getData());
                                // getting the last document
                              //  UID2 = document.getId();

                                suite.setText(document.getData().get("Suite")+"");
                                street.setText(document.getData().get("Street")+"");
                                city.setText(document.getData().get("City")+"");
                                stateProvince.setText(document.getData().get("State").toString());
                                zipCode.setText(document.getData().get("Zip Code")+"");
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private void SearchByName() {
        //String test="";
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get("Name").toString().contains(searchByName.getText().toString()))
                                {
                                    fullName.setText(document.getData().get("Name")+"");
                                    uniqueID.setText(document.getData().get("UID")+"");
                                    phoneNumber.setText(document.getData().get("Phone")+"");
                                    eMail.setText(document.getData().get("EMail").toString());
                                    licenseNumber.setText(document.getData().get("License")+"");
                                    ccType.setText(document.getData().get("Card Type").toString());
                                    ccNumber.setText(document.getData().get("Card Number").toString());
                                    UID2=document.getId();
                                    CollectionReference ref2=db.collection(COLLECTION_NAME)
                                            .document(uniqueID.getText().toString())
                                            .collection("Address");
                                    Query query2 = ref2.whereEqualTo("UID", UID2);
                                    query2.get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {


                                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                                            // getting the last document
                                                            //  UID2 = document.getId();

                                                            suite.setText(document.getData().get("Suite")+"");
                                                            street.setText(document.getData().get("Street")+"");
                                                            city.setText(document.getData().get("City")+"");
                                                            stateProvince.setText(document.getData().get("State").toString());
                                                            zipCode.setText(document.getData().get("Zip Code")+"");
                                                        }

                                                    } else {
                                                        Log.w(TAG, "Error getting documents.", task.getException());
                                                    }
                                                }
                                            });
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }

    private void update() {
        if (UID2.length() > 0) {
            Map<String, Object> updateDetails = new HashMap<>();
            updateDetails.put("Name", fullName.getText().toString());
            updateDetails.put("UID", uniqueID.getText().toString());
            updateDetails.put("EMail", eMail.getText().toString());
            updateDetails.put("Phone", phoneNumber.getText().toString());
            updateDetails.put("License", licenseNumber.getText().toString());
            updateDetails.put("Card Type", ccType.getText().toString());
            updateDetails.put("Card Number", ccNumber.getText().toString());
            db.collection(COLLECTION_NAME).document(UID2).update(updateDetails);

            Map<String, Object> updateDetails2 = new HashMap<>();
            updateDetails2.put("Suite", suite.getText().toString());
            updateDetails2.put("Street", street.getText().toString());
            updateDetails2.put("State", stateProvince.getText().toString());
            updateDetails2.put("City", city.getText().toString());
            updateDetails2.put("Zip Code", zipCode.getText().toString());
            db.collection(COLLECTION_NAME).document(UID2).collection("Address").document(UID2).update(updateDetails2);
            Toast.makeText(ManageCustomerActivity.this,"Info Updated",Toast.LENGTH_LONG).show();

            // call searchByID to verify
            // SearchByID();
        }
    }

    private void delete() {
        if (UID2.length() > 0) {
            db.collection(COLLECTION_NAME).document(UID2).delete();
            fullName.setText("");
            uniqueID.setText("");
            phoneNumber.setText("");
            eMail.setText("");
            licenseNumber.setText("");
            ccType.setText("");
            ccNumber.setText("");
            suite.setText("");
            street.setText("");
            city.setText("");
            stateProvince.setText("");
            zipCode.setText("");
            Toast.makeText(ManageCustomerActivity.this,"Customer Info Deleted",Toast.LENGTH_LONG).show();

        }
    }
}
