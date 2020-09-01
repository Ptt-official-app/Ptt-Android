package tw.y_studio.ptt.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.y_studio.ptt.Adapter.SimpleAdapter;
import tw.y_studio.ptt.R;
import tw.y_studio.ptt.UI.ClickFix;
import tw.y_studio.ptt.Utils.StringUtils;

import static android.content.Context.MODE_PRIVATE;

public class LoginPageFragment extends Fragment {
    private View Mainview=null;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_page_fragment_layout, container, false);

        Bundle bundle = getArguments();//取得Bundle
        Mainview=view;

        loginBt = view.findViewById(R.id.login_page_button);
        passwordET = view.findViewById(R.id.login_page_editTextText_password);
        accountET = view.findViewById(R.id.login_page_editTextText_account);
        showPassword = view.findViewById(R.id.login_page_imageButton_showpassword);
        forgetTV = view.findViewById(R.id.login_page_textView_forgot);

        String forgetTVText = "忘記密碼？";
        SpannableString content = new SpannableString(forgetTVText);
        content.setSpan(new UnderlineSpan(), 0, forgetTVText.length(), 0);
        forgetTV.setText(content);

        String id = getActivity().getSharedPreferences(
                "MainSetting", MODE_PRIVATE).getString("APIPTTID","");
        accountET.setText(id);

        loginBt.setBackgroundColor(getResources().getColor(R.color.slateGrey));

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = accountET.getText().toString();

                if(!StringUtils.isAccount(text)){
                    Toast.makeText(getContext(),"format incorrect",Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences preference = getActivity().getSharedPreferences(
                        "MainSetting", MODE_PRIVATE);
                SharedPreferences.Editor editor = preference.edit();
                editor.putString(StringUtils.notNullImageString("APIPTTID"),text);
                editor.apply();
                editor.commit();
                getActivity().onBackPressed();
            }
        });


        if(!isShowPassword){
            showPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_visibility_off_24));
        }else{
            showPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_visibility_24));
        }

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //passwordET.setInputType(InputType.TYPE_NULL);
                if(isShowPassword){
                    showPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_visibility_off_24));
                    //passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    showPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_visibility_24));
                    //passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordET.setTransformationMethod(null);
                }
                passwordET.setSelection(passwordET.getText().length());
                //passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isShowPassword=!isShowPassword;
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(Mainview.getWindowToken(), 0);
        }catch (Exception e){

        }

        Mainview=null;
    }
}
