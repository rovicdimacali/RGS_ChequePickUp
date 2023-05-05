package com.example.rgs_chequepickup;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

class SqlDatabase extends SQLiteOpenHelper {

    private final Context context;
    private static final String db_name = "rgs_express.db";
    private static final int db_version = 1;
    private static final String tbl_name = "exp_accounts";
    private static final String col_id = " id";
    private static final String col_username = "username";
    private static final String col_password = "passwd";
    private static final String col_email = "email";

    public SqlDatabase(@Nullable Context context) {
        super(context, db_name, null, db_version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tbl_query = "CREATE TABLE " + tbl_name +
                " (" + col_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                col_username + " TEXT, " +
                col_email + " TEXT, " +
                col_password + " PASSWORD);";
        db.execSQL(tbl_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tbl_name);
        onCreate(db);
    }

    void addUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(col_username, username);
        cv.put(col_email, email);
        cv.put(col_password, password);
        long result = db.insert(tbl_name, null, cv);

        if (result == -1) {
            Toast.makeText(context, "Data not inserted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Data inserted!", Toast.LENGTH_SHORT).show();
        }
    }
}
