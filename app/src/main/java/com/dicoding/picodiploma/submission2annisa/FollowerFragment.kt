package com.dicoding.picodiploma.submission2annisa

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.submission2annisa.databinding.FragmentFollowerBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray


class FollowerFragment : Fragment() {

    companion object{
        const val EXTRA_USERS = "extra_users"
        private  val ARG_USERNAME="username"
        fun newInstance(username: String?): FollowerFragment {
            val fragment = FollowerFragment()
            val bundel = Bundle()
            bundel.putString(ARG_USERNAME, username)
            fragment.arguments = bundel
            return fragment
        }
    }

    private lateinit var binding: FragmentFollowerBinding
    private lateinit var followadapter  : FollowerAdapter
    private var listfollow : ArrayList<AvatarData> = ArrayList()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followadapter = FollowerAdapter(listfollow)

        val username = arguments?.getString(ARG_USERNAME)
        rvconfig()
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
                                AvatarData(
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
        binding.rvfollower.layoutManager = LinearLayoutManager(activity)
        binding.rvfollower.setHasFixedSize(true)
        val dataadapter = FollowerAdapter(listfollow)
        binding.rvfollower.adapter = followadapter

        dataadapter.setOnItemClickCallback(object :
                FollowerAdapter.OnItemClickCallback {
            override fun onItemClicked(UserData: AvatarData) {

            }
        })
    }
    private fun setRv(){
        binding.rvfollower.layoutManager = LinearLayoutManager(activity)
        binding.rvfollower.setHasFixedSize(true)
        val dataadapter = FollowerAdapter(listfollow)
        binding.rvfollower.adapter = followadapter

        dataadapter.setOnItemClickCallback(object :
                FollowerAdapter.OnItemClickCallback {
            override fun onItemClicked(UserData: AvatarData) {

            }
        })

    }


}