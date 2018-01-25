package dispositivos.moviles.karla.cuatro;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import dispositivos.moviles.karla.cuatro.AlbumAdapter2.AlbumAdapter2;

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
 * Version -mil ocho mil | TRES
 * Tarda mil años en insertar 5mil elementos ._." solo hay que esperar jajaja mucho pero si lo hace :D
 * Que estaba insertando en exponencial?.... Pues casi....
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int WORD_EDIT = 1;
    public static final int WORD_ADD = -1;
    private static final int CONFIGURACION = 2;

    private final String URL_JSON_DATA = "https://jsonplaceholder.typicode.com/photos";
    private final Boolean NO_EXISTE = true;
    private final String PRIMERA_KEY = "aplicacion creada por primera vez";
    private final String NO_CONFIG = KEY_ID; // orden del los elementos no configurado

    Context miContexto;
    private RecyclerView mRecyclerView;
    private AlbumAdapter2 adaptador;

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
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        Boolean esPrimera = sP.getBoolean(PRIMERA_KEY,NO_EXISTE);
        if(esPrimera) {
            porPrimeraVez();
            sP.edit().putBoolean(PRIMERA_KEY, false).apply();
        }else{
            porNesimaVez();
        }
    }

    private String getConfig_Order_Data(){
        return sP.getString("ORDENAR_LIST", NO_CONFIG);
    }

    private void checkConfig(){
        switch (getConfig_Order_Data()){
            case "Titulo":
                cambiaCursor(KEY_WORD);
                break;
            case "Insertado":
                cambiaCursor(KEY_ID);
                break;
            case "Id":
                cambiaCursor(KEY_Id);
                break;
            default:
                cambiaCursor(KEY_ID);
                break;
        }
    }

    private void cambiaCursor(String key_orden_columna){
        Cursor cursor = getBaseContext().getContentResolver().query(Uri.parse(
                CONTENT_URI.toString()), null, null, null, key_orden_columna);
        adaptador.swapCursor(cursor, key_orden_columna);
    }

    private void porPrimeraVez() {
        getSupportLoaderManager().initLoader(0, null, this);
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
                        && data.getStringExtra(EXTRA_REPLY_I).length() != 0) { // faltan ands
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
            Toast.makeText(this,"Ahora tienes: " + adaptador.getItemCount(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean regreso;
        switch (item.getItemId()){
            case R.id.añadir:
                Intent intent = new Intent(getBaseContext(), EditAlbumActivity.class);
                startActivityForResult(intent, WORD_EDIT);
                regreso = true;
                break;
            case R.id.refresacar:
                getContentResolver().delete(Contract.ROW_COUNT_URI,null,null);
                checkConfig();
                Toast.makeText(this,"refrescando ahorita no hace nada jejeje",Toast.LENGTH_LONG).show();
                //new ClaseExtra().execute(URL_JSON_DATA);
                regreso = true;
                break;
            case R.id.configuraciones:
                intent = new Intent(getBaseContext(), ConfiguracionActivity.class);
                startActivityForResult(intent, CONFIGURACION);
                regreso = true;
                break;
            case R.id.eliminar:
                getContentResolver().delete(Contract.ROW_COUNT_URI,null,null);
                checkConfig();
                regreso = true;
                break;
            default:
                regreso =  super.onOptionsItemSelected(item);
                break;
        }
        return regreso;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Toast.makeText(this, "Descargando e insertando a la base... ", Toast.LENGTH_LONG).show();

        return new CursorLoaderAlbum(this, URL_JSON_DATA, getConfig_Order_Data());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adaptador.swapCursor(data, getConfig_Order_Data());
        Toast.makeText(this, data.getCount() + " elementos " , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adaptador.swapCursor(null,null);

    }
}
