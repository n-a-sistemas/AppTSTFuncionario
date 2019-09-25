package com.example.apptstfuncionario;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.List;

public class Sessao{
    public static List<AuthUI.IdpConfig> criarLogin() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );
        return providers;
    }
}
