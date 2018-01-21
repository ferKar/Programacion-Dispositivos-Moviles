package dispositivos.moviles.karla.cuatro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import dispositivos.moviles.karla.cuatro.AlbumAdapter2.AlbumAdapter2;


/**
 * Created by ferKarly.
 * Clase-Programación de Dispositivos Moviles
 * Version -mil ocho mil
 */
public class AlbumInfoActivity extends AppCompatActivity {

    private static final int NO_ID = -100;
    private static final String NO_WORD = "vacio";
    private TextView infoTituloView,infoAlbumIdView,infoIDView,infoUrlView,infoUrlThumbView;
    String url;
    String turl;
    int mId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_info);
        infoTituloView = (TextView) findViewById(R.id.tituloInfoView);
        infoAlbumIdView = (TextView) findViewById(R.id.albumIdInfoView);
        infoIDView = (TextView) findViewById(R.id.idInfoView);
        infoUrlView = (TextView) findViewById(R.id.urlInfoView);
        infoUrlThumbView =(TextView) findViewById(R.id.thumbnailInfoView);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int id = extras.getInt(AlbumAdapter2.EXTRA_ID, NO_ID);
            String word = extras.getString(AlbumAdapter2.EXTRA_WORD, NO_WORD);
            String alb = extras.getString(AlbumAdapter2.EXTRA_AlbumId, NO_WORD);
            String albid = extras.getString(AlbumAdapter2.EXTRA_id, NO_WORD);
            url = extras.getString(AlbumAdapter2.EXTRA_url, NO_WORD);
            turl = extras.getString(AlbumAdapter2.EXTRA_Turl, NO_WORD);

            infoTituloView.setText(word);
            infoAlbumIdView.setText(alb);
            infoIDView.setText(albid);
            infoUrlView.setText(url);
            infoUrlThumbView.setText(turl);
            mId = id;

        }

        Picasso.with(this).load(url).error(R.drawable.ic_info_black_24dp).into((ImageView) findViewById(R.id.imageView));
        Picasso.with(this).load(turl).error(R.drawable.ic_info_black_24dp).into((ImageView) findViewById(R.id.imageView2));

    }

    public void eliminame(View view) {
        Intent replyIntent = new Intent();
        replyIntent.putExtra(AlbumAdapter2.EXTRA_ID, mId);
        replyIntent.putExtra("ELIM", true);
        setResult(MainActivity.MORE_INFO, replyIntent);
        finish();
    }

    public void mandar (View ver){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void mandar2 (View ver){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(turl)));
    }

}
