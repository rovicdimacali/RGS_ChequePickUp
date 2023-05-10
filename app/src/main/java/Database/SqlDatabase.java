package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqlDatabase extends SQLiteOpenHelper {

    private final Context context;
    private static final String db_name = "rgs_express.db";
    private static final int db_version = 1;
    private static final String tbl_name = "exp_accounts";
    private static final String col_id = " id";
    private static final String col_name = "name";
    private static final String col_password = "passwd";
    private static final String col_email = "email";
    private static final String col_signup_otp = "signup_otp";
    private static final String col_phone = "phone_num";

    public SqlDatabase(@Nullable Context context) {
        super(context, db_name, null, db_version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tbl_query = "CREATE TABLE " + tbl_name +
                " (" + col_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                col_name + " TEXT, " +
                col_email + " TEXT, " +
                col_password + " PASSWORD, " +
                col_phone + " TEXT, " +
                col_signup_otp + " BIGINT);";
        db.execSQL(tbl_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tbl_name);
        onCreate(db);
    }

    public int addUser(String name, String email, String password, String phone, long otp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(col_name, name);
        cv.put(col_email, email);
        cv.put(col_password, password);
        cv.put(col_phone, phone);
        cv.put(col_signup_otp, otp);
        long result = db.insert(tbl_name, null, cv);

        if (result == -1) {
            return -1;
            //Toast.makeText(context, "Data not inserted!", Toast.LENGTH_SHORT).show();
        } else {
            String tbl_query = "UPDATE " + tbl_name + " SET signup_otp = 0 WHERE name = '" + name + "' " +
                    "AND email = '" + email + "';";
            db.execSQL(tbl_query);
            return 1;
            //Toast.makeText(context, "Data inserted!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor checkAccount(String name, String email, String pass){
        SQLiteDatabase db = this.getReadableDatabase();
        String user_check = null;

        if(pass.isEmpty() && !(name.isEmpty())){
            user_check = "SELECT * FROM " + tbl_name +
                    " WHERE email = '" + email + "';";
        }
        else if(!(pass.isEmpty()) && name.isEmpty()) {
            user_check = "SELECT * FROM " + tbl_name +
                    " WHERE passwd = '" + pass + "' AND email = '" + email + "';";
        }
        else if(pass.isEmpty() && name.isEmpty()) {
            user_check = "SELECT * FROM " + tbl_name +
                    " WHERE email = '" + email + "';";
        }

        Cursor cur = db.rawQuery(user_check, null);
        return cur;
    }

    public void forgotPassword(String email){

    }
}
