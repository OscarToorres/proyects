package com.example.proyecto.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyecto.R;
import com.example.proyecto.basedatos.BDUtilities;
import com.example.proyecto.classobjects.Usuario;

import java.util.List;

public class StartActivity extends AppCompatActivity {

    Button google, register,facebook;
    TextView iniciarSesion;
    ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        BDUtilities dbUtilities = new BDUtilities(this);

        register = findViewById(R.id.start_register);
        google = findViewById(R.id.start_google);
        facebook = findViewById(R.id.start_facebook);
        iniciarSesion = findViewById(R.id.start_iniciarSesion);
        close = findViewById(R.id.start_close);

        register.setOnClickListener(view -> {
            Intent register = new Intent(StartActivity.this,RegisterActivity2.class);
            startActivity(register);
        });
        iniciarSesion.setOnClickListener(view -> {
            Intent login = new Intent(StartActivity.this,LoginActivity.class);
            startActivity(login);
        });
        close.setOnClickListener(view -> {
            Intent start = new Intent(StartActivity.this,MainActivity.class);
            startActivity(start);
            finish();
        });
    }
}