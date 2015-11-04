package com.daliavi.popmovies;

/**
 * todo class for movie data
 */
public class MovieData {
    public String id;
    public String title;
    public String release_date;
    public String poster_url;
    public String vote_average;
    public String overview;

    public MovieData() {super();}

    public MovieData(String id, String title, String release_date, String poster_url, String vote_average, String overview ){
        super();
        this.id=id;
        this.title=title;
        this.release_date = release_date;
        this.poster_url = poster_url;
        this.vote_average = vote_average;
        this.overview = overview;
    }
}
