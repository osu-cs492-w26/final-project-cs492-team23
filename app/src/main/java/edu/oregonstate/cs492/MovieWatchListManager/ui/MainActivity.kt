package edu.oregonstate.cs492.MovieWatchListManager.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import edu.oregonstate.cs492.MovieWatchListManager.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Feel free to move the code from below into a dedicated fragment, make sure you move the Youtube view in the activity_main.xml too
        val youtubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                //Can replace programmatically with the key from the api, currently it should show the Fight Club trailer
                val videoId = "O-b2VfmmbyA"
                youTubePlayer.loadVideo(videoId, 0F)
            }
        })
    }


}