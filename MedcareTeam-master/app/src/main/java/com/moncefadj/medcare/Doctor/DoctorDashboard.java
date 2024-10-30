package com.moncefadj.medcare.Doctor;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moncefadj.medcare.DataClasses.PatientData;
import com.moncefadj.medcare.DataClasses.PatientsDatabase;
import com.moncefadj.medcare.HelperClasses.PatientsAdapter;
import com.moncefadj.medcare.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DoctorDashboard extends AppCompatActivity {

    ImageButton docProfileBtn;
    RecyclerView patientsRecycler;
    PatientsAdapter patientsAdapter;
    String uidDoctor;
    PatientsDatabase patientsData;

    ArrayList<PatientData> patientsList;
    TextInputLayout daysInput;
    AutoCompleteTextView autoCompleteTextView;
    String[] days;
    ArrayAdapter arrayAdapter;
    DatabaseReference doctorRef;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        uidDoctor = FirebaseAuth.getInstance().getCurrentUser().getUid();
        doctorRef = FirebaseDatabase.getInstance().getReference("Users").child("Doctors").child(uidDoctor);

        //Acceder au profile du médecin
        docProfileBtn = findViewById(R.id.CompteMedecin);
        docProfileBtn.setOnClickListener(v -> {

            Intent intentLoadNewActivity = new Intent(DoctorDashboard.this, DoctorProfile.class);
            startActivity(intentLoadNewActivity);

        });

        daysAutoCompleteTxt();
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String day = daysInput.getEditText().getText().toString();
                daysInput.setError(null);
                recycStuff(day);
                patientsList.clear();
                loadPatients(day);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void recycStuff(String day) {

        patientsList = new ArrayList<PatientData>();
        patientsRecycler = findViewById(R.id.patients_recycler);
        patientsRecycler.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        patientsRecycler.setLayoutManager(manager);
        patientsAdapter = new PatientsAdapter(this, patientsList, uidDoctor, day);
        patientsRecycler.setAdapter(patientsAdapter);

        swipeRefreshLayout = findViewById(R.id.swiper);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                patientsList.clear();
                loadPatients(day);
                patientsAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void loadPatients(String day) {

        DatabaseReference patRef = doctorRef.child("PatientsWithRdv").child(day);
        patRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()){

                    PatientData patients = data.getValue(PatientData.class);
                    if (!patientexist(patients )) {

                        patientsList.add(patients);

                    }

                }

                patientsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }

        });

    }

    private boolean patientexist(PatientData patients) {

        boolean exist = false;
        int i = 0;
        while ( ( i< patientsList.size() )&& (!exist)){

            if(patientsList.get(i).getName() == patients.getName()){

                exist = true;

            } else {

                    i++;

            }

        }

        return exist;

    }

    private void daysAutoCompleteTxt() {

        daysInput = findViewById(R.id.days_input_dashboard);

        autoCompleteTextView = findViewById(R.id.auto_complete_text);
        days = getResources().getStringArray(R.array.days);
        arrayAdapter = new ArrayAdapter(this, R.layout.drop_down_item, days);
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.clearFocus();

    }

}