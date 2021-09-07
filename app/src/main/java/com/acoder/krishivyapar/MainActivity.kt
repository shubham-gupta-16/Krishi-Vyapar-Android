package com.acoder.krishivyapar

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.acoder.krishivyapar.api.ApiData
import com.acoder.krishivyapar.databinding.ActivityMainBinding
import com.acoder.krishivyapar.fragments.HomeFragment
import com.acoder.krishivyapar.fragments.MarketFragment
import com.acoder.krishivyapar.fragments.RequestsFragment
import com.acoder.krishivyapar.views.MyViewPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var apiData: ApiData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiData = ApiData(this)

//        val auth = FirebaseAuth.getInstance()
//        if (auth.currentUser == null)
        if (!apiData.hasCurrentUser()) {
            startActivity(Intent(this, AuthSignUpActivity::class.java))
            finish()
            return
        }

        setupViews(binding)

        val badge = binding.bottomNavigation.getOrCreateBadge(R.id.action_home)
        badge.isVisible = true
        badge.number = 99
    }

    private fun setupViews(binding: ActivityMainBinding) {
//        bottomNavigation Item Select
        binding.bottomNavigation.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_home -> binding.viewpager2.setCurrentItem(0, false)
                R.id.action_market -> binding.viewpager2.setCurrentItem(1, false)
                R.id.action_request -> binding.viewpager2.setCurrentItem(2, false)
                R.id.action_account -> logout()
            }
            return@setOnItemSelectedListener false;
        }

        setupViewPager(binding.viewpager2)

//        onPageChanged at viewPager2
        binding.viewpager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                when (position) {
                    0 -> binding.bottomNavigation.menu.findItem(R.id.action_home).isChecked = true
                    1 -> binding.bottomNavigation.menu.findItem(R.id.action_market).isChecked = true
                    2 -> binding.bottomNavigation.menu.findItem(R.id.action_request).isChecked =
                        true
                }
            }
        })

    }

    private fun logout(){
        apiData.logout()
        startActivity(Intent(this, AuthSignUpActivity::class.java))
        finish()
    }

    private fun setupViewPager(viewPager2: ViewPager2) {
//        applying fragments by using adapter
        val adapter = MyViewPagerAdapter(supportFragmentManager, lifecycle)
        adapter.addFragment(HomeFragment())
        adapter.addFragment(MarketFragment())
        adapter.addFragment(RequestsFragment())
        viewPager2.adapter = adapter

//        disable viewpager swipe
        viewPager2.isUserInputEnabled = false
//        custom fade animation on page change
        viewPager2.setPageTransformer { page, _ ->
            page.alpha = 0f;
            page.visibility = View.VISIBLE;

            // Start Animation for a short period of time
            page.animate()
                .alpha(1f).duration =
                page.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        }
    }
}