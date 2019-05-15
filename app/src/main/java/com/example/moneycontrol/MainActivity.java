package com.example.moneycontrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;
    private SignInButton sigInButton;
    private Button button, login, rpass;
    public static final int SIGN_IN_CODE = 777;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private ProgressBar progressBar;
    private EditText email, password;
    private TextView load, or;
    private View v, v2, v3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        login = findViewById(R.id.BtnLogin);
        email = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        or = findViewById(R.id.or);
        button = findViewById(R.id.BtnSingUp);
        v2 = findViewById(R.id.line2);
        v = findViewById(R.id.line1);
        v3 = findViewById(R.id.footer_line);
        load = findViewById(R.id.loading);
        rpass = findViewById(R.id.forgot_password);
        rpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SideNav.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SingUp.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        //opciones de inicio de sesion
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
        sigInButton = (SignInButton) findViewById(R.id.signInButton);
        sigInButton.setSize(SignInButton.SIZE_WIDE);
        sigInButton.setColorScheme(SignInButton.COLOR_LIGHT);
        sigInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IT = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(IT, SIGN_IN_CODE);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    goUserDetail();
                }
            }
        };
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            firebaseAuthWithGoogle(result.getSignInAccount());
        } else {
            Toast.makeText(this, R.string.log_in_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {
        progressBar.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);
        sigInButton.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        or.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        login.setVisibility(View.GONE);
        v.setVisibility(View.GONE);
        v2.setVisibility(View.GONE);
        v3.setVisibility(View.GONE);
        rpass.setVisibility(View.GONE);

        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);
                sigInButton.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                or.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                login.setVisibility(View.VISIBLE);
                v.setVisibility(View.VISIBLE);
                v2.setVisibility(View.VISIBLE);
                v3.setVisibility(View.VISIBLE);
                load.setVisibility(View.GONE);
                rpass.setVisibility(View.VISIBLE);

                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.firebase_auth_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goUserDetail() {

        Intent intent = new Intent(this, UserDetails.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
            //Toast.makeText(getApplicationContext(), R.string.close_s, Toast.LENGTH_LONG).show();
        }
    }

    private void goSingup() {
        Intent intent = new Intent(this, SingUp.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void Login(View view) {
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        if (Email.isEmpty() || Password.isEmpty()) {
            //Toast.makeText(MainActivity.this, R.string.empty, Toast.LENGTH_SHORT).show();
            email.setError("Empty field"/*,getResources().getDrawable(R.drawable.ic_logout)*/);
            password.setError("Empty field"/*,getResources().getDrawable(R.drawable.ic_logout)*/);
            //showAlerter(This);

        } else {
            firebaseAuth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Log.e("logged", "onComplete: " + task.isSuccessful());
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(MainActivity.this, UserDetails.class);
                                // Intent intent2 = new Intent(getApplicationContext(), UserDetails.class);
                                //  intent.putExtras(intent2);
                                startActivity(intent);

                            } else {
                                Toast toast = Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_LONG);
                                toast.show();
                                email.setError("");
                                password.setError("");

                            }


                        }
                    });
        }
    }


}
