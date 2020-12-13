package itu.proj.wilo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import itu.proj.wilo.databinding.ActivityLoginBinding
import itu.proj.wilo.ui.login.LoginViewModel
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var loginViewModel: LoginViewModel
    lateinit var binding: ActivityLoginBinding
    lateinit var loading: ProgressBar
    private lateinit var cookie: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(binding.root)
        setContentView(R.layout.activity_login)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.email
        val password = binding.password
        val loginButton = binding.loginButton
        loading = binding.loading
        val registerButton = binding.registerButton

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            loginButton.isEnabled = loginState.isDataValid

            if (loginState.emailError != null) {
                email.error = getString(loginState.emailError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        email.afterTextChanged {
            loginViewModel.loginDataChanged(
                email.text.toString(),
                password.text.toString()
            )
        }
        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    email.text.toString(),
                    password.text.toString()
                )
            }
        }

        loginButton.setOnClickListener {
            val url = "http://xklemr00.pythonanywhere.com/login"
            // Initialize a new RequestQueue instance
            val requestQueue = Volley.newRequestQueue(binding.root.context)

            val postData = JSONObject()
            postData.put("email", email.text.toString())
            postData.put("password", password.text.toString())

            // Initialize a new JsonArrayRequest instance
            val jsonArrayRequest = JsonObjectRequest(
                Request.Method.POST,
                url,
                postData,
                onResponse(this)
            ) { Toast.makeText(
                    applicationContext,
                    "Login failed, try again.",
                    Toast.LENGTH_LONG
                ).show()
                loading.visibility = View.GONE
            }
            loading.visibility = View.VISIBLE
            requestQueue.add(jsonArrayRequest)
        }
        registerButton.setOnClickListener {
            val switchActivityIntent = Intent(this, RegisterActivity::class.java)
            startActivity(switchActivityIntent)
        }
    }

    fun callbackRequestResponse(response: JSONObject){
        val statusMessage = response.getString("status")

        try {
            val statusCode = response.getInt("status_code")

            if (statusCode == 200) {
                loading.visibility = View.GONE
                val cookie = response.getString("cookie_id")
                val switchActivityIntent = Intent(this, MainActivity::class.java)
                switchActivityIntent.putExtra("EXTRA_SESSION_ID", cookie)
                startActivity(switchActivityIntent)
            }
            loading.visibility = View.GONE
        }
        catch (e: JSONException) {
            loading.visibility = View.GONE
            Toast.makeText(applicationContext, statusMessage, Toast.LENGTH_LONG).show()
        }
    }
}

private fun onResponse(activity: LoginActivity): (response: JSONObject) -> Unit {
    return { response ->
        try {
            activity.callbackRequestResponse(response)
        }
        catch (e: JSONException) {
            Toast.makeText(activity.applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            try {
                afterTextChanged.invoke(editable.toString())
            }
            catch (e: Exception){}
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}