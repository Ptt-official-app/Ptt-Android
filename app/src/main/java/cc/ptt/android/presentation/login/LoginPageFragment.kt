package cc.ptt.android.presentation.login

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import cc.ptt.android.FragmentTouchListener
import cc.ptt.android.HomeActivity
import cc.ptt.android.R
import cc.ptt.android.data.common.PreferenceConstants
import cc.ptt.android.databinding.LoginPageFragmentBinding
import cc.ptt.android.presentation.base.BaseFragment
import cc.ptt.android.presentation.common.KeyboardUtils
import cc.ptt.android.utils.observeEventNotNull
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.absoluteValue

class LoginPageFragment : BaseFragment(), FragmentTouchListener, View.OnClickListener {
    private val viewModel by viewModel<LoginPageViewModel>()

    private lateinit var binding: LoginPageFragmentBinding

    private var isShowPassword = false

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
            .getSharedPreferences(PreferenceConstants.prefName, Context.MODE_PRIVATE)
            .getString(PreferenceConstants.id, "") ?: ""
        binding.root.addOnLayoutChangeListener { _, _, top, _, bottom, _, oldTop, _, oldBottom ->
            if ((bottom - top).absoluteValue < (oldBottom - oldTop)) {
                binding.scrollLoginPage.post {
                    binding.scrollLoginPage.smoothScrollTo(0, binding.spaceLoginPageTitleToSelector.top)
                }
            }
        }
        binding.apply {
            textLoginPageServiceTerms.paintFlags = textLoginPageServiceTerms.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            editLoginPageAccount.setText(id)
            btnLoginPageLogin.setOnClickListener(this@LoginPageFragment)
            binding.btnLoginPageLogin.isEnabled = false
            binding.btnLoginPageLogin.isClickable = false
            btnLoginPageShowPassword.setOnClickListener(this@LoginPageFragment)
            editLoginPageAccount.addTextChangedListener(
                beforeTextChanged = { char: CharSequence?, start: Int, count: Int, after: Int ->
                },
                onTextChanged = { char: CharSequence?, start: Int, count: Int, after: Int ->
                    textLoginPageAccountMessage.isVisible = false
                    textLoginPagePasswordMessage.isVisible = false
                    val password = binding.editLoginPagePassword.text.toString()
                    val haveAccount = char?.isNotEmpty() == true && password.isNotBlank()
                    loginButtonEnable(haveAccount)
                },
                afterTextChanged = {}
            )
            editLoginPagePassword.addTextChangedListener(
                beforeTextChanged = { char: CharSequence?, start: Int, count: Int, after: Int ->
                },
                onTextChanged = { char: CharSequence?, start: Int, count: Int, after: Int ->
                    textLoginPageAccountMessage.isVisible = false
                    textLoginPagePasswordMessage.isVisible = false
                    val account = binding.editLoginPageAccount.text.toString()
                    val havePassword = char?.isNotEmpty() == true && account.isNotBlank()
                    loginButtonEnable(havePassword)
                },
                afterTextChanged = {}
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

            viewModel.apply {
                passwordMessage.observe(viewLifecycleOwner) {
                    binding.textLoginPagePasswordMessage.setText(it)
                    binding.textLoginPagePasswordMessage.isVisible = true
                }
                errorMessage.observeEventNotNull(viewLifecycleOwner) {
                    Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                }
                loginSuccess.observe(viewLifecycleOwner) {
                    Toast.makeText(requireActivity(), "登入成功！", Toast.LENGTH_SHORT).show()
                    currentActivity.onBackPressed()
                }
            }
        }
    }

    private fun loginButtonEnable(isEnable: Boolean) {
        if (isEnable) {
            binding.btnLoginPageLogin.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.tangerine)
            )
            binding.btnLoginPageLogin.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        } else {
            binding.btnLoginPageLogin.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.black)
            )
            binding.btnLoginPageLogin.setTextColor(ContextCompat.getColor(requireContext(), R.color.tangerine))
        }
        binding.btnLoginPageLogin.isEnabled = isEnable
        binding.btnLoginPageLogin.isClickable = isEnable
    }

    override fun onAnimOver() {}

    override fun onDestroyView() {
        super.onDestroyView()
        KeyboardUtils.hideSoftInput(requireActivity())
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

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnLoginPageLogin -> {
                KeyboardUtils.hideSoftInput(requireActivity())
                val account = binding.editLoginPageAccount.text.toString()
                val password = binding.editLoginPagePassword.text.toString()
                viewModel.checkLoginLegal(account, password)
            }
            R.id.btnLoginPageShowPassword -> {
                if (isShowPassword) {
                    binding.btnLoginPageShowPassword.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_baseline_visibility_off_24
                        )
                    )
                    binding.editLoginPagePassword.transformationMethod = PasswordTransformationMethod.getInstance()
                } else {
                    binding.btnLoginPageShowPassword.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_baseline_visibility_24
                        )
                    )
                    binding.editLoginPagePassword.transformationMethod = null
                }
                binding.editLoginPagePassword.text?.length?.let { length ->
                    binding.editLoginPagePassword.setSelection(length)
                }
                isShowPassword = !isShowPassword
            }
        }
    }
}
