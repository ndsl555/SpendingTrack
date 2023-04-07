package com.example.inventory.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDao {

    @Query("SELECT * from Invoice")
    fun getItems(): Flow<List<Invoice>>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(invoice: Invoice)

    @Update
    suspend fun update(invoice: Invoice)

    @Delete
    suspend fun delete(invoice: Invoice)

    @Query("DELETE FROM Invoice")
    suspend fun deleteAll()

    @Query("DELETE FROM Invoice WHERE date= :date AND number= :number" )
    fun deleteByarg(date: String,number: String)

}