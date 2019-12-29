package com.example.myrentalapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ReturnToInventoryActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private final String TAG = "ReadAllData";
    private String COLLECTION_NAME="BookingInfo";
    String COLLECTION_NAME2="VehicleInventory";
    String UID1;
    String UID2;

    Map<String,Object> update;

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
    Button btnReturnToInventory;

    CheckBox cbFine;

    String makeExtract;
    String modelExtract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_to_inventory);

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
        btnReturnToInventory=(Button)findViewById(R.id.btnReturnToInventory);

        cbFine=(CheckBox)findViewById(R.id.cbFine);

        btnSearchByID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchByID();
            }
        });

        btnReturnToInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReturnTOInventory();
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
                                String[] test=document.getData().get("Selected Car").toString().split(" ");
                                makeExtract=test[0];
                                for(int i=1;i<test.length;i++)
                                {
                                    modelExtract=test[i]+" ";
                                }
                                Log.d(TAG, "onComplete: "+makeExtract+"=="+modelExtract);

                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void ReturnTOInventory()
    {
        db.collection(COLLECTION_NAME2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                 if ((document.getData().get("Model").toString().equals(modelExtract.substring(0, modelExtract.length() - 1)))) {
                                        update = new HashMap<>();
                                        update.put("Quantity", Integer.parseInt(document.getData().get("Quantity").toString()) + 1);
                                     UID1 = document.getId();
                                     db.collection(COLLECTION_NAME2).document(UID1)
                                             .update(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                                         @Override
                                         public void onSuccess(Void aVoid) {

                                         }
                                     });
                                    }

                                }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(searchByID.getText().toString().toUpperCase())) {

                                    UID2 = document.getId();
                                    update = new HashMap<>();
                                    if (cbFine.isChecked()) {

                                        update.put("Status", "Vehicle Returned Late. Fine Applied");
                                        if (document.getData().get("Total Time").toString().contains("day/days")) {
                                            update.put("Total Cost", Double.parseDouble(document.getData().get("Total Price").toString()) + 20.0);
                                        } else if (document.getData().get("Total Time").toString().contains("hour/hours")) {
                                            update.put("Total Cost", Double.parseDouble(document.getData().get("Total Price").toString()) + 5.0);
                                        }
                                    } else {
                                        update = new HashMap<>();
                                        update.put("Status", "Vehicle Returned on Time");
                                    }
                                    db.collection(COLLECTION_NAME).document(UID2)
                                            .update(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                    Toast.makeText(ReturnToInventoryActivity.this,"Database Updated",Toast.LENGTH_SHORT).show();

                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}
