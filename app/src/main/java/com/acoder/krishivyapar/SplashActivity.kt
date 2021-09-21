package com.acoder.krishivyapar

import android.content.Intent
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.acoder.krishivyapar.api.Api
import com.acoder.krishivyapar.databinding.ActivitySplashBinding
import java.lang.reflect.Method
import java.util.*
import android.net.Uri

import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val api = Api(this)


        if (!api.hasCurrentUser()) {
            Thread.sleep(2000)
            goToSignUpPage()
        } else {
            fetchBase(api)
        }
    }

    private fun fetchBase(api: Api){
        api.requestLoadBase(this).atSuccess { name ->
            if (name == null)
                startActivity(Intent(this, AuthNameRegisterActivity::class.java))
            else
                startActivity(Intent(this, MainActivity::class.java))
            finish()
        }.atError { code, message ->
            if (code == Api.INVALID_TOKEN || code == Api.USER_NOT_EXIST) {
                api.logout()
                goToSignUpPage()
                return@atError
            } else if (code == 404)
            api.reFetchBaseUrl("/krishi-vyapar-php/") {
                fetchBase(api)
            } else
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }.execute()
    }

    private fun goToSignUpPage(){
        startActivity(Intent(this, AuthSignUpActivity::class.java))
        finish()
    }



    private fun checkSystemWritePermission(): Boolean {
        var retVal = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(this)
            Log.d("MainActivity", "Can Write Settings: $retVal")
            if (retVal) {
                Toast.makeText(this, "Write allowed :-)", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Write not allowed :-(", Toast.LENGTH_LONG).show()
                openAndroidPermissionsMenu()
            }
        }
        return retVal
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun openAndroidPermissionsMenu() {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.data = Uri.parse("package:" + this.packageName)
        startActivity(intent)
    }

    fun setMobileDataState(mobileDataEnabled: Boolean) {
        try {
            val telephonyService =
                getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val setMobileDataEnabledMethod: Method =
                Objects.requireNonNull(telephonyService).javaClass.getDeclaredMethod(
                    "setDataEnabled",
                    Boolean::class.javaPrimitiveType
                )
            setMobileDataEnabledMethod.invoke(telephonyService, mobileDataEnabled)
        } catch (ex: Exception) {
            Log.e("MainActivity", "Error setting mobile data state", ex)
        }
    }

    fun getMobileDataState(): Boolean {
        try {
            val telephonyService = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val getMobileDataEnabledMethod: Method =
                Objects.requireNonNull(telephonyService).javaClass
                    .getDeclaredMethod("getDataEnabled")
            return getMobileDataEnabledMethod.invoke(telephonyService) as Boolean
        } catch (ex: Exception) {
            Log.e("MainActivity", "Error getting mobile data state", ex)
        }
        return false
    }
}