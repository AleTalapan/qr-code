package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Informații despre baza de date
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    // Tabelul și coloanele pentru utilizatori
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Comanda SQL pentru crearea tabelului
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Verifică dacă un utilizator există deja în baza de date
    public boolean isUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                null,
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null
        );
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // Adaugă un utilizator nou în baza de date
    public boolean insertUser(String username, String password) {
        if (isUserExists(username)) {
            return false; // Utilizatorul există deja
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        db.insert(TABLE_USERS, null, values);
        db.close();
        return true; // Utilizatorul a fost adăugat
    }

    // Validează autentificarea utilizatorului
    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                null,
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null
        );

        boolean isValid = cursor.moveToFirst();
        cursor.close();
        db.close();
        return isValid;
    }

    // Actualizează parola unui utilizator
    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);
        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_USERNAME + "=?", new String[]{username});
        db.close();
        return rowsAffected > 0; // Returnează true dacă parola a fost actualizată
    }

    // Șterge un utilizator din baza de date
    public boolean deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_USERS, COLUMN_USERNAME + "=?", new String[]{username});
        db.close();
        return rowsDeleted > 0; // Returnează true dacă utilizatorul a fost șters
    }
}
