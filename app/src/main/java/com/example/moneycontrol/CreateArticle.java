package com.example.moneycontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateArticle extends AppCompatActivity {
    private DatabaseReference RootReference, myDta;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private Button BtnCreate;
    private EditText ArticleName, ArticleDesc, Price;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private String currentUserId;
    FirebaseAuth firebaseAuth;
    private ArrayList<Articles> articlesArrayList = new ArrayList<>();
    private Button BtnUpload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_articles);
        BtnUpload =findViewById(R.id.btnUploadImg);
        firebaseAuth =FirebaseAuth.getInstance();
        RootReference = FirebaseDatabase.getInstance().getReference();
        currentUserId=RootReference.push().getKey();
        BtnCreate = findViewById(R.id.btnCreateArticle);
        ArticleName =findViewById(R.id.TxtArticleName);
        ArticleDesc =findViewById(R.id.TxtArticleDesc);
        Price =findViewById(R.id.TextPrice);
        BtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ArticleName.getText().length() == 0 || ArticleDesc.getText().length() == 0 || Price.getText().length()==0) {
                    Alerter.create(CreateArticle.this).setText(R.string.empty)
                            .setTitle("Error").setBackgroundColorRes(R.color.colorAccent)
                            .setIcon(R.drawable.ic_format_list)
                            .enableVibration(true)
                            .setDismissable(true).show();
                    Toast.makeText(CreateArticle.this, R.string.empty, Toast.LENGTH_LONG).show();
                } else {
                    String IdOn, NameOn, LastNameOn, EmailOn, PasswordOn;
                    IdOn=currentUserId;
                    NameOn = ArticleName.getText().toString();
                    LastNameOn = ArticleDesc.getText().toString();
                   // LoadFirebaseData(IdOn, NameOn, LastNameOn, EmailOn, PasswordOn);
                    Toast.makeText(CreateArticle.this, R.string.done, Toast.LENGTH_LONG).show();
                }
            }
        });
        BtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateArticle.this, MainActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
    private void LoadFirebaseData(String idOn, String nameOn, String descriptionOn, String priceOn, String passwordOn) {
        Map<String, Object> ArticleData = new HashMap<>();
        ArticleData.put("Id",idOn);
        ArticleData.put("ArticleName",nameOn);
        ArticleData.put("LastName", descriptionOn);
        ArticleData.put("Email", priceOn);
        ArticleData.put("Password", passwordOn);
        RootReference.child("User").push().setValue(ArticleData);
    }
}
