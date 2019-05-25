package com.example.moneycontrol;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
    private String currentArticleId, currentUserId, Image;
    FirebaseAuth firebaseAuth;
    private ArrayList<Articles> articlesArrayList = new ArrayList<>();
    private Button BtnUpload;
    private StorageReference myStorage;
    private static final int GALERY_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_articles);
        Image="https://firebasestorage.googleapis.com/v0/b/espantajopo-cd7bc.appspot.com/o/fotos%2Fstorage%2Femulated%2F0%2FDCIM%2FFacebook%2FFB_IMG_1558545927300.jpg?alt=media&token=90cc8c75-d130-486c-8eeb-a7dbe6cbece9";
        BtnUpload =findViewById(R.id.btnUploadImg);
        firebaseAuth =FirebaseAuth.getInstance();
        RootReference = FirebaseDatabase.getInstance().getReference();
        currentArticleId=RootReference.push().getKey();
        currentUserId=firebaseAuth.getCurrentUser().getUid();
        myStorage = FirebaseStorage.getInstance().getReference();
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
                    String IdOn,  NameOn,  DescriptionOn,  ImageOn,  UserIdOn;
                    Double PriceOn;

                    //ImageOn=Image;
                    IdOn=currentArticleId;
                    NameOn = ArticleName.getText().toString();
                    DescriptionOn = ArticleDesc.getText().toString();
                    PriceOn=Double.parseDouble(Price.getText().toString());
                    UserIdOn=currentUserId;
                  // LoadFirebaseData(IdOn, NameOn, DescriptionOn, PriceOn, UserIdOn);
                    Toast.makeText(CreateArticle.this, R.string.done, Toast.LENGTH_LONG).show();
                }
            }
        });
        BtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");// * es para que tome todas las extenciones
                startActivityForResult(intent,GALERY_INTENT);
            }
        });
    }
    private void LoadFirebaseData(String idOn, String nameOn, String descriptionOn, Double priceOn,String imageOn, String userIdOn) {
        Map<String, Object> ArticleData = new HashMap<>();
        ArticleData.put("IdArticle",idOn);
        ArticleData.put("Name",nameOn);
        ArticleData.put("Description", descriptionOn);
        ArticleData.put("Price", priceOn);
        ArticleData.put("Image", imageOn);
        ArticleData.put("UserId", userIdOn);
        RootReference.child("Articles").push().setValue(ArticleData);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== GALERY_INTENT && resultCode == RESULT_OK){
             final Uri uri=data.getData();
            final StorageReference filepath = myStorage.child("fotos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (ArticleName.getText().length() == 0 || ArticleDesc.getText().length() == 0 || Price.getText().length()==0) {
                                Alerter.create(CreateArticle.this).setText(R.string.empty)
                                        .setTitle("Error").setBackgroundColorRes(R.color.colorAccent)
                                        .setIcon(R.drawable.ic_format_list)
                                        .enableVibration(true)
                                        .setDismissable(true).show();
                                Toast.makeText(CreateArticle.this, R.string.empty, Toast.LENGTH_LONG).show();
                            }else {
                                Alerter.create(CreateArticle.this)
                                        .setTitle(R.string.upload_img)
                                        .setText(R.string.upload_img_s)
                                        .setIcon(R.drawable.ic_image)
                                        .setBackgroundColorRes(R.color.purble_black)
                                        .enableVibration(true)
                                        .setDismissable(true)
                                        .enableProgress(true)
                                        .show();
                                String IdOn, NameOn, DescriptionOn, UserIdOn;
                                Double PriceOn;
                                String url = uri.toString();
                                IdOn = currentArticleId;
                                NameOn = ArticleName.getText().toString();
                                DescriptionOn = ArticleDesc.getText().toString();
                                PriceOn = Double.parseDouble(Price.getText().toString());
                                UserIdOn = currentUserId;
                                Map<String, Object> ArticleData = new HashMap<>();
                                ArticleData.put("IdArticle", IdOn);
                                ArticleData.put("Name", NameOn);
                                ArticleData.put("Description", DescriptionOn);
                                ArticleData.put("Price", PriceOn);
                                ArticleData.put("Image", url);
                                ArticleData.put("UserId", UserIdOn);
                                RootReference.child("Articles").push().setValue(ArticleData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Se ha creado el nuevo articulo", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Error al subir", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            }
                        }
                    });

                    /* Toast.makeText(UserDetails.this,"se subio exitosamente",Toast.LENGTH_SHORT).show();*/
                }
            });//*/
                }


        }
    }

