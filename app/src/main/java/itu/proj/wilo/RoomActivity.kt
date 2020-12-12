package itu.proj.wilo

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import itu.proj.wilo.databinding.ActivityRoomBinding
import itu.proj.wilo.ui.hotel.RoomViewModel
import java.util.*
import java.util.concurrent.TimeUnit


class RoomActivity : AppCompatActivity() {
    lateinit var roomViewModel: RoomViewModel
    lateinit var binding: ActivityRoomBinding
    lateinit var loading: ProgressBar
    private var hotelId: Int = -1
    private var cookie: String? = null
    private var hotelName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cookie = intent.getStringExtra("EXTRA_SESSION_ID")
        hotelId = intent.getIntExtra("EXTRA_HOTEL_ID", -1)
        hotelName = intent.getStringExtra("EXTRA_HOTEL_NAME")

        setContentView(R.layout.activity_room)
        roomViewModel = ViewModelProvider(this).get(RoomViewModel::class.java)
        binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*val yourDate = Calendar.getInstance().timeInMillis
        val millionSeconds = yourDate - TimeUnit.toMillis(2)
        val out = TimeUnit.MILLISECONDS.toDays(millionSeconds).toString()*/

        var price = "1000 Kč"
        binding.textPricePerNight.text = price
        binding.textPersonsSum.setText("2")
        var date = "20/12/2020"
        binding.textDateStart.setText(date)
        date = "28/12/2020"
        binding.textDateEnd.setText(date)
        binding.textDaysSum.text = "8"
        price = "8000 Kč"
        binding.textPriceSum.text = price
    }
}
