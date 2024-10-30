package com.moncefadj.medcare.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moncefadj.medcare.Doctor.DoctorDashboard;
import com.moncefadj.medcare.Doctor.DoctorProfile;
import com.moncefadj.medcare.Patient.PatientHome;
import com.moncefadj.medcare.R;
import com.moncefadj.medcare.Underbar.underbar;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIMER = 4000;

    ImageView splashBg;
    TextView splashAppName;
    LottieAnimationView lottieAnimationView;

    SharedPreferences sharedPreferences;
    public static String MyPREFERENCES = "MyPrefs";
    public static String firstTime = "firstTimeKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        splashBg = findViewById(R.id.splash_bg);
        splashAppName = findViewById(R.id.splash_app_name);
        lottieAnimationView = findViewById(R.id.lottie);

        splashBg.animate().translationY(-5000).setDuration(1000).setStartDelay(4000);
        splashAppName.animate().translationY(1800).setDuration(1000).setStartDelay(4000);
        lottieAnimationView.animate().translationY(1800).setDuration(1000).setStartDelay(4000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                boolean isFirstTime = sharedPreferences.getBoolean(firstTime, true);

                if (isFirstTime) {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(firstTime, false);
                    editor.commit();

                    Intent intent = new Intent(SplashScreen.this, OnBoardingScreen.class);
                    startActivity(intent);
                    finish();

                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        // search if current user is patient or doctor
                        String currentUid = user.getUid();
                        DatabaseReference doctors = FirebaseDatabase.getInstance().getReference().child("Users").child("Doctors");
                        doctors.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(currentUid)) {  // user is a patient
                                    Intent intent = new Intent(SplashScreen.this, DoctorDashboard.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(SplashScreen.this, PatientHome.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        },SPLASH_TIMER);
    }
}