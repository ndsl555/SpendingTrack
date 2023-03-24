package com.example.inventory.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class Rule (
    @PrimaryKey(autoGenerate = true)
    val id : Int=0,
    @ColumnInfo(name="bud_permonth")
    val itemRule: Int,
)