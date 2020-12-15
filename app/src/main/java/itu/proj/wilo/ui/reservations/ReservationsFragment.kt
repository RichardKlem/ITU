package itu.proj.wilo.ui.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import itu.proj.wilo.MainActivity
import itu.proj.wilo.R
import itu.proj.wilo.databinding.FragmentReservationsBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


private fun onResponse(fragment: ReservationsFragment): (response: JSONArray) -> Unit {
    return { response ->
        try {
            fragment.callbackRequestResponse(response)
        }
        catch (e: JSONException) {
            fragment.callbackRequestResponse(response)
            Toast.makeText(fragment.context, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
class ReservationsFragment : Fragment() {

    private lateinit var reservationsViewModel: ReservationsViewModel
    private var _binding: FragmentReservationsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reservationsViewModel =
                ViewModelProvider(this).get(ReservationsViewModel::class.java)

        _binding = FragmentReservationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val mainActivity: MainActivity = activity as MainActivity

        val cookie = mainActivity.globalCookie
        val url = "http://93.153.43.141:5001/getBookings/$cookie"
        val requestQueue = Volley.newRequestQueue(this.context)
        //val postData = JSONObject()
        //postData.put("CookieUserID", cookie)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            onResponse(this)
        ) { throw Exception("User bookings information retrieving failed.") }
        // Set no retry policy, because bug in Volley is by default doubling requests
        jsonArrayRequest.retryPolicy = DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        requestQueue.add(jsonArrayRequest)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun callbackRequestResponse(response: JSONArray) {
        val linView = binding.linearView

        // Dynamic
        for (i in 0 until response.length()) {
            val row: JSONObject = response.getJSONObject(i)
            val cardView = this.layoutInflater.inflate(R.layout.reservation_template, null, false)
            val roomName = cardView.findViewById<TextView>(R.id.text_room_name)
            val priceSum = cardView.findViewById<TextView>(R.id.text_price)
            val dates = cardView.findViewById<TextView>(R.id.text_dates)
            val persons = cardView.findViewById<TextView>(R.id.text_persons_sum)
            roomName.text = row.getString("room_name")
            priceSum.text = row.getInt("total_price").toString()
            val dateText = "Od ${row.getString("start_date")} do ${row.getString("end_date")}"
            dates.text = dateText
            persons.text = row.getInt("adult_count").toString()
            linView.addView(cardView)
        }
    }
}