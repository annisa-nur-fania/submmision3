package com.dicoding.picodiploma.submission2annisa

import android.content.ContentValues
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.submission2annisa.Favorite.FavoriteData
import com.dicoding.picodiploma.submission2annisa.Favorite.FavoriteHelper
import com.dicoding.picodiploma.submission2annisa.databinding.ActivityAvatarDetailBinding
import com.dicoding.picodiploma.submission2annisa.db.DatabaseContract
import com.dicoding.picodiploma.submission2annisa.db.DatabaseContract.AvatarFavColumn.Companion.CONTENT_URI
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AvatarDetail : AppCompatActivity() {
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
                R.string.followers,
                R.string.following
        )
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_FAV = "extra_fav"
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var binding: ActivityAvatarDetailBinding
    private lateinit var users : ArrayList<AvatarData>
    private lateinit var favoriteHelper: FavoriteHelper
    private var datafav : FavoriteData? = null
    private var stan: Int? = 0
    private var positionbtnfav = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAvatarDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataDetail()



        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        datafav = intent.getParcelableExtra(EXTRA_FAV)
        if (datafav != null) {
            stan = intent.getIntExtra(EXTRA_POSITION, 0)
        } else {
            datafav = FavoriteData()
        }


        binding.btnFav.setOnClickListener {
            if (positionbtnfav) {
                favoriteHelper.deleteDataId(binding.btsNamefull.text.toString())
                positionbtnfav = false
                stateFavorite(positionbtnfav)
                Toast.makeText(this, "Dalate USER",
                        Toast.LENGTH_LONG).show()
            } else {
                insertFavorite()
                positionbtnfav=true
                stateFavorite(positionbtnfav)

            }
        }


    }

    private fun insertFavorite() {
        val getData = intent.getParcelableExtra<AvatarData>(EXTRA_DATA)
        val avatar = getData?.photoavatar
        val username = getData?.username.toString()
        val namefull = getData?.nameavatar.toString()
        val location = getData?.location.toString()
        val company = getData?.company.toString()
        val repository = getData?.repository.toString()
        val follower = getData?.follower.toString()
        val following = getData?.follower.toString()

        val values = ContentValues()

        values.put(DatabaseContract.AvatarFavColumn.USERNAME, username)
        values.put(DatabaseContract.AvatarFavColumn.NAME, namefull)
        values.put(DatabaseContract.AvatarFavColumn.AVATAR, avatar)
        values.put(DatabaseContract.AvatarFavColumn.COMPANY, company)
        values.put(DatabaseContract.AvatarFavColumn.LOCATION, location)
        values.put(DatabaseContract.AvatarFavColumn.REPOSITORY, repository)
        //values.put(DatabaseContract.AvatarFavColumn.FOLLOWER, repository)
        //values.put(DatabaseContract.AvatarFavColumn.FOLLOWING, repository)

        contentResolver.insert(CONTENT_URI, values)

        Toast.makeText(this, "Add Data",
                Toast.LENGTH_LONG).show()

    }

    private fun dataDetail(){
        val avatar = intent.getParcelableExtra<AvatarData>(EXTRA_DATA)
        binding.apply {
            Glide.with(this@AvatarDetail)
                .load(avatar?.photoavatar)
                .apply(RequestOptions().override(100, 100))
                .into(photoAvatar)

            btsUsername.text = avatar?.username.toString()
            btsNamefull.text = avatar?.nameavatar.toString()
            btsCompany.text = avatar?.company.toString()
            btsLocation.text = avatar?.location.toString()
            btsRepository.text = avatar?.repository.toString()

        }

        val tabLayout=findViewById<TabLayout>(R.id.tabslayout)
        val viewPager2=findViewById<ViewPager2>(R.id.view_pager)
        val sectionsPagerAdapter=SectionsPagerAdapter(this,supportFragmentManager,lifecycle)
        sectionsPagerAdapter.username= avatar?.username

        viewPager2.adapter=sectionsPagerAdapter
        TabLayoutMediator(tabLayout,viewPager2){tab,position->
            tab.text = resources.getString(TAB_TITLES[position])

        }.attach()
        supportActionBar?.elevation = 0f

    }
    private fun stateFavorite(btnfav: Boolean) {
        if (btnfav) {
            binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}