package dispositivos.moviles.karla.cuatro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static dispositivos.moviles.karla.cuatro.Contract.CONTENT_URI;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_ALBUMID;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_Id;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_ThumbnailUrl;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_URL;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_WORD;

/**
 * Created by ferKarly on 24/01/18.
 */

class CursorLoaderAlbum extends AsyncTaskLoader<Cursor> {
    public final String NO_INTERNET = "NO INTERNET :c";
    public final String JSON_EXCEP_TITULO = "IMPOSIBLE PASAR A JSON";
    public final String JSON_EXCEP_URL = "https://siliconangle.com/files/2013/02/no-data.png";
    public final int JSON_EXCEP_ALBUMID = -2;
    public final int JSON_EXCEP_ID = -2;
    private final String url_json_data;
    private final Context context;
    private final String key_orden_columna;

    public CursorLoaderAlbum(Context context, String url_json_data,String key_orden_columna) {
        super(context);
        this.context = context;
        this.url_json_data = url_json_data;
        this.key_orden_columna = key_orden_columna;
    }

    @Override
    public Cursor loadInBackground() {
        String JSON_DATA;
        try {
            JSON_DATA = run(url_json_data);
        } catch (IOException e) {
            JSON_DATA = NO_INTERNET;
        }
        convirteAlbums(JSON_DATA);
        Cursor cursor = context.getContentResolver().query(Uri.parse(
                CONTENT_URI.toString()), null, null, null, key_orden_columna);
        return cursor;
    }

    private String run(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = new OkHttpClient().newCall(request).execute();
        return response.body().string();
    }

    private void convirteAlbums(String json_data){
        try {
            JSONArray cadTipoJson = new JSONArray(json_data);
            int hasta = cadTipoJson.length(); //int usado para pruebas más pequeñas
            for (int i = 0; i < hasta; i++) {
                insertaBase(cadTipoJson.getJSONObject(i));
            }
        }catch (JSONException e) {
            insertaBaseNoInternet(json_data);
        }
    }

    private void insertaBaseNoInternet(String json_data){
        ContentValues values = new ContentValues();
        values.put(KEY_WORD, JSON_EXCEP_TITULO + "\n" + json_data);
        values.put(KEY_ALBUMID, JSON_EXCEP_ALBUMID);
        values.put(KEY_Id, JSON_EXCEP_ID);
        values.put(KEY_URL, JSON_EXCEP_URL);
        values.put(KEY_ThumbnailUrl, JSON_EXCEP_URL);
        context.getContentResolver().insert(CONTENT_URI, values);
    }

    private void insertaBase(JSONObject jsObj) throws JSONException{
        ContentValues values = new ContentValues();
        values.put(KEY_WORD, jsObj.getString("title"));
        values.put(KEY_ALBUMID, jsObj.getInt("albumId"));
        values.put(KEY_Id, jsObj.getInt("id"));
        values.put(KEY_URL, jsObj.getString("url"));
        values.put(KEY_ThumbnailUrl, jsObj.getString("thumbnailUrl"));
        context.getContentResolver().insert(CONTENT_URI, values);
    }

    protected void onStartLoading() {
        forceLoad();
    }

}
