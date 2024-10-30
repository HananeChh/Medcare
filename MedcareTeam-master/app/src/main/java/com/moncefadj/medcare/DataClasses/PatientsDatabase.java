package com.moncefadj.medcare.DataClasses;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class PatientsDatabase {

    private DatabaseReference databaseReference;

    public PatientsDatabase(String uid){

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("Users").child("Doctors").child(uid).child("PatientsWithRdv");

    }

    public Task<Void> add (PatientData patientData){

        return databaseReference.push().setValue(patientData);

    }

    public Query get (){

        return databaseReference.orderByKey();

    }

}
