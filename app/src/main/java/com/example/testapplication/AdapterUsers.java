package com.example.testapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static com.example.testapplication.MainActivity.usersArrayList;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.viewHolder> {

    private ArrayList<Users> modelList;
    private Context mContext;
    private FirebaseFirestore db;

    public AdapterUsers(ArrayList<Users> modelList, Context mContext) {
        this.modelList = modelList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_adapter, parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.name.setText(usersArrayList.get(position).getName());
        holder.subName.setText(usersArrayList.get(position).getSubName());
        db = FirebaseFirestore.getInstance();

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
                dialog.setMessage("Do you want to Remove "+usersArrayList.get(position).getName());
                dialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(0, usersArrayList.size());
                        Toast.makeText(mContext, "User Removed", Toast.LENGTH_SHORT).show();

                        DocumentReference doc = db.collection("UserData")
                                .document(usersArrayList.get(position).getNote_Id());
                        doc.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(mContext, "Deleted from firestore", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(mContext, "Deletion Failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        usersArrayList.remove(position);
                    }
                });
                dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alertDialog=dialog.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView name, subName, delete;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name);
            subName = itemView.findViewById(R.id.user_sub_name);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
