package com.example.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.room.room.Constant
import com.example.room.room.Movie
import com.example.room.room.MovieDb
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {
    val db by lazy { MovieDb(this) }
    var movieId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setupview()
        setupListener()
        movieId = intent.getIntExtra("movie_id", 0)
    }

    fun setupview(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
            Constant.TYPE_CREATE -> {
                btn_update.visibility = android.view.View.GONE
            }
            Constant.TYPE_READ -> {
                btn_save.visibility = android.view.View.GONE
                btn_update.visibility = android.view.View.GONE
                getMovie()
            }
            Constant.TYPE_UPDATE -> {
                btn_save.visibility = android.view.View.GONE
                getMovie()
            }
        }
    }

    fun setupListener() {
        btn_save.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
                db.movieDao().addMovie(
                    Movie(0, et_title.text.toString(), et_description.text.toString())
                )
                finish()
            }
        }
        btn_update.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
                db.movieDao().UpdateMovie(
                    Movie(movieId, et_title.text.toString(), et_description.text.toString())
                )
                finish()
            }
        }
    }

    fun getMovie(){
        movieId = intent.getIntExtra("movie_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val movies = db.movieDao().getMovie(movieId)[0]
            et_title.setText(movies.title)
            et_description.setText(movies.desc)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}