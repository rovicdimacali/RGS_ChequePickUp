package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class sqlPickUp extends SQLiteOpenHelper {
    private final Context context;
    private static final String db_name = "rgs_express.db";
    private static final int db_version = 1;
    private static final String tbl_name = "pickup_history";
    private static final String col_id = " id";
    private static final String col_company = "Company";
    private static final String col_person = "Contact_Person";
    private static final String col_address = "Address";
    private static final String col_contact = "Contact_Number";
    private static final String col_code = "Company_Code";

    public sqlPickUp(@Nullable Context context){
        super(context, db_name, null, db_version);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String tbl_query = "CREATE TABLE " + tbl_name +
                " (" + col_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                col_company + " TEXT, " +
                col_person + " TEXT, " +
                col_address + " TEXT, " +
                col_contact + " TEXT, " +
                col_code + " TEXT);";
        db.execSQL(tbl_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tbl_name);
        onCreate(db);
    }

    public int addHistory(String comp, String per, String add, String cont, String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(col_company, comp);
        cv.put(col_person, per);
        cv.put(col_address, add);
        cv.put(col_contact, cont);
        cv.put(col_code, code);
        long result = db.insert(tbl_name, null, cv);

        if (result == -1) {
            return -1;
            //Toast.makeText(context, "Data not inserted!", Toast.LENGTH_SHORT).show();
        } else {
            return 1;
            //Toast.makeText(context, "Data inserted!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteHistory(String id){

    }
}
