package com.moncefadj.medcare.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moncefadj.medcare.DataClasses.DoctorData;
import com.moncefadj.medcare.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditDoctorProfile extends AppCompatActivity {

    TextView name,fullSpecialty,address;
    TextInputLayout nameEdTxt,addressEdTxt,specialtyEdText, phoneEdTxt, descEdTxt,passEdTxt;
    ImageButton backToProfileBtn;
    ImageView confirmEditsBtn,profile;

    String uidDoctor;
    DatabaseReference doctorRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor_profile);

        name = findViewById(R.id.edit_profile_doctor_name);
        fullSpecialty = findViewById(R.id.edit_profile_doctor_specialty);
        address = findViewById(R.id.edit_profile_doctor_address);

        nameEdTxt = findViewById(R.id.edit_name);
        addressEdTxt = findViewById(R.id.edit_address);
        specialtyEdText = findViewById(R.id.edit_specialty);
        phoneEdTxt = findViewById(R.id.edit_phone);
        descEdTxt = findViewById(R.id.edit_desc);
        passEdTxt = findViewById(R.id.edit_password);
        profile = findViewById(R.id.edit_profile_doc_image);


        uidDoctor = FirebaseAuth.getInstance().getCurrentUser().getUid();
        doctorRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Doctors").child(uidDoctor);

        backToProfileBtn = findViewById(R.id.edit_back_to_doctor_profile);
        confirmEditsBtn = findViewById(R.id.confirm_edits_btn);

        backToProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditDoctorProfile.this, DoctorProfile.class);
                startActivity(intent);
                finish();
            }
        });

        confirmEditsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        setProfileData();

    }

    private void updateData() {
        if (nameEdTxt.getEditText().getText().toString().isEmpty()) {
            nameEdTxt.setError("Nom obligatoire");
            return;
        }
        if (addressEdTxt.getEditText().getText().toString().isEmpty()) {
            addressEdTxt.setError("Adresse obligatoire");
            return;
        }
        if (specialtyEdText.getEditText().getText().toString().isEmpty()) {
            specialtyEdText.setError("Specialty obligatoire");
            return;
        }
        if (phoneEdTxt.getEditText().getText().toString().isEmpty()) {
            phoneEdTxt.setError("Phone obligatoire");
            return;
        }
        if (descEdTxt.getEditText().getText().toString().isEmpty()) {
            phoneEdTxt.setError("Description obligatoire");
            return;
        }
        if (passEdTxt.getEditText().getText().toString().isEmpty()) {
            passEdTxt.setError("Mot de passe obligatoire");
            return;
        }

        HashMap<String , Object> map = new HashMap<>();
        map.put("name", nameEdTxt.getEditText().getText().toString());
        map.put("address", addressEdTxt.getEditText().getText().toString());
        map.put("fullSpecialty", specialtyEdText.getEditText().getText().toString());
        map.put("phone", phoneEdTxt.getEditText().getText().toString());
        map.put("desc", descEdTxt.getEditText().getText().toString());
        map.put("password", passEdTxt.getEditText().getText().toString());

        doctorRef.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(EditDoctorProfile.this, DoctorProfile.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditDoctorProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setProfileData() {

        doctorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    DoctorData doctorData = snapshot.getValue(DoctorData.class);

                    String img =doctorData.getProfileImg();
                    Picasso.get().load(img).into(profile);

                    name.setText(doctorData.getName());
                    fullSpecialty.setText(doctorData.getFullSpecialty());
                    address.setText(doctorData.getAddress());

                    nameEdTxt.getEditText().setText(doctorData.getName());
                    addressEdTxt.getEditText().setText(doctorData.getAddress());
                    specialtyEdText.getEditText().setText(doctorData.getFullSpecialty());
                    phoneEdTxt.getEditText().setText(doctorData.getPhone());
                    descEdTxt.getEditText().setText(doctorData.getDesc());
                    passEdTxt.getEditText().setText(doctorData.getPassword());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}