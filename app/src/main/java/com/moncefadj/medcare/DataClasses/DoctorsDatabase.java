package com.moncefadj.medcare.DataClasses;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DoctorsDatabase {

    private DatabaseReference databaseReference;

    public DoctorsDatabase(){

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference().child("Users").child("Doctors");

    }

    public Task<Void> add (DoctorDataForHomePatient doctorData){

        return databaseReference.push().setValue(doctorData);

    }

    public Query get (){

        return databaseReference.orderByKey();

    }

}
