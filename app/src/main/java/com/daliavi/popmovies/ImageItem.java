package com.daliavi.popmovies;

/**
 * Created by telmate on 11/4/15.
 */
public class ImageItem {
    //private Bitmap image;
    private String url;
    private String movieId;

    public ImageItem( String url, String movieId) {
        super();
        //this.image = image;
        this.url = url;
        this.movieId = movieId;
    }

   // public Bitmap getImage() {
   //     return image;
   // }

   // public void setImage(Bitmap image) {
   //     this.image = image;
   // }

    public String getUrl() {
        return url;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setUrl(String title) {
        this.url = title;
    }
}
