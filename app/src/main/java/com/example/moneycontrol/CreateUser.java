package com.example.moneycontrol;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateUser extends AppCompatActivity implements View.OnClickListener{
    private DatabaseReference RootReference, myDta;
    private FirebaseDatabase firebaseDatabase;
    private Button BtnSingup;
    private EditText Name, Lastname,Email, Password;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private ArrayList<User> userArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sing_up);
        BtnSingup = findViewById(R.id.btn_signUp);
        RootReference = FirebaseDatabase.getInstance().getReference();
        Name=findViewById(R.id.TxtName);
        Lastname=findViewById(R.id.TxtLastname);
        Email=findViewById(R.id.TxtEmail);
        Password=findViewById(R.id.TxtPassword);
        BtnSingup.setOnClickListener(this);
    }
    private void LoadFirebaseData(String nameOn, String lastNameOn, String emailOn, String passwordOn) {
        Map<String, Object> UserData = new HashMap<>();
        UserData.put("Name", nameOn);
        UserData.put("LastName", lastNameOn);
        UserData.put("Email", emailOn);
        UserData.put("Password", passwordOn);
        RootReference.child("User").push().setValue(UserData);
    }

    @Override
    public void onClick(View v) {
        if (Name.getText().length() == 0 || Lastname.getText().length() == 0 || Email.getText().length() == 0 || Password.getText().length() == 0) {
            Toast.makeText(this, R.string.empty, Toast.LENGTH_LONG).show();
        } else {
            switch (v.getId()) {
                case R.id.BtnSingUp:
                    String NameOn, LastNameOn, EmailOn, PasswordOn;
                    NameOn = Name.getText().toString();
                    LastNameOn = Lastname.getText().toString();
                    EmailOn = Email.getText().toString();
                    PasswordOn = Password.getText().toString();
                    LoadFirebaseData(NameOn, LastNameOn, EmailOn, PasswordOn);
                    Toast.makeText(this, R.string.done, Toast.LENGTH_LONG).show();
                    break;
            }
        }

    }
    private void getData(){
        RootReference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    userArrayList.clear();
                    for(final DataSnapshot ds: dataSnapshot.getChildren()){
                        User user= ds.getValue(User.class);
                        String name = user.getName();
                        String lastname = user.getLastname();
                        String email = user.getEmail();
                        String password = user.getPassword();
                        userArrayList.add(user);
                    }
                    /*UserAdapter = new UserAdapter (userArrayList,R.layout.contact_detail);
                    recyclerView.setAdapter(contactAdapter);*/
                }else{

                    Toast.makeText(getApplicationContext(), R.string.empty, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
