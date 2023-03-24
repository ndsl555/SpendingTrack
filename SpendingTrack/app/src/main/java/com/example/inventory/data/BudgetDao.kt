package com.example.inventory.data
import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertbud(budget: Budget)

    //@Query("SELECT * from budget ORDER BY name ASC")
    //fun getbudItems(): Flow<List<Budget>>

    @Query("SELECT * FROM budget")
    fun getLastBudget(): Flow<List<Budget>>

    /*@Query("SELECT * from item WHERE id = :id")
    fun getItem(id: Int): Flow<Item>*/

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.


    /*@Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)*/
    //
    /*@Query("SELECT * from budget WHERE id = :id")
    fun getBudget(id:Int)


    @Query("UPDATE settings-table SET barcode = :newcode WHERE id = :id")
    suspend fun updateBud(id:Int,newcode:String)*/
}