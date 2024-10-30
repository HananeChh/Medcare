package com.moncefadj.medcare.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moncefadj.medcare.DataClasses.DoctorDataForHomePatient;
import com.moncefadj.medcare.DataClasses.DoctorsDatabase;
import com.moncefadj.medcare.Doctor.EditDoctorProfile;
import com.moncefadj.medcare.HelperClasses.doctorsAdapter;
import com.moncefadj.medcare.Medicaments.liste_medicaments;
import com.moncefadj.medcare.PatientHome.SpecialitiesAdapter;
import com.moncefadj.medcare.DataClasses.SpecialtiesData;
import com.moncefadj.medcare.PatientSearch.Search;
import com.moncefadj.medcare.ProfilePatient.PatientProfile;
import com.moncefadj.medcare.R;

import java.util.ArrayList;

public class PatientHome extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<SpecialtiesData> specialtiesData;
    SpecialitiesAdapter specialitiesAdapter;
    MeowBottomNavigation bottomNavigation;
    Toast toast;
    //vertical view
    RecyclerView doctorsRecycler;
    doctorsAdapter docAdapter;
     DoctorsDatabase docdata;
    boolean enableaniimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);

//underbar
        bottomNavigation = (MeowBottomNavigation) findViewById(R.id.bottom_navigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_med));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_search_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_profil));
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override

            public void onShowItem(MeowBottomNavigation.Model item) {
                Intent intent = null;
                switch (item.getId()) {
                    case 2: intent = new Intent(getApplicationContext(), liste_medicaments.class);
                        startActivity(intent);
                        break;
                    case 3: intent = new Intent(getApplicationContext(), Search.class);
                        startActivity(intent);
                        break;
                    case 4: intent = new Intent(getApplicationContext(), PatientProfile.class);
                        startActivity(intent);
                        break;
                    default:break;

                }

            }

        });
        boolean enableAnimation;
        //set home fragment initialy selected
        bottomNavigation.show(1, enableAnimation = true);
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                //display toast

            }

            ;
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                //display toast

            }
        });

        //show doctors

        doctorsRecycler = findViewById(R.id.doctors_recycler);
        doctorsRecycler.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        doctorsRecycler.setLayoutManager(manager);
        docAdapter = new doctorsAdapter(this);
        doctorsRecycler.setAdapter(docAdapter);
        docdata = new DoctorsDatabase();

        loadDocData();




        //assign varible
        recyclerView=findViewById(R.id.recycler_view);

        //creat integer array
        Integer[] categorieLogo={R.drawable.eye_ic_use,R.drawable.heart_ic_use
                ,R.drawable.lung_ic,R.drawable.tooth};

        //creat string array
        String[] categorieName={"Ophtalmologie","Cardiologie"
                ,"Pneumologie","Chir. dentaire"};

        //initilize arraylist
        specialtiesData =new ArrayList<>();
        for (int i=0;i<categorieLogo.length;i++){
            SpecialtiesData model=new SpecialtiesData(categorieLogo[i],categorieName[i]);
            specialtiesData.add(model);

        }

        //Design Horizontal lyout
        LinearLayoutManager layoutManager=new LinearLayoutManager(
                PatientHome.this,LinearLayoutManager.HORIZONTAL, false
        );
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //initialize mainAdapter
        specialitiesAdapter =new SpecialitiesAdapter(PatientHome.this, specialtiesData);
        //set mainAdapter to recyclerview
        recyclerView.setAdapter(specialitiesAdapter);
    }
    public class Myadapter extends ArrayAdapter<String> {
        Context context;
        int[] imgs;
        String mytitles[];
        String mydescription[];

        public Myadapter(Context c, String[] titles, int[] imgs, String[] description) {
            super(c, R.layout.verow, R.id.medcin, titles);
            this.context = c;
            this.imgs = imgs;
            this.mydescription = description;
            this.mytitles = titles;
        }
        @Override
        public int getCount() {
            return imgs.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater layoutInflater=(LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View verow = layoutInflater.inflate(R.layout.verow,parent,false);
            ImageView images;
            images =  verow.findViewById(R.id.icon);
            TextView mytitle;
            mytitle= verow.findViewById(R.id.medcin);
            TextView mydescription;
            mydescription=  verow.findViewById(R.id.text2);
            images.setImageResource(imgs[position]);
            return verow;
        }
    }


    //showing doctors

   private  void loadDocData() {
        docdata.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<DoctorDataForHomePatient> othDoctors = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()){
                    

                    DoctorDataForHomePatient doctors;
                    doctors = data.getValue(DoctorDataForHomePatient.class);
                    if(doctors.getSpecialty().matches("Généraliste")){
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