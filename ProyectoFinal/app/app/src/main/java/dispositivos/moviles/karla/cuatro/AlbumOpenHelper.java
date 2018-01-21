package dispositivos.moviles.karla.cuatro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static dispositivos.moviles.karla.cuatro.Contract.ALL_ITEMS;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_ALBUMID;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_ID;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_Id;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_ThumbnailUrl;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_URL;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_WORD;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.WORD_LIST_TABLE;
import static dispositivos.moviles.karla.cuatro.Contract.DATABASE_NAME;

/**
 * Created by trin on 18/01/18.
 */

class AlbumOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = AlbumOpenHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    SQLiteDatabase mWritableDB;
    SQLiteDatabase mReadableDB;

    Context miContexto;

    public AlbumOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        miContexto = context;
    }

    private String creaTabla() {
        String cmd = "CREATE TABLE " + WORD_LIST_TABLE + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_WORD + " TEXT, " +
                KEY_ALBUMID + " TEXT, " +
                KEY_Id + " TEXT, " +
                KEY_URL + " TEXT, " +
                KEY_ThumbnailUrl + " TEXT);";
        return cmd;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(creaTabla());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(AlbumOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + WORD_LIST_TABLE);
        onCreate(db);
    }

    public Cursor query(int position) {
        String query;
        if (position != ALL_ITEMS) {
            position++; // Because database starts counting at 1.
            query = "SELECT  * FROM " + WORD_LIST_TABLE + " WHERE " + KEY_ID + "=" + position + ";";
        } else {
            query = "SELECT  * FROM " + WORD_LIST_TABLE + " ORDER BY " + KEY_WORD + " ASC ";
        }

        Cursor cursor = null;
        try {
            if (mReadableDB == null) {
                mReadableDB = this.getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, null);
        } catch (Exception e) {
            Log.d(TAG, "QUERY EXCEPTION! " + e);
        } finally {
            return cursor;
        }
    }

    public Cursor count() {
        MatrixCursor cursor = new MatrixCursor(new String[]{Contract.CONTENT_PATH});
        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            int count = (int) DatabaseUtils.queryNumEntries(mReadableDB, WORD_LIST_TABLE);
            cursor.addRow(new Object[]{count});
        } catch (Exception e) {
            Log.d(TAG, "COUNT EXCEPTION " + e);
        }
        return cursor;
    }

    public long insert(ContentValues values){
        long added = 0;
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            added = mWritableDB.insert(WORD_LIST_TABLE, null, values);
        } catch (Exception e) {
            Log.d(TAG, "INSERT EXCEPTION " + e);
        }
        return added;
    }

    public int update(String[] selectionArgs, ContentValues values) {
        int updated = 0;
        try {
            if (mWritableDB == null) { mWritableDB = getWritableDatabase(); }
            updated = mWritableDB.update(WORD_LIST_TABLE, values, KEY_ID + " = ?", selectionArgs);
        } catch (Exception e) {
            Log.d (TAG, "UPDATE EXCEPTION " + e);
        }
        return updated;
    }

    public int delete(String[] selectionArgs) {
        int deleted = 0;
        try {
            if (mWritableDB == null) { mWritableDB = this.getWritableDatabase(); }
            deleted = mWritableDB.delete(WORD_LIST_TABLE, KEY_ID + " = ? ", selectionArgs);
        } catch (Exception e) {
            Log.d (TAG, "DELETE EXCEPTION " + e);
        }
        return deleted;
    }


}
