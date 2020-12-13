package itu.proj.wilo.ui.account


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import itu.proj.wilo.MainActivity
import itu.proj.wilo.LoginActivity
import itu.proj.wilo.R
import itu.proj.wilo.databinding.FragmentAccountBinding
import org.json.JSONException
import org.json.JSONObject


private fun onResponse(fragment: AccountFragment): (response: JSONObject) -> Unit {
    return { response ->
        try {
            fragment.callbackRequestResponse(response)
            }
        catch (e: JSONException) {
            Toast.makeText(fragment.context, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
}

class AccountFragment : Fragment() {

    private lateinit var accountViewModel: AccountViewModel
    private var _binding: FragmentAccountBinding? = null


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accountViewModel =
                ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textName
        accountViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

        // Get parent activity
        val mainActivity:  MainActivity = activity as MainActivity

        binding.logoutButton.setOnClickListener {
            val switchActivityIntent = Intent(activity, LoginActivity::class.java)
            startActivity(switchActivityIntent)
        }

        val url = "http://xklemr00.pythonanywhere.com/account"
        val requestQueue = Volley.newRequestQueue(root.context)
        val postData = JSONObject()
        postData.put("CookieUserID", mainActivity.globalCookie)

        val jsonArrayRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            postData,
            onResponse(this)
        ) { throw Exception("User account information retrieving failed.") }
        requestQueue.add(jsonArrayRequest)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun callbackRequestResponse(response: JSONObject) {
        val address = response.getString("address")
        val birthDate = response.getString("birth_date")
        val email = response.getString("email")
        val name = response.getString("name")
        val phoneNumber = response.getString("phone_number")
        val role = response.getString("role")

        binding.textAddress.text = address
        binding.textBirthdate.text = birthDate
        binding.textEmail.text = email
        binding.textName.text = name
        binding.textPhoneNumber.text = phoneNumber
        binding.avatar.setImageResource(R.drawable.ic_bussiness_man)
    }
}