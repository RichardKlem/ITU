package itu.proj.wilo.ui.babysitter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import itu.proj.wilo.BabySitterReserveActivity
import itu.proj.wilo.MainActivity
import itu.proj.wilo.databinding.FragmentBabysittingBinding
import itu.proj.wilo.ui.hotel.BabysittingViewModel


class BabysittingFragment : Fragment() {
    private lateinit var babysittingViewModel: BabysittingViewModel
    private var _binding: FragmentBabysittingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        babysittingViewModel =
                ViewModelProvider(this).get(BabysittingViewModel::class.java)

        _binding = FragmentBabysittingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Get parent activity
        val mainActivity:  MainActivity = activity as MainActivity

        val imageBs1 = binding.imageBs1
        val imageBs2 = binding.imageBs2
        val imageBs3 = binding.imageBs3
        val imageBs4 = binding.imageBs4
        val imageBs5 = binding.imageBs5

        /*switchActivityIntent.putExtra("EXTRA_BABYSITTER_ID", 1)
        switchActivityIntent.putExtra("EXTRA_BABYSITTER_NAME", imageBs1.)
        switchActivityIntent.putExtra("EXTRA_BABYSITTER_AGE", cookie)
        switchActivityIntent.putExtra("EXTRA_BABYSITTER_DESCRIPTION", cookie)
        switchActivityIntent.putExtra("EXTRA_BABYSITTER_PRICE", cookie)*/

        val cookie = mainActivity.globalCookie
        imageBs1.setOnClickListener {
            val switchActivityIntent = Intent(mainActivity, BabySitterReserveActivity::class.java)
            switchActivityIntent.putExtra("EXTRA_SESSION_ID", cookie)
            startActivity(switchActivityIntent)
        }
        imageBs2.setOnClickListener {
            val switchActivityIntent = Intent(mainActivity, BabySitterReserveActivity::class.java)
            switchActivityIntent.putExtra("EXTRA_SESSION_ID", cookie)
            startActivity(switchActivityIntent)
        }
        imageBs3.setOnClickListener {
            val switchActivityIntent = Intent(mainActivity, BabySitterReserveActivity::class.java)
            switchActivityIntent.putExtra("EXTRA_SESSION_ID", cookie)
            startActivity(switchActivityIntent)
        }
        imageBs4.setOnClickListener {
            val switchActivityIntent = Intent(mainActivity, BabySitterReserveActivity::class.java)
            switchActivityIntent.putExtra("EXTRA_SESSION_ID", cookie)
            startActivity(switchActivityIntent)
        }
        imageBs5.setOnClickListener {
            val switchActivityIntent = Intent(mainActivity, BabySitterReserveActivity::class.java)
            switchActivityIntent.putExtra("EXTRA_SESSION_ID", cookie)
            startActivity(switchActivityIntent)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
