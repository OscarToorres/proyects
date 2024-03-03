package com.example.proyecto.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.R;
import com.example.proyecto.basedatos.BDUtilities;
import com.example.proyecto.classobjects.Usuario;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button login;
    TextView forgotPassword;
    EditText username, password;
    Toast info;
    BDUtilities utilidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        utilidades = new BDUtilities(this);
        info = Toast.makeText(LoginActivity.this,"",Toast.LENGTH_SHORT);

        forgotPassword = findViewById(R.id.login_forgotPassword);
        login = findViewById(R.id.login_login);
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);

        forgotPassword.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == forgotPassword){
            Intent resetPass = new Intent(LoginActivity.this,ResetPasswordActivity.class);
            startActivity(resetPass);
        }else if(view == login){
            Usuario usuario = utilidades.comprobarUsuario(username.getText().toString(), password.getText().toString());
            System.out.println(usuario.toString());
            if (usuario.getId() != null) {
                Intent main = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(main);
                info.setText("Iniciando sesion...");
            } else {
                info.setText("Usuario o contraseña incorrecto");
            }
        }
        info.show();
    }
}