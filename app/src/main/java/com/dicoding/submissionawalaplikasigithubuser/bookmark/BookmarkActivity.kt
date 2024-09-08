package com.dicoding.submissionawalaplikasigithubuser.bookmark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissionawalaplikasigithubuser.databinding.ActivityBookmarkBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookmarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getList()
    }
    
    private fun getList() {
        CoroutineScope(Dispatchers.IO).launch {
            val userDao = UserDb(this@BookmarkActivity).userDao()
            val liveDataUsers: LiveData<List<EntityUser>> = userDao.loadAll()

            withContext(Dispatchers.Main) {
                liveDataUsers.observe(this@BookmarkActivity, Observer { users ->
                    binding.listBookmark.apply {
                        layoutManager = LinearLayoutManager(this@BookmarkActivity)
                        adapter = BookmarkAdapter().apply {
                            setData(users)
                        }
                    }
                })
            }
        }
    }
}