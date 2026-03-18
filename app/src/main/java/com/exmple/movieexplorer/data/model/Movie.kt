package com.exmple.movieexplorer.data.model

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val title: String,
    @SerializedName("poster_path") val posterUrl: String?,
    @SerializedName("overview") val overview: String,
    @SerializedName("vote_average") val rating: Float
)

data class MovieResponse(val results: List<Movie>)
