package dispositivos.moviles.karla.cuatro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import dispositivos.moviles.karla.cuatro.AlbumAdapter2.AlbumAdapter2;

/**
 * Created by ferKarly.
 * Clase-Programación de Dispositivos Moviles
 * Version -mil ocho mil
 */
public class EditAlbumActivity extends AppCompatActivity {

    private static final int NO_ID = -99;
    private static final String NO_WORD = "zzzzzzzzzzzzzzzzz";

    private EditText editTituloView,editAlbumIdView,editIDView,editUrlView,editUrlThumbView;

    //for the intent reply.
    public static final String EXTRA_REPLY_T = "dispositivos.moviles.karla.cuatro.TITULO";
    public static final String EXTRA_REPLY_A = "dispositivos.moviles.karla.cuatro.ALBUMID";
    public static final String EXTRA_REPLY_I = "dispositivos.moviles.karla.cuatro.ID";
    public static final String EXTRA_REPLY_U = "dispositivos.moviles.karla.cuatro.URL";
    public static final String EXTRA_REPLY_Ut = "dispositivos.moviles.karla.cuatro.TURL";


    int mId = MainActivity.WORD_ADD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_album);

        editTituloView = (EditText) findViewById(R.id.tituloEditView);
        editAlbumIdView = (EditText) findViewById(R.id.albumEditView);
        editIDView = (EditText) findViewById(R.id.idEditView);
        editUrlView = (EditText) findViewById(R.id.urlEditView);
        editUrlThumbView =(EditText) findViewById(R.id.thumbnailUrlEditView);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int id = extras.getInt(AlbumAdapter2.EXTRA_ID, NO_ID);
            String word = extras.getString(AlbumAdapter2.EXTRA_WORD, NO_WORD);
            String alb = extras.getString(AlbumAdapter2.EXTRA_AlbumId, NO_WORD);
            String albid = extras.getString(AlbumAdapter2.EXTRA_id, NO_WORD);
            String url = extras.getString(AlbumAdapter2.EXTRA_url, NO_WORD);
            String turl = extras.getString(AlbumAdapter2.EXTRA_Turl, NO_WORD);

            if (id != NO_ID &&
                    !word.equals(NO_WORD) &&
                    !alb.equals(NO_WORD) &&
                    !albid.equals(NO_WORD) &&
                    !url.equals(NO_WORD) &&
                    !turl.equals(NO_WORD)){
                mId = id;
                editTituloView.setHint(word);
                editAlbumIdView.setHint(alb);
                editIDView.setHint(albid);
                editUrlView.setHint(url);
                editUrlThumbView.setHint(turl);
            } else{
                Toast.makeText(this, "pos quien sabe", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void returnReply(View view) {
        String word = ((EditText) findViewById(R.id.tituloEditView)).getText().toString();
        String albumIdA = ((EditText) findViewById(R.id.albumEditView)).getText().toString();
        String idA = ((EditText) findViewById(R.id.idEditView)).getText().toString();
        String urlA = ((EditText) findViewById(R.id.urlEditView)).getText().toString();
        String thumburlA = ((EditText) findViewById(R.id.thumbnailUrlEditView)).getText().toString();

        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY_T, word);
        replyIntent.putExtra(EXTRA_REPLY_A, albumIdA);
        replyIntent.putExtra(EXTRA_REPLY_I, idA);
        replyIntent.putExtra(EXTRA_REPLY_U, urlA);
        replyIntent.putExtra(EXTRA_REPLY_Ut, thumburlA);

        replyIntent.putExtra(AlbumAdapter2.EXTRA_ID, mId);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    public void returnCancel(View view) {
        Intent replyIntent = new Intent();

        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }
}
