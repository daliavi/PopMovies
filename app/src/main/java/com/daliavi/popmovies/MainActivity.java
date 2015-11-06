package com.daliavi.popmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private GridViewAdapter gridAdapter;

    private GridViewAdapter adapter;
    private ArrayList<MovieData>  movie_data = new ArrayList<MovieData>()
    {{
            add(new MovieData("http://www.oilerie.com/mm5/images/img_no_thumb.jpg", "no_id", "","","",""));
        }};
    private String str = "";
    private StringBuffer buf = new StringBuffer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        InputStream is = getResources().openRawResource(R.raw.app);

        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            if (is != null) {
                while ((str = r.readLine()) != null) {
                    buf.append(str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { is.close(); } catch (Throwable ignore) {}
        }

        FetchMovieTask moviesTask = new FetchMovieTask();
        moviesTask.execute();

        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new GridViewAdapter(this, R.layout.poster_grid_item, movie_data);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MovieData item = (MovieData) parent.getItemAtPosition(position);
                //Toast.makeText(getApplicationContext(),item.getMovieId().toString(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
                intent.putExtra("movie_data", item);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<MovieData>> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
        private ArrayList<MovieData> getMovieDataFromJson(String movieJsonStr, int numItems)
                throws JSONException {

            /* movie data comes from http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key={API_KEY}
            JSON format:
            {   page: 1,
                results: [
                {
                    adult: false,
                    backdrop_path: "/dkMD5qlogeRMiEixC4YNPUvax2T.jpg",
                    genre_ids: [
                        28,
                        12,
                        878,
                        53
                    ],
                    id: 135397,
                    original_language: "en",
                    original_title: "Jurassic World",
                    overview: "Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.",
                    release_date: "2015-06-12",
                    poster_path: "/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg",
                    popularity: 50.019738,
                    title: "Jurassic World",
                    video: false,
                    vote_average: 6.9,
                    vote_count: 2843
                },*/

            //    image URL: http://image.tmdb.org/t/p/w185/kqjL17yufvn9OVLyXYpvtyrFfak.jpg (change the .jpg part)

            // These are the names of the JSON objects that need to be extracted.
            final String TMDB_RESULTS = "results"; //items array
            final String TMDB_POSTER = "poster_path";
            final String TMDB_TITLE = "original_title";
            final String TMDB_RELEASE_DATE = "release_date";
            final String TMDB_ID = "id";
            final String TMDB_VOTE_AVG = "vote_average";
            final String TMDB_OVERVIEW = "overview";
            movie_data.clear();

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(TMDB_RESULTS);

            for(int i = 0; i < movieArray.length(); i++) {
                JSONObject artistItemObject = movieArray.getJSONObject(i);

                String movieId = artistItemObject.getString(TMDB_ID);
                String moviePosterId = artistItemObject.getString(TMDB_POSTER);
                String movieTitle = artistItemObject.getString(TMDB_TITLE);
                String movieReleaseDate = artistItemObject.getString(TMDB_RELEASE_DATE);
                String movieOverview = artistItemObject.getString(TMDB_OVERVIEW);
                String movieRating = artistItemObject.getString(TMDB_VOTE_AVG);

                //logs for debugging, remove after

                Log.v(LOG_TAG, "Movie name: " + movieTitle + " ");
                Log.v(LOG_TAG, "Movie ID: " + movieId + " ");
                Log.v(LOG_TAG, "Movie poster id: " + moviePosterId + " ");

                movie_data.add(new MovieData("http://image.tmdb.org/t/p/w342/" + moviePosterId, movieId,
                        movieTitle, movieReleaseDate, movieOverview, movieRating));
            }

            Log.v(LOG_TAG, "everything is ok ");
            return movie_data;
        }

        @Override
        protected ArrayList<MovieData> doInBackground(String... params){
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;
            String LOG_TAG = "Daliavi-tag";
            String POPULARITY_SORT_DESC = "popularity.desc";
            String API_KEY = buf.toString();

            try {
                // Construct the URL for the TMDB http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key={API KEY}

                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";


                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, POPULARITY_SORT_DESC)
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to TMDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String

                int responseCode = urlConnection.getResponseCode();
                InputStream inputStream = urlConnection.getInputStream();

                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // print out the completed buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
                Log.v(LOG_TAG, "JSON String: " + moviesJsonStr);

            } catch (IOException e) {

                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the artist data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(moviesJsonStr, 10);
            } catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieData> result) {
            super.onPostExecute(result);
            adapter.notifyDataSetChanged();
            if (result != null) {
                Log.v(LOG_TAG, "List update");
            }
        }
    }
}
