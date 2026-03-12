package edu.oregonstate.cs492.MovieWatchListManager.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StreamingServices(
    val results: StreamingServiceRegion?
)

@JsonClass(generateAdapter = true)
data class StreamingServiceRegion(
    @Json(name ="US") val UnitedStates: UnitedStatesStreamingServices?
)

@JsonClass(generateAdapter = true)
data class UnitedStatesStreamingServices(
    val rent: List<StreamingService>?,
    val buy: List<StreamingService>?,
    @Json(name = "flatrate") val flatRate: List<StreamingService>?,
)

@JsonClass(generateAdapter = true)
data class StreamingService(
    //Path to get the logo of the movie
    //Add to the end of this string: https://image.tmdb.org/t/p/original
    @Json(name = "logo_path") val logoPath: String?,

    //Name of the streaming service
    @Json(name = "provider_name") val providerName: String?,
)