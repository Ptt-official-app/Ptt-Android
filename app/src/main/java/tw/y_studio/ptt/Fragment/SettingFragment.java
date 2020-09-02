package tw.y_studio.ptt.Fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
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
import tw.y_studio.ptt.HomeActivity;
import tw.y_studio.ptt.R;
import tw.y_studio.ptt.UI.BaseFragment;
import tw.y_studio.ptt.UI.ClickFix;
import tw.y_studio.ptt.UI.CustomLinearLayoutManager;
import tw.y_studio.ptt.UI.StaticValue;
import tw.y_studio.ptt.Utils.DebugUtils;
import tw.y_studio.ptt.Utils.StringUtils;
import tw.y_studio.ptt.Utils.WebUtils;

import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends BaseFragment {
    private View Mainview=null;
    public static SettingFragment newInstance() {
        Bundle args = new Bundle();
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public static SettingFragment newInstance(Bundle args) {
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SimpleAdapter mAdapter;

    private List<Map<String, Object>> data;


    //private String BoardName = "";
    //private String BoardSubName = "";
    //private TextView mTextView_BoardName;
    //private TextView mTextView_BoardSubName;
    //private AppCompatImageView Go2Back;

    //private BottomNavigationView navigation;
    //private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    private ClickFix mClickFix = new ClickFix();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hot_article_list_fragment_layout, container, false);

        Mainview=view;

        data = new ArrayList<>();
        Bundle bundle = getArguments();//取得Bundle

        mRecyclerView = Mainview.findViewById(R.id.article_list_fragment_recyclerView);

        mAdapter = new SimpleAdapter(getThisActivity(),data);

        final CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mSwipeRefreshLayout= Mainview.findViewById(R.id.article_list_fragment_refresh_layout);
        //mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {

                    @Override
                    public void onRefresh() {

                        //loadData();
                        mSwipeRefreshLayout.setRefreshing(false);

                    }

                });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();


                if(!GattingData)
                    if (lastVisibleItem >= totalItemCount - 30 ) {
                        // loadNextData();
                    }


            }
        });

        mAdapter.setOnItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view,final int position) {
                if(mClickFix.isFastDoubleClick()) return;
                SharedPreferences preference2 = getThisActivity().getSharedPreferences(
                        "MainSetting", MODE_PRIVATE);
                if(StringUtils.notNullString(data.get(position).get("type")).equals("int")){
                    androidx.appcompat.app.AlertDialog.Builder builder25 = new androidx.appcompat.app.AlertDialog.Builder(getContext()); // 創建訊息方塊
                    builder25.setTitle((int)data.get(position).get("titleKey"));
                    builder25.setSingleChoiceItems((int)data.get(position).get("valueArrayKey"), preference2.getInt(StringUtils.notNullImageString(data.get(position).get("key")),(int)data.get(position).get("defaultValue")), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                            SharedPreferences preference = getThisActivity().getSharedPreferences(
                                    "MainSetting", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preference.edit();
                            editor.putInt(StringUtils.notNullImageString(data.get(position).get("key")),which);
                            editor.apply();
                            editor.commit();
                            switch (((int)data.get(position).get("position"))){
                                case 0:
                                    int theme = which;

                                    if(theme==1){

                                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                                        //setTheme(R.style.AppLightTheme);
                                    }else if(theme==0){
                                        //setTheme(R.style.AppTheme);
                                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                    }else {
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                                    }
                                    break;
                            }


                        }
                    });

                    builder25.setPositiveButton(R.string.clen_buttom, new DialogInterface.OnClickListener() {

                        @Override

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    });

                    // builder25.setCancelable(false);//案空白處不關閉
                    androidx.appcompat.app.AlertDialog alertDialog6 =builder25.create();
                    alertDialog6.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
                    alertDialog6.show();
                    alertDialog6.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                }else if(((int)data.get(position).get("position"))==4){
                    showPolicy();
                }else if(StringUtils.notNullString(data.get(position).get("type")).equals("string")){

                    if(((int)data.get(position).get("position"))==5) {
                        try {
                            ((HomeActivity)getContext()).loadFragment(LoginPageFragment.newInstance(),getParentFragment());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }

                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    View Layout = inflater.inflate(R.layout.edit_dialog_layout, null);
                    EditText editBox = Layout.findViewById(R.id.edit_dialog_editText);
                    editBox.setHint((int)data.get(position).get("hintKey"));
                    androidx.appcompat.app.AlertDialog.Builder builder25 = new androidx.appcompat.app.AlertDialog.Builder(getContext()); // 創建訊息方塊
                    builder25.setTitle((int)data.get(position).get("titleKey"));
                    //builder25.setMessage("input password");
                    editBox.setText(getThisActivity().getSharedPreferences(
                            "MainSetting", MODE_PRIVATE).getString(StringUtils.notNullImageString(data.get(position).get("key")),""));

                    if(((int)data.get(position).get("position"))==5){
                        editBox.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    }

                    builder25.setView(Layout);
                    builder25.setPositiveButton("save", new DialogInterface.OnClickListener() {

                        @Override

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            if(((int)data.get(position).get("position"))==5){
                                if(!StringUtils.isAccount(editBox.getText().toString())){
                                    Toast.makeText(getContext(),"format incorrect",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            SharedPreferences preference = getThisActivity().getSharedPreferences(
                                    "MainSetting", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preference.edit();
                            editor.putString(StringUtils.notNullImageString(data.get(position).get("key")),editBox.getText().toString());
                            editor.apply();
                            editor.commit();


                        }

                    });

                    // builder25.setCancelable(false);//案空白處不關閉
                    androidx.appcompat.app.AlertDialog alertDialog6 =builder25.create();
                    alertDialog6.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
                    alertDialog6.show();
                    alertDialog6.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));

                }



            }
        });

        return view;
    }

    protected void onAnimOver() {
        loadData();
    }


    private void showPolicy(){
        String URL = "https://www.ptt.cc/index.ua.html";
        WebUtils.turnOnUrl(getContext(),URL);
    }
    private boolean GattingData = false;
    private void loadData(){
        if(GattingData) return;

        data.clear();
        mAdapter.notifyDataSetChanged();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Map<String,Object> datt=new HashMap<>();
            datt.put("title",getThisActivity().getString(R.string.setting_theme));
            datt.put("position",0);
            datt.put("type","int");
            datt.put("key","THEME");
            datt.put("titleKey",R.string.setting_theme);
            datt.put("valueArrayKey",Build.VERSION.SDK_INT >= Build.VERSION_CODES.P?R.array.setting_theme_array:R.array.setting_theme_array_low_p);
            datt.put("defaultValue",0);
            data.add(datt);
        }

        if(true){
            Map<String,Object> datt=new HashMap<>();
            datt.put("title",getThisActivity().getString(R.string.setting_search_item_style));
            datt.put("position",2);
            datt.put("type","int");
            datt.put("key","SEARCHSTYLE");
            datt.put("titleKey",R.string.setting_search_item_style);
            datt.put("valueArrayKey",R.array.setting_search_item_style_array);
            datt.put("defaultValue",0);
            data.add(datt);
        }
        if(true){
            Map<String,Object> datt=new HashMap<>();
            datt.put("title",getThisActivity().getString(R.string.setting_post_bottom_style));
            datt.put("position",5);
            datt.put("type","int");
            datt.put("key","POSTBOTTOMSTYLE");
            datt.put("titleKey",R.string.setting_post_bottom_style);
            datt.put("valueArrayKey",R.array.setting_post_bottom_style_array);
            datt.put("defaultValue",0);
            data.add(datt);
        }
         if(true){
            Map<String,Object> datt=new HashMap<>();
            datt.put("title",getThisActivity().getString(R.string.ptt_policy));
            datt.put("position",4);
            data.add(datt);
        }
        if(true){
            Map<String,Object> datt=new HashMap<>();
            datt.put("title",getThisActivity().getString(R.string.set_ptt_id));
            datt.put("position",5);
            datt.put("type","string");
            datt.put("key","APIPTTID");
            datt.put("titleKey",R.string.set_ptt_id);
            datt.put("hintKey",R.string.set_ptt_id);
            data.add(datt);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(data!=null)
            data.clear();
    }
}
