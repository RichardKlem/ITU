package itu.proj.wilo

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import itu.proj.wilo.databinding.ActivityBabysitterReserveBinding
import itu.proj.wilo.ui.hotel.BabySitterReserveViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale
import java.util.concurrent.TimeUnit


class BabySitterReserveActivity : AppCompatActivity() {
    lateinit var babySitterReserveViewModel: BabySitterReserveViewModel
    lateinit var binding: ActivityBabysitterReserveBinding
    lateinit var loading: ProgressBar
    private var cookie: String? = null
    lateinit var startDate: EditText
    lateinit var endDate: EditText
    lateinit var hoursSum: TextView
    private lateinit var myCalendar: Calendar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cookie = intent.getStringExtra("EXTRA_SESSION_ID")

        setContentView(R.layout.activity_babysitter_reserve)
        babySitterReserveViewModel = ViewModelProvider(this).get(BabySitterReserveViewModel::class.java)
        binding = ActivityBabysitterReserveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myCalendar = Calendar.getInstance()

        startDate = binding.textDateStart
        endDate = binding.textDateEnd
        hoursSum = binding.textHoursSum
        hoursSum.text = "0"
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
                this@BabySitterReserveActivity, sDate, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }
        endDate.setOnClickListener { // TODO Auto-generated method stub
            DatePickerDialog(
                this@BabySitterReserveActivity, eDate, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
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
        val priceSumText = "${price * hoursSum.text.toString().toInt()} Kč"
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
        hoursSum.text = days.toString()
        val finalPrice = "${binding.textPricePerNight.text.toString().toInt() * days} Kč"
        binding.textPriceSum.text = finalPrice
        return true
    }
}
