package com.example.proyecto.basedatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.widget.DrawableUtils;

import com.example.proyecto.classobjects.Usuario;

import java.util.ArrayList;
import java.util.List;

public class BDUtilities {

    private BDHelper dbHelper;
    private SQLiteDatabase db;

    public BDUtilities(Context context) {
        this.dbHelper = new BDHelper(context);
    }

    public boolean addUser(Usuario usuario){
        System.out.println(usuario.toString());
        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(BDSchema.Usuarios.COL_NOMBRE,usuario.getNombreUsuario());
        cv.put(BDSchema.Usuarios.COL_EMAIL,usuario.getEmail());
        cv.put(BDSchema.Usuarios.COL_PASSWORD,usuario.getPassword());
        if (db.insert(BDSchema.Usuarios.TABLE_NAME,null,cv) == -1){
          return false;
        }else {
            return true;
        }
    }

    public Usuario comprobarUsuario(String user, String password){
        Cursor c;
        Usuario usuario = new Usuario();
        db = dbHelper.getWritableDatabase();
        System.out.println(user + "  " + password);
        c = db.rawQuery("SELECT * FROM usuarios WHERE nombreUsuario like '" + user
                + "' AND password like '" + password + "'", null);
        if (c.moveToFirst()){
            usuario.setId(Integer.parseInt(c.getString(0)));
            usuario.setNombre(c.getString(1));
            usuario.setEmail(c.getString(2));
            usuario.setPassword(c.getString(3));
            return usuario;
        } else {
            return usuario;
        }
    }

    public List<Usuario> recuperarUsuarios(){
        Cursor c;
        List<Usuario> usuarios = new ArrayList<>();
        db = dbHelper.getWritableDatabase();
        c = db.rawQuery("SELECT * FROM usuarios", null);
        while (c.moveToNext()){
            Usuario usuario = new Usuario();
            usuario.setId(Integer.parseInt(c.getString(0)));
            usuario.setNombre(c.getString(1));
            usuario.setEmail(c.getString(2));
            usuario.setPassword(c.getString(3));
            usuarios.add(usuario);
        }
        return usuarios;
    }


}
