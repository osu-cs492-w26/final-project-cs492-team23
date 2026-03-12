package edu.oregonstate.cs492.MovieWatchListManager.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import edu.oregonstate.cs492.MovieWatchListManager.R
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Feel free to move the code from below into a dedicated fragment, make sure you move the YouTube view in the activity_main.xml too
        val youtubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        lifecycle.addObserver(youtubePlayerView)

        youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                //Can replace programmatically with the key from the api, currently it should show the Fight Club trailer
                val videoId = "O-b2VfmmbyA"
                youTubePlayer.loadVideo(videoId, 0F)
            }
        })

        //Review button code, can be moved to another fragment, make sure to move the button in the .xml too
        val reviewButton = findViewById<Button>(R.id.review_button)
        reviewButton.setOnClickListener {
            //Can change this url by putting in the movie_id e.g. https://www.themoviedb.org/movie/{movie_id}/reviews
            val uri = "https://www.themoviedb.org/movie/550/reviews".toUri()
            val intent = Intent(Intent.ACTION_VIEW, uri)

            //Call the intent
            try {
                startActivity(intent)
            }catch (e: ActivityNotFoundException){
                Log.d("MainActivity", "Error on the review intent ${e.message}")
            }

        }
    }


}