package com.acoder.krishivyapar.api

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.androidnetworking.interfaces.UploadProgressListener
import org.json.JSONException
import org.json.JSONObject


class APISystem<T>(
    var builder: EasyNetwork.Builder,
    var jCall: (JSONObject, successCallback: T?) -> Unit
) {

    var uploadProgressListener: UploadProgressListener? = null;
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

    fun setUploadProgressListener(listener: (byteUploaded: Long, totalBytes: Long) -> Unit): APISystem<T> {
        uploadProgressListener = UploadProgressListener { bytesUploaded, totalBytes ->
            Log.d("tagtag", "$bytesUploaded / $totalBytes")
            listener(bytesUploaded, totalBytes)
        }
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
        errorCallback: ((code: Int, message: String) -> Unit)?,
        responseListener: (JSONObject) -> Unit
    ) {
        val b = builder.build()
        if (uploadProgressListener != null)
            b.uploadProgressListener = uploadProgressListener
        b.getAsString(object : StringRequestListener {
            override fun onResponse(response: String) {
                Log.i("apiTag", response)
                try {
                    val obj = JSONObject(response)
                    val statusCode = obj.getInt("status")
                    Handler(Looper.getMainLooper()).post {
                        if (statusCode == statusCodeOk) {
                            responseListener(obj)
                        } else if (errorCallback != null)
                            errorCallback(statusCode, obj.optString("message", "no message"))
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                    if (errorCallback != null)
                        Handler(Looper.getMainLooper()).post {
                            errorCallback(jsonErrorCode, "Json status not available")
                        }

                }
            }

            override fun onError(anError: ANError) {
                if (errorCallback != null)
                    Handler(Looper.getMainLooper()).post {
                        errorCallback(
                            anError.errorCode,
                            if (anError.message != null) anError.message!! else ""
                        )
                    }
                if (anError.message != null)
                    Log.i("errorTag", anError.message.toString())

            }
        })
    }
}