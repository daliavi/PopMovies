package com.daliavi.popmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Bundle bundle = getIntent().getExtras();
        MovieData item = bundle.getParcelable("movie_data");


        ImageView imageViewPoster = (ImageView)findViewById(R.id.imgPoster);
        Picasso.with(this)
                .load(item.getPosterUrl())
                .into(imageViewPoster);

        TextView textViewTitle = (TextView)findViewById(R.id.txtTitle);
        textViewTitle.setText(item.getMovieTitle().toString());

        TextView textViewReleaseDate = (TextView)findViewById(R.id.txtReleaseDate);
        textViewReleaseDate.setText("Release date: " + item.getMovieReleaseDate().toString());

        TextView textViewRating = (TextView)findViewById(R.id.txtRating);
        textViewRating.setText("Rating: " + item.getMovieRating().toString());

        TextView textViewOverview = (TextView)findViewById(R.id.txtOverview);
        textViewOverview.setText(item.getMovieOverview().toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
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
}
