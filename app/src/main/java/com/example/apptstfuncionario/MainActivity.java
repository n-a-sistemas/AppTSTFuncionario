package com.example.apptstfuncionario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Funcionario funcionario = new Funcionario();


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.sair){




            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("LOGIN", "false");
            editor.apply();

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Sessao.criarLogin())
                            .build(),
                    123
            );
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {

            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {

                if (response.isNewUser()){

                    this.funcionario.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    this.funcionario.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    this.funcionario.setValido(false);
                    databaseReference
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

    @Override
    public void onBackPressed() {
        sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);

        String resultado = sharedPreferences.getString("LOGIN", "");
        if (!Boolean.parseBoolean(resultado)){ finish(); }
    }
}

