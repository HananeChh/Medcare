
package com.moncefadj.medcare.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moncefadj.medcare.Common.LoginActivity;
import com.moncefadj.medcare.Doctor.DoctorProfile;
import com.moncefadj.medcare.R;
import com.moncefadj.medcare.Underbar.underbar;

import java.util.HashMap;

public class PatientSignUp extends AppCompatActivity {

    FirebaseUser uPatient;
    String uidPatient;
    FirebaseAuth fAuth;
    FirebaseDatabase db;
    DatabaseReference patientsRef;
    DatabaseReference patientRef;

    TextInputLayout nameInput,emailInput,passInput,confPassInput,phoneInput,dayInput,monthInput,yearInput;
    String nameTxt,emailTxt,passTxt,confPassTxt,phoneTxt,dayTxt,monthTxt,yearTxt,profiletxt;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sign_up);

        signUpBtn = findViewById(R.id.sign_up_button);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getEditTextInputs();

                clearErrors();   // this fun will clear the SetError (if it was appearing before) of all EditTexts

                singUpUser();

            }
        });


    }

    private void singUpUser() {
        // check inputs if are empty ....
        if (nameTxt.isEmpty()) {
            nameInput.setError("le nom et le prénom sont obligatoires");
            return;  // we will leave the OnClick fun
        }

        if (emailTxt.isEmpty()) {
            emailInput.setError("l'Email est obligatoire");
            return;
        }

        if (passTxt.isEmpty()) {
            passInput.setError("le Mot de passe est obligatoire");
            return;
        }

        if (confPassTxt.isEmpty()) {
            confPassInput.setError("la confiramtion est obligatoire");
            return;
        }

        if (phoneTxt.isEmpty()) {
            phoneInput.setError("le Téléphone est obligatoire");
            return;
        }

        if (dayTxt.isEmpty()) {
            dayInput.setError("le jour est obligatoire");
            return;
        }

        if (monthTxt.isEmpty()) {
            monthInput.setError("le mois est obligatoire");
            return;
        }

        if (yearTxt.isEmpty()) {
            yearInput.setError("l'année est obligatoire");
            return;
        }
        if (!passTxt.equals(confPassTxt)) {
            passInput.setError("Les mots de passe ne sont pas equivalents");
            confPassInput.setError("Les mots de passe ne sont pas equivalents");
            return;
        }

        int dayInt = Integer.parseInt(dayTxt);
        int monthInt = Integer.parseInt(monthTxt);
        int yearInt = Integer.parseInt(yearTxt);

        if (dayInt < 1 || dayInt > 31) {
            dayInput.setError("Veuillez pas dépasser la limite");
            return;
        }
        if (monthInt < 1 || monthInt > 12) {
            monthInput.setError("Veuillez pas dépasser la limite");
            return;
        }
        if (yearInt > 2002 || dayInt > 2021) {
            yearInput.setError("Veuillez pas dépasser la limite");
            return;
        }


        fAuth = FirebaseAuth.getInstance();
        fAuth.createUserWithEmailAndPassword(emailTxt, passTxt).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                addPatientToDB();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PatientSignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addPatientToDB() {
        uPatient = FirebaseAuth.getInstance().getCurrentUser();
        uidPatient = uPatient.getUid();

        db = FirebaseDatabase.getInstance();
        patientsRef = db.getReference().child("Users").child("Patients");
        patientRef = patientsRef.child(uidPatient);

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", uidPatient);
        map.put("name", nameTxt);
        map.put("email", emailTxt);
        map.put("phone", phoneTxt);
       // map.put("password",passTxt);
        map.put("password", passTxt);
        map.put("profile", profiletxt);

        HashMap<String, Object> birthMap = new HashMap<>();
        birthMap.put("day", dayTxt);
        birthMap.put("month", monthTxt);
        birthMap.put("year", yearTxt);


        patientRef.child("BirthDay").updateChildren(birthMap);
        patientRef.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(PatientSignUp.this, underbar.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PatientSignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearErrors() {
        nameInput.setError(null);
        emailInput.setError(null);
        passInput.setError(null);
        confPassInput.setError(null);
        phoneInput.setError(null);
        dayInput.setError(null);
        monthInput.setError(null);
        yearInput.setError(null);
    }

    private void getEditTextInputs() {

        nameInput = findViewById(R.id.name_inputLayout);
        emailInput = findViewById(R.id.email_inputLayout);
        passInput = findViewById(R.id.password_InputLayout);
        confPassInput = findViewById(R.id.conf_passwordInputLayout);
        phoneInput = findViewById(R.id.phone_InputLayout);
        dayInput = findViewById(R.id.day_InputLayout);
        monthInput = findViewById(R.id.month_InputLayout);
        yearInput = findViewById(R.id.year_InputLayout);

        nameTxt = nameInput.getEditText().getText().toString();
        emailTxt = emailInput.getEditText().getText().toString();
        passTxt = passInput.getEditText().getText().toString();
        confPassTxt = confPassInput.getEditText().getText().toString();
        phoneTxt = phoneInput.getEditText().getText().toString();
        dayTxt = dayInput.getEditText().getText().toString();
        monthTxt = monthInput.getEditText().getText().toString();
        yearTxt = yearInput.getEditText().getText().toString();
    }


    public void backToLogin(View view) {
        Intent intent = new Intent(PatientSignUp.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PatientSignUp.this, LoginActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}