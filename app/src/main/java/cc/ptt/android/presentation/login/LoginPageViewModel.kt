package cc.ptt.android.presentation.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.ptt.android.R
import cc.ptt.android.data.common.PreferenceConstants
import cc.ptt.android.data.common.StringUtils
import cc.ptt.android.data.source.remote.user.IUserRemoteDataSource
import cc.ptt.android.presentation.common.event.Event
import kotlinx.coroutines.launch
import okhttp3.*

class LoginPageViewModel(private val source: IUserRemoteDataSource) : ViewModel() {

    private val _passwordMessage = MutableLiveData<Int>()
    val passwordMessage: LiveData<Int> = _passwordMessage

    private val _errorMessage = MutableLiveData<Event<Int>>()
    val errorMessage: LiveData<Event<Int>> = _errorMessage

    private val _loginSuccess = MutableLiveData<Unit>()
    val loginSuccess: LiveData<Unit> = _loginSuccess

    fun checkLoginLegal(context: Context, account: String, password: String) = viewModelScope.launch {
        if (!StringUtils.isAccount(account)) {
            _passwordMessage.value = R.string.not_this_password
            return@launch
        }
        try {
            // TODO: 4/5/21 wait for api fixed
//            source.existUser(
//                "test_client_id",
//                "test_client_secret",
//                account
//            )
            val login = source.login(
                "test_client_id",
                "test_client_secret",
                account,
                password
            )
            Log.i("Login", "checkLoginLegal: id: ${login.userId},\n token: ${login.accessToken}, \n type: ${login.tokenType}")
            val preference = context.getSharedPreferences(PreferenceConstants.prefName, Context.MODE_PRIVATE)
            val editor = preference.edit()
            editor.putString(StringUtils.notNullImageString(PreferenceConstants.id), login.userId)
            editor.putString(PreferenceConstants.accessToken, login.accessToken)
            editor.putString(PreferenceConstants.tokenType, login.tokenType)
            editor.apply()
            editor.commit()
            _loginSuccess.value = Unit
        } catch (e: Exception) {
            e.printStackTrace()
            _errorMessage.value = Event(R.string.server_error)
        }
    }
}
