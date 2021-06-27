package com.dicoding.picodiploma.consummerapp.Favorite

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.consummerapp.R
import com.dicoding.picodiploma.consummerapp.databinding.ItemFavoriteBinding
import com.dicoding.picodiploma.consummerapp.db.DatabaseContract.AvatarFavColumn.Companion.CONTENT_URI
import com.dicoding.picodiploma.consummerapp.db.MapFavHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AvatarFavActivity: AppCompatActivity() {

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    private lateinit var binding: ItemFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.rcvAvatarFav.layoutManager = LinearLayoutManager(this)
        binding.rcvAvatarFav.setHasFixedSize(true)
        favoriteAdapter = FavoriteAdapter(this)
        binding.rcvAvatarFav.adapter = favoriteAdapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadDataAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        if (savedInstanceState == null) {
            loadDataAsync()
        } else {
            savedInstanceState.getParcelableArrayList<FavoriteData>(EXTRA_STATE)?.also {
                favoriteAdapter.item = it
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadDataAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rcvAvatarFav.visibility = View.GONE
            val defferdFav = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MapFavHelper.listCursor(cursor)
            }
            binding.rcvAvatarFav.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
            val datafavorite = defferdFav.await()
            if (datafavorite.size > 0) {
                favoriteAdapter.item = datafavorite
            } else {
                favoriteAdapter.item = ArrayList()
                binding.progressBar.visibility = View.GONE
                binding.rcvAvatarFav.visibility = View.GONE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, favoriteAdapter.item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                FavDetailActivity.REQUEST_DELETE ->
                    when (resultCode) {
                        FavDetailActivity.RESULT_DELETE -> {
                            val position = data.getIntExtra(FavDetailActivity.EXTRA_POSITION, 0)
                            favoriteAdapter.removeItem(position)
                            Toast.makeText(this, getString(R.string.delete_favorite), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }
    }


}