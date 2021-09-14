package com.acoder.krishivyapar.api

import android.content.Context
import android.util.Log
import com.acoder.krishivyapar.models.LocationModel
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONObject
import kotlin.math.log

class Api(context: Context) : ApiData(context) {

    companion object {
        const val USER_NOT_EXIST = 44
        const val INVALID_TOKEN = 41
    }

    fun fetchLocations(keyword: String): APISystem<(locationList: ArrayList<LocationModel>) -> Unit> {
        val apiSys = APISystem<(list: ArrayList<LocationModel>) -> Unit>(
            requestBuilder("location/index.php").addPost("q", keyword)
        ) { obj, successCallback ->
            if (successCallback == null) return@APISystem
            val list = ArrayList<LocationModel>();
            val jArr = obj.getJSONArray("locations")
            for (i in 0 until jArr.length()){
                val each = jArr.getJSONObject(i)
                val locality = each.optString("locality")
                val city = each.optString("city")
                val state = each.optString("state")
                val lat = each.optDouble("lat")
                val lng = each.optDouble("lng")
                list.add(LocationModel("$locality, $city, $state", lat.toFloat(), lng.toFloat()))
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

    fun requestVerifyUser(): APISystem<(name: String?) -> Unit> {
        val apiSys = APISystem<(name: String?) -> Unit>(
            authRequestBuilder("auth/verify_user.php")
        ) { obj, successCallback ->
            if (successCallback == null) return@APISystem
            val name = obj.tryString("name");
            updateName(name)
            successCallback(name)
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
            localSignUp(token, name)
            successCallback(name)
        }
        return apiSys
    }

    fun requestUpdateName(newName: String): APISystem<() -> Unit> {
        val apiSys = APISystem<() -> Unit>(
            authRequestBuilder("auth/update_name.php").addPost("name", newName)
        ) { _, successCallback ->
            if (successCallback == null) return@APISystem
            successCallback()
        }
        return apiSys
    }

    fun reFetchBaseUrl(path: String = "") {
        EasyNetwork.Builder("https://onetakego.com/acoder/url_helper?id=2").build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    if (response == null) return
                    if (response.optInt("status") != 0) return
                    setBaseUrl(response.optString("url") + path)
                }

                override fun onError(anError: ANError?) {
                }

            })
    }
}

