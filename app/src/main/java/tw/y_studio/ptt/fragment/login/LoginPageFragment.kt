package tw.y_studio.ptt.fragment.login

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.method.PasswordTransformationMethod
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import tw.y_studio.ptt.FragmentTouchListener
import tw.y_studio.ptt.HomeActivity
import tw.y_studio.ptt.R
import tw.y_studio.ptt.databinding.LoginPageFragmentLayoutBinding
import tw.y_studio.ptt.ui.BaseFragment
import tw.y_studio.ptt.utils.StringUtils.isAccount
import tw.y_studio.ptt.utils.StringUtils.notNullImageString

class LoginPageFragment : BaseFragment(), FragmentTouchListener {
    private var isShowPassword = false

    private lateinit var binding: LoginPageFragmentLayoutBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as HomeActivity).fragmentTouchListener = this
    }

    override fun onDetach() {
        (requireActivity() as HomeActivity).fragmentTouchListener = null
        super.onDetach()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginPageFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments // 取得Bundle
        val id = currentActivity
            .getSharedPreferences("MainSetting", Context.MODE_PRIVATE)
            .getString("APIPTTID", "") ?: ""
        val forgetTVText = "忘記密碼？"
        val content = SpannableString(forgetTVText)
        content.setSpan(UnderlineSpan(), 0, forgetTVText.length, 0)

        binding.apply {

            loginPageTextViewForgot.text = content
            loginPageEditTextTextAccount.setText(id)
            loginPageButton.setBackgroundColor(resources.getColor(R.color.slateGrey))
            loginPageButton.setOnClickListener(
                View.OnClickListener {
                    val text = loginPageEditTextTextAccount.text.toString()
                    if (!isAccount(text)) {
                        Toast.makeText(context, "format incorrect", Toast.LENGTH_SHORT)
                            .show()
                        return@OnClickListener
                    }
                    val preference = currentActivity
                        .getSharedPreferences("MainSetting", Context.MODE_PRIVATE)
                    val editor = preference.edit()
                    editor.putString(notNullImageString("APIPTTID"), text)
                    editor.apply()
                    editor.commit()
                    currentActivity.onBackPressed()
                }
            )

            if (!isShowPassword) {
                loginPageImageButtonShowpassword.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_visibility_off_24
                    )
                )
            } else {
                loginPageImageButtonShowpassword.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_visibility_24
                    )
                )
            }
            loginPageImageButtonShowpassword.setOnClickListener {
                if (isShowPassword) {
                    loginPageImageButtonShowpassword.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_baseline_visibility_off_24
                        )
                    )
                    loginPageEditTextTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                } else {
                    loginPageImageButtonShowpassword.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_baseline_visibility_24
                        )
                    )
                    loginPageEditTextTextPassword.transformationMethod = null
                }
                loginPageEditTextTextPassword.setSelection(loginPageEditTextTextPassword.text.length)
                isShowPassword = !isShowPassword
            }
        }
    }

    override fun onAnimOver() {}

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            val inputMethodManager = currentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(mainView.windowToken, 0)
        } catch (e: Exception) {
        }
    }

    override fun onTouchEvent(event: MotionEvent, defaultTouchEvent: Boolean): Boolean {
        return defaultTouchEvent
    }

    companion object {
        fun newInstance(): LoginPageFragment {
            val args = Bundle()
            val fragment = LoginPageFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(args: Bundle?): LoginPageFragment {
            val fragment = LoginPageFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
