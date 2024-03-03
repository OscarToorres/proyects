package com.example.proyecto.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto.R;
import com.example.proyecto.basedatos.BDUtilities;
import com.example.proyecto.classobjects.Usuario;

public class RegisterActivity2 extends AppCompatActivity {

    Button confirm;
    EditText username, password, confirmPass, email;
    Toast info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        getSupportActionBar().hide();
        BDUtilities utilities = new BDUtilities(this);
        info = Toast.makeText(RegisterActivity2.this, "", Toast.LENGTH_SHORT);

        confirm = findViewById(R.id.register2_registrarse);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.createpassword);
        confirmPass = findViewById(R.id.confirmPassword);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarCampos() == true){
                    if (password.getText().toString().equals(confirmPass.getText().toString())) {
                        Usuario user = new Usuario(username.getText().toString(),
                                email.getText().toString(),
                                password.getText().toString());
                        if (utilities.addUser(user) == true) {
                            info.setText("Usuario añadido");
                            Intent main = new Intent(RegisterActivity2.this, MainActivity.class);
                            startActivity(main);
                        } else {
                            info.setText("No se ha podido añadir el usuario");
                        }
                    } else {
                        info.setText("Las contraseñas no coinciden");
                    }
                }
                info.show();
            }
        });
    }

    private boolean comprobarCampos(){
        if ("".equals(username.getText().toString()) || "".equals(email.getText().toString()) ||
                "".equals(confirmPass.getText().toString()) || "".equals(password.getText().toString())){
            info.setText("Tienes campos sin rellegar");
            return false;
        } else {
            return true;
        }
    }
}