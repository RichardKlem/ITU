package itu.proj.wilo

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.widget.*
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
    lateinit var date: EditText
    lateinit var timeFrom: EditText
    lateinit var timeTo: EditText
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

        date = binding.textDateStart
        timeFrom = binding.textTimeStart
        timeTo = binding.textTimeEnd
        hoursSum = binding.textHoursSum
        hoursSum.text = "0"
        val reserveButton = binding.button

        val sDate =
                OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    myCalendar[Calendar.YEAR] = year
                    myCalendar[Calendar.MONTH] = monthOfYear
                    myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                    updateLabel(date)
                }
        val sTime =
            OnTimeSetListener { view, hours, minutes ->
                myCalendar[Calendar.HOUR] = hours
                myCalendar[Calendar.MINUTE] = minutes
                updateLabelTime(timeFrom)
            }
        val eTime =
                OnTimeSetListener { view, hours, minutes ->
                myCalendar[Calendar.HOUR] = hours
                myCalendar[Calendar.MINUTE] = minutes
                    updateLabelTime(timeTo)
            }

        date.setOnClickListener {
            DatePickerDialog(
                this@BabySitterReserveActivity, sDate, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }
        timeFrom.setOnClickListener {
            TimePickerDialog (
                this@BabySitterReserveActivity, sTime, myCalendar[Calendar.HOUR], myCalendar[Calendar.MINUTE], true
            ).show()
        }
        timeTo.setOnClickListener {
            TimePickerDialog(
                    this@BabySitterReserveActivity, eTime, myCalendar[Calendar.HOUR], myCalendar[Calendar.MINUTE], true
            ).show()
        }


        date.afterTextChanged {
            if (date.text.isNotEmpty() && date.text.isNotEmpty()) {
                if (!isDatesValid(date.text.toString(),date.text.toString(), date.text.toString())) {
                    date.error = "Wrong date"
                    date.error = "Wrong date"
                    reserveButton.isEnabled = false
                }
                reserveButton.isEnabled = true
            }
        }
        timeFrom.afterTextChanged {
            Toast.makeText(
                    applicationContext,
                    timeFrom.text.toString(),
                    Toast.LENGTH_LONG
            ).show()

            if (timeFrom.text.isNotEmpty() && date.text.isNotEmpty()) {
                if (!isDatesValid(date.text.toString(),timeFrom.text.toString(), timeTo.text.toString())) {
                    timeFrom.error = "Wrong date"
                    timeTo.error = "Wrong date"
                    reserveButton.isEnabled = false
                }
                reserveButton.isEnabled = true
            }
        }
        timeTo.afterTextChanged {
            if (timeTo.text.isNotEmpty() && date.text.isNotEmpty()) {
                if (!isDatesValid(date.text.toString(),timeFrom.text.toString(), timeTo.text.toString())) {
                    timeFrom.error = "Wrong date"
                    timeTo.error = "Wrong date"
                    reserveButton.isEnabled = false
                }
                reserveButton.isEnabled = true
            }
        }

        val price = 250
        val priceText = "$price Kč"
        binding.textPricePerHour.text = priceText
        val priceSumText = "${price * hoursSum.text.toString().toInt()} Kč"
        binding.textPriceSum.text = priceSumText
    }
    private fun updateLabel(date: EditText) {
        val myFormat = "yyyy-MM-dd" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        date.setText(sdf.format(myCalendar.time))
    }
    private fun updateLabelTime(time: EditText) {
        val myFormat = "hh:mm" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        time.setText(sdf.format(myCalendar.time))
    }
    private fun isDatesValid(dateString: String, startTimeString: String, endTimeString: String): Boolean {
        val myFormat = "yyyy-MM-dd hh:mm"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val today = Calendar.getInstance()

        val stringTime = dateString + " " + startTimeString
        val endTime = dateString + " " + endTimeString

        val startDateDB: Date? = sdf.parse(stringTime)
        val endDateDB: Date? = sdf.parse(endTime)
        val diff: Long = endDateDB!!.time - startDateDB!!.time
        val hours = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS).toInt()
        if ((today.after(startDateDB)) || (hours > 12)) {
            return false
        }
        date.error = null
        hoursSum.text = hours.toString()
        val finalPrice = "${binding.textPricePerHour.text.toString().toInt() * hours} Kč"
        binding.textPriceSum.text = finalPrice
        return true
    }
}
