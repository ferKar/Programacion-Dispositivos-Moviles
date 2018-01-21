package dispositivos.moviles.karla.cuatro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static dispositivos.moviles.karla.cuatro.Contract.CONTENT_PATH;
import static dispositivos.moviles.karla.cuatro.Contract.CONTENT_URI;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_ALBUMID;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_ID;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_Id;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_ThumbnailUrl;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_URL;
import static dispositivos.moviles.karla.cuatro.Contract.Columnas.KEY_WORD;

/**
 * Created by trin on 18/01/18.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.WordViewHolder> {

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView wordItemView, albumItemView, idItemView;
        Button delete_button, edit_button, more_button;
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
            more_button = itemView.findViewById(R.id.more_button);
        }

        @Override
        public void onClick(View view) {
            final Intent intento = new Intent(contexto, AlbumInfoActivity.class);
            intento.putExtra("albumIdExtra", albumItemView.getText());
            intento.putExtra("idExtra", idItemView.getText());
            intento.putExtra("titleExtra", wordItemView.getText());
            intento.putExtra("urlExtra", url);
            intento.putExtra("thumbnailUrlExtra", tUrl);
            contexto.startActivity(intento);
            Toast.makeText(contexto, "si es clicleable", Toast.LENGTH_SHORT).show();
        }
    }

    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_WORD = "WORD";
    public static final String EXTRA_POSITION = "POSITION";

    private static final String TAG = AlbumAdapter.class.getSimpleName();

    private String queryUri = CONTENT_URI.toString();
    private String selectionClause = null;
    private String selectionArgs[] = null;
    private String sortOrder = "ASC";

    private final LayoutInflater mInflater;
    private Context mContext;

    public AlbumAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.album_item, parent, false);
        return new WordViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(final WordViewHolder holder, int position) {
        final WordViewHolder h = holder;

        String word = "";
        int id = -1;

        Cursor cursor = mContext.getContentResolver().query(Uri.parse(
                queryUri), null, null, null, sortOrder);
        if (cursor != null) {
            if (cursor.moveToPosition(position)) {
                int indexWord = cursor.getColumnIndex(KEY_WORD);
                int indexAlbumId = cursor.getColumnIndex(KEY_ALBUMID);
                int indexID = cursor.getColumnIndex(KEY_Id);
                word = cursor.getString(indexWord);
                holder.wordItemView.setText(word);
                holder.albumItemView.setText(cursor.getString(indexAlbumId));
                holder.idItemView.setText(cursor.getString(indexID));
                holder.url = cursor.getString(cursor.getColumnIndex(KEY_URL));
                holder.tUrl = cursor.getString(cursor.getColumnIndex(KEY_ThumbnailUrl));
                int indexId = cursor.getColumnIndex(KEY_ID);
                id = cursor.getInt(indexId);
            } else {
                holder.wordItemView.setText(R.string.error_no_word);
            }
            cursor.close();
        } else {
            Log.e(TAG, "onBindViewHolder: Cursor is null.");
        }


        holder.delete_button.setOnClickListener(new MyButtonOnClickListener(id, word) {

            @Override
            public void onClick(View v) {
                selectionArgs = new String[]{Integer.toString(id)};
                int deleted = mContext.getContentResolver().delete(CONTENT_URI, CONTENT_PATH,
                        selectionArgs);
                if (deleted > 0) {
                    notifyItemRemoved(h.getAdapterPosition());
                    notifyItemRangeChanged(h.getAdapterPosition(), getItemCount());
                } else {
                    Log.d(TAG, mContext.getString(R.string.not_deleted) + deleted);
                }
            }
        });

        holder.edit_button.setOnClickListener(new MyButtonOnClickListener(id, word) {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditAlbumActivity.class);
                intent.putExtra(EXTRA_ID, id);
                intent.putExtra(EXTRA_POSITION, h.getAdapterPosition());
                intent.putExtra(EXTRA_WORD, word);
                ((Activity) mContext).startActivityForResult(intent, MainActivity.WORD_EDIT);
            }
        });

        holder.more_button.setOnClickListener(new MyButtonOnClickListener(id, word) {

            @Override
            public void onClick(View v) {
                final Intent intento = new Intent(holder.contexto, AlbumInfoActivity.class);
                intento.putExtra("albumIdExtra", holder.albumItemView.getText());
                intento.putExtra("idExtra", holder.idItemView.getText());
                intento.putExtra("titleExtra", holder.wordItemView.getText());
                intento.putExtra("urlExtra", holder.url);
                intento.putExtra("thumbnailUrlExtra", holder.tUrl);
                holder.contexto.startActivity(intento);
            }
        });

    }

    @Override
    public int getItemCount() {
        Cursor cursor =
                mContext.getContentResolver().query(
                        Contract.ROW_COUNT_URI, new String[]{"count(*) AS count"},
                        selectionClause, selectionArgs, sortOrder);
        try {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count;
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION getItemCount: " + e);
            return -1;
        }
    }
}
