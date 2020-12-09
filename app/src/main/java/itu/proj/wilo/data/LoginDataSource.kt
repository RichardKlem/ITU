package itu.proj.wilo.data

import android.content.Context
import android.os.Environment
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import itu.proj.wilo.data.model.LoggedInUser
import itu.proj.wilo.ui.login.LoginActivity
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.io.PrintWriter


class ResponseResult {
    var data = ""
}
private fun onResponse(activity: LoginActivity): (response: JSONObject) -> Unit {
    return { response ->
        try {
            val fileName = "cookie.txt"

            activity.binding.root.context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
                output.write(response.toString().toByteArray())
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(val activity: LoginActivity) {
    fun login(email: String, password: String): Result<LoggedInUser> {
        val result = ResponseResult()
        try {
            val url = "http://xklemr00.pythonanywhere.com/login"
            // Initialize a new RequestQueue instance
            val requestQueue = Volley.newRequestQueue(activity.binding.root.context)

            val postData = JSONObject()
            postData.put("email", email)
            postData.put("password", password)

            // Initialize a new JsonArrayRequest instance
            val jsonArrayRequest = JsonObjectRequest(
                Request.Method.POST,
                url,
                postData,
                onResponse(activity)
            ) { errmsg -> result.data = errmsg.toString() }

            requestQueue.add(jsonArrayRequest)
            var text: String
            activity.binding.root.context.openFileInput("cookie.txt").use { stream ->
                text = stream.bufferedReader().use {
                    it.readText()
                }
                Log.d("TAG", "LOADED: $text")
            }
            val fakeUser = LoggedInUser(email, "Pepa Placeholder", text)
            return Result.Success(fakeUser)

        } catch (e: Throwable) {
            return Result.Error(IOException("Error  in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}