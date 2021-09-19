package com.acoder.krishivyapar.fragments.add

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.acoder.krishivyapar.adapters.ImagesRecyclerAdapter
import com.acoder.krishivyapar.databinding.FragmentImagesBinding


class ImagesFragment : Fragment() {

    private lateinit var binding: FragmentImagesBinding
    private var listener: ((images: Map<String, Int>) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized)
            binding = FragmentImagesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = getAllShownImagesPath(requireActivity())
        Toast.makeText(context, "${list.size}", Toast.LENGTH_SHORT).show()
        val adapter = ImagesRecyclerAdapter(requireContext(), list)
        binding.imageRecycler.adapter = adapter
        binding.imageRecycler.itemAnimator = null
        binding.imageRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.nextButton.setOnClickListener {
            val imageList = adapter.getImages()
            if (imageList.isNotEmpty())
                listener?.invoke(imageList)
        }
    }


    private fun getAllShownImagesPath(activity: Activity): MutableList<Uri> {
        val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val listOfAllImages: MutableList<Uri> = mutableListOf()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val cursor = activity.contentResolver.query(uriExternal, projection, null, null, MediaStore.Images.Media.DATE_ADDED + " DESC")
        if (cursor != null) {
            val columnIndexID: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val imageId: Long = cursor.getLong(columnIndexID)
                val uriImage = Uri.withAppendedPath(uriExternal, "" + imageId)
                listOfAllImages.add(uriImage)
            }
            cursor.close()
        }
        return listOfAllImages
    }

    fun setOnNextClickListener(listener: (images: Map<String, Int>) -> Unit) {
        this.listener = listener
    }


}