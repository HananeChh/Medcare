package com.moncefadj.medcare.HelperClasses;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.moncefadj.medcare.DataClasses.PatientData;
import com.moncefadj.medcare.R;

import java.util.ArrayList;


public class PatientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  final  Context context;
    private ArrayList<PatientData> PatientsList ;
    String UidDoctor, day;


    public PatientsAdapter(Context context , ArrayList<PatientData> PatientsList, String uidDoctor, String vDay) {
        this.context = context;
        this.PatientsList = PatientsList;
        this.UidDoctor = uidDoctor;
        this.day = vDay;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.activity_patients_adapter, parent,false);
        return new com.moncefadj.medcare.HelperClasses.patientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        com.moncefadj.medcare.HelperClasses.patientsViewHolder viewHolder = (com.moncefadj.medcare.HelperClasses.patientsViewHolder) holder;
        PatientData patients = PatientsList.get(position);
        viewHolder.name.setText(patients.getName());
        viewHolder.time.setText(patients.getTime());

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(viewHolder.img.getContext());
                builder.setTitle("Voulez vous vraiment supprimer ce patient?");
                builder.setMessage("Si vous cliquez sur Oui, vous aller annuler le rendez-vous pris par ce patient et il sera par la suite supprimé de votre liste de reception.");

                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String  time = PatientsList.get(position).getTime();
                        String id = PatientsList.get(position).getId();
                        FirebaseDatabase.getInstance().getReference().child("Users").child("Doctors").child(UidDoctor).child("PatientsWithRdv").child(day).child(id).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Users").child("Doctors").child(UidDoctor).child("rdvTimes").child(day).child(time).child("available").setValue("yes");

                    }
                });

                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                builder.show();
            }
        });

        /* viewHolder.delete.setOnClickListener(view ->  {

                AlertDialog.Builder builder=new AlertDialog.Builder(viewHolder.img.getContext());
                builder.setTitle("Voulez vous vraiment supprimer ce patient?");
                builder.setMessage("Si vous cliquez sur Oui, vous aller annuler le rendez-vous pris par ce patient et il sera par la suite supprimé de votre liste de reception.");
                builder.setPositiveButton("oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String str = patients.getId();

                        FirebaseDatabase.getInstance().getReference().child("Users").child("Doctors").child(UidDoctor).child("PatientsWithRdv").child("Vendredi").child(str)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            PatientsList.remove(patients);
                                            notifyDataSetChanged();
                                            Toast.makeText(context , "data deleted succesfully ", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            Toast.makeText(context , "erreur "+task.getException().getMessage() , Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    }
                });

                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                builder.show();
        }); */

    }


    @Override
    public int getItemCount() {
        return PatientsList.size();
    }

}