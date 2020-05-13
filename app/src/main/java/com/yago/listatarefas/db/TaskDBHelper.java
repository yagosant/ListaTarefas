package com.yago.listatarefas.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TaskDBHelper extends SQLiteOpenHelper {

    //contrutor
    public TaskDBHelper(@Nullable Context context) {
        //os dados vem do TaskContract criado antes
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE TABLE
        String sqlQuery = String.format
                ("CREATE TABLE %s (_id INTEGER PRIMARY  KEY AUTOINCREMENT," + "%s TEXT, %s TEXT)"
                        ,TaskContract.TABLE, TaskContract.Columns.TAREFA, TaskContract.Columns.PRAZO
                );
        //executando o comando acima
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TABLE);
    }
}
