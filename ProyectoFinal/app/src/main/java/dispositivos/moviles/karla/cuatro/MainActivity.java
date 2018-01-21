package dispositivos.moviles.karla.cuatro;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;

import dispositivos.moviles.karla.cuatro.AlbumAdapter2.AlbumAdapter2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static dispositivos.moviles.karla.cuatro.Contract.CONTENT_URI;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_ALBUMID;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_ID;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_Id;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_ThumbnailUrl;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_URL;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_WORD;
import static dispositivos.moviles.karla.cuatro.EditAlbumActivity.EXTRA_REPLY_A;
import static dispositivos.moviles.karla.cuatro.EditAlbumActivity.EXTRA_REPLY_I;
import static dispositivos.moviles.karla.cuatro.EditAlbumActivity.EXTRA_REPLY_T;
import static dispositivos.moviles.karla.cuatro.EditAlbumActivity.EXTRA_REPLY_U;
import static dispositivos.moviles.karla.cuatro.EditAlbumActivity.EXTRA_REPLY_Ut;

/**
 * Created by ferKarly.
 * Clase-Programación de Dispositivos Moviles
 * Version -mil ocho mil | DOS
 * Al ordenar por Id compra con strings jejeje en la tabla lo guarde como strings por eso xD.
 * Tarda mil años en insertar 5mil elementos ._." solo hay que esperar jajaja mucho pero si lo hace :D
 *
 */
public class MainActivity extends AppCompatActivity{
    public static final int WORD_EDIT = 1;
    public static final int WORD_ADD = -1;
    public static final int MORE_INFO = -3000;
    private static final int CONFIGURACION = 2;

    private RecyclerView mRecyclerView;
    private AlbumAdapter2 adaptador;

    private final String URL_JSON_DATA = "https://jsonplaceholder.typicode.com/photos";
    private final Boolean NO_EXISTE = true;
    private final String PRIMERA_KEY = "apicacion creada por primera vez";
    private final String NO_CONFIG = "no configurado";

    Context miContexto;

    private SharedPreferences sP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        miContexto = this;
        sP = PreferenceManager.getDefaultSharedPreferences(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new AlbumAdapter2(this);
        checkConfig();
        mRecyclerView.setAdapter(adaptador);
        Boolean esPrimera = sP.getBoolean(PRIMERA_KEY,NO_EXISTE);
        if(esPrimera) {
            porPrimeraVez();
            sP.edit().putBoolean(PRIMERA_KEY, false).apply();
        }else{
            porNesimaVez();
        }
    }

    private void checkConfig(){
        String ordenaAct = sP.getString("ORDENAR_LIST", NO_CONFIG);
        if (ordenaAct.equals("Titulo")) {
            cambiaCursor(KEY_WORD);
        }else if (ordenaAct.equals("Insertado")){
            cambiaCursor(KEY_ID);
        }else if (ordenaAct.equals("Id")){
            cambiaCursor(KEY_Id);
        }else{
            cambiaCursor(KEY_ID);
        }
    }

    private void cambiaCursor(String key_orden_columna){
        Cursor cursor = getBaseContext().getContentResolver().query(Uri.parse(
                CONTENT_URI.toString()), null, null, null, key_orden_columna);
        adaptador.swapCursor(cursor, key_orden_columna);
    }

    private void porPrimeraVez() {
        new ClaseExtra().execute(URL_JSON_DATA);
    }

