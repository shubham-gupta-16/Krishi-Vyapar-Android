package com.acoder.krishivyapar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.acoder.krishivyapar.adapters.SliderAdapter
import com.acoder.krishivyapar.api.Api
import com.acoder.krishivyapar.api.BaseApi
import com.acoder.krishivyapar.databinding.ActivityFullAdViewBinding
import com.acoder.krishivyapar.models.AdModel
import com.acoder.krishivyapar.models.ImageModel
import com.bumptech.glide.Glide
import com.shubhamgupta16.materialkit.AnimUtils
import java.util.ArrayList
import kotlin.math.roundToInt

class FullAdViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullAdViewBinding
    private lateinit var api: Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullAdViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        api = Api(this)

        binding.toolbar.setNavigationOnClickListener{
            finish()
        }
        fetchPost()
    }

    private fun fetchPost(){
        binding.loadingLayout.v.visibility = View.VISIBLE
        val id = intent.getIntExtra("id", 0)
        if (id == 0) return
        api.requestAdById(id).atSuccess { adModel ->
            if (isDestroyed) return@atSuccess
            updateMainInfoUI(adModel)
            updateImagesUI(adModel.images)
            AnimUtils.fadeHideView(binding.loadingLayout.v)
        }.execute()
    }

    @SuppressLint("SetTextI18n")
    private fun updateMainInfoUI(model: AdModel){
        binding.price.text = model.formatPrice(model.price)
        binding.subPrice.text = "${model.quantity.roundToInt()} ${model.unit} - ${model.formatPrice((model.price/model.quantity).roundToInt().toFloat())} per ${model.unit}"
        binding.title.text = model.title
        binding.locText.text = model.location.getTrimmed()
        Glide.with(this).load(model.user.profileUrl).error(R.drawable.placeholder_user).placeholder(R.drawable.placeholder_user).into(binding.userIcon)
        binding.userName.text = model.user.name
        binding.userMobile.text = model.user.mobile
        if (model.description != null && model.description!!.isNotEmpty()){
            binding.descriptionLayout.visibility = View.VISIBLE
            binding.descriptionText.text = model.description
        } else
            binding.descriptionLayout.visibility = View.GONE
    }

    private fun updateImagesUI(imageArr: List<ImageModel?>){
        if (imageArr.isEmpty()) return
        val baseImageUrl = BaseApi.getImagesUrl(this);
        val thumbURL = baseImageUrl + imageArr[0]?.getPath("360")
        val slideList = ArrayList<String>()

        for (image in imageArr) {
            slideList.add(baseImageUrl + image?.getPath())
        }
        val adapter = SliderAdapter(this, slideList, false, true)
        binding.slidePager.adapter = adapter
        binding.slideShowIndicator.applyIndicator(binding.slidePager, adapter, false)
        adapter.notifyDataSetChanged()
//        Glide.with(this).load(thumbURL).into(binding.thumb)
    }
}