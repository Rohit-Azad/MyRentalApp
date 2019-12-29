package com.example.myrentalapp;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class DBConnection {
    private FirebaseFirestore db ;

    public DBConnection() {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

    }

    public CollectionReference getCollectionReference(String collectionName) {
        return db.collection(collectionName);
    }

}
