package com.acoder.krishivyapar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.acoder.krishivyapar.adapters.MyViewPagerAdapter
import com.acoder.krishivyapar.api.ApiData
import com.acoder.krishivyapar.databinding.ActivityMainBinding
import com.acoder.krishivyapar.fragments.main.HomeFragment
import com.acoder.krishivyapar.fragments.main.MarketFragment
import com.acoder.krishivyapar.fragments.main.RequestsFragment
import com.acoder.krishivyapar.utils.LocaleHelper
import com.acoder.krishivyapar.utils.setEndListener

class MainActivity : AppCompatActivity() {

    private lateinit var apiData: ApiData
    private lateinit var binding: ActivityMainBinding

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiData = ApiData(this)

//        val auth = FirebaseAuth.getInstance()
//        if (auth.currentUser == null)
        if (!apiData.hasCurrentUser()) {
            startActivity(Intent(this, AuthSignUpActivity::class.java))
            finish()
            return
        }

        setupViews()

//        ********** badges code **************
//        val badge = binding.bottomNavigation.getOrCreateBadge(R.id.action_home)
//        badge.isVisible = true
//        badge.number = 99
    }

    private fun logout(){
        apiData.logout()
        startActivity(Intent(this, AuthSignUpActivity::class.java))
        finish()
    }

    private fun setupViews() {
//        bottomNavigation Item Select
//        binding.bottomNavigation.itemIconTintList = null
        binding.bottomNavigation.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_home -> binding.viewpager2.setCurrentItem(0, false)
                R.id.action_chat -> binding.viewpager2.setCurrentItem(1, false)
                R.id.action_favorite -> binding.viewpager2.setCurrentItem(2, false)
                R.id.action_account -> item.isChecked = true
//                todo show account fragment instead
            }
            return@setOnItemSelectedListener false;
        }

        setupViewPager(binding.viewpager2)
        setupFabButton()

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
                    1 -> binding.bottomNavigation.menu.findItem(R.id.action_chat).isChecked = true
                    2 -> binding.bottomNavigation.menu.findItem(R.id.action_favorite).isChecked =
                        true
                }
            }
        })

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

    private var isFabCardVisible = false
    private fun setupFabButton() {
        val scale = 0.9f;
        animateFabCard(0f, scale, scale, 50f, 1)
        binding.fab.setOnClickListener {
            if (isFabCardVisible)
                animateFabCard(0f, scale, scale, 50f, 200)
            else
                animateFabCard(1f, 1f, 1f, 0f, 200)
        }

        binding.addNewAd.setOnClickListener {
            startActivity(Intent(this, AddNewAdActivity::class.java))
            // TODO: 9/17/2021 improve performance by adding this onStart with no anim
            animateFabCard(0f, scale, scale, 50f, 200)
        }
    }

    private fun animateFabCard(alpha: Float, x: Float, y: Float, transY: Float, duration: Long) {
        isFabCardVisible = alpha != 0f
        if (alpha == 1f){
            binding.fabCard.visibility = View.VISIBLE
            binding.fabCard.alpha = 0f
            binding.fabIcon.animate().rotation(45f).setDuration(100).start()
        } else
            binding.fabIcon.animate().rotation(0f).setDuration(100).start()
        binding.fab.isClickable = false
        binding.fabCard.animate()
            .alpha(alpha)
            .scaleX(x)
            .scaleY(y)
            .setInterpolator(DecelerateInterpolator())
            .translationY(transY)
            .setStartDelay(50)
            .setDuration(duration)
            .setEndListener {
                if (alpha == 0f)
                    binding.fabCard.visibility = View.GONE
                binding.fab.isClickable = true
            }
            .start()

        val anim = AnimationUtils.loadAnimation(this, R.anim.click_anim)
        binding.fab.startAnimation(anim)

    }

}