package com.example.room.room

import androidx.room.*

@Entity
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val desc: String
)
