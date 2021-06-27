package com.dicoding.picodiploma.submission2annisa


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.submission2annisa.Favorite.AvatarFavActivity
import com.dicoding.picodiploma.submission2annisa.Option.SettingActivity
import com.dicoding.picodiploma.submission2annisa.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityMainBinding
    private val itemavatar = ArrayList<AvatarData>()
    private lateinit var adapter: ListAvatarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapter = ListAvatarAdapter(itemavatar)



        if (adapter.itemCount == 0){
            showLoading(false)
        }


        rcvconfig()
        search()
        getListAvatar()

    }

    private fun search() {
        binding.fiturSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener, androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    return true
                } else {
                    itemavatar.clear()
                    searchData(query)
                }
                return true
            }
             override fun onQueryTextChange(newText: String): Boolean {
            return false}
        })

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val moveIntent = Intent(this@MainActivity, SettingActivity::class.java)
            startActivity(moveIntent)
        } else if (item.itemId == R.id.action_fav) {
            val intent = Intent(this, AvatarFavActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }


    fun getListAvatar() {

        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")

        val url = "https://api.github.com/users"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray
            ) {
                try {
                    val results = String(responseBody)
                    val respondArray = JSONArray(results)
                    //Toast.makeText(context, "Apakah : "+respondArray.toString(), Toast.LENGTH_LONG).show()
                    for (i in 0 until respondArray.length()) {
                        val respondObject = respondArray.getJSONObject(i)
                        val Name: String = respondObject.getString("login")
                        getDetail(Name)
                    }
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                    Toast.makeText(this@MainActivity, e.message.toString(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable
            ) {

                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request 1"
                    403 -> "$statusCode : Forbidden 1"
                    404 -> "$statusCode : Not Found 1"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("onFailure Search User", error.message.toString())
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }
    fun searchData(id: String?){
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")

        val url = "https://api.github.com/search/users?q=$id"
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray
            ) {
                try {
                    binding.progressBar.visibility = View.INVISIBLE
                    val results = String(responseBody)
                    Log.d(TAG, results)
                    val rsepondArray = JSONObject(results)
                    //Toast.makeText(this@MainActivity, "Apakah : "+rsepondArray.toString(), Toast.LENGTH_LONG).show()
                    val items = rsepondArray.getJSONArray("items")
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        val avatar =item.getString("avatar_url")
                        val data = AvatarData()
                        data.username=username
                        data.photoavatar=avatar
                        //Toast.makeText(this@MainActivity, "Apakah : "+Name.toString(), Toast.LENGTH_LONG).show()
                        getDetail(username)
                    }
                } catch (e: java.lang.Exception) {
                    Log.d("Exception", e.message.toString())
                    Toast.makeText(
                            this@MainActivity,
                            "Exception " + e.message.toString(),
                            Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request 2"
                    403 -> "$statusCode : Forbidden 2"
                    404 -> "$statusCode : Not Found 2"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("onFailure", error.message.toString())
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }
    fun getDetail(id: String?){
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")

        val url = "https://api.github.com/users/$id"
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray
            ) {
                try{
                    val results = String(responseBody)
                    val jsonObject = JSONObject(results)
                    val photoavatar = jsonObject.getString("avatar_url").toString()
                    val username = jsonObject.getString("login").toString()
                    val nameavatar = jsonObject.getString("name").toString()
                    val location = jsonObject.getString("location").toString()
                    val company = jsonObject.getString("company").toString()
                    val followers = jsonObject.getString("followers")
                    val following = jsonObject.getString("following")
                    val repository = jsonObject.getString("repos_url")
                    //Toast.makeText(this@MainActivity, "Apakah detail : "+company, Toast.LENGTH_LONG).show()
                    itemavatar.add(
                            AvatarData(
                                    photoavatar,
                                    username,
                                    nameavatar,
                                    location,
                                    company,
                                    followers,
                                    following,
                                    repository
                            )


                    )
                    showrv()
                }catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                    Toast.makeText(
                            this@MainActivity,
                            "Exception" + e.message.toString(),
                            Toast.LENGTH_LONG
                    ).show()

                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request 3"
                    403 -> "$statusCode : Forbidden 3"
                    404 -> "$statusCode : Not Found 3"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("onFailure", error.message.toString())
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
            }

        })
    }
    private fun rcvconfig(){
        binding.rcvAvatar.layoutManager=LinearLayoutManager(binding.rcvAvatar.context)
        binding.rcvAvatar.setHasFixedSize(true)
        binding.rcvAvatar.addItemDecoration(
                DividerItemDecoration(
                        binding.rcvAvatar.context,
                        DividerItemDecoration.VERTICAL
                )
        )
    }

    private fun showrv(){
        binding.rcvAvatar.layoutManager=LinearLayoutManager(this)
        val adapterdata=ListAvatarAdapter(filterlist)
        binding.rcvAvatar.adapter=adapter
        adapterdata.setOnItemClickCallback(object : ListAvatarAdapter.OnItemClickCallback{
            override fun onItemClicked(data: AvatarData) {
                selectUser(data)
            }
        })
    }
    private fun selectUser(dataa: AvatarData){
        AvatarData(
                dataa.photoavatar,
                dataa.username,
                dataa.nameavatar,
                dataa.company,
                dataa.location,
                dataa.repository,
                dataa.follower,
                dataa.following
        )
        val intent = Intent(this@MainActivity, AvatarDetail::class.java)
        intent.putExtra(AvatarDetail.EXTRA_DATA, dataa)
        startActivity(intent)
    }
    private fun showLoading( state :Boolean){
        if(state){
            binding.progressBar.visibility= View.VISIBLE
            binding.rcvAvatar.visibility=View.GONE
        }else{
            binding.rcvAvatar.visibility=View.VISIBLE
            binding.progressBar.visibility=View.GONE
        }
    }
}