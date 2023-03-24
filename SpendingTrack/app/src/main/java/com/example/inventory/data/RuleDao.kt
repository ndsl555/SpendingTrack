package com.example.inventory.data
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface RuleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertrule(rule: Rule)

    @Query("SELECT * FROM Rule")
    fun getLastRule(): Flow<List<Rule>>
}