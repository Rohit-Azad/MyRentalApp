package com.example.myrentalapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewInventoryActivity extends AppCompatActivity {
    String COLLECTION_NAME="VehicleInventory";
    String TAG="A freakin test";

    private FirebaseFirestore db;

    ArrayList<String> types=new ArrayList<>();



    int quantity=0;
    int seat=0;
    EditText etQuantity;
    EditText etSeats;
    EditText etMake;
    EditText etModel;
    EditText etPriceDaily;
    EditText etPriceHourly;
    EditText etColors;
    EditText etNewType;
    Spinner myTypeSpinner;
    Button btnSeatPlus;
    Button btnSeatMinus;
    Button btnQuantityPlus;
    Button btnQuantityMinus;
    Button btnBack;
    Button submit;

    ArrayList<Integer> IDs;
    ArrayList<String> typesTruncated=new ArrayList<>();;

    int nextID;

    String [] key={"ID","Make","Model","Quantity","Seats","Color","Type","Daily Price","Hourly Price"};
    String [] testAgain={"Hatchback","Sedan","SUV","Van","Truck"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_inventory);
        myTypeSpinner=(Spinner)findViewById(R.id.typeSpinner);
        etNewType=(EditText)findViewById(R.id.etTypeNew);
        IDs=new ArrayList<>();
//        Intent intent=getIntent();
//        typesTruncated=intent.getStringArrayListExtra("data");

        initialize();
        //readSelectedData();
        int test=GetNextID();

        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //  temp.add(document.getData().get(key[6]).toString());
                                types.add(document.getData().get(key[6]).toString());

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        typesTruncated=(removeDuplicates(types));
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                AddNewInventoryActivity.this, android.R.layout.simple_spinner_item, typesTruncated);
                       // myTypeSpinner.setSelection(0);

                        myTypeSpinner.setAdapter(spinnerArrayAdapter);
                    }
                });

        etQuantity=(EditText)findViewById(R.id.etQuantity);
        etQuantity.setText("0");
        etSeats=(EditText)findViewById(R.id.etSeats);
        etSeats.setText("0");
        etMake=(EditText)findViewById(R.id.etMake);
        etModel=(EditText)findViewById(R.id.etModel);
        etPriceDaily=(EditText)findViewById(R.id.etDailyPrice);
        etPriceHourly=(EditText)findViewById(R.id.etHourlyPrice);
        etColors=(EditText)findViewById(R.id.etColors);


        btnSeatPlus=(findViewById(R.id.btnPlusSeats));
        btnSeatMinus=(findViewById(R.id.btnMinusSeats));
        btnQuantityMinus=(findViewById(R.id.btnMinusQuantity));
        btnQuantityPlus=(findViewById(R.id.btnPlusQuantity));
        btnBack=(Button)findViewById(R.id.btnBack);
        submit=(Button)findViewById(R.id.btnSubmitDetails);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AddNewInventoryActivity.this,ManagerActivity.class);
                startActivity(i);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etColors.getText().toString().isEmpty() ||
                        (myTypeSpinner.getSelectedItem().toString().isEmpty()&& etNewType.getText().toString().isEmpty()) ||
                etMake.getText().toString().isEmpty() ||
                etModel.getText().toString().isEmpty() ||
                etPriceDaily.getText().toString().isEmpty() ||
                etPriceDaily.getText().toString().equals("0")||
                etPriceHourly.getText().toString().isEmpty() ||
                etPriceHourly.getText().toString().equals("0")||
                etSeats.getText().toString().equals("0") ||
                etQuantity.getText().toString().equals("0"))
                {
                    Toast.makeText(AddNewInventoryActivity.this,"Please fill in all the details before submitting",Toast.LENGTH_SHORT).show();
                }
                else {
                    addData();
                }
            }
        });

        btnQuantityPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increment(view);

            }
        });

        btnQuantityMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrement(view);

            }
        });
        btnSeatPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increment1(view);

            }
        });
        btnSeatMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrement1(view);

            }
        });


    }

    private void addData() {
        String[] colorExtract=etColors.getText().toString().split(" ");

        Map<String, Object> user = new HashMap<>();
        user.put(key[0], GetNextID());
        user.put(key[1],etMake.getText().toString());
        user.put(key[2],etModel.getText().toString());
        user.put(key[3],Integer.parseInt(etQuantity.getText().toString().trim()));
        user.put(key[4],Integer.parseInt(etSeats.getText().toString().trim()));
        user.put(key[5], Arrays.asList(colorExtract));
        if(!etNewType.getText().toString().isEmpty()) {

            user.put(key[6], etNewType.getText().toString());
            myTypeSpinner.setEnabled(false);
        }
        else
        {
            myTypeSpinner.setEnabled(true);
            user.put(key[6], myTypeSpinner.getSelectedItem().toString());
        }
        user.put(key[7],Integer.parseInt(etPriceDaily.getText().toString()));
        user.put(key[8],Integer.parseInt(etPriceHourly.getText().toString()));

        // Add a new document with a generated ID
//        db.collection(COLLECTION_NAME)
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG,
//                                "DocumentSnapshot added with ID: " + documentReference.getId());
//                        Toast.makeText(AddNewInventoryActivity.this,"Data Added to Firebase",Toast.LENGTH_SHORT).show();
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(Exception e) {
//                        Log.d(TAG,
//                                "Error adding document " + e);
//                    }});
        db.collection(COLLECTION_NAME)
                .document(GetNextID()+" - "+etMake.getText().toString())
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddNewInventoryActivity.this,"Data Added to Firebase",Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void increment (View view) {
        quantity = quantity + 1;
        display(quantity);
    }

    public void decrement (View view) {
        if (quantity>0){
            quantity = quantity - 1;
            display(quantity);
        }

    }
    public void increment1 (View view) {
        seat = seat + 1;
        display1(seat);
    }

    public void decrement1 (View view) {
        if (seat>0){
            seat = seat - 1;
            display1(seat);
        }

    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        etQuantity.setText("" + number);
    }
    private void display1(int number) {
        etSeats.setText("" + number);
    }
    private void initialize() {

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    private int GetNextID() {
        final ArrayList<Integer> temp=new ArrayList<>();

        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                temp.add(Integer.parseInt(document.getData().get(key[0]).toString()));

                                nextID=(Collections.max(temp)+1);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return nextID;

    }

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {
        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();
        // Traverse through the first list
        for (T element : list) {
            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }
        // return the new list
        return newList;
    }
}
