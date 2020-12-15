package itu.proj.wilo

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import itu.proj.wilo.databinding.ActivityHotelBinding
import itu.proj.wilo.ui.hotel.HotelViewModel

class HotelActivity : AppCompatActivity() {
    lateinit var hotelViewModel: HotelViewModel
    lateinit var binding: ActivityHotelBinding
    lateinit var loading: ProgressBar
    private var hotelId: Int = -1
    private var cookie: String? = null
    private var hotelName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cookie = intent.getStringExtra("EXTRA_SESSION_ID")
        hotelId = intent.getIntExtra("EXTRA_HOTEL_ID", -1)
        hotelName = intent.getStringExtra("EXTRA_HOTEL_NAME")

        setContentView(R.layout.activity_hotel)
        hotelViewModel = ViewModelProvider(this).get(HotelViewModel::class.java)
        binding = ActivityHotelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when (hotelId) {
            1 -> {
                val header = "Rooms of $hotelName"
                binding.textHeader.text = header
                binding.imageRoom1.setImageResource(R.drawable.hotel_1_room_1)
                binding.imageRoom2.setImageResource(R.drawable.hotel_1_room_2)
                binding.imageRoom3.setImageResource(R.drawable.hotel_1_room_3)
                binding.textRoom1.text = "Single Room"
                binding.textRoom2.text = "Double room"
                binding.textRoom3.text = "Pokoj 3"
            }
            2 -> {
                val header = "Rooms of $hotelName"
                binding.textHeader.text = header
                binding.imageRoom1.setImageResource(R.drawable.hotel_2_room_1)
                binding.imageRoom2.setImageResource(R.drawable.hotel_2_room_2)
                binding.imageRoom3.setImageResource(R.drawable.hotel_2_room_3)
                binding.textRoom1.text = "Single Room"
                binding.textRoom2.text = "Double room"
                binding.textRoom3.text = "Pokoj 3"
            }
            3 -> {
                val header = "Rooms of $hotelName"
                binding.textHeader.text = header
                binding.imageRoom1.setImageResource(R.drawable.hotel_3_room_1)
                binding.imageRoom2.setImageResource(R.drawable.hotel_3_room_2)
                binding.imageRoom3.setImageResource(R.drawable.hotel_3_room_3)
                binding.textRoom1.text = "Single Room"
                binding.textRoom2.text = "Double room"
                binding.textRoom3.text = "Deluxe Double Room"
            }
        }
        val hotelImage_1 = binding.imageRoom1
        val hotelImage_2 = binding.imageRoom2
        val hotelImage_3 = binding.imageRoom3

        hotelImage_1.setOnClickListener {
            val cookie = cookie
            val switchActivityIntent = Intent(this, RoomActivity::class.java)
            switchActivityIntent.putExtra("EXTRA_SESSION_ID", cookie)
            switchActivityIntent.putExtra("EXTRA_HOTEL_ID", 1)
            switchActivityIntent.putExtra("EXTRA_HOTEL_NAME", "Hotel Crowne Plaza")
            startActivity(switchActivityIntent)
        }
        hotelImage_2.setOnClickListener {
            val cookie = cookie
            val switchActivityIntent = Intent(this, RoomActivity::class.java)
            switchActivityIntent.putExtra("EXTRA_SESSION_ID", cookie)
            switchActivityIntent.putExtra("EXTRA_HOTEL_ID", 2)
            switchActivityIntent.putExtra("EXTRA_HOTEL_NAME", "Hotel Sunset Lodge")
            startActivity(switchActivityIntent)
        }
        hotelImage_3.setOnClickListener {
            val cookie = cookie
            val switchActivityIntent = Intent(this, RoomActivity::class.java)
            switchActivityIntent.putExtra("EXTRA_SESSION_ID", cookie)
            switchActivityIntent.putExtra("EXTRA_HOTEL_ID", 3)
            switchActivityIntent.putExtra("EXTRA_HOTEL_NAME", "Hotel Knights Inn")
            startActivity(switchActivityIntent)
        }
    }
}
