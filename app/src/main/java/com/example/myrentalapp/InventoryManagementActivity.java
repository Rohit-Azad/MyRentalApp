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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class InventoryManagementActivity extends AppCompatActivity {
    EditText etSearchMake;
    Button btnSearchMake;
    Button btnAddNew;
    Button btnUpdateOld;
    ScrollView svResults;

    String COLLECTION_NAME="VehicleInventory";
    String TAG="A freakin test";

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_management);

        initialize();

        etSearchMake=(EditText)findViewById(R.id.etSearchMake);
        btnSearchMake=(Button)findViewById(R.id.btnSearchMake);
        btnAddNew=(Button)findViewById(R.id.btnAddNew);
        btnUpdateOld=(Button)findViewById(R.id.btnUpdateOld);
        svResults=(ScrollView)findViewById(R.id.svSearchResults);

        btnSearchMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchByMake();
            }
        });

        btnUpdateOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(InventoryManagementActivity.this,UpdateDataActivity.class);
                startActivity(i);
            }
        });

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(InventoryManagementActivity.this,AddNewInventoryActivity.class);
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

    private void SearchByMake() {
        // Create a reference
        CollectionReference ref = db.collection(COLLECTION_NAME);
        svResults.removeAllViews();
        final LinearLayout l=new LinearLayout(InventoryManagementActivity.this);
        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        l.setLayoutParams(p);
        l.setOrientation(LinearLayout.VERTICAL);
        final TextView tv=new TextView(InventoryManagementActivity.this);
        tv.setLayoutParams(p);
        tv.setTextSize(18);
        tv.setTextColor(Color.WHITE);

        //EditText et = (EditText)findViewById(R.id.etSearchValue);

        // Create a query against the collection.
        Query query = ref.whereEqualTo("Make", etSearchMake.getText().toString());
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
//                            TextView tv = (TextView) findViewById(R.id.tvReadOnlyDetails);
//                            tv.setText("");
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                tv.append("ID: " + document.getData().get("ID")+"   Model: "+document.getData().get("Model")+"    Quantity: "+document.getData().get("Quantity") +"\n" );
//                                tv.append("Make=> " + document.getData().get("Make") +"\n" );
//                                tv.append("Model=> " + document.getData().get("Model") +"\n" );
//                                tv.append("Seats=> " + document.getData().get("Seats") +"\n" );
//                                tv.append("UID=> " + document.getId() +"\n\n" );
                                //   tv.append("\t\t" + key[0] + " is " + document.getData().get(key[0]) +"\n" );


                                Log.d(TAG, document.getId() + " => " + document.getData());

                                // getting the last document
//                                UID = document.getId();
//                                etDPrice.setText(document.getData().get("Daily Price")+"");
//                                etQuantity.setText(document.getData().get("Quantity")+"");
//                                etHPrice.setText(document.getData().get("Hourly Price")+"");
//                                String colors=document.getData().get("Color").toString();
//                                etColor.setText(colors.substring(1,colors.length()-1));
//                                quantity=Integer.parseInt(etQuantity.getText().toString());
                            }
                            l.addView(tv);
                            svResults.addView(l);


                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
