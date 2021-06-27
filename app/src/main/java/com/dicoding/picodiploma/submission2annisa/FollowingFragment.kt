package com.dicoding.picodiploma.submission2annisa

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.submission2annisa.databinding.FragmentFollowingBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception


class FollowingFragment : Fragment() {

    companion object{

        private  val ARG_USERNAME="username"
        fun newInstance(username: String?): FollowingFragment {
            val fragment = FollowingFragment()
            val bundel = Bundle()
            bundel.putString(ARG_USERNAME, username)
            fragment.arguments = bundel
            return fragment
        }


        const val EXTRA_USERS = "extra_users"

    }

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var Followingadapter  : FollowingAdapter
    private val listtfollowing : ArrayList<AvatarData> = ArrayList()


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Followingadapter = FollowingAdapter(listtfollowing)
        val username = arguments?.getString(ARG_USERNAME)

        rvconfig()
        getFollowing(username.toString())
    }
    private fun getFollowing(query: String) {
        binding.progressbar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")

        val url = "https://api.github.com/users/$query/following"
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
                        listtfollowing.add(
                                AvatarData(
                                        avatars,
                                        Name
                                        )
                        )
                        setRv()
                        //Toast.makeText(context, "Apakah folllowing: "+Name, Toast.LENGTH_LONG).show()

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
        binding.rvfollowing.layoutManager=LinearLayoutManager(binding.rvfollowing.context)
        binding.rvfollowing.setHasFixedSize(true)
        binding.rvfollowing.addItemDecoration(
                DividerItemDecoration(
                        binding.rvfollowing.context,
                        DividerItemDecoration.VERTICAL
                )
        )
    }
    private fun setRv(){
        binding.rvfollowing.layoutManager = LinearLayoutManager(activity)
        binding.rvfollowing.setHasFixedSize(true)
        val dataadapter = FollowingAdapter(listfollowing)
        binding.rvfollowing.adapter = Followingadapter

        dataadapter.setOnItemClickCallback(object :
                FollowingAdapter.OnItemClickCallback {
            override fun onItemClicked(UserData: AvatarData) {

            }
        })

    }


}