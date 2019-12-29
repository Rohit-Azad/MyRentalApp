package com.example.myrentalapp;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

public class ReadBookingsDataActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private final String TAG = "ReadAllData";
    String COLLECTION_NAME="BookingInfo";
    HorizontalScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_bookings_data);

        sv=(HorizontalScrollView) findViewById(R.id.svReadAllCustomerData);
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
                            TextView label_id=new TextView(ReadBookingsDataActivity.this);
                            TextView label_make=new TextView(ReadBookingsDataActivity.this);
                            TextView label_type=new TextView(ReadBookingsDataActivity.this);
                            TextView label_seats=new TextView(ReadBookingsDataActivity.this);
                            TextView label_model=new TextView(ReadBookingsDataActivity.this);
                            TextView label_quantity=new TextView(ReadBookingsDataActivity.this);
                            TextView label_hourlyPrice=new TextView(ReadBookingsDataActivity.this);
                            TextView label_dailyPrice=new TextView(ReadBookingsDataActivity.this);
                            TextView label_TotalVehicles=new TextView(ReadBookingsDataActivity.this);
                            TextView label_Colors=new TextView(ReadBookingsDataActivity.this);
                            label_TotalVehicles.setTextSize(24);
                            TextView totalVehicles=new TextView(ReadBookingsDataActivity.this);
                            totalVehicles.setTextSize(24);
                            TextView tViewSpacing ;
                            LinearLayout layout = new LinearLayout(ReadBookingsDataActivity.this);
                            layout.setOrientation(LinearLayout.VERTICAL);


                            LinearLayout.LayoutParams llParams =
                                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                                            ViewGroup.LayoutParams.FILL_PARENT);
                            TableRow.LayoutParams tRowParams=new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            TableLayout.LayoutParams tLayoutParams=new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            layout.setLayoutParams(llParams);
                            tLayout = new TableLayout(ReadBookingsDataActivity.this);
                            tLayout_2 = new TableLayout(ReadBookingsDataActivity.this);
                            tr_head = new TableRow(ReadBookingsDataActivity.this);
                            tr_admin = new TableRow(ReadBookingsDataActivity.this);
                            tViewSpacing = new TextView(ReadBookingsDataActivity.this);

                            //setting parameters to layouts
                            tViewSpacing.setLayoutParams(llParams);
                            tViewSpacing.setTextSize(18);
                            tr_head.setLayoutParams(tRowParams);
                            tr_admin.setLayoutParams(tRowParams);
                            tLayout.setLayoutParams(tLayoutParams);
                            tLayout_2.setLayoutParams(tLayoutParams);
                            tr_head.setBackgroundColor(Color.BLACK);
                            tr_admin.setBackgroundColor(Color.BLUE);

                            TextView tv = new TextView(ReadBookingsDataActivity.this);
                            tv.setLayoutParams(llParams);

                            //using custom function to set labels to the table header
                            tableDataGenerator(label_id,"Name",tr_head);
                            tableDataGenerator(label_type,"Unique Booking ID",tr_head);
                            tableDataGenerator(label_make,"UID",tr_head);
                            tableDataGenerator(label_model,"Selected Car",tr_head);
                            tableDataGenerator(label_seats,"Selected Color",tr_head);
                            tableDataGenerator(label_quantity,"Total Price",tr_head);
                            tLayout.addView(tr_head);
                            int i=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                tv.append("\t\t" + "ID is " + document.getData().get("ID") +"\n" );
//                                tv.append("Vehicle Type: " + document.getData().get("Type") +"\n" );
//                                tv.append("Vehicle Make: " + document.getData().get("Make") +"\n" );
                                TableRow tr_data = new TableRow(ReadBookingsDataActivity.this);


                                //setting ids to rows
                                tr_data.setId(100 + i);
                                // tr_data2.setId(200 + i);

                                //setting layout parameters
                                tr_data.setLayoutParams(tRowParams);


                                //data row background color
                                tr_data.setBackgroundColor(Color.GRAY);


                                //declaring table data textviews along with their ids
                                // with counter increasing with every iteration
                                TextView type = new TextView(ReadBookingsDataActivity.this);
                                type.setId(100 + i);
                                TextView seats = new TextView(ReadBookingsDataActivity.this);
                                seats.setId(100 + i);
                                TextView admin = new TextView(ReadBookingsDataActivity.this);
                                admin.setId(100 + i);
                                TextView hourlyPrice = new TextView(ReadBookingsDataActivity.this);
                                hourlyPrice.setId(100 + i);
                                TextView quantity = new TextView(ReadBookingsDataActivity.this);
                                quantity.setId(100 + i);
                                TextView model = new TextView(ReadBookingsDataActivity.this);
                                model.setId(i);
                                TextView dailyPrice = new TextView(ReadBookingsDataActivity.this);
                                dailyPrice.setId(100 + i);
                                TextView id = new TextView(ReadBookingsDataActivity.this);
                                id.setId(100 + i);
                                TextView make = new TextView(ReadBookingsDataActivity.this);
                                make.setId(100 + i);
                                TextView colors = new TextView(ReadBookingsDataActivity.this);
                                make.setId(100 + i);

                                //populating the tables with values grouped by key i.e. admin
                                tableDataGenerator(id, document.getData().get("Name").toString(), tr_data);
                                tableDataGenerator(type, document.getData().get("Unique Booking ID").toString(), tr_data);

                                tableDataGenerator(make, document.getData().get("UID").toString(), tr_data);
                                tableDataGenerator(model, document.getData().get("Selected Car").toString(), tr_data);
                                tableDataGenerator(seats, document.getData().get("Selected Color").toString(), tr_data);
                                tableDataGenerator(quantity, document.getData().get("Total Price").toString(), tr_data);

                                //adding row to table layout
                                tLayout.addView(tr_data);
                                i++;

                            }

                            layout.addView(tLayout);
                         //   layout.addView(tLayout_2);
                        //    layout.addView(tv);

                            sv.addView(layout);
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

       // tr.setGravity(Gravity.CENTER);
        tr.addView(name);
    }
}
