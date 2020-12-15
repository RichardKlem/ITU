package itu.proj.wilo.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "There will be settings. Now there is nothing to do. See you in the next version!"
    }
    val text: LiveData<String> = _text
}