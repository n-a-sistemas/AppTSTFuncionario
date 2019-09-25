package com.example.apptstfuncionario;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Funcionario funcionario = new Funcionario();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        final String resultado = sharedPreferences.getString("LOGIN", "");

        conectaBanco();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!Boolean.parseBoolean(resultado)){
                    criarLogin();
                    ranking();
                }
            }
        }, 2000);

    }

    private void criarLogin() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                123
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123){

            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK){

                if (response.isNewUser()){

                    this.funcionario.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    this.funcionario.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    this.funcionario.setValido(false);
                    databaseReference
                            .child("projetotst")
                            .child("funcionario")
                            .child(funcionario.getUid())
                            .setValue(funcionario);
                }

                sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("LOGIN", "true");
                editor.apply();
            }
            else {
                if (response == null){
                    finish();
                }
            }
        }
    }

    public void ranking(){
        Intent intent = new Intent(
                SplashScreen.this, MainActivity.class
        );
        startActivity(intent);

    }

    public void conectaBanco(){
        FirebaseApp.initializeApp(SplashScreen.this);
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}
