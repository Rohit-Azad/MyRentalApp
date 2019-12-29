package com.example.myrentalapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ManageBookingsActivity extends AppCompatActivity {

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

    int hoursOrDays;
    int newPrice;
    String dayOrHour;
    int ppdFinder;
    double tax;
    int insure;
    DatePickerDialog.OnDateSetListener date2;
    DatePickerDialog.OnDateSetListener date;
    Calendar myCalendar1 = Calendar.getInstance();
    Calendar myCalendar2 = Calendar.getInstance();
    // Calendar mcurrentTime1 = Calendar.getInstance();
    Calendar mcurrentTime1;
    Calendar mcurrentTime2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bookings);

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
                Intent i= new Intent(ManageBookingsActivity.this,ReadBookingsDataActivity.class);
                startActivity(i);
            }
        });

        btnSearchByID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchByID();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();

            }
        });

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel2();
            }

        };

        pickUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String[]test=totalTime.getText().toString().split(" ");
                int timeConverter=Integer.parseInt(test[0]);
                dayOrHour=test[1];

                ppdFinder=(int)Double.parseDouble((price.getText().toString()))/timeConverter;
                if(dayOrHour.equals("day/days")) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(ManageBookingsActivity.this, date, myCalendar1
                            .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                            myCalendar1.get(Calendar.DAY_OF_MONTH)).show();

                }
                else if(dayOrHour.equals("hour/hours")) {
                    // TODO Auto-generated method stub
                    //  Calendar mcurrentTime = Calendar.getInstance();
                    mcurrentTime2 = Calendar.getInstance();
                    mcurrentTime2.add(Calendar.HOUR_OF_DAY,1);
                    int hour = mcurrentTime2.get(Calendar.HOUR_OF_DAY);
                    int minute = 0;

                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(ManageBookingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            pickUp.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                            pickUp.setFocusable(false);
                            pickUp.setEnabled(false);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Pickup Time");
                    mTimePicker.show();
                }
            }
        });


        dropOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[]test=totalTime.getText().toString().split(" ");
                int timeConverter=Integer.parseInt(test[0]);
                dayOrHour=test[1];

                ppdFinder=(int)Double.parseDouble((price.getText().toString()))/timeConverter;
                if(dayOrHour.equals("day/days")) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(ManageBookingsActivity.this, date2, myCalendar2
                            .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                            myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
                }
                else {
                    // TODO Auto-generated method stub
                    mcurrentTime1 = Calendar.getInstance();
                    mcurrentTime1.add(Calendar.HOUR_OF_DAY,3);
                    int hour = mcurrentTime1.get(Calendar.HOUR_OF_DAY);
                    int minute = 0;

                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(ManageBookingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            dropOff.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                            dropOff.setFocusable(false);
                            dropOff.setEnabled(false);
                            int total=Integer.parseInt(dropOff.getText().toString().substring(0,2))-Integer.parseInt(pickUp.getText().toString().substring(0,2));
                            // tvStart.setText("You Selected: "+etStart.getText()+" to "+etEnd.getText()+"\n"+"Total Hours: "+total + " hours");
                            totalTime.setText(total + " hour/hours");
                            hoursOrDays=total;
                            newPrice=total*ppdFinder;
                            insure=total*4;
                            tax=((newPrice+4)*hoursOrDays)*0.15;
                            taxes.setText(String.valueOf(tax));
                           // insure=hoursOrDays*15;
                            insurance.setText(insure);
                            price.setText(newPrice);
                            totalPrice.setText(String.valueOf(newPrice+insure+tax));

                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Return Time");
                    mTimePicker.show();
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
                                String[]test=totalTime.getText().toString().split(" ");
                                int timeConverter=Integer.parseInt(test[0]);
                                dayOrHour=test[1];

                                ppdFinder=(int)Double.parseDouble((price.getText().toString()))/timeConverter;


                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        pickUp.setText(sdf.format(myCalendar1.getTime()));
        pickUp.setFocusable(false);
//        pickUp.setEnabled(false);
    }

    private void updateLabel2() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dropOff.setText(sdf.format(myCalendar2.getTime()));
        dropOff.setFocusable(false);
//        dropOff.setEnabled(false);
        long diff = myCalendar2.getTimeInMillis() - myCalendar1.getTimeInMillis();
        float dayCount = (float) diff / (24 * 60 * 60 * 1000);
        hoursOrDays= (int) dayCount;
        newPrice=hoursOrDays*ppdFinder;
        // tvStart.setText("You Selected: "+etStart.getText()+" to "+etEnd.getText()+"\n"+(int)dayCount+" days");
        insure=(int)dayCount*15;
        totalTime.setText((int)dayCount+" day/days");
        tax=((newPrice+insure)*0.15);
        taxes.setText(String.valueOf(tax));

        insurance.setText(String.valueOf(insure));
        price.setText(String.valueOf(newPrice));
        totalPrice.setText(String.valueOf(newPrice+insure+tax));


    }

    private void update() {
        if (UID2.length() > 0) {

            Map<String, Object> updateDetails = new HashMap<>();
            updateDetails.put("Car Price", Integer.parseInt(price.getText().toString()));
            updateDetails.put("Tax(15%)", tax);
            updateDetails.put("Insurance", insure);
            updateDetails.put("Total Time", totalTime.getText().toString());
            updateDetails.put("Total Price", newPrice+insure+tax);
            updateDetails.put("Pickup",pickUp.getText().toString());
            updateDetails.put("DropOff",dropOff.getText().toString());
//            updateDetails.put("Card Type", ccType.getText().toString());
//            updateDetails.put("Card Number", ccNumber.getText().toString());
            db.collection(COLLECTION_NAME).document(UID2).update(updateDetails);

            Toast.makeText(ManageBookingsActivity.this,"Info Updated",Toast.LENGTH_LONG).show();
        }
    }

    private void delete() {
        if (UID2.length() > 0) {
            db.collection(COLLECTION_NAME).document(UID2).delete();
            fullName.setText("");
            uniqueID.setText("");
            taxes.setText("");
            price.setText("");
            insurance.setText("");
            totalTime.setText("");
            pickUp.setText("");
            dropOff.setText("");
            selectedCar.setText("");
            uniqueBookingID.setText("");
            totalPrice.setText("");
            searchByID.setText("");
            Toast.makeText(ManageBookingsActivity.this,"Booking Info Deleted",Toast.LENGTH_LONG).show();

        }
    }

}
