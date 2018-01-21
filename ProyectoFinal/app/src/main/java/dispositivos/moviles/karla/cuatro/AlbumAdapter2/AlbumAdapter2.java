package dispositivos.moviles.karla.cuatro.AlbumAdapter2;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import dispositivos.moviles.karla.cuatro.AlbumAdapter;
import dispositivos.moviles.karla.cuatro.R;

import static dispositivos.moviles.karla.cuatro.Contract.CONTENT_URI;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_ALBUMID;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_ID;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_Id;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_ThumbnailUrl;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_URL;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_WORD;

/**
 * Created by ferKarly.
 * Clase-Programación de Dispositivos Moviles
 * Version -mil ocho mil
 * Clase salvaje para usar el cursor en la actividad principal.... funciona but no tambien.
 */
public class AlbumAdapter2 extends RecyclerView.Adapter<AlbumAdapter2.WordViewHolder> {

    public class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView wordItemView, albumItemView, idItemView;
        Button delete_button, edit_button;
        String url, tUrl;
        Context contexto;

        public WordViewHolder(View itemView) {
            super(itemView);
            contexto = itemView.getContext();
            wordItemView = itemView.findViewById(R.id.word);
            albumItemView = itemView.findViewById(R.id.albumId);
            idItemView = itemView.findViewById(R.id.idView);
            delete_button = itemView.findViewById(R.id.delete_button);
            edit_button = itemView.findViewById(R.id.edit_button);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(contexto, "click", Toast.LENGTH_SHORT).show();
        }
    }

    public static final String EXTRA_ID = "algunaExtra";
    public static final String EXTRA_WORD = "WORD";
    public static final String EXTRA_AlbumId = "AlbumId";
    public static final String EXTRA_id = "ID";
    public static final String EXTRA_url = "URL";
    public static final String EXTRA_Turl = "Turl";

    public static final String EXTRA_POSITION = "POSITION";

    private static final String TAG = AlbumAdapter.class.getSimpleName();

    private String queryUri = CONTENT_URI.toString();
    private String sortOrder = KEY_ID;

    private final LayoutInflater mInflater;
    private Context mContext;

    private Cursor item;
    
    public AlbumAdapter2(Context context,String sortOrder) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.sortOrder = sortOrder;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.album_item, parent, false);
        return new WordViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
       
        String ERROR_CURSOR = "error_no_word";
        String tit = ERROR_CURSOR,
                alb = ERROR_CURSOR,
                idI = ERROR_CURSOR,
                urlI = ERROR_CURSOR,
                tUrlI = ERROR_CURSOR;

        int indexWord = item.getColumnIndex(KEY_WORD);
        int indexAlbumId = item.getColumnIndex(KEY_ALBUMID);
        int indexID = item.getColumnIndex(KEY_Id);
        int indexUrl = item.getColumnIndex(KEY_URL);
        int indexTurl = item.getColumnIndex(KEY_ThumbnailUrl);

        if (item.moveToPosition(position)) {
            tit = item.getString(indexWord);
            alb = item.getString(indexAlbumId);
            idI = item.getString(indexID);
            urlI = item.getString(indexUrl);
            tUrlI = item.getString(indexTurl);
        }

        item.close();

        holder.wordItemView.setText(tit);
        holder.albumItemView.setText(alb);
        holder.idItemView.setText(idI);
        holder.url = urlI;
        holder.tUrl = tUrlI;
    }

    @Override
    public int getItemCount() {
        return (item != null) ? item.getCount() : -1;
    }

    public void swapCursor(Cursor nuevoCursor) {
        if (nuevoCursor != null) {
            item = nuevoCursor;
            notifyDataSetChanged();
        }
    }


}
