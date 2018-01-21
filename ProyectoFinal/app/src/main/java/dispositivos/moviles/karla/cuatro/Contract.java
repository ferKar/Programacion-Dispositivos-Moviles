package dispositivos.moviles.karla.cuatro;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ferKarly.
 * Clase-Programación de Dispositivos Moviles
 * Version -mil ocho mil
 */
public final class Contract {

    private Contract() {}

    public static final int ALL_ITEMS = -2;
    public static final String COUNT = "count";
    public static final String AUTHORITY =
            "dispositivos.moviles.karla.cuatro.provider";
    public static final String CONTENT_PATH = "albums";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CONTENT_PATH);
    public static final Uri ROW_COUNT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + CONTENT_PATH + "/" + COUNT);
    public static final String SINGLE_RECORD_MIME_TYPE =
            "vnd.android.cursor.item/vnd."+ AUTHORITY +"." + CONTENT_PATH;
    public static final String MULTIPLE_RECORDS_MIME_TYPE =
            "vnd.android.cursor.item/vnd."+ AUTHORITY +"." + CONTENT_PATH;
    public static final String DATABASE_NAME = "albumslist";

    public static abstract class Columnas implements BaseColumns {

        public static final String WORD_LIST_TABLE = "word_entries";
        public static final String KEY_ID = "_id";
        public static final String KEY_WORD = "word";
        public static final String KEY_ALBUMID = "albumID";
        public static final String KEY_Id = "id";
        public static final String KEY_URL = "url";
        public static final String KEY_ThumbnailUrl = "thumbnailUrl";
    }

}
