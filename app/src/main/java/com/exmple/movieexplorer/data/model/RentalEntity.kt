package com.exmple.movieexplorer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rentals")
data class RentalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val movieId: Int,
    val title: String,
    val posterUrl: String,
    val rating: Float,
    val days: Int = 1
)

fun getRentalPricePerDay(rating: Float): Int = when {
    rating >= 8.0f -> 149
    rating >= 6.0f -> 99
    rating >= 4.0f -> 59
    else -> 29
}

fun totalPrice(rental: RentalEntity): Int = getRentalPricePerDay(rental.rating) * rental.days
