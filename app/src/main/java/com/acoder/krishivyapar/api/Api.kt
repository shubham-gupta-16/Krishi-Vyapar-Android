package com.acoder.krishivyapar.api

import android.util.Log
import org.json.JSONObject

class Api {
    companion object {
        private const val baseUrl = "https://7e95-2405-204-a425-eed7-94f3-eeb6-32b3-2ffd.ngrok.io" +
                "/krishi-vyapar-server/"
        const val USER_NOT_EXIST = 44
        const val INVALID_TOKEN = 41

        fun userExist(mobile: String): APISystem<(name: String, uid: String) -> Unit> {
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

        fun verifyUser(apiData: ApiData): APISystem<(name: String?) -> Unit> {
            val apiSys = APISystem<(name: String?) -> Unit>(
                authRequestBuilder(apiData,"auth/verify_user.php")
            ) { obj, successCallback ->
                if (successCallback == null) return@APISystem
                val name = obj.tryString("name");
                apiData.updateName(name)
                successCallback(name)
            }
            return apiSys
        }


        fun signUp(apiData: ApiData, mobile: String, uid: String): APISystem<(name: String?) -> Unit> {
            val apiSys = APISystem<(name: String?) -> Unit>(
                requestBuilder("auth/signup.php").addPost("mobile", mobile).addPost("uid", uid)
            ) { obj, successCallback ->
                if (successCallback == null) return@APISystem
                val token = obj.optString("token");

                val name = obj.tryString("name");
                Log.d("TAG", "signUp: $name")
                apiData.localSignUp(token, name)
                successCallback(name)
            }
            return apiSys
        }

        fun updateName(apiData: ApiData, newName:String): APISystem<()->Unit>{
            val apiSys = APISystem<() -> Unit>(
                authRequestBuilder(apiData,"auth/update_name.php").addPost("name", newName)
            ) { _, successCallback ->
                if (successCallback == null) return@APISystem
                successCallback()
            }
            return apiSys
        }

        private fun JSONObject.tryString(key: String): String? {
            val value = this.optString(key)
            if (value === "null") return null
            return value
        }

        private fun requestBuilder(path: String): EasyNetwork.Builder {
            return authRequestBuilder(null, path)
        }

        private fun authRequestBuilder(apiData: ApiData?, path: String): EasyNetwork.Builder {
            val builder = EasyNetwork.Builder(baseUrl + path)
    if (apiData != null) builder.addHeader("Auth", apiData.getToken())
            return builder
        }
    }
}

