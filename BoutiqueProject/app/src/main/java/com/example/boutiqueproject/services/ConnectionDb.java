package com.example.boutiqueproject.services;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
public class ConnectionDb {
    private static int version = 1;
    private static String nom = "boutiqueBD.db";
    private static SQLiteDatabase bd;

    public static SQLiteDatabase getBd(Context context) {
        DbHelper dDbHelper = new DbHelper(context, nom, null, version);
        bd = dDbHelper.getWritableDatabase();
        return bd;
    }

    public static void close() {
        bd.close();
    }

    public static void copyFromAssets(Context context){
        try {
            InputStream in = context.getAssets().open(nom);
            File file = context.getDatabasePath(nom);
            FileOutputStream out = new FileOutputStream(file);
            byte[] tampon = new byte[10];
            while (in.read(tampon)!= -1){
                out.write(tampon);
                tampon = new byte[10];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkIfDatabaseExists(Context context) {
        File bdFile = context.getDatabasePath(nom);
        return bdFile.exists();
    }
}