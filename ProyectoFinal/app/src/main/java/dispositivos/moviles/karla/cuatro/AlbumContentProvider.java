package dispositivos.moviles.karla.cuatro;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import static dispositivos.moviles.karla.cuatro.Contract.ALL_ITEMS;
import static dispositivos.moviles.karla.cuatro.Contract.AUTHORITY;
import static dispositivos.moviles.karla.cuatro.Contract.CONTENT_PATH;
import static dispositivos.moviles.karla.cuatro.Contract.CONTENT_URI;
import static dispositivos.moviles.karla.cuatro.Contract.COUNT;
import static dispositivos.moviles.karla.cuatro.Contract.MULTIPLE_RECORDS_MIME_TYPE;
import static dispositivos.moviles.karla.cuatro.Contract.SINGLE_RECORD_MIME_TYPE;
import static java.lang.Integer.parseInt;

/**
 * Created by ferKarly.
 * Clase-Programación de Dispositivos Moviles
 * Version -mil ocho mil
 */
public class AlbumContentProvider extends ContentProvider {
    private static final String TAG = AlbumContentProvider.class.getSimpleName();

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private AlbumOpenHelper mDB;

    private static final int URI_ALL_ITEMS_CODE = 10;
    private static final int URI_ONE_ITEM_CODE = 20;
    static final int URI_COUNT_CODE = 30;

    @Override
    public boolean onCreate() {
        mDB = new AlbumOpenHelper(getContext());
        initializeUriMatching();
        return true;
    }

    private void initializeUriMatching() {

        // Matches a URI that is just the authority + the path, triggering the return of all words.
        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH, URI_ALL_ITEMS_CODE);

        // Matches a URI that references one word in the list by its index.
        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH + "/#", URI_ONE_ITEM_CODE);

        // Matches a URI that returns the number of rows in the table.
        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH + "/" + COUNT, URI_COUNT_CODE);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor cursor = null;

        // Determine integer code from the URI matcher and switch on it.
        switch (sUriMatcher.match(uri)) {
            case URI_ALL_ITEMS_CODE:
                cursor =  mDB.query(ALL_ITEMS, sortOrder);
                Log.d(TAG, "case all items " + cursor);
                break;
            case URI_ONE_ITEM_CODE:
                cursor =  mDB.query(parseInt(uri.getLastPathSegment()), sortOrder);
                Log.d(TAG, "case one item " + cursor);
                break;
            case URI_COUNT_CODE:
                cursor = mDB.count();
                Log.d(TAG, "case count " + cursor);
                break;
            case UriMatcher.NO_MATCH:
                // You should do some error handling here.
                Log.d(TAG, "NO MATCH FOR THIS URI IN SCHEME: " + uri);
                break;
            default:
                // You should do some error handling here.
                Log.d(TAG, "INVALID URI - URI NOT RECOGNIZED: "  + uri);
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case URI_ALL_ITEMS_CODE:
                return MULTIPLE_RECORDS_MIME_TYPE;
            case URI_ONE_ITEM_CODE:
                return SINGLE_RECORD_MIME_TYPE;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = mDB.insert(values);
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int regreso = 0;
        switch (sUriMatcher.match(uri)) {
            case URI_COUNT_CODE:
                regreso =  mDB.deleteAll();
                break;
            case UriMatcher.NO_MATCH:
                Log.d(TAG, "NO MATCH FOR THIS URI IN SCHEME: " + uri);
                break;
            default:
                regreso = mDB.delete(selectionArgs);
        }

        return regreso;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return mDB.update(selectionArgs, values);
    }

}
