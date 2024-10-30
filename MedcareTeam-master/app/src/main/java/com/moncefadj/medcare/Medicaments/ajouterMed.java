package com.moncefadj.medcare.Medicaments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.moncefadj.medcare.Common.LoginActivity;
import com.moncefadj.medcare.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;


public class ajouterMed extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, AdapterView.OnItemClickListener , DatePickerDialog.OnDateSetListener {
    private EditText EnomMed;
    private EditText Description;
    private EditText dateDebut, dateFin ;
    private TextView heure , heure2,heure3;
    private Button ajouter_med ;
    private Spinner instructions;
    private Button ajouter_au_list;
    private  int index =0;
    DatePickerDialog datePickerDialog;
    FirebaseDatabase data_base;
    DatabaseReference medsReference;
    DatabaseReference medReference;
    Dialog dialog;
    ImageView back;
    FirebaseUser uPatient;
    String uidPatient ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_med);
        EnomMed = (EditText) findViewById(R.id.nomMed);
        heure = (TextView) findViewById(R.id.heure);
        heure2 = (TextView) findViewById(R.id.heure2);
        heure3 = (TextView) findViewById(R.id.heure3);
        Description = (EditText) findViewById(R.id.description);
        back = (ImageView) findViewById(R.id.back_to_liste);

        ajouter_med = (Button) findViewById(R.id.ajoutertemps);
        ajouter_au_list = (Button ) findViewById(R.id.ajouterAuliste);
        instructions = (Spinner) findViewById(R.id.spinnerInst);
        ArrayAdapter<CharSequence> instructAdapter = ArrayAdapter.createFromResource(this, R.array.instructions, android.R.layout.simple_spinner_item);
        instructAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        instructions.setAdapter(instructAdapter);
        instructions.getOnItemSelectedListener();
        dateDebut = (EditText) findViewById(R.id.dateDebut);
        dateFin = (EditText)  findViewById(R.id.dateFin);
        // ** Dialog ** to ask patient if he want to add a med or mesure
        dialog = new Dialog(ajouterMed.this);
        dialog.setContentView(R.layout.custom_med_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_white));
        }
        dialog.setCancelable(false);
        // we can make animation for Dialog by mentioned it in Style
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        Button med_button = dialog.findViewById(R.id.add_med);
        Button mesure_button = dialog.findViewById(R.id.add_mesure);
        ImageView cancelDialogBtn = dialog.findViewById(R.id.cancel_dialog_btn);
        //----------------------buttons methods------------------------------------------------------

        ajouter_med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opentimepicker();

            }
        });
        ajouter_au_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });
        dateDebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        dateFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker2();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getApplicationContext(),liste_medicaments.class);
                startActivity(intent);
            }
        });


    }





    private void sendData() {
        String name = EnomMed.getText().toString();
        String descrip = Description.getText().toString();
        String time = heure.getText().toString();
        String time2 = heure2.getText().toString();
        String time3 = heure3.getText().toString();
        String instruction = instructions.getSelectedItem().toString();
        String datedebut = dateDebut.getText().toString();
        String datefin = dateFin.getText().toString();
        try {
            if (name.isEmpty()) {
                EnomMed.setError("Le nom du médicament est obligatoire!");

            }
            if (descrip.isEmpty()) {
                Description.setError("Veuillez donner une description! ");

            }
            if (datedebut.isEmpty()) {
                dateDebut.setError("la date de debut de prise est obligatoire !");

            }
            if (datefin.isEmpty()) {
                dateFin.setError("La date de fin de prise est obligatoire!");

            }
            if (time.isEmpty()) {
                heure.setError("Veuillez donner au moins une heure!");
                return;
            }
        }
        catch (Exception e){
            movetoliste();

        }




        uPatient = FirebaseAuth.getInstance().getCurrentUser();
        uidPatient = uPatient.getUid();
        data_base = FirebaseDatabase.getInstance();
        medsReference = data_base.getReference().child("Users").child("Patients").child(uidPatient).child("Medicaments");
        medReference = medsReference.child(name);
        medReference.setValue( new medDataDb(name,descrip,time,time2,time3,datedebut,datefin,instruction));


        finish();
    }
    private void movetoliste() {
        Intent intent = new Intent(this , liste_medicaments.class);
        startActivity(intent);
    }



    private void opentimepicker() {
        DialogFragment timePickerDialog = new TimePickerFragment();
        timePickerDialog.show(getSupportFragmentManager(), "time picker");

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onTimeSet(TimePicker timePicker, int intHourOfDay, int intMinute) {
        if(heure.getText().toString().equals("")) {

            heure.setText((intHourOfDay + ":" + intMinute));
            Calendar c = Calendar.getInstance();
            String[] parts = heure.getText().toString().split(":");
            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
            c.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
            c.set(Calendar.SECOND, 0);
            startAlarm(c);
        }
        else if (heure2.getText().toString().equals("")){
            heure2.setText((intHourOfDay + ":" + intMinute ));
            Calendar c = Calendar.getInstance();
            String[] parts = heure2.getText().toString().split(":");
            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
            c.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
            c.set(Calendar.SECOND, 0);
            startAlarm(c);

        }
        else {
            heure3.setText((intHourOfDay + ":" + intMinute ));
            Calendar c = Calendar.getInstance();
            String[] parts = heure3.getText().toString().split(":");
            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
            c.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
            c.set(Calendar.SECOND, 0);
            startAlarm(c);
        }


        ajouter_med.setText("Ajouter une autre heure de prise ");
        ajouter_med.setTextColor(Color.GRAY);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        Random r = new Random();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,r.nextInt(1000) , intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String choice = parent.getItemAtPosition(position).toString();
        Toast.makeText(getApplicationContext(),choice, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dateDebut.setText(dayOfMonth +","+ month +"," + year);
    }
    public void openDatePicker(){
        final Calendar calendar = Calendar.getInstance();
        int myear = calendar.get(Calendar.YEAR);
        int mmonth = calendar.get(Calendar.MONTH);
        int mday = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(ajouterMed.this,R.style.AppCompatDialogStyle ,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(year , month, day);
                dateDebut.setText(new SimpleDateFormat("dd/mm/yyyy").format(calendar.getTime()));
                int myear = calendar.get(Calendar.YEAR);
                int mmonth = calendar.get(Calendar.MONTH);
                int mday = calendar.get(Calendar.DAY_OF_MONTH);




            }

        },myear,mmonth,mday);
        datePickerDialog.show();
    }
    public void openDatePicker2(){
        final Calendar calendar = Calendar.getInstance();
        int myear = calendar.get(Calendar.YEAR);
        int mmonth = calendar.get(Calendar.MONTH);
        int mday = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(ajouterMed.this,R.style.AppCompatDialogStyle ,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(year , month, day);
                dateFin.setText(new SimpleDateFormat("dd/mm/yyyy").format(calendar.getTime()));
                int myear = calendar.get(Calendar.YEAR);
                int mmonth = calendar.get(Calendar.MONTH);
                int mday = calendar.get(Calendar.DAY_OF_MONTH);




            }

        },myear,mmonth,mday);
        datePickerDialog.show();
    }
}