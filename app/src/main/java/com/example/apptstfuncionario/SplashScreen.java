package com.example.apptstfuncionario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        final String resultado = sharedPreferences.getString("LOGIN", "");



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!Boolean.parseBoolean(resultado)) {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Sessao.criarLogin())
                                    .build(),
                            123
                    );
                    ranking();
                }
            }
        }, 2000);


    }

    public void ranking(){
        Intent intent = new Intent(
                SplashScreen.this, MainActivity.class
        );
        startActivity(intent);

    }

}
