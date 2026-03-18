package com.exmple.movieexplorer.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.exmple.movieexplorer.data.model.RentalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RentalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRental(rental: RentalEntity)

    @Query("SELECT * FROM rentals")
    fun getAllRentals(): Flow<List<RentalEntity>>

    @Query("SELECT * FROM rentals")
    suspend fun getAllRentalsOnce(): List<RentalEntity>

    @Query("UPDATE rentals SET days = :days WHERE id = :id")
    suspend fun updateRentalDays(id: Int, days: Int)

    @Delete
    suspend fun deleteRental(rental: RentalEntity)
}
