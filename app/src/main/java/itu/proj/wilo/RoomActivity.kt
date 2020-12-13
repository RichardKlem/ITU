package itu.proj.wilo

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import itu.proj.wilo.databinding.ActivityRoomBinding
import itu.proj.wilo.ui.hotel.RoomViewModel
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale
import java.util.concurrent.TimeUnit


private fun onResponse(activity: RoomActivity): (response: JSONObject) -> Unit {
    return { response ->
        try {
            activity.callbackRequestResponse(response)
        }
        catch (e: JSONException) {
            Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
}

class RoomActivity : AppCompatActivity() {
    lateinit var roomViewModel: RoomViewModel
    lateinit var binding: ActivityRoomBinding
    lateinit var loading: ProgressBar
    private var hotelId: Int = -1
    private var cookie: String? = null
    private var hotelName: String? = null
    lateinit var personNum: EditText
    lateinit var startDate: EditText
    lateinit var endDate: EditText
    lateinit var daysSum: TextView
    private lateinit var myCalendar: Calendar
    private var price: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cookie = intent.getStringExtra("EXTRA_SESSION_ID")
        hotelId = intent.getIntExtra("EXTRA_HOTEL_ID", -1)
        hotelName = intent.getStringExtra("EXTRA_HOTEL_NAME")

        setContentView(R.layout.activity_room)
        roomViewModel = ViewModelProvider(this).get(RoomViewModel::class.java)
        binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myCalendar = Calendar.getInstance()

        price = 1000
        binding.textPricePerNight.text = price.toString()
        personNum = binding.textPersonsSum
        personNum.setText("2")
        startDate = binding.textDateStart
        endDate = binding.textDateEnd
        daysSum = binding.textDaysSum
        daysSum.text = "0"
        val reserveButton = binding.button

        reserveButton.setOnClickListener {

            reserveButton.isClickable = false
            val url = "http://xklemr00.pythonanywhere.com/bookRoom"
            val requestQueue = Volley.newRequestQueue(this)
            val postData = JSONObject()
            postData.put("CookieUserID", cookie)
            postData.put("adult_count", personNum.text.toString())
            postData.put("approved", "1")
            postData.put("start_date", startDate.text.toString())
            postData.put("end_date", endDate.text.toString())
            postData.put("id_room", 1)
            postData.put("room_count", 2)

            val jsonArrayRequest = JsonObjectRequest(
                Request.Method.POST,
                url,
                postData,
                onResponse(this)
            ) { throw Exception("User account information retrieving failed.") }
            requestQueue.add(jsonArrayRequest)
        }


        val sDate =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                updateLabel(startDate)
            }
        val eDate =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                updateLabel(endDate)
            }

        startDate.setOnClickListener { // TODO Auto-generated method stub
            DatePickerDialog(
                this@RoomActivity, sDate, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }
        endDate.setOnClickListener { // TODO Auto-generated method stub
            DatePickerDialog(
                this@RoomActivity, eDate, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        personNum.afterTextChanged {
            if (!isPersonValid(personNum)) {
                personNum.error = "Chybný počet osob"
            }
        }

        startDate.afterTextChanged {
            if (startDate.text.isNotEmpty() && endDate.text.isNotEmpty()) {
                if (!isDatesValid(startDate.text.toString(), endDate.text.toString())) {
                    startDate.error = "Chybně zadané datum"
                    endDate.error = "Chybně zadané datum"
                    reserveButton.isEnabled = false
                }
                reserveButton.isEnabled = true
            }
        }
        endDate.afterTextChanged {
            if (startDate.text.isNotEmpty() && endDate.text.isNotEmpty()) {
                if (!isDatesValid(startDate.text.toString(), endDate.text.toString())) {
                    startDate.error = "Chybně zadané datum"
                    endDate.error = "Chybně zadané datum"
                    reserveButton.isEnabled = false
                }
                reserveButton.isEnabled = true
            }
        }

        val priceText = "$price Kč"
        binding.textPricePerNight.text = priceText
        val priceSumText = "${price.toString().toInt() * daysSum.text.toString().toInt() * personNum.text.toString().toInt()} Kč"
        binding.textPriceSum.text = priceSumText
    }
    private fun updateLabel(date: EditText) {
        val myFormat = "yyyy-MM-dd" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        date.setText(sdf.format(myCalendar.time))
    }
    private fun isDatesValid(startDateString: String, endDateString: String): Boolean {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val today = Calendar.getInstance()
        val startDateDB: Date? = sdf.parse(startDateString)
        val endDateDB: Date? = sdf.parse(endDateString)
        val diff: Long = endDateDB!!.time - startDateDB!!.time
        val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
        if ((today.after(startDateDB)) || (days < 1)) {
            return false
        }
        startDate.error = null
        endDate.error = null
        daysSum.text = days.toString()
        val finalPrice = "${price * days * personNum.text.toString().toInt()} Kč"
        binding.textPriceSum.text = finalPrice
        return true
    }
    private fun isPersonValid(num: EditText) : Boolean{
        if (num.text.toString().toInt() < 1) {
            return false
        }
        return true
    }

    fun callbackRequestResponse(response: JSONObject) {
        Toast.makeText(this, response.toString(), Toast.LENGTH_LONG).show()
    }
    /**
     * Extension function to simplify setting an afterTextChanged action to EditText components.
     */
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

