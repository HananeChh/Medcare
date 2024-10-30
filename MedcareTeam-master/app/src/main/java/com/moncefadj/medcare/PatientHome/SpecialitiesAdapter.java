package com.moncefadj.medcare.PatientHome;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moncefadj.medcare.DataClasses.SpecialtiesData;
import com.moncefadj.medcare.Patient.OneSpecialtyDoctors;
import com.moncefadj.medcare.Patient.cardiologie;
import com.moncefadj.medcare.Patient.Ophtalmologie;
import com.moncefadj.medcare.Patient.Pneumologie;
import com.moncefadj.medcare.R;

import java.util.ArrayList;

public class SpecialitiesAdapter extends RecyclerView.Adapter<SpecialitiesAdapter.viewHolder> {
    ArrayList<SpecialtiesData> specialtiesData;
    Context context;


    String[] specialite={"Ophtalmologie","Cardiologie","Pneumologie","Chirurgie dentaire"};
    public SpecialitiesAdapter(Context context, ArrayList<SpecialtiesData> specialtiesData){
        this.context=context;
        this.specialtiesData = specialtiesData;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //creat view
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        //set logo to imageView
        holder.imageView.setImageResource(specialtiesData.get(position).getCategorieLogo());
        //set name to text view
        holder.textView.setText(specialtiesData.get(position).getCategorieName());


    }

    @Override
    public int getItemCount() {
        return specialtiesData.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        //initialize variable
        ImageView imageView;
        TextView textView;

int id;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            //assign variable
            imageView=itemView.findViewById(R.id.image_view);
            textView=itemView.findViewById(R.id.text_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  ophtalmon ophta=new ophtalmon();
                    int pos=getAdapterPosition();
                    if (pos==0) {
                        Intent intent = new Intent(context, Ophtalmologie.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                      //  ophta.setTextView1(textView);

                    }

                    if (pos==1) {
                        Intent intent = new Intent(context, cardiologie.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }

                   if (pos==2) {
                        Intent intent = new Intent(context, Pneumologie.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }

                    if (pos==3) {
                        Intent intent = new Intent(context, OneSpecialtyDoctors.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }

            });

        }
    }

}
