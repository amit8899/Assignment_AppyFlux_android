package com.example.testapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.testapplication.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static ArrayList<Users> usersArrayList;
    private ImageButton imageButton;
    private EditText getUserName, getSubName;
    private Button btnDone;
    private String UserName, SubName;
    private AlertDialog alert;
    private FirebaseFirestore db;
    private DocumentReference docReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        imageButton = findViewById(R.id.add_items);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddData();
            }
        });
        populateData();
    }

    private void AddData() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        alertDialog.setView(customLayout);
        alert = alertDialog.create();
//        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getUserName = customLayout.findViewById(R.id.edit_user);
        getSubName = customLayout.findViewById(R.id.edit_sub);
        btnDone = customLayout.findViewById(R.id.btn_add_done);

        btnDone.setOnClickListener(this);
        alert.show();
    }


    private void populateData() {
        if(usersArrayList == null){
            usersArrayList = new ArrayList<>();

            db = FirebaseFirestore.getInstance();
            db.collection("UserData").orderBy("name", Query.Direction.ASCENDING).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d: list){
                                Users users = d.toObject(Users.class);
                                usersArrayList.add(users);
                            }
                            if(HomeFragment.getInstance() != null && HomeFragment.getInstance().adapterUsers != null){
                                HomeFragment.getInstance().adapterUsers.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(HomeFragment.getInstance() != null && HomeFragment.getInstance().adapterUsers != null){
            HomeFragment.getInstance().adapterUsers.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_done:
                UserName = String.valueOf(getUserName.getText());
                SubName = String.valueOf(getSubName.getText());
                if(UserName.length() != 0 && SubName.length() != 0){
                    if(usersArrayList != null){
                        docReference = db.collection("UserData").document();
                        Users users = new Users(UserName, SubName, docReference.getId());
                        addDataFireStore(users);
                        usersArrayList.add(0, users);
                        if(HomeFragment.getInstance() != null){
                            HomeFragment.getInstance().adapterUsers.notifyItemInserted(0);
                            HomeFragment.getInstance().adapterUsers.notifyItemRangeChanged(0, usersArrayList.size());
                        }
                        alert.cancel();
                    }
                }else {
                    getUserName.setError("Name Required");
                }
                break;
        }
    }

    private void addDataFireStore(Users u) {
        try {
            docReference.set(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Added to FireBase", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, "Addition Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}