package com.moncefadj.medcare.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moncefadj.medcare.DataClasses.DoctorDataForHomePatient;
import com.moncefadj.medcare.DataClasses.DoctorsDatabase;
import com.moncefadj.medcare.HelperClasses.adapter;
import com.moncefadj.medcare.R;

import java.util.ArrayList;

public class OneSpecialtyDoctors extends AppCompatActivity {
  //  ArrayList<SpecialtiesData> data=new ArrayList<SpecialtiesData>();
    //vertical view
  adapter docAdapter;
    DoctorsDatabase docdata;
    RecyclerView doctorsRecycler;
    ArrayList<DoctorDataForHomePatient> doclist= new ArrayList<>();
    ArrayList<DoctorDataForHomePatient> dliste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chirurgie_dentaire);
        doctorsRecycler = findViewById(R.id.doctors);
        doctorsRecycler.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        doctorsRecycler.setLayoutManager(manager);
        docAdapter = new adapter(this);
        doctorsRecycler.setAdapter(docAdapter);
        docdata = new DoctorsDatabase();

        loadDocData();




    }
   private void loadDocData() {
        docdata.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<DoctorDataForHomePatient> othDoctors = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()){


                    DoctorDataForHomePatient doctors = data.getValue(DoctorDataForHomePatient.class);
                    if(doctors.getSpecialty().matches("Chirurgie Dentaire")) {
                        othDoctors.add(doctors);
                    }
                }

                docAdapter.setItems(othDoctors);

                docAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}