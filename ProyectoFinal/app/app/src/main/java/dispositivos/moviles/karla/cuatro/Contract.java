/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dispositivos.moviles.karla.cuatro;

import android.net.Uri;
import android.provider.BaseColumns;

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
