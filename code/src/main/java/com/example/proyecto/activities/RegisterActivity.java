package com.example.proyecto.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto.R;
import com.example.proyecto.classobjects.Usuario;
import com.example.proyecto.utilities.Dates;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Button chooseDate,keepGoing;
    EditText nombre,apellidos,pais,birthday,genero;
    Dates date;
    Toast info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        date = new Dates();
        info = Toast.makeText(RegisterActivity.this, "", Toast.LENGTH_SHORT);

        birthday = findViewById(R.id.birthday);
        nombre = findViewById(R.id.name);
        apellidos = findViewById(R.id.surname);
        pais = findViewById(R.id.country);
        genero = findViewById(R.id.gender);


        chooseDate = findViewById(R.id.chooseDate);
        keepGoing = findViewById(R.id.keepGoing);

        chooseDate.setOnClickListener(this);
        keepGoing.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == chooseDate){
            date.createDate(RegisterActivity.this,birthday);
        }else if (view == keepGoing) {
            Intent cont = new Intent(RegisterActivity.this,RegisterActivity2.class);
            cont.putExtra("nombre",nombre.getText().toString());
            cont.putExtra("apellidos",apellidos.getText().toString());
            cont.putExtra("cumpleaños",birthday.getText().toString());
            cont.putExtra("pais",pais.getText().toString());
            cont.putExtra("genero",genero.getText().toString());
//            if (this.comprobarCampos() == true) {
                startActivity(cont);
//            }
        }
        info.show();
    }

    private boolean comprobarCampos(){
        if ("".equals(birthday.getText().toString()) || "".equals(nombre.getText().toString()) ||
                "".equals(apellidos.getText().toString()) || "".equals(pais.getText().toString())
                || "".equals(genero.getText().toString())){
            info.setText("Tienes campos sin rellegar");
            return false;
        } else {
            info.setText("Todos los campos estan completos");
            return true;
        }
    }
}