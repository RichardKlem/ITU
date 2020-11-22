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
import com.example.itu.R
import com.example.itu.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.ExecutionException


/*class MyCustomListener() : View.OnClickListener {

    override fun onClick(v: View) {

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(v.context)
        val url = "https://pastebin.com/raw/2bW31yqa"

        val future = RequestFuture.newFuture<JSONObject>()
        val request = JsonObjectRequest(Request.Method.GET, url, null, future, future)
        queue.add(request)
        var response = JSONObject()
        try {
            response = future.get()// this will block
        } catch (e: InterruptedException) {
            // exception handling
        } catch (e: ExecutionException) {
            // exception handling
        }

        Snackbar.make(v, response.toString(), Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }
}*/

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

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
        val url = "http://xklemr00.pythonanywhere.com/getHotelsAdmin"
        //val listener = MyCustomListener()
        //btn.setOnClickListener(listener)
        btn.setOnClickListener(View.OnClickListener { // Empty the TextView
            textView.text = ""

            // Initialize a new RequestQueue instance
            val requestQueue = Volley.newRequestQueue(root.context)

            // Initialize a new JsonArrayRequest instance
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    // Do something with response
                    //mTextView.setText(response.toString());

                    // Process the JSON
                    try {
                        // Loop through the array elements
                        for (i in 0 until response.length()) {
                            // Get current json object
                            val obj = JSONObject(response.getJSONObject(i).toString()).toString(4)
                            /*val address = obj.getString("address")
                            val category = obj.getString("category")
                            val description = obj.getString("description")
                            val email = obj.getString("email")*/

                            textView.text = obj.toString()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            ) { // Do something when error occurred
                errmsg ->
                textView.text = "$errmsg"
            }

            // Add JsonArrayRequest to the RequestQueue
            requestQueue.add(jsonArrayRequest)
        })

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}