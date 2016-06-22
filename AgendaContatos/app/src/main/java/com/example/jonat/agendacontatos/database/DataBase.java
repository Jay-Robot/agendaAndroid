package com.example.jonat.agendacontatos.database;

/**
 * Created by jonat on 18/06/2016.
 * criando e mantendo o banco de dados
 */

import android.content.Context;
import android.database.sqlite.*;


//classe banco de dados herdando de SQLite : padrão android
public class DataBase extends SQLiteOpenHelper{

    public DataBase(Context context){
        super(context,"AGENDA",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(ScriptSQL.getCreateContato());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
