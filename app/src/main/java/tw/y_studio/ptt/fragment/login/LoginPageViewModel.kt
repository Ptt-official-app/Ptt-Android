package tw.y_studio.ptt.fragment.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tw.y_studio.ptt.R
import tw.y_studio.ptt.utils.StringUtils

class LoginPageViewModel : ViewModel() {

    private val _passwordMessage = MutableLiveData<Int>()
    val passwordMessage: LiveData<Int> = _passwordMessage

    private val _loginSuccess = MutableLiveData<Unit>()
    val loginSuccess: LiveData<Unit> = _loginSuccess

    fun checkLoginLegal(context: Context, account: String, password: String) = viewModelScope.launch {
        if (!StringUtils.isAccount(account)) {
            _passwordMessage.value = R.string.not_this_password
            return@launch
        }
        val preference = context.getSharedPreferences("MainSetting", Context.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putString(StringUtils.notNullImageString("APIPTTID"), account)
        editor.apply()
        editor.commit()
        _loginSuccess.value = Unit
    }
}
