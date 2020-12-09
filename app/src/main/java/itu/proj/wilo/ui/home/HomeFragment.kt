package itu.proj.wilo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import itu.proj.wilo.R
import itu.proj.wilo.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.ExecutionException

class ResponseResult {
    var data = ""
}
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    val result = ResponseResult()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val btn: Button = root.findViewById(R.id.button_search2)
        val url = "http://xklemr00.pythonanywhere.com/login"


        btn.setOnClickListener {
            textView.text = ""
            // Initialize a new RequestQueue instance
            val requestQueue = Volley.newRequestQueue(root.context)
            val postData = JSONObject()
            postData.put("email", "admin@iis-hotel.cz")
            postData.put("password", "1234")

            // Initialize a new JsonArrayRequest instance
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST,
                url,
                postData,
                onResponse(result)
            ) { errmsg -> textView.text = "$errmsg" }

            // Add JsonArrayRequest to the RequestQueue
            requestQueue.add(jsonObjectRequest)
            textView.text = "this.result.data"
        }

        return root
    }

    private fun onResponse(result: ResponseResult): (response: JSONObject) -> Unit {
        return { response ->
            try {
                val obj = JSONObject(response.toString()).toString(4)
                this.result.data = obj.toString()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}