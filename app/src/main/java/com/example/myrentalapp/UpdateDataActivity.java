package com.example.myrentalapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

public class UpdateDataActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private final String TAG = "ReadAllData";
    private String COLLECTION_NAME="VehicleInventory";
    private String UID ;
    EditText etDPrice;
    EditText etQuantity;
    EditText etHPrice;
    EditText etColor;
    EditText etSearchByModel;

    int quantity;

    Button btnQuantityPlus;
    Button btnQuantityMinus;
    Button btnSearch;
    Button btnUpdate;
    Button btnSearchByModel;
    Button btnDelete;
    Button btnAddToInventory;
   // Button btnAddNewType;

    ScrollView svDetails;


    TableRow tr_1;
    TableRow.LayoutParams tRowParams=new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    TableLayout.LayoutParams tLayoutParams=new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    LinearLayout.LayoutParams p =
            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
    LinearLayout.LayoutParams imgParam =
            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);

        initialize();

        etDPrice = (EditText)findViewById(R.id.etDPrice);
        etQuantity = (EditText)findViewById(R.id.etQuantity);
        etHPrice = (EditText)findViewById(R.id.etHPrice);
        etColor=(EditText)findViewById(R.id.etColor);
        etSearchByModel=(EditText)findViewById(R.id.etNameSearch);

        btnQuantityMinus=(Button)findViewById(R.id.btnMinus);
        btnQuantityPlus=(Button)findViewById(R.id.btnPlus);
        btnSearch=(Button)findViewById(R.id.btnSearch);
        btnUpdate=(Button)findViewById(R.id.btnUpdate);
        btnSearchByModel =(Button)findViewById(R.id.btnSearchByName);
        btnAddToInventory=(Button) findViewById(R.id.btnAddNewVehicle);
     //   btnAddNewType=(Button)findViewById(R.id.btnAddNewType);
        btnDelete=(Button)findViewById(R.id.btnDelete);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchByID();
            }
        });
        btnSearchByModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchByModel();
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
        btnAddToInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(UpdateDataActivity.this,AddNewInventoryActivity.class);
                startActivity(i);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
                svDetails.removeAllViews();
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

    private void searchByID() {
        // Create a reference
        CollectionReference ref = db.collection(COLLECTION_NAME);

        EditText et = (EditText)findViewById(R.id.etSearchValue);

        // Create a query against the collection.
        Query query = ref.whereEqualTo("ID", Integer.parseInt(et.getText().toString()));
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            TextView tv = (TextView) findViewById(R.id.tvReadOnlyDetails);
                            tv.setText("");


                            for (QueryDocumentSnapshot document : task.getResult()) {
                                svDetails = (ScrollView) findViewById(R.id.svDetails);
                                svDetails.removeAllViews();

                                final TableLayout tableLayout_1=new TableLayout(UpdateDataActivity.this);
                                final LinearLayout l = new LinearLayout(UpdateDataActivity.this);
                                l.setOrientation(LinearLayout.VERTICAL);
                                final TextView heading = new TextView(UpdateDataActivity.this);

                                final TextView label_model = new TextView(UpdateDataActivity.this);
                                //    label_model.setTextSize(22);
                                final TextView label_type = new TextView(UpdateDataActivity.this);
                                //   label_type.setTextSize(22);
                                final TextView label_priceHourly = new TextView(UpdateDataActivity.this);
                                //    label_priceHourly.setTextSize(22);
                                final TextView label_street = new TextView(UpdateDataActivity.this);
                                //    label_street.setTextSize(22);
                                final TextView label_name = new TextView(UpdateDataActivity.this);
                                //    label_name.setTextSize(22);
                                final TextView label_prices = new TextView(UpdateDataActivity.this);
                                //   label_prices.setTextSize(22);

                                final TextView city = new TextView(UpdateDataActivity.this);
                                //    city.setTextSize(20);
                                final TextView type = new TextView(UpdateDataActivity.this);
                                // type.setTextSize(20);
                                final TextView priceHour = new TextView(UpdateDataActivity.this);
                                //   priceHour.setTextSize(20);
                                final TextView priceDaily = new TextView(UpdateDataActivity.this);
                                //   priceDaily.setTextSize(20);
                                final TextView name = new TextView(UpdateDataActivity.this);
                                //    name.setTextSize(20);
                                final TextView model = new TextView(UpdateDataActivity.this);
                                final TextView heading1=new TextView(UpdateDataActivity.this);

                                tableLayout_1.setLayoutParams(tLayoutParams);

                                tr_1=new TableRow(UpdateDataActivity.this);
                                tableDataGenerator(label_name, "Make:", tr_1);
                                tableDataGenerator(name, document.getData().get("Make").toString(), tr_1);
                                //   tableLayout_1.removeView(tr_1);
                                tableLayout_1.addView(tr_1);

                                tr_1 = new TableRow(UpdateDataActivity.this);
                                tableDataGenerator(label_model, "model:", tr_1);
                                tableDataGenerator(model, document.getData().get("Model").toString(), tr_1);
                                //    tableLayout_1.removeView(tr_1);
                                tableLayout_1.addView(tr_1);

                                tr_1 = new TableRow(UpdateDataActivity.this);
                                tableDataGenerator(label_type, "Type:", tr_1);
                                tableDataGenerator(type, document.getData().get("Type").toString(), tr_1);
                                //   tableLayout_1.removeView(tr_1);
                                tableLayout_1.addView(tr_1);

                                tr_1=new TableRow(UpdateDataActivity.this);
                                tableDataGenerator(label_priceHourly, "Seats:", tr_1);
                                tableDataGenerator(priceHour,document.getData().get("Seats").toString(), tr_1);
                                //  tableLayout_1.removeView(tr_1);
                                tableLayout_1.addView(tr_1);
                                l.addView(tableLayout_1);
                                svDetails.addView(l);

                                Log.d(TAG, document.getId() + " => " + document.getData());
                                // getting the last document
                                UID = document.getId();
                                etDPrice.setText(document.getData().get("Daily Price")+"");
                                etQuantity.setText(document.getData().get("Quantity")+"");
                                etHPrice.setText(document.getData().get("Hourly Price")+"");
                                String colors=document.getData().get("Color").toString();
                                etColor.setText(colors.substring(1,colors.length()-1));
                                quantity=Integer.parseInt(etQuantity.getText().toString());
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private void searchByModel() {
    // Create a reference
    CollectionReference ref = db.collection(COLLECTION_NAME);

    //EditText et = (EditText)findViewById(R.id.etSearchValue);

    // Create a query against the collection.
    Query query = ref.whereEqualTo("Model", etSearchByModel.getText().toString());
    query.get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        TextView tv = (TextView) findViewById(R.id.tvReadOnlyDetails);
                        tv.setText("");

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            tv.append("Type=> " + document.getData().get("Type") +"\n" );
                            tv.append("Make=> " + document.getData().get("Make") +"\n" );
                            tv.append("Model=> " + document.getData().get("Model") +"\n" );
                            tv.append("Seats=> " + document.getData().get("Seats") +"\n" );
                            tv.append("UID=> " + document.getId() +"\n\n" );
                            //   tv.append("\t\t" + key[0] + " is " + document.getData().get(key[0]) +"\n" );

                            Log.d(TAG, document.getId() + " => " + document.getData());

                            // getting the last document
                            UID = document.getId();
                            etDPrice.setText(document.getData().get("Daily Price")+"");
                            etQuantity.setText(document.getData().get("Quantity")+"");
                            etHPrice.setText(document.getData().get("Hourly Price")+"");
                            String colors=document.getData().get("Color").toString();
                            etColor.setText(colors.substring(1,colors.length()-1));
                            quantity=Integer.parseInt(etQuantity.getText().toString());
                        }

                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }
            });
    }

    private void update() {
        String [] colors=etColor.getText().toString().trim().split(",");
        if (UID.length() > 0) {
            Map<String, Object> updateDetails = new HashMap<>();
            updateDetails.put("Daily Price", etDPrice.getText().toString());
            updateDetails.put("Quantity", etQuantity.getText().toString());
            updateDetails.put("Hourly Price", etHPrice.getText().toString());
            updateDetails.put("Color", Arrays.asList(colors));
            db.collection(COLLECTION_NAME).document(UID).update(updateDetails);

            // call searchByID to verify
          //  searchByID();
        }
    }
    public void increment (View view) {
        //int quantity=0;
        quantity = quantity + 1;
        display(quantity);
    }

    public void decrement (View view) {
        //  int quantity=Integer.parseInt(etQuantity.getText().toString());
        if (quantity>0){
            quantity = quantity - 1;
            display(quantity);
        }

    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        etQuantity.setText("" + number);
    }

    private void tableDataGenerator(TextView name, String label, TableRow tr)
    {
        tr.removeView(name);
        tr.setLayoutParams(tRowParams);
        tr.setPadding(5,2,5,2);
        name.setTextColor(Color.WHITE);
        name.setText(label);
        name.setTextSize(22);
        name.setPadding(5, 3, 5, 3);
        tr.addView(name);
    }



    private void delete() {
        if (UID.length() > 0) {
            db.collection(COLLECTION_NAME).document(UID).delete();
            etColor.setText("");
            etDPrice.setText("");
            etQuantity.setText("");
            etHPrice.setText("");
        }
    }

}
