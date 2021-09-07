package com.acoder.krishivyapar.api

import android.util.Log
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import org.json.JSONException
import org.json.JSONObject


class APISystem<T>(
    var builder: EasyNetwork.Builder,
    var jCall: (JSONObject, successCallback: T?) -> Unit
) {

    var successCallback: T? = null;
    var errorCallback: ((code: Int, message: String) -> Unit)? = null;

    fun atSuccess(successCallback: T): APISystem<T> {
        this.successCallback = successCallback
        return this
    }

    fun atError(errorCallback: (code: Int, message: String) -> Unit): APISystem<T> {
        this.errorCallback = errorCallback
        return this
    }

    fun execute() {
        getResponseFromAPI(
            builder, errorCallback
        ) { obj ->
            jCall(obj, successCallback)
        }
    }

    private val statusCodeOk = 200
    private val jsonErrorCode = 978

    private fun getResponseFromAPI(
        builder: EasyNetwork.Builder,
        callback: ((code: Int, message: String) -> Unit)?,
        responseListener: (JSONObject) -> Unit
    ) {
        builder.build().getAsString(object : StringRequestListener {
            override fun onResponse(response: String) {
                Log.i("apiTag", response)
                try {
                    val obj = JSONObject(response)
                    val statusCode = obj.getInt("status")
                    if (statusCode == statusCodeOk) {
                        responseListener(obj)
                    } else if (callback != null)
                        callback(statusCode, obj.optString("message", "no message"))

                } catch (e: JSONException) {
                    e.printStackTrace()
                    if (callback != null)
                        callback(jsonErrorCode, "Json status not available")

                }
            }

            override fun onError(anError: ANError) {
                Log.i("errorTag", anError.message!!)
                if (callback != null)
                    callback(
                        anError.errorCode,
                        if (anError.message != null) anError.message!! else ""
                    )
            }
        })
    }
}