package com.acoder.krishivyapar.api

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import com.acoder.krishivyapar.models.AdListModel
import com.acoder.krishivyapar.models.AdModel
import com.acoder.krishivyapar.models.LocationModel
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONObject
import java.io.File


class Api(context: Context) : ApiData(context) {

    companion object {
        const val USER_NOT_EXIST = 44
        const val INVALID_TOKEN = 41
    }

    fun mediaUriToFilePath(context: Context, _uri: Uri): String? {
        var filePath: String?
        Log.d("erer", "URI = $_uri")
        if ("content" == _uri.scheme) {
            val cursor: Cursor? = context.contentResolver
                .query(_uri, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null)
            cursor?.moveToFirst()
            filePath = cursor?.getString(0)
            cursor?.close()
        } else {
            filePath = _uri.path
        }
        return filePath
    }

    fun requestUploadAd(context: Context, adMap: HashMap<String, String>, images: Map<String, Int>):
            APISystem<(String?) -> Unit> {
        val api = Api(context)
        val map = hashMapOf<String, File>()
        for ((key, value) in images) {
            val path = mediaUriToFilePath(context, key.toUri());
            if (path != null) {
                val file = File(path)
                map["image_$value"] = file
            }
        }

        val apiSys = APISystem<(name: String?) -> Unit>(
            authRequestBuilder("post_ad.php")
                .addPosts(adMap)
                .addPost("locText", api.getLocation()?.locText)
                .addPost("lat", api.getLocation()?.lat.toString())
                .addPost("lng", api.getLocation()?.lng.toString())
                .addFiles(map)
        ) { obj, successCallback ->
            if (successCallback == null) return@APISystem
            successCallback("name")
        }
        return apiSys
    }

    fun requestLoadBase(context: Context): APISystem<(name: String?) -> Unit> {
        val categoryManager = CategoryManager(context)
        val apiSys = APISystem<(name: String?) -> Unit>(
            authRequestBuilder("base.php")
                .addPost("categoryVersion", categoryManager.getCategoryVersion().toString())
                .addPost("subCategoryVersion", categoryManager.getSubCategoryVersion().toString())
        ) { obj, successCallback ->
            if (successCallback == null) return@APISystem
            val categoryVersion = obj.getInt("categoryVersion");
            val subCategoryVersion = obj.getInt("subCategoryVersion");
            if (categoryVersion > categoryManager.getCategoryVersion())
                categoryManager.initializeCategories(categoryVersion, obj.getJSONArray("category"))
            if (subCategoryVersion > categoryManager.getSubCategoryVersion())
                categoryManager.initializeSubCategories(
                    subCategoryVersion,
                    obj.getJSONArray("subCategory")
                )
            val name = obj.tryString("name");
            updateName(name)
            successCallback(name)
        }
        return apiSys
    }

    fun requestAdById(id: Int): APISystem<(adModel: AdModel) -> Unit> {
        val apiSys = APISystem<(adModel: AdModel) -> Unit>(
            authRequestBuilder("post.php")
                .addPost("id", "$id")
        ) { obj, successCallback ->
            if (successCallback == null) return@APISystem
            val adModel = AdModel.newInstance(obj.getJSONObject("post"))
            successCallback(adModel)
        }
        return apiSys
    }

    fun requestAds(
        page: Int,
        query: String?
    ): APISystem<(locationList: ArrayList<AdListModel>, maxPage:Int, page:Int) -> Unit> {
        val location = getLocation()
        val lat = location?.lat ?: 0;
        val lng = location?.lng ?: 0;
        val requestBuilder = authRequestBuilder("post_list.php")
            .addPost("page", page.toString())
            .addPost("lat", lat.toString())
            .addPost("lng", lng.toString())
            .addPost("type", "0")
        if (query != null)
            requestBuilder.addPost("q", query)
        val apiSys = APISystem<(list: ArrayList<AdListModel>, maxPage:Int, page:Int) -> Unit>(requestBuilder)
        { obj, successCallback ->
            if (successCallback == null) return@APISystem
            val list = ArrayList<AdListModel>();
            val jArr = obj.getJSONArray("posts")
            val maxPage = obj.getInt("maxPage")
            for (i in 0 until jArr.length()) {
                val posts = jArr.getJSONObject(i)
                list.add(AdListModel.newInstance(posts))
            }
            successCallback(list, maxPage, page)
        }
        return apiSys
    }

    fun fetchLocations(keyword: String): APISystem<(locationList: ArrayList<LocationModel>) -> Unit> {
        val apiSys = APISystem<(list: ArrayList<LocationModel>) -> Unit>(
            authRequestBuilder("location/index.php").addPost("q", keyword)
        ) { obj, successCallback ->
            if (successCallback == null) return@APISystem
            val list = ArrayList<LocationModel>();
            val jArr = obj.getJSONArray("locations")
            for (i in 0 until jArr.length()) {
                val each = jArr.getJSONObject(i)
                list.add(parseLocation(each))
            }
            successCallback(list)
        }
        return apiSys
    }

    fun requestUserExist(mobile: String): APISystem<(name: String, uid: String) -> Unit> {
        val apiSys = APISystem<(name: String, uid: String) -> Unit>(
            requestBuilder("auth/exist.php").addPost("mobile", mobile)
        ) { obj, successCallback ->
            if (successCallback == null) return@APISystem
            val name = obj.optString("name");
            val uid = obj.optString("uid");
            successCallback(name, uid)
        }
        return apiSys
    }
    fun requestSignUp(mobile: String, uid: String): APISystem<(name: String?) -> Unit> {
        val apiSys = APISystem<(name: String?) -> Unit>(
            requestBuilder("auth/signup.php").addPost("mobile", mobile).addPost("uid", uid)
        ) { obj, successCallback ->
            if (successCallback == null) return@APISystem
            val token = obj.optString("token");

            val name = obj.tryString("name");
            Log.d("TAG", "signUp: $name")
            localSignUp(token, name, mobile)
            successCallback(name)
        }
        return apiSys
    }

    fun requestUpdateName(newName: String): APISystem<() -> Unit> {
        val apiSys = APISystem<() -> Unit>(
            authRequestBuilder("auth/update_name.php").addPost("name", newName)
        ) { _, successCallback ->
            if (successCallback == null) return@APISystem
            updateName(newName)
            successCallback()
        }
        return apiSys
    }

    fun reFetchBaseUrl(path: String = "", listener: () -> Unit) {
        EasyNetwork.Builder("https://onetakego.com/acoder/url_helper?id=2").build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response == null) return
                    if (response.optInt("status") != 0) return
                    val baseUrl = response.optString("url")
                    if ("$baseUrl$path" != BaseApi.getUrl(context)) {
                        Log.d("base", baseUrl + path)
                        BaseApi.setUrl(context, baseUrl + path)
                        listener()
                    }
                }

                override fun onError(anError: ANError?) {
                }

            })
    }
}

