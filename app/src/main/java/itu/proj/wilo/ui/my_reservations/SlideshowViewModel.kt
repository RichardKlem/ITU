package itu.proj.wilo.ui.my_reservations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SlideshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is My reservations Fragment"
    }
    val text: LiveData<String> = _text
}