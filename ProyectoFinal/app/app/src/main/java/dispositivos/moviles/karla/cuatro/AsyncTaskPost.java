package dispositivos.moviles.karla.cuatro;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by k on 24/10/17.
 */

class AsyncTaskPost extends AsyncTaskLoader<LinkedList<Post>> {

    private final String urlG = "https://jsonplaceholder.typicode.com/photos";
    private int debu = 0;

    public AsyncTaskPost(Context context) {
        super(context);
    }

    @Override
    public LinkedList<Post> loadInBackground() {
        try {
            return makeListAlbums(run(urlG));
        } catch (IOException e) {
            e.printStackTrace();
            return makeListAlbums("Verifica tu conexión" + debu++);
        }
    }

    private String run(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = new OkHttpClient().newCall(request).execute();
        return response.body().string();
    }

    private LinkedList<Post> makeListAlbums(String cadenaTipo){
        LinkedList<Post> listaAlbums = new LinkedList<>();
        try {
            JSONArray cadTipoJson = new JSONArray(cadenaTipo);
            for (int i = 0; i < cadTipoJson.length(); i++) {
                JSONObject jsonObj = cadTipoJson.getJSONObject(i);
                listaAlbums.addLast(hasmePost(jsonObj));
            }
        }catch (JSONException e) {
            listaAlbums.addFirst(new Post(cadenaTipo,-2,-2,"",""));
        }
        return  listaAlbums;
    }

    private Post hasmePost(JSONObject jsObj){
        Post ps = new Post();
        try {
            ps.setAlbumId(jsObj.getInt("albumId"));
            ps.setId(jsObj.getInt("id"));
            ps.setTitle(jsObj.getString("title"));
            ps.setUrl(jsObj.getString("url"));
            ps.setThumbnailUrl(jsObj.getString("thumbnailUrl"));
            return ps;
        }catch (JSONException e){
            return ps;
        }
    }

    protected void onStartLoading()
    {
        forceLoad();
    }
}
