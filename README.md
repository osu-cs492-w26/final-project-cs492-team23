# Movie Watchlist Manager

Movie Watchlist Manager is an Android application that allows users to explore movies, search for their favorites, view detailed information including trailers and streaming availability, and maintain a personalized watchlist and viewing history.

## Features

- **Explore Movies**: Browse through various categories including Popular, Top Rated, Upcoming, and Now Playing movies.
- **Search**: Easily search for any movie in the extensive TMDB database.
- **Detailed Movie Information**: 
    - View movie overviews, ratings, and release dates.
    - Watch movie trailers directly within the app using the integrated YouTube player.
    - See similar movie recommendations.
    - Check which streaming services are currently offering the movie.
- **Personal Watchlist**: Save movies you want to see later.
- **Watch History**: Keep track of the movies you have already watched.
- **UI**: Clean and intuitive interface built with Material Design components.

## Tech Stack

- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel) with Repository pattern.
- **Local Database**: Room for persistent storage of saved movies and watch history.
- **Networking**: Retrofit and Moshi for consuming The Movie Database (TMDB) API.
- **Image Loading**: Glide for efficient image rendering.
- **Navigation**: Android Jetpack Navigation component for seamless fragment transitions.
- **UI Components**: RecyclerView, ConstraintLayout, and Material 3.
- **Media**: `android-youtube-player` for trailer playback.

## Contributors

Navigation and Database - Owen Jones
API integration - Aidan Daly
UI/UX - Téa Kidder
