package com.moncefadj.medcare.Medicaments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moncefadj.medcare.R;
import java.util.ArrayList;

import static androidx.core.content.res.ResourcesCompat.getDrawable;
import static com.moncefadj.medcare.R.drawable.rounded_corner_white;
import static java.lang.String.*;

public class
medAdapter extends RecyclerView.Adapter<medAdapter.viewHolder> {
    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView nameitm,descriptionitm, timeitm , timeitm2, timeitm3;
        ImageButton deletemed ;
        ImageView medimg;
        CardView item_med;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            item_med = (CardView) itemView.findViewById(R.id.meditemcard);
            nameitm = (TextView) itemView.findViewById(R.id.nameitem);
            descriptionitm = (TextView) itemView.findViewById(R.id.descriptionitem);
            timeitm = (TextView) itemView.findViewById(R.id.timeitem);
            timeitm2 = (TextView) itemView.findViewById(R.id.timeitem2);
            timeitm3 = (TextView) itemView.findViewById(R.id.timeitm3);
            deletemed = (ImageButton) itemView.findViewById(R.id.deletemed);
            medimg = (ImageView) itemView.findViewById(R.id.medimg);

        }
    }

    private final Context context;
    private final ArrayList<medDataDb> list ;
    Dialog dialog;
    DatabaseReference dbreference ;
    FirebaseUser uPatient;
    String uidPatient;
    public medAdapter(Context c, ArrayList<medDataDb> list) {
        this.context = c;
        this.list = list ;
    }

    @NonNull
    @Override
    public medAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_item, parent , false);
         viewHolder myviewHolder = new viewHolder(view);
         myviewHolder.item_med.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 dialog = new Dialog(context);
                 dialog.setContentView(R.layout.med_dialog);
                 dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                     Drawable draw = context.getResources().getDrawable(R.drawable.rounded_corner_white);
                     dialog.getWindow().setBackgroundDrawable(draw);
                 }
                 dialog.setCancelable(false);
                 // we can make animation for Dialog by mentioned it in Style
                 dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                 TextView nom = (TextView) dialog.findViewById(R.id.nom_meD);
                 ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel_med_D);
                 TextView dateDebut = (TextView) dialog.findViewById(R.id.debutD);
                 TextView datefin = (TextView) dialog.findViewById(R.id.finD);
                 TextView instruction = (TextView) dialog.findViewById(R.id.instructionM);
                 nom.setText(list.get(myviewHolder.getAbsoluteAdapterPosition()).getNomMed());
                 dateDebut.setText(list.get(myviewHolder.getAbsoluteAdapterPosition()).getDatedebut());
                 datefin.setText(list.get(myviewHolder.getAbsoluteAdapterPosition()).getDatefin());
                 instruction.setText(list.get(myviewHolder.getAbsoluteAdapterPosition()).getInstruction());
                 cancel.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         dialog.dismiss();
                     }
                 });
                 dialog.show();
             }
         });
        return myviewHolder;


    }


    @Override
    public void onBindViewHolder(medAdapter.viewHolder holder, int position) {
        medData medicament = list.get(position);
        holder.nameitm.setText(medicament.getNomMed());
        holder.descriptionitm.setText(medicament.getDescriptionMed());
        holder.timeitm.setText(medicament.getTempsMed());
        holder.timeitm2.setText(medicament.getTempsMed2());
        holder.timeitm3.setText(medicament.getTempsMed3());
        holder.deletemed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.medimg.getContext());
                builder.setTitle("Voulez vous vraiment supprimer ce medicament?");
                builder.setMessage("Si vous cliquez sur Oui, le medicament sera plus disponible dans votre liste et donc pas de rappel pour le prendre ");

                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        FirebaseDatabase.getInstance().getReference().child("Users").child("Patients").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Medicaments").child(medicament.getNomMed()).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            list.remove(list.get(position));
                                            notifyDataSetChanged();
                                            Toast.makeText(context , "Médicament supprimé.", Toast.LENGTH_LONG).show();
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
