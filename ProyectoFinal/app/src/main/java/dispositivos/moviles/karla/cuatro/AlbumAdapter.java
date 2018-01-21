package dispositivos.moviles.karla.cuatro;

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

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.WordViewHolder> {

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView wordItemView, albumItemView, idItemView;
        Button delete_button, edit_button;
        String url, tUrl;
        int idHolder;

        public
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
            final Intent intento = new Intent(contexto, AlbumInfoActivity.class);
            intento.putExtra("albumIdExtra_More", albumItemView.getText());
            intento.putExtra("idExtra_More", idItemView.getText());
            intento.putExtra("titleExtra_More", wordItemView.getText());
            intento.putExtra("urlExtra_More", url);
            intento.putExtra("thumbnailUrlExtra_More", tUrl);
            contexto.startActivity(intento);
        }
    }

    public static final String EXTRA_ID = "algunaExtra";
    public static final String EXTRA_WORD = "WORD";
    public static final String EXTRA_AlbumId = "AlbumId";
    public static final String EXTRA_id = "ID";
    public static final String EXTRA_url = "URL";
    public static final String EXTRA_Turl = "Turl";
    public static final String EXTRA_POSITION = "POSITION";


    private String queryUri = CONTENT_URI.toString();
    private String sortOrder = KEY_ID;

    private final LayoutInflater mInflater;
    private Context mContext;

    public AlbumAdapter(Context context,String sortOrder) {
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
    public void onBindViewHolder(final WordViewHolder holder, int position) {

        Cursor cursor = mContext.getContentResolver().query(Uri.parse(
                queryUri), null, null, null, sortOrder);

        String ERROR_CURSOR = "error_no_word";
        String tit = ERROR_CURSOR,
                alb = ERROR_CURSOR,
                idI = ERROR_CURSOR,
                urlI = ERROR_CURSOR,
                tUrlI = ERROR_CURSOR;

        int id = -1;

        int indexWord = cursor.getColumnIndex(KEY_WORD);
        int indexAlbumId = cursor.getColumnIndex(KEY_ALBUMID);
        int indexID = cursor.getColumnIndex(KEY_Id);
        int indexUrl = cursor.getColumnIndex(KEY_URL);
        int indexTurl = cursor.getColumnIndex(KEY_ThumbnailUrl);
        int indexKEY = cursor.getColumnIndex(KEY_ID);

        if (cursor.moveToPosition(position)) {
            tit = cursor.getString(indexWord);
            alb = cursor.getString(indexAlbumId);
            idI = cursor.getString(indexID);
            urlI = cursor.getString(indexUrl);
            tUrlI = cursor.getString(indexTurl);
            id = cursor.getInt(indexKEY);
        }

        cursor.close();

        holder.wordItemView.setText(tit);
        holder.albumItemView.setText(alb);
        holder.idItemView.setText(idI);
        holder.url = urlI;
        holder.tUrl = tUrlI;
        holder.idHolder = id;

        holder.delete_button.setOnClickListener(new MyButtonOnClickListener(id, "") {

            @Override
            public void onClick(View v) {
                int deleted = mContext.getContentResolver().delete(CONTENT_URI, CONTENT_PATH,
                        new String[]{Integer.toString(id)});
                if (deleted > 0) {
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), getItemCount());
                }
            }
        });

        holder.edit_button.setOnClickListener(new MyButtonOnClickListener(0, "") {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditAlbumActivity.class);
                intent.putExtra(EXTRA_ID, holder.idHolder);
                intent.putExtra(EXTRA_POSITION, holder.getAdapterPosition());
                intent.putExtra(EXTRA_AlbumId, holder.albumItemView.getText());
                intent.putExtra(EXTRA_id, holder.idItemView.getText());
                intent.putExtra(EXTRA_WORD, holder.wordItemView.getText());
                intent.putExtra(EXTRA_url, holder.url);
                intent.putExtra(EXTRA_Turl, holder.tUrl);

                ((Activity) mContext).startActivityForResult(intent, MainActivity.WORD_EDIT);
            }
        });
    }

    @Override
    public int getItemCount() {
        Cursor cursor = mContext.getContentResolver().query(Uri.parse(
                queryUri), null, null, null, sortOrder);
        int regreso = (cursor != null) ? cursor.getCount() : 0;
        cursor.close();
        return regreso;
    }



}
