package com.example.newsapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.bottomNav.menu.getItem(0).isCheckable = true
        addFragment(ImagesFragment())
        binding.bottomNav.setOnNavigationItemSelectedListener {menu ->

            when(menu.itemId){

                R.id.news -> {
                    addFragment(ImagesFragment())
                    true
                }

                R.id.bookmark -> {
                    addFragment(BookmarkFragment())
                    true
                }
                else -> false
            }
        }

    }

    private fun addFragment(fr : Fragment) {
        replaceFragment(
            fr, R.id.fragment_container
        )
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}