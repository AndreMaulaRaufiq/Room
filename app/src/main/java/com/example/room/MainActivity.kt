package com.example.room

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.room.room.Constant
import com.example.room.room.Movie
import com.example.room.room.MovieDb
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {
    val db by lazy { MovieDb(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupListener()
        setupRecycleView()
    }


    override fun onStart() {
        super.onStart()
        loadMovie()
    }

    fun loadMovie(){
        CoroutineScope(Dispatchers.IO).launch {
            val movies = db.movieDao().getMovies()
            Log.d("MainActivity", "dbresponse:$movies")
            withContext(Dispatchers.Main) {
                (rv_movie.adapter as MovieAdapter).setData(movies)
            }
        }
    }

    fun setupListener() {
        btn_add.setOnClickListener() {
            intentEdit(0, Constant.TYPE_CREATE)
        }
    }

    fun intentEdit(movieId: Int, intentType: Int){
        startActivity(Intent(this, AddActivity::class.java)
            .putExtra("movie_id", movieId)
            .putExtra("intent_type", intentType))
    }

    private fun setupRecycleView() {
        val movieAdapter = MovieAdapter(arrayListOf(), object : MovieAdapter.OnAdapterListener {
            override fun onClick(movie: Movie) {
                intentEdit(movie.id, Constant.TYPE_READ)
            }

            override fun onUpdate(movie: Movie) {
                intentEdit(movie.id, Constant.TYPE_UPDATE)
            }

            override fun onDelete(movie: Movie) {
                deleteDialog(movie)
            }
        })
        rv_movie.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = movieAdapter
        }
    }

    private fun deleteDialog(movie: Movie){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Konfirmasi")
            setMessage("Yakin hapus ${movie.title}")
            setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            setNegativeButton("Hapus") { dialog, _ ->
                dialog.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.movieDao().DeleteMovie(movie)
                    loadMovie()
                }
            }
        }
        alertDialog.show()
    }
}