package com.dicoding.submissionawalaplikasigithubuser.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.submissionawalaplikasigithubuser.data.response.ApiResponse
import com.dicoding.submissionawalaplikasigithubuser.data.retrofit.ApiConfig
import com.dicoding.submissionawalaplikasigithubuser.databinding.FragmentFollowerBinding
import com.dicoding.submissionawalaplikasigithubuser.ui.GithubAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FollowerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowerFragment : Fragment() {
    private lateinit var fAdapter: GithubAdapter
    private lateinit var _binding: FragmentFollowerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowerBinding.inflate(layoutInflater, container, false)

        showLoading(true)
        val data = arguments?.getString("user")
        fAdapter = GithubAdapter(requireContext(), arrayListOf())

        val recyclerView = _binding.followerlist
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = fAdapter

        getFollower(data!!)
        return _binding.root
    }

    fun getFollower(data : String){
        ApiConfig.apiService.getFollower(data).enqueue(object : Callback<ArrayList<ApiResponse>> {
            override fun onResponse(
                call: Call<ArrayList<ApiResponse>>,
                response: Response<ArrayList<ApiResponse>>
            ){
                if(response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        fAdapter.setData(data)
                    }
                }
                showLoading(false)
            }
            override fun onFailure(call: Call<ArrayList<ApiResponse>>, t: Throwable) {
                Log.d("error", ""+t.stackTraceToString())
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            _binding.progresfollower.visibility = View.VISIBLE
        } else {
            _binding.progresfollower.visibility = View.GONE
        }
    }
}