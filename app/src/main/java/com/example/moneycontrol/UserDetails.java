package com.example.moneycontrol;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tapadoo.alerter.Alerter;

public class UserDetails extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private ImageView photoImageView;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView idTextView;
    private DrawerLayout drawer;
    private GoogleApiClient googleApiClient;
    private Button upload, articleBtn; // upload image
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private StorageReference myStorage;
    private static final int GALERY_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

 /*       Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
cu
*/
        myStorage = FirebaseStorage.getInstance().getReference();
        photoImageView = (ImageView) findViewById(R.id.photoImageView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        idTextView = (TextView) findViewById(R.id.idTextView);
        upload = (Button) findViewById(R.id.addimage);
        articleBtn= findViewById(R.id.createArt);

        articleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(UserDetails.this, CreateArticle.class);
                startActivity(intent);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");// * es para que tome todas las extenciones
                startActivityForResult(intent,GALERY_INTENT);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setUserData(user);
                    Alerter.create(UserDetails.this)
                            .setTitle(R.string.welcome)
                            .setText(getText(R.string.welcome)+" "+user.getEmail())
                            .setIcon(R.drawable.ic_person)
                            .setBackgroundColorRes(R.color.purble_black)
                            .enableVibration(true)
                            .setDismissable(true)
                            .show();
                } else {
                    goLogInScreen();
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== GALERY_INTENT && resultCode == RESULT_OK){
            Uri uri=data.getData();
            StorageReference filepath = myStorage.child("fotos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Alerter.create(UserDetails.this)
                            .setTitle(R.string.upload_img)
                            .setText(R.string.upload_img_s)
                            .setIcon(R.drawable.ic_image)
                            .setBackgroundColorRes(R.color.purble_black)
                            .enableVibration(true)
                            .setDismissable(true)
                            .enableProgress(true)
                            .show();
                    /*Toast.makeText(UserDetails.this,"se subio exitosamente",Toast.LENGTH_SHORT).show();*/
                }
            });
        }
    }
    /* private void setSupportActionBar(Toolbar toolbar) {
    }*/

    private void setUserData(FirebaseUser user) {
        nameTextView.setText(user.getDisplayName());
        emailTextView.setText(user.getEmail());
        // idTextView.setText(user.getUid());
        Glide.with(this).load(user.getPhotoUrl()).into(photoImageView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logOut(View view) {
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.close_sesion_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void revoke(View view) {
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.revoke, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

}
