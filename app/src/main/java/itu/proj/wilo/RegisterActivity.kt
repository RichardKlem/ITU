package itu.proj.wilo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import itu.proj.wilo.databinding.ActivityRegisterBinding
import itu.proj.wilo.ui.registration.RegistrationViewModel
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    lateinit var registrationViewModel: RegistrationViewModel
    lateinit var binding: ActivityRegisterBinding

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var reEnteredPassword: EditText
    lateinit var loading: ProgressBar
    lateinit var fullName: EditText
    lateinit var registrationResponse: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = binding.email
        password = binding.password
        reEnteredPassword = binding.reenterpassword
        loading = binding.loading
        fullName = binding.fullname
        registrationResponse = binding.registrationResponse

        val phone = binding.phonenumber
        val address = binding.address
        val birthDate = binding.birthdate
        val registerButton = binding.registerButton


        registrationViewModel.registrationFormState.observe(this@RegisterActivity, Observer {
            val registrationState = it ?: return@Observer
            registerButton.isEnabled = registrationState.isDataValid

            if (registrationState.emailError != null) {
                email.error = getString(registrationState.emailError)
            }
            if (registrationState.nameError != null) {
                fullName.error = getString(registrationState.nameError)
            }
            if (registrationState.passwordError != null) {
                password.error = getString(registrationState.passwordError)
            }
            if (registrationState.passwordReenterError != null) {
                reEnteredPassword.error = getString(registrationState.passwordReenterError)
            }
        })

        email.afterTextChanged {
            registrationViewModel.loginDataChanged(
                email.text.toString(),
                fullName.text.toString(),
                password.text.toString(),
                reEnteredPassword.text.toString()
            )
        }
        fullName.afterTextChanged {
            registrationViewModel.loginDataChanged(
                email.text.toString(),
                fullName.text.toString(),
                password.text.toString(),
                reEnteredPassword.text.toString()
            )
        }
        password.afterTextChanged {
            registrationViewModel.loginDataChanged(
                email.text.toString(),
                fullName.text.toString(),
                password.text.toString(),
                reEnteredPassword.text.toString()
            )
        }
        reEnteredPassword.afterTextChanged {
            registrationViewModel.loginDataChanged(
                email.text.toString(),
                fullName.text.toString(),
                password.text.toString(),
                reEnteredPassword.text.toString()
            )
        }

        registerButton.setOnClickListener {
            registerButton.isClickable = false
            val requestQueue = Volley.newRequestQueue(binding.root.context)

            val url = "https://xklemr00.pythonanywhere.com/registration"
            val postData = JSONObject()
            postData.put("name", fullName.text.toString())
            if (birthDate.text.toString() != ""){
                postData.put("birth_date", birthDate.text.toString())
            }
            postData.put("phone_number", phone.text.toString())
            postData.put("address", address.text.toString())
            postData.put("email", email.text.toString())
            postData.put("password", password.text.toString())

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST,
                url,
                postData,
                onResponse(this)
            ) {
                Toast.makeText(
                    applicationContext,
                    "Registration failed, try again.",
                    Toast.LENGTH_LONG
                ).show()
                loading.visibility = View.GONE
            }
            // Set no retry policy, because bug in Volley is by default doubling requests
            jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            requestQueue.add(jsonObjectRequest)
            loading.visibility = View.VISIBLE
        }
    }
    fun callbackRequestResponse(response: JSONObject){
        val statusCode = response.getInt("statusCode")
        val statusMessage = response.getString("status")

        registrationResponse.text = statusMessage
        Toast.makeText(applicationContext, statusMessage, Toast.LENGTH_LONG).show()

        if (statusCode == 200) {
            val switchActivityIntent = Intent(this, LoginActivity::class.java)
            startActivity(switchActivityIntent)
        }
        loading.visibility = View.GONE
    }
    // Extension function to simplify setting an afterTextChanged action to EditText components.
    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}
private fun onResponse(activity: RegisterActivity): (response: JSONObject) -> Unit {
    return { response ->
        try {
            activity.callbackRequestResponse(response)
        }
        catch (e: JSONException) {
            Toast.makeText(activity.applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
}

