package com.dicoding.picodiploma.submission2annisa.Favorite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.submission2annisa.R
import com.dicoding.picodiploma.submission2annisa.SectionPagerFavoriteAdapter
import com.dicoding.picodiploma.submission2annisa.databinding.FavoriteDetailBinding
import com.dicoding.picodiploma.submission2annisa.db.DatabaseContract.AvatarFavColumn.Companion.CONTENT_URI
import com.dicoding.picodiploma.submission2annisa.db.MapFavHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FavDetailActivity: AppCompatActivity(), View.OnClickListener {
    companion object {
        private val TAG = FavDetailActivity::class.java.simpleName

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
        const val REQUEST_DELETE = 200
        const val RESULT_DELETE = 301
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_FAV = "extra_fav"
        const val EXTRA_STAN = "EXTRA_STAN"
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var favoriteHelper: FavoriteHelper
    private lateinit var binding: FavoriteDetailBinding
    private var position: Int? = 0
    private var datafav: FavoriteData? = null
    private var stateBtnFavorite = false
    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FavoriteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        datafav = intent.getParcelableExtra(EXTRA_FAV)
        if (datafav != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
        } else {
            datafav = FavoriteData()
        }


        dataDetail()


        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + datafav?.id)
        try {
            val cursor = contentResolver.query(uriWithId, null, null, null, null)
            if (cursor != null) {
                datafav = MapFavHelper.objectCursor(cursor)
                stateBtnFavorite = true
                stateBtnFavoriteUser(stateBtnFavorite)
                cursor.close()
                Log.d(TAG, "cursor: " + datafav)
            }
            Log.d(TAG, "hasil: " + datafav)
        } catch (e: Exception) {
            stateBtnFavorite = false
        }
        stateBtnFavoriteUser(stateBtnFavorite)
        binding.btnFav.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_fav) {
            if (stateBtnFavorite) {
                stateBtnFavorite = false
                stateBtnFavoriteUser(stateBtnFavorite)

                contentResolver.delete(uriWithId, null, null)
                Toast.makeText(this, this.getString(R.string.delete_user), Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent()
                intent.putExtra(EXTRA_POSITION, position)
                setResult(RESULT_DELETE, intent)
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun stateBtnFavoriteUser(state: Boolean) {
        if (state) {
            binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }
    private fun dataDetail() {
        val avatar = intent.getParcelableExtra<FavoriteData>(EXTRA_DATA)
        binding.apply {
            Glide.with(this@FavDetailActivity)
                .load(avatar?.avatar)
                .apply(RequestOptions().override(100, 100))
                .into(photoAvatar)

            btsUsername.text = avatar?.username.toString()
            btsNamefull.text = avatar?.name.toString()
            btsCompany.text = avatar?.company.toString()
            btsLocation.text = avatar?.location.toString()
            btsRepository.text = avatar?.repository.toString()

        }
        val tabLayout=findViewById<TabLayout>(R.id.tabslayout)
        val viewPager2=findViewById<ViewPager2>(R.id.view_pager1)
        val sectionsPagerAdapter= SectionPagerFavoriteAdapter(this,supportFragmentManager,lifecycle)
        sectionsPagerAdapter.username= avatar?.username

        viewPager2.adapter=sectionsPagerAdapter
        TabLayoutMediator(tabLayout,viewPager2){tab,position->
            tab.text = resources.getString(TAB_TITLES[position])

        }.attach()
        supportActionBar?.elevation = 0f
    }
}