    private void porNesimaVez() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WORD_EDIT) {

            if (resultCode == RESULT_OK) {
                if (data.getStringExtra(EXTRA_REPLY_T).length() != 0
                        && data.getStringExtra(EXTRA_REPLY_A).length() != 0) {
                    ContentValues values = new ContentValues();
                    values.put(KEY_WORD, data.getStringExtra(EXTRA_REPLY_T));
                    values.put(KEY_ALBUMID,data.getStringExtra(EXTRA_REPLY_A));
                    values.put(KEY_Id, data.getStringExtra(EXTRA_REPLY_I));
                    values.put(KEY_URL, data.getStringExtra(EXTRA_REPLY_U));
                    values.put(KEY_ThumbnailUrl, data.getStringExtra(EXTRA_REPLY_Ut));

                    int id = data.getIntExtra(AlbumAdapter2.EXTRA_ID, -99);

                    if (id == WORD_ADD) {
                        getContentResolver().insert(CONTENT_URI, values);
                    } else if (id >= 0) {
                        String[] selectionArgs = {Integer.toString(id)};
                        getContentResolver().update(CONTENT_URI, values, KEY_ID, selectionArgs);
                    }
                    // Update the UI.
                    checkConfig();
                } else {
                    Toast.makeText(
                            getApplicationContext(), R.string.empty_word_not_saved,
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        else if (requestCode == CONFIGURACION){
            checkConfig();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.añadir) {
            Intent intent = new Intent(getBaseContext(), EditAlbumActivity.class);
            startActivityForResult(intent, WORD_EDIT);
            return true;
        }

        if (id == R.id.refresacar) {
            getContentResolver().delete(Contract.ROW_COUNT_URI,null,null);
            checkConfig();
            Toast.makeText(this,"refrescando",Toast.LENGTH_LONG);
            new ClaseExtra().execute(URL_JSON_DATA);
            return true;
        }

        if (id == R.id.configuraciones) {
            Intent intent = new Intent(getBaseContext(), ConfiguracionActivity.class);
            startActivityForResult(intent, CONFIGURACION);
            return true;
        }

        if (id == R.id.eliminar) {
            getContentResolver().delete(Contract.ROW_COUNT_URI,null,null);
            checkConfig();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ClaseExtra extends AsyncTask<String,Void,LinkedList<Post>> {

        public final String NO_INTERNET = "NO INTERNET :c";
        public final String JSON_EXCEP_TITULO = "IMPOSIBLE PASAR A JSON";
        public final String JSON_EXCEP_URL = "liga de gatitos";
        public final int JSON_EXCEP_ALBUMID = -2;
        public final int JSON_EXCEP_ID = -2;

        @Override
        protected LinkedList<Post> doInBackground(String... url_json_data) {
            String JSON_DATA;
            try {
                JSON_DATA = run(url_json_data[0]);
            } catch (IOException e) {
                JSON_DATA = NO_INTERNET;
            }
            return makeListAlbums(JSON_DATA);
        }

        private String run(String url) throws IOException {
            Request request = new Request.Builder().url(url).build();
            Response response = new OkHttpClient().newCall(request).execute();
            return response.body().string();
        }

        private LinkedList<Post> makeListAlbums(String json_data){
            LinkedList<Post> listaAlbums = new LinkedList<>();
            try {
                JSONArray cadTipoJson = new JSONArray(json_data);
                for (int i = 0; i < cadTipoJson.length(); i++) {
                    JSONObject jsonObj = cadTipoJson.getJSONObject(i);
                    listaAlbums.addLast(hasmePost(jsonObj));
                }
            }catch (JSONException e) {
                listaAlbums.addFirst(new Post(
                        JSON_EXCEP_TITULO +"\n" + json_data,
                        JSON_EXCEP_ALBUMID,
                        JSON_EXCEP_ID,
                        JSON_EXCEP_URL,
                        JSON_EXCEP_URL));
            }
            return  listaAlbums;
        }

        private Post hasmePost(JSONObject jsObj){
            Post ps;
            try {
                ps = new Post(jsObj.getString("title"),
                        jsObj.getInt("albumId"),
                        jsObj.getInt("id"),
                        jsObj.getString("url"),
                        jsObj.getString("thumbnailUrl"));
            }catch (JSONException e){
                ps = new Post();
            }
            return ps;
        }

        @Override
        protected void onPostExecute(LinkedList<Post> posts) {
            super.onPostExecute(posts);
            for (Post word : posts) {
                ContentValues values = new ContentValues();
                values.put(KEY_WORD, word.getTitle());
                values.put(KEY_ALBUMID, word.getAlbumId());
                values.put(KEY_Id, word.getId());
                values.put(KEY_URL, word.getUrl());
                values.put(KEY_ThumbnailUrl, word.getThumbnailUrl());
                getContentResolver().insert(CONTENT_URI, values);
            }
            checkConfig();
        }

    }
}
