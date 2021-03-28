package tw.y_studio.ptt.fragment.login

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import tw.y_studio.ptt.FragmentTouchListener
import tw.y_studio.ptt.HomeActivity
import tw.y_studio.ptt.R
import tw.y_studio.ptt.databinding.LoginPageFragmentBinding
import tw.y_studio.ptt.ui.BaseFragment
import tw.y_studio.ptt.utils.StringUtils.isAccount
import tw.y_studio.ptt.utils.StringUtils.notNullImageString

class LoginPageFragment : BaseFragment(), FragmentTouchListener {
    private var isShowPassword = false

    private lateinit var binding: LoginPageFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        binding = LoginPageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments // 取得Bundle
        val id = currentActivity
            .getSharedPreferences("MainSetting", Context.MODE_PRIVATE)
            .getString("APIPTTID", "") ?: ""
        binding.root.viewTreeObserver.addOnGlobalFocusChangeListener { oldFocus, newFocus ->
            if (oldFocus is EditText || newFocus is EditText) {
                // wait for keyboard has been shown, scroll view to correct place.
                binding.scrollLoginPage.postDelayed(
                    {
                        binding.scrollLoginPage.smoothScrollTo(0, binding.spaceLoginPageTitleToSelector.top)
                    },
                    150
                )
            }
        }
        binding.apply {
            textLoginPageServiceTerms.paintFlags = textLoginPageServiceTerms.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            editLoginPageAccount.setText(id)
            btnLoginPageLogin.setOnClickListener(
                View.OnClickListener {
                    val text = editLoginPageAccount.text.toString()
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
                btnLoginPageShowPassword.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_visibility_off_24
                    )
                )
            } else {
                btnLoginPageShowPassword.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_visibility_24
                    )
                )
            }
            btnLoginPageShowPassword.setOnClickListener {
                if (isShowPassword) {
                    btnLoginPageShowPassword.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_baseline_visibility_off_24
                        )
                    )
                    editLoginPagePassword.transformationMethod = PasswordTransformationMethod.getInstance()
                } else {
                    btnLoginPageShowPassword.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_baseline_visibility_24
                        )
                    )
                    editLoginPagePassword.transformationMethod = null
                }
                editLoginPagePassword.text?.length?.let { length -> editLoginPagePassword.setSelection(length) }
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
