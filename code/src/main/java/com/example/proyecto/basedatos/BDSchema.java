package com.example.proyecto.basedatos;

import android.provider.BaseColumns;

public class BDSchema {

    public static class Usuarios implements BaseColumns {
        public static final String TABLE_NAME ="usuarios";
        public static final String COL_ID ="id";
        public static final String COL_NOMBRE ="nombreUsuario";
        public static final String COL_EMAIL ="email";
        public static final String COL_PASSWORD ="password";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " +  Usuarios.TABLE_NAME + " (" +
                    Usuarios.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Usuarios.COL_NOMBRE + " TEXT UNIQUE NOT NULL,"+
                    Usuarios.COL_EMAIL + " TEXT UNIQUE NOT NULL,"+
                    Usuarios.COL_PASSWORD + " TEXT NOT NULL)";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Usuarios.TABLE_NAME;
}

