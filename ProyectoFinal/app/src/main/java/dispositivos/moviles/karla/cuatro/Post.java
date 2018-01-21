package dispositivos.moviles.karla.cuatro;

import android.support.annotation.NonNull;

/**
 * Created by ferKarly.
 * Clase-Programación de Dispositivos Moviles
 * Version -mil ocho mil
 */
class Post implements Comparable<Post>{
    private int albumId;
    private int id;
    private String title;
    private String url;
    private String thumbnailUrl;

    public Post(){
        this.albumId = -1;
        this.id = -1;
        this.title = "No se pudo pasar a tipo Json";
        this.url = "no se puso pasar a tipo Json :c";
        this.thumbnailUrl = "no se pudo pasar a Json :c";
    }

    public Post(String title, int albumId, int id, String url, String thumbnailUrl){
        this.albumId = albumId;
        this.id = id;
        this.title = title;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getAlbumId()
    {
        return albumId;
    }

    public void setAlbumId(int albumId)
    {
        this.albumId = albumId;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getThumbnailUrl()
    {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl)
    {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public int compareTo(@NonNull Post s) {
        if (id > s.getId()) return 1;
        return (id == s.getId()) ? 0 : -1;
    }

}
