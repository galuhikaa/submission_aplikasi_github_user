package com.dicoding.submissionawalaplikasigithubuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.dicoding.submissionawalaplikasigithubuser.bookmark.BookmarkActivity
import com.dicoding.submissionawalaplikasigithubuser.data.response.ApiResponse
import com.dicoding.submissionawalaplikasigithubuser.data.response.GitHubResponse
import com.dicoding.submissionawalaplikasigithubuser.data.retrofit.ApiConfig
import com.dicoding.submissionawalaplikasigithubuser.databinding.ActivityMainBinding
import com.dicoding.submissionawalaplikasigithubuser.theme.SettingPreferences
import com.dicoding.submissionawalaplikasigithubuser.theme.ThemeActivity
import com.dicoding.submissionawalaplikasigithubuser.theme.ThemeViewModel
import com.dicoding.submissionawalaplikasigithubuser.theme.ViewModelFactory
import com.dicoding.submissionawalaplikasigithubuser.theme.dataStore
import com.dicoding.submissionawalaplikasigithubuser.ui.GithubAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var useradapter: GithubAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        useradapter = GithubAdapter(this, arrayListOf())
        binding.githubUser.setHasFixedSize(false)
        binding.githubUser.adapter = useradapter

        getUser()

        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            ThemeViewModel::class.java
        )
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    val query = searchView.text.toString()
                    searching(query)
                    false
                }
        }
    }

    //menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bookmark -> {
                val intentBookmark = Intent(this@MainActivity, BookmarkActivity::class.java)
                startActivity(intentBookmark)
                return true
            }R.id.themes -> {
                val intentTheme = Intent(this@MainActivity, ThemeActivity::class.java)
                startActivity(intentTheme)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun searching(query: String){
        showLoading(true)
        ApiConfig.apiService.getSearch(query).enqueue(object : Callback<GitHubResponse>{
            override fun onResponse(
                call: Call<GitHubResponse>,
                response: Response<GitHubResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val searchResults = response.body()?.items as ArrayList<ApiResponse>
                    if (searchResults != null) {
                        setDataAdapter(searchResults)
                    }
                }
            }
            override fun onFailure(call: Call<GitHubResponse>, t: Throwable) {
                showLoading(false)
                Log.d("error", ""+t.stackTraceToString())
            }
        })
    }

    fun getUser(){
        showLoading(true)
        ApiConfig.apiService.getGithub().enqueue(object : Callback<ArrayList<ApiResponse>>{
            override fun onResponse(
                call: Call<ArrayList<ApiResponse>>,
                response: Response<ArrayList<ApiResponse>>
            ){
                showLoading(false)
                if(response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        setDataAdapter(data)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<ApiResponse>>, t: Throwable) {
                showLoading(false)
                Log.d("error", ""+t.stackTraceToString())
            }
        })
    }

    fun setDataAdapter(data: ArrayList<ApiResponse>) {
        useradapter.setData(data)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
