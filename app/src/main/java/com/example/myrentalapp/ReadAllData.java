package com.example.myrentalapp;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ReadAllData extends AppCompatActivity {
    private FirebaseFirestore db;
    private final String TAG = "ReadAllData";
    HorizontalScrollView sv;
    String COLLECTION_NAME="VehicleInventory";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_all_data);
        sv=(HorizontalScrollView) findViewById(R.id.svReadAll);
        initialize();
        readAllData();
    }
    private void initialize() {

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }
    private void readAllData() {
        final ProgressDialog pd = new ProgressDialog(ReadAllData.this);
        pd.setMessage("loading Please Wait!");
        pd.show();
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            sv.removeAllViews();
                            TableLayout tLayout;
                            TableLayout tLayout_2;
                            TableRow tr_head;
                            TableRow tr_admin;

                            //declaring label textviews
                            TextView label_id=new TextView(ReadAllData.this);
                            TextView label_make=new TextView(ReadAllData.this);
                            TextView label_type=new TextView(ReadAllData.this);
                            TextView label_seats=new TextView(ReadAllData.this);
                            TextView label_model=new TextView(ReadAllData.this);
                            TextView label_quantity=new TextView(ReadAllData.this);
                            TextView label_hourlyPrice=new TextView(ReadAllData.this);
                            TextView label_dailyPrice=new TextView(ReadAllData.this);
                            TextView label_TotalVehicles=new TextView(ReadAllData.this);
                            TextView label_Colors=new TextView(ReadAllData.this);
                            label_TotalVehicles.setTextSize(24);
                            TextView totalVehicles=new TextView(ReadAllData.this);
                            totalVehicles.setTextSize(24);
                            TextView tViewSpacing ;
                            LinearLayout layout = new LinearLayout(ReadAllData.this);
                            layout.setOrientation(LinearLayout.VERTICAL);


                            LinearLayout.LayoutParams llParams =
                                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                                            ViewGroup.LayoutParams.FILL_PARENT);
                            TableRow.LayoutParams tRowParams=new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            TableLayout.LayoutParams tLayoutParams=new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            layout.setLayoutParams(llParams);
                            tLayout = new TableLayout(ReadAllData.this);
                            tLayout_2 = new TableLayout(ReadAllData.this);
                            tr_head = new TableRow(ReadAllData.this);
                            tr_admin = new TableRow(ReadAllData.this);
                            tViewSpacing = new TextView(ReadAllData.this);

                            //setting parameters to layouts
                            tViewSpacing.setLayoutParams(llParams);
                            tViewSpacing.setTextSize(18);
                            tr_head.setLayoutParams(tRowParams);
                            tr_admin.setLayoutParams(tRowParams);
                            tLayout.setLayoutParams(tLayoutParams);
                            tLayout_2.setLayoutParams(tLayoutParams);
                            tr_head.setBackgroundColor(Color.BLACK);
                            tr_admin.setBackgroundColor(Color.BLUE);

                            TextView tv = new TextView(ReadAllData.this);
                            tv.setLayoutParams(llParams);

                            //using custom function to set labels to the table header
                            tableDataGenerator(label_id,"ID",tr_head);
                            tableDataGenerator(label_type,"Type",tr_head);
                            tableDataGenerator(label_make,"Make",tr_head);
                            tableDataGenerator(label_model,"Model",tr_head);
                            tableDataGenerator(label_seats,"Seats",tr_head);
                            tableDataGenerator(label_quantity,"Quantity",tr_head);
                            tableDataGenerator(label_hourlyPrice,"Price/Hour",tr_head);
                            tableDataGenerator(label_dailyPrice,"Price/Day",tr_head);
                            tableDataGenerator(label_Colors,"Colors Available",tr_head);

                            tLayout.addView(tr_head);
                            int i=0;
                            int tVehicles=0;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                tv.append("\t\t" + "ID is " + document.getData().get("ID") +"\n" );
//                                tv.append("Vehicle Type: " + document.getData().get("Type") +"\n" );
//                                tv.append("Vehicle Make: " + document.getData().get("Make") +"\n" );
                                TableRow tr_data = new TableRow(ReadAllData.this);


                                //setting ids to rows
                                tr_data.setId(100 + i);
                              // tr_data2.setId(200 + i);

                                //setting layout parameters
                                tr_data.setLayoutParams(tRowParams);


                                //data row background color
                                tr_data.setBackgroundColor(Color.GRAY);


                                //declaring table data textviews along with their ids
                                // with counter increasing with every iteration
                                TextView type = new TextView(ReadAllData.this);
                                type.setId(100 + i);
                                TextView seats = new TextView(ReadAllData.this);
                                seats.setId(100 + i);
                                TextView admin = new TextView(ReadAllData.this);
                                admin.setId(100 + i);
                                TextView hourlyPrice = new TextView(ReadAllData.this);
                                hourlyPrice.setId(100 + i);
                                TextView quantity = new TextView(ReadAllData.this);
                                quantity.setId(100 + i);
                                TextView model = new TextView(ReadAllData.this);
                                model.setId(i);
                                TextView dailyPrice = new TextView(ReadAllData.this);
                                dailyPrice.setId(100 + i);
                                TextView id = new TextView(ReadAllData.this);
                                id.setId(100 + i);
                                TextView make = new TextView(ReadAllData.this);
                                make.setId(100 + i);
                                TextView colors = new TextView(ReadAllData.this);
                                make.setId(100 + i);

                                //populating the tables with values grouped by key i.e. admin
                                tableDataGenerator(id, document.getData().get("ID").toString(), tr_data);
                                tableDataGenerator(type, document.getData().get("Type").toString(), tr_data);

                                tableDataGenerator(make, document.getData().get("Make").toString(), tr_data);
                                tableDataGenerator(model, document.getData().get("Model").toString(), tr_data);
                                tableDataGenerator(seats, document.getData().get("Seats").toString(), tr_data);
                                tableDataGenerator(quantity, document.getData().get("Quantity").toString(), tr_data);
                                tableDataGenerator(hourlyPrice, document.getData().get("Hourly Price").toString(), tr_data);
                                tableDataGenerator(dailyPrice, document.getData().get("Daily Price").toString(), tr_data);
                                tableDataGenerator(colors, document.getData().get("Color").toString().substring(1,(document.getData().get("Color").toString().length())-1), tr_data);

                                //adding row to table layout
                                tLayout.addView(tr_data);
                                tVehicles+= Integer.parseInt(document.getData().get("Quantity").toString());
                                i++;

                            }
                            TableRow tr_data2 = new TableRow(ReadAllData.this);
                            tr_data2.setLayoutParams(tRowParams);
                            tr_data2.setBackgroundColor(Color.BLUE);
                            tableDataGenerator(label_TotalVehicles, "Total Vehicles", tr_data2);

                            tableDataGenerator(totalVehicles,tVehicles+" ", tr_data2);
                            tLayout_2.addView(tr_data2);


                            //tv.append("Total Vehicles => "+totalVehicles);

                            layout.addView(tLayout);
                            layout.addView(tLayout_2);
                            layout.addView(tv);

                            sv.addView(layout);
                            pd.dismiss();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private void tableDataGenerator(TextView name, String label, TableRow tr)
    {
        tr.removeView(name);
        name.setText(label);
        name.setTextColor(Color.WHITE);
        name.setTextSize(18);
        name.setPadding(8, 8, 8, 8);
        tr.addView(name);
    }
}
