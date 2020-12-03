package tw.y_studio.ptt.ui.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import tw.y_studio.ptt.R;
import tw.y_studio.ptt.ui.BaseFragment;
import tw.y_studio.ptt.ui.ClickFix;
import tw.y_studio.ptt.utils.StringUtils;

public class LoginPageFragment extends BaseFragment {

    public static LoginPageFragment newInstance() {
        Bundle args = new Bundle();
        LoginPageFragment fragment = new LoginPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static LoginPageFragment newInstance(Bundle args) {
        LoginPageFragment fragment = new LoginPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Button loginBt;
    private ImageButton showPassword;
    private EditText passwordET;
    private EditText accountET;
    private TextView forgetTV;

    private ClickFix mClickFix = new ClickFix();
    private boolean isShowPassword = false;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_page_fragment_layout, container, false);

        setMainView(view);

        Bundle bundle = getArguments(); // 取得Bundle

        loginBt = findViewById(R.id.login_page_button);
        passwordET = findViewById(R.id.login_page_editTextText_password);
        accountET = findViewById(R.id.login_page_editTextText_account);
        showPassword = findViewById(R.id.login_page_imageButton_showpassword);
        forgetTV = findViewById(R.id.login_page_textView_forgot);

        String forgetTVText = "忘記密碼？";
        SpannableString content = new SpannableString(forgetTVText);
        content.setSpan(new UnderlineSpan(), 0, forgetTVText.length(), 0);
        forgetTV.setText(content);

        String id =
                getCurrentActivity()
                        .getSharedPreferences("MainSetting", MODE_PRIVATE)
                        .getString("APIPTTID", "");

        accountET.setText(id);

        loginBt.setBackgroundColor(getResources().getColor(R.color.slateGrey));

        loginBt.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String text = accountET.getText().toString();

                        if (!StringUtils.isAccount(text)) {
                            Toast.makeText(getContext(), "format incorrect", Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                        SharedPreferences preference =
                                getCurrentActivity()
                                        .getSharedPreferences("MainSetting", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preference.edit();
                        editor.putString(StringUtils.notNullImageString("APIPTTID"), text);
                        editor.apply();
                        editor.commit();
                        getCurrentActivity().onBackPressed();
                    }
                });

        if (!isShowPassword) {
            showPassword.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_baseline_visibility_off_24));
        } else {
            showPassword.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_baseline_visibility_24));
        }

        showPassword.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (isShowPassword) {
                            showPassword.setImageDrawable(
                                    getResources()
                                            .getDrawable(R.drawable.ic_baseline_visibility_off_24));
                            passwordET.setTransformationMethod(
                                    PasswordTransformationMethod.getInstance());
                        } else {
                            showPassword.setImageDrawable(
                                    getResources()
                                            .getDrawable(R.drawable.ic_baseline_visibility_24));
                            passwordET.setTransformationMethod(null);
                        }
                        passwordET.setSelection(passwordET.getText().length());
                        isShowPassword = !isShowPassword;
                    }
                });

        return view;
    }

    protected void onAnimOver() {}

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager)
                            getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getMainView().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }
}
