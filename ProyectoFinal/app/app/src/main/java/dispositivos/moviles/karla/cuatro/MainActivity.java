package dispositivos.moviles.karla.cuatro;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.LinkedList;

import static dispositivos.moviles.karla.cuatro.Contract.CONTENT_URI;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_ALBUMID;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_Id;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_ThumbnailUrl;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_URL;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_WORD;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LinkedList<Post>>{
    public static final int WORD_EDIT = 1;
    public static final int WORD_ADD = -1;

    private RecyclerView mRecyclerView;
    private AlbumAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AlbumAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        getSupportLoaderManager().initLoader(0, null, this);
        Toast.makeText(this, "Creacion", Toast.LENGTH_SHORT).show();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<LinkedList<Post>> onCreateLoader(int id, Bundle args) {
        Toast.makeText(this, "Descargando..." +id, Toast.LENGTH_SHORT).show();
        return new AsyncTaskPost(this);
    }

    @Override
    public void onLoadFinished(Loader<LinkedList<Post>> loader, LinkedList<Post> data) {
        data = new LinkedList<>();
        data.add(new Post("Cambio",198,102,"una url","wwww."));

        for(Post word: data) {
            ContentValues values = new ContentValues();
            values.put(KEY_WORD, word.getTitle());
            values.put(KEY_ALBUMID,word.getAlbumId());
            values.put(KEY_Id,word.getId());
            values.put(KEY_URL,word.getUrl());
            values.put(KEY_ThumbnailUrl,word.getThumbnailUrl());
            getContentResolver().insert(CONTENT_URI, values);
        }
        mAdapter.notifyDataSetChanged();
        //Toast.makeText(this, "Termino ", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLoaderReset(Loader<LinkedList<Post>> loader) {

    }}
