package itu.proj.wilo.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import itu.proj.wilo.HotelActivity
import itu.proj.wilo.MainActivity
import itu.proj.wilo.R
import itu.proj.wilo.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
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

        // Get parent activity
        val mainActivity:  MainActivity = activity as MainActivity

        val hotelImage_1 = root.findViewById<ImageView>(R.id.image_hotel_1)
        val hotelImage_2 = root.findViewById<ImageView>(R.id.image_hotel_2)
        val hotelImage_3 = root.findViewById<ImageView>(R.id.image_hotel_3)

        hotelImage_1.setOnClickListener {
            val cookie = mainActivity.globalCookie
            val switchActivityIntent = Intent(activity, HotelActivity::class.java)
            switchActivityIntent.putExtra("EXTRA_SESSION_ID", cookie)
            switchActivityIntent.putExtra("EXTRA_HOTEL_ID", 1)
            switchActivityIntent.putExtra("EXTRA_HOTEL_NAME", "Wilo Crowne Plaza")
            startActivity(switchActivityIntent)
        }
        hotelImage_2.setOnClickListener {
            val cookie = mainActivity.globalCookie
            val switchActivityIntent = Intent(activity, HotelActivity::class.java)
            switchActivityIntent.putExtra("EXTRA_SESSION_ID", cookie)
            switchActivityIntent.putExtra("EXTRA_HOTEL_ID", 2)
            switchActivityIntent.putExtra("EXTRA_HOTEL_NAME", "Wilo Sunset Lodge")
            startActivity(switchActivityIntent)
        }
        hotelImage_3.setOnClickListener {
            val cookie = mainActivity.globalCookie
            val switchActivityIntent = Intent(activity, HotelActivity::class.java)
            switchActivityIntent.putExtra("EXTRA_SESSION_ID", cookie)
            switchActivityIntent.putExtra("EXTRA_HOTEL_ID", 3)
            switchActivityIntent.putExtra("EXTRA_HOTEL_NAME", "Wilo Knights Inn")
            startActivity(switchActivityIntent)
        }
        return root
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
