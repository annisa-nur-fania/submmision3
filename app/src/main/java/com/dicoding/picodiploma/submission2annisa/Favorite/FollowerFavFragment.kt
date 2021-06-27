package com.dicoding.picodiploma.submission2annisa

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.submission2annisa.Favorite.FollowerFavAdapter
import com.dicoding.picodiploma.submission2annisa.Favorite.FollowersData
import com.dicoding.picodiploma.submission2annisa.databinding.FragmentFollowerFavBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray


class FollowerFavFragment : Fragment() {

    private lateinit var binding: FragmentFollowerFavBinding
    private lateinit var followerAdapter: FollowerFavAdapter
    private var listfollow : ArrayList<FollowersData> = ArrayList()

    companion object {

        private  val ARG_USERNAME="username"
        fun newInstance(username: String?): FollowerFragment {
            val fragment = FollowerFragment()
            val bundel = Bundle()
            bundel.putString(ARG_USERNAME, username)
            fragment.arguments = bundel
            return fragment
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerFavBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followerAdapter = FollowerFavAdapter(listfollow)

        val username = arguments?.getString(ARG_USERNAME)
        rvconfig()
        //Toast.makeText(context, "Apakah follwer : "+getFollower(username.toString()), Toast.LENGTH_LONG).show()
        //getFollower(username.toString())
        getFollower(username.toString())


    }
    private fun getFollower(query: String) {
        binding.progressbar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")

        val url = "https://api.github.com/users/$query/followers"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.progressbar.visibility = View.INVISIBLE
                try {
                    val results = String(responseBody)
                    val respondArray = JSONArray(results)

                    for (i in 0 until respondArray.length()) {
                        val respondObject = respondArray.getJSONObject(i)
                        val avatars = respondObject.getString("avatar_url").toString()
                        val Name = respondObject.getString("login").toString()

                        //Toast.makeText(context, "Apakah follwer : "+Name, Toast.LENGTH_LONG).show()
                        listfollow.add(
                            FollowersData(
                                avatars,
                                Name)
                        )
                        setRv()

                    }
                } catch (e: Exception) {
                    Log.d("Followers", e.message.toString())
                    Toast.makeText(
                        context,
                        "Exception" + e.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressbar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request 4"
                    403 -> "$statusCode : Forbidden 4"
                    404 -> "$statusCode : Not Found 4"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("onFailure", error.message.toString())
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun rvconfig(){
        binding.rvFavFollower.layoutManager = LinearLayoutManager(activity)
        binding.rvFavFollower.setHasFixedSize(true)
        val dataadapter = FollowerFavAdapter(listfollow)
        binding.rvFavFollower.adapter = followerAdapter

        dataadapter.setOnItemClickCallback(object :
            FollowerFavAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FollowersData) {

            }
        })
    }
    private fun setRv(){
        binding.rvFavFollower.layoutManager = LinearLayoutManager(activity)
        binding.rvFavFollower.setHasFixedSize(true)
        val dataadapter = FollowerFavAdapter(listfollow)
        binding.rvFavFollower.adapter = followerAdapter

        dataadapter.setOnItemClickCallback(object :
            FollowerFavAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FollowersData) {

            }
        })

    }

}