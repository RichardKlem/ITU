package itu.proj.wilo

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import itu.proj.wilo.databinding.ActivityRoomBinding
import itu.proj.wilo.ui.hotel.RoomViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale
import java.util.concurrent.TimeUnit


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

        personNum = binding.textPersonsSum
        personNum.setText("2")
        startDate = binding.textDateStart
        endDate = binding.textDateEnd
        daysSum = binding.textDaysSum
        daysSum.text = "0"
        val reserveButton = binding.button

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

        /*val yourDate = Calendar.getInstance().timeInMillis
        val millionSeconds = yourDate - TimeUnit.toMillis(2)
        val out = TimeUnit.MILLISECONDS.toDays(millionSeconds).toString()
        val myFormat = "YYYY-MM-DD"
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm:ss a")
        val dateTime = LocalDateTime.parse(date, formatter)
        val formatter2 = DateTimeFormatter.ofPattern("dd MMM yyyy")
        println(dateTime.format(formatter2))*/

        val price = 1000
        val priceText = "$price Kč"
        binding.textPricePerNight.text = priceText
        val priceSumText = "${price * daysSum.text.toString().toInt()} Kč"
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
        //(startDateDB.time < today) ||
        if ((today.after(startDateDB)) || (days < 1)) {
            return false
        }
        startDate.error = null
        endDate.error = null
        daysSum.text = days.toString()
        val finalPrice = "${binding.textPricePerNight.text.toString().toInt() * days} Kč"
        binding.textPriceSum.text = finalPrice
        return true
    }
    private fun isPersonValid(num: EditText) : Boolean{
        if (num.text.toString().toInt() < 1) {
            return false
        }
        return true
    }
}
