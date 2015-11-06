package com.daliavi.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by telmate on 11/4/15.
 */
public class MovieData implements Parcelable {

    private String posterUrl;
    private String movieId;
    private String movieTitle;
    private String movieReleaseDate;
    private String movieOverview;
    private String movieRating;
    //private ImageView moviePoster;

    public MovieData(String posterUrl, String movieId, String movieTitle, String movieReleaseDate,
                     String movieOverview, String movieRating) {
        super();

        this.posterUrl = posterUrl;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieReleaseDate = movieReleaseDate;
        this.movieOverview = movieOverview;
        this.movieRating = movieRating;
        //this.moviePoster = moviePoster;
    }

    private MovieData(Parcel in){
        posterUrl = in.readString();
        movieId = in.readString();
        movieTitle = in.readString();
        movieReleaseDate = in.readString();
        movieOverview = in.readString();
        movieRating = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterUrl);
        dest.writeString(movieId);
        dest.writeString(movieTitle);
        dest.writeString(movieReleaseDate);
        dest.writeString(movieOverview);
        dest.writeString(movieRating);
    }

    public static final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>(){
        public MovieData createFromParcel(Parcel in){
            return new MovieData(in);
        }

        public MovieData[] newArray(int size){
            return new MovieData[size];
        }
    };

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public String getMovieRating() {
        return movieRating;
    }

    public void setPosterUrl(String title) {
        this.posterUrl = title;
    }
}
