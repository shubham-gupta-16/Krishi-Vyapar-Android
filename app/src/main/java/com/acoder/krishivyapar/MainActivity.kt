package com.acoder.krishivyapar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.acoder.krishivyapar.fragments.MarketFragment
import com.acoder.krishivyapar.fragments.RequestsFragment
import com.acoder.krishivyapar.views.MyViewPagerAdapter

import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager2 = findViewById<ViewPager2>(R.id.viewpager2);

        viewPager2.isUserInputEnabled = false
        viewPager2.setPageTransformer { page, _ ->
            page.alpha = 0f;
            page.visibility = View.VISIBLE;

            // Start Animation for a short period of time
            page.animate()
                .alpha(1f).duration =
                page.resources.getInteger(android.R.integer.config_shortAnimTime).toLong();
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when(item.itemId){
                R.id.action_market -> viewPager2.setCurrentItem(0, false)
                R.id.action_request -> viewPager2.setCurrentItem(1, false)
            }
            return@setOnItemSelectedListener false;
        }

        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                when(position){
                    0-> bottomNavigationView.menu.findItem(R.id.action_market).isChecked = true
                    1-> bottomNavigationView.menu.findItem(R.id.action_request).isChecked = true
                }
            }
        })

        setupViewPager(viewPager2);
    }

    private fun setupViewPager(viewPager2: ViewPager2) {
        val adapter = MyViewPagerAdapter(supportFragmentManager, lifecycle)

        adapter.addFragment(MarketFragment())
        adapter.addFragment(RequestsFragment())

        viewPager2.adapter = adapter
    }
}