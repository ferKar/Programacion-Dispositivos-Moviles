package dispositivos.moviles.karla.cuatro.AlbumAdapter2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import dispositivos.moviles.karla.cuatro.AlbumInfoActivity;
import dispositivos.moviles.karla.cuatro.EditAlbumActivity;
import dispositivos.moviles.karla.cuatro.MainActivity;
import dispositivos.moviles.karla.cuatro.R;

import static dispositivos.moviles.karla.cuatro.Contract.CONTENT_PATH;
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
 */
public class AlbumAdapter2 extends RecyclerView.Adapter<AlbumAdapter2.WordViewHolder> {

    public static final String EXTRA_ID = "algunaExtra";
    public static final String EXTRA_WORD = "WORD";
    public static final String EXTRA_AlbumId = "AlbumId";
    public static final String EXTRA_id = "ID";
    public static final String EXTRA_url = "URL";
    public static final String EXTRA_Turl = "Turl";
    public static final String EXTRA_POSITION = "POSITION";


    private final LayoutInflater mInflater;
    private Context mContext;

    private Cursor item;

    public class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView wordItemView, albumItemView, idItemView;
        Button delete_button, edit_button;
        String url, tUrl;
        int idHolder;
        String configSort = KEY_ID;

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

            delete_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v){
                    int deleted = mContext.getContentResolver().delete(CONTENT_URI, CONTENT_PATH,
                            new String[]{Integer.toString(idHolder)});
                    if (deleted > 0) {
                        Cursor cursor = mContext.getContentResolver().query(Uri.parse(
                                CONTENT_URI.toString()), null, null, null, configSort);
                        item = cursor;
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), getItemCount());
                    }
                }
            });

            edit_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v){
                    Intent intent = new Intent(mContext, EditAlbumActivity.class);
                    intent.putExtra(EXTRA_ID, idHolder);
                    intent.putExtra(EXTRA_POSITION, getAdapterPosition());
                    intent.putExtra(EXTRA_AlbumId, albumItemView.getText());
                    intent.putExtra(EXTRA_id, idItemView.getText());
                    intent.putExtra(EXTRA_WORD, wordItemView.getText());
                    intent.putExtra(EXTRA_url, url);
                    intent.putExtra(EXTRA_Turl, tUrl);
                    ((Activity) mContext).startActivityForResult(intent, MainActivity.WORD_EDIT);
                }
            });
        }

        @Override
        public void onClick(View view) {
            Intent intento = new Intent(contexto, AlbumInfoActivity.class);
            intento.putExtra(EXTRA_ID, idHolder);
            intento.putExtra(EXTRA_POSITION, getAdapterPosition());
            intento.putExtra(EXTRA_AlbumId, albumItemView.getText());
            intento.putExtra(EXTRA_id, idItemView.getText());
            intento.putExtra(EXTRA_WORD, wordItemView.getText());
            intento.putExtra(EXTRA_url, url);
            intento.putExtra(EXTRA_Turl, tUrl);
            mContext.startActivity(intento);
        }
    }

    public AlbumAdapter2(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
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

        int id = -1;

        int indexWord = item.getColumnIndex(KEY_WORD);
        int indexAlbumId = item.getColumnIndex(KEY_ALBUMID);
        int indexID = item.getColumnIndex(KEY_Id);
        int indexUrl = item.getColumnIndex(KEY_URL);
        int indexTurl = item.getColumnIndex(KEY_ThumbnailUrl);
        int indexKEY = item.getColumnIndex(KEY_ID);

        if (item.moveToPosition(position)) {
            tit = item.getString(indexWord);
            alb = item.getString(indexAlbumId);
            idI = item.getString(indexID);
            urlI = item.getString(indexUrl);
            tUrlI = item.getString(indexTurl);
            id = item.getInt(indexKEY);
        }

        holder.wordItemView.setText(tit);
        holder.albumItemView.setText(alb);
        holder.idItemView.setText(idI);
        holder.url = urlI;
        holder.tUrl = tUrlI;
        holder.idHolder = id;
        holder.configSort = keySort;
    }

    @Override
    public int getItemCount() {
        return (item != null) ? item.getCount() : -1;
    }

    String keySort = "no key sort defautl ID solo para el boton de eliminar :/";

    public void swapCursor(Cursor nuevoCursor,String key_sort_delate) {
        if (nuevoCursor != null) {
            item = nuevoCursor;
            keySort = key_sort_delate;
            notifyDataSetChanged();
        }
    }


}
