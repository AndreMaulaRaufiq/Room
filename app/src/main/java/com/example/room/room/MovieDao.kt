package com.example.room.room

import androidx.room.*

@Dao
interface MovieDao {

    @Insert
    suspend fun addMovie(movie: Movie)

    @Update
    suspend fun UpdateMovie(movie: Movie)

    @Delete
    suspend fun DeleteMovie(movie: Movie)

    @Query ("SELECT * FROM movie")
    suspend fun getMovies():List<Movie>

    @Query ("SELECT * FROM movie WHERE id = :movie_id")
    suspend fun getMovie(movie_id: Int):List<Movie>


}