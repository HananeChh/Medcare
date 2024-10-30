package com.moncefadj.medcare.Medicaments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.moncefadj.medcare.R;

import java.util.ArrayList;

public class mesureAdapter extends RecyclerView.Adapter<mesureAdapter.viewHolder>{
    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView nameitm, valeur, time;
        ImageButton deletemesure;
        ImageView mesureimg;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            nameitm = (TextView) itemView.findViewById(R.id.mesurename);
            valeur = (TextView) itemView.findViewById(R.id.mesurevaleur);
            time = (TextView) itemView.findViewById(R.id.timemesure);
            deletemesure = (ImageButton) itemView.findViewById(R.id.deletemesure);
            mesureimg = (ImageView) itemView.findViewById(R.id.mesureimg);

        }
    }


    private final Context context;
    private final ArrayList<mesureData> list ;
    public mesureAdapter(Context c,ArrayList<mesureData> list) {
        this.list = list;
        this.context = c;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_mesure_item, parent , false);
        return new mesureAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        mesureData onemesure = list.get(position);
        holder.nameitm.setText(onemesure.getNomMesure());
        holder.valeur.setText( onemesure.getValeur());
        holder.time.setText(onemesure.getTime());
        holder.deletemesure.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               AlertDialog.Builder builder=new AlertDialog.Builder(holder.mesureimg.getContext());
               builder.setTitle("Voulez vous vraiment supprimer cette mesure?");
               builder.setMessage("Si vous cliquez sur Oui, la mesure sera plus disponible dans votre liste ");

               builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {


                       FirebaseDatabase.getInstance().getReference().child("Users").child("Patients").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Mesures").child(onemesure.getNomMesure()).removeValue()
                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if (task.isSuccessful()){
                                           list.remove(list.get(position));
                                           notifyDataSetChanged();
                                           Toast.makeText(context , "Mesure supprimée", Toast.LENGTH_LONG).show();
                                       }
                                       else{
                                           Toast.makeText(context , "Erreur "+task.getException().getMessage() , Toast.LENGTH_LONG).show();
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
           }


       });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }





}
