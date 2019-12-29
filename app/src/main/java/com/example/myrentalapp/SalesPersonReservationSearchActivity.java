package com.example.myrentalapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SalesPersonReservationSearchActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private final String TAG = "ReadAllData";
    private String COLLECTION_NAME="BookingInfo";
    private String UID2;

    EditText fullName;
    EditText uniqueBookingID;
    EditText selectedCar;
    EditText pickUp;
    EditText dropOff;
    EditText price;
    EditText insurance;
    EditText taxes;
    EditText totalPrice;
    EditText selectedColor;
    EditText uniqueID;
    EditText searchByID;
    EditText searchByName;
    EditText totalTime;

    Button btnSearchByID;
    Button btnShowAll;
    Button btnDelete;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_person_reservation_search);

        initialize();

        fullName=(EditText)findViewById(R.id.etFName);
        insurance =(EditText)findViewById(R.id.etInsurance);
        selectedCar =(EditText)findViewById(R.id.etSelectedCar);
        pickUp=(EditText)findViewById(R.id.etPickup);
        uniqueBookingID=(EditText)findViewById(R.id.etBooking);
        price =(EditText)findViewById(R.id.etPrice);
        dropOff=(EditText)findViewById(R.id.etDropOff);
        taxes =(EditText)findViewById(R.id.etTaxes);
        totalPrice =(EditText)findViewById(R.id.etTotalPrice);
        selectedColor=(EditText)findViewById(R.id.etSelectedColor);
        uniqueID=(EditText)findViewById(R.id.etUniqueID);
        searchByID=(EditText)findViewById(R.id.etSearchValue);
        searchByName=(EditText)findViewById(R.id.etNameSearch);
        totalTime=(EditText)findViewById(R.id.etTotalTime);

        btnSearchByID=(Button)findViewById(R.id.btnSearch);
        btnDelete=(Button) findViewById(R.id.btnDelete);
        btnUpdate=(Button)findViewById(R.id.btnUpdate);
        btnShowAll=(Button)findViewById(R.id.btnShowAll);

        btnSearchByID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchByID();
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
        Query query = ref.whereEqualTo("Unique Booking ID", searchByID.getText().toString().toUpperCase());
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
                                uniqueBookingID.setText(document.getData().get("Unique Booking ID")+"");
                                selectedCar.setText(document.getData().get("Selected Car").toString());
                                selectedColor.setText(document.getData().get("Selected Color").toString());
                                pickUp.setText(document.getData().get("Pickup").toString());
                                dropOff.setText(document.getData().get("DropOff").toString());
                                totalTime.setText(document.getData().get("Total Time").toString());
                                taxes.setText(document.getData().get("Tax(15%)")+"");
                                price.setText(document.getData().get("Car Price").toString());
                                insurance.setText(document.getData().get("Insurance").toString());
                                totalPrice.setText(document.getData().get("Total Price").toString());

                               }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
