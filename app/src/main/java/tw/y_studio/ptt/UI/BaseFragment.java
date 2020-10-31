package tw.y_studio.ptt.UI;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.Fragment;

import tw.y_studio.ptt.Utils.DebugUtils;

public class BaseFragment extends Fragment {

    protected Context mContext = null;
    protected BaseActivity mActivity = null;
    protected View mMainView = null;
    private Handler mUIHandler = new Handler(Looper.getMainLooper());

    public void setActivity(BaseActivity activity){
        this.mActivity = activity;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof BaseActivity){
            this.mActivity = (BaseActivity)activity;
        }



    }

    public <T extends View> T findViewById(int id){
       return (T) mMainView.findViewById(id);
    }

    protected void onAnimOver(){

    }

    protected void runOnUI(Runnable r){
        mUIHandler.post(r);
    }

    protected void setMainView(View view){
        this.mMainView = view;
    }

    protected View getMainView(){
        return this.mMainView;
    }

    private boolean isFirstStart = false;
    @Override
    public Animation onCreateAnimation(int transit, final boolean enter, int nextAnim) {

        if(isFirstStart) return super.onCreateAnimation(transit,enter,nextAnim);

        try{
            Animation anim = AnimationUtils.loadAnimation(mActivity, nextAnim);

            anim.setAnimationListener(new Animation.AnimationListener() {

                public void onAnimationStart(Animation animation) {

                }

                public void onAnimationRepeat(Animation animation) {

                }

                public void onAnimationEnd(Animation animation) {

                    isFirstStart=true;

                    onAnimOver();
                    animation.setAnimationListener(null);

                }
            });
            return anim;
        }catch (Exception e){
            onAnimOver();
            isFirstStart=true;
            return super.onCreateAnimation(transit,enter,nextAnim);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
        this.mActivity = null;
        this.mMainView = null;

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public Context getContext(){
        if(this.mContext == null){
            return super.getContext();
        }else {
            return this.mContext;
        }
    }

    public Activity getCurrentActivity(){
        if(this.mActivity != null){
            return this.mActivity;
        }else if(this.mContext != null){
            return (Activity) this.mContext;
        }else{
            return getActivity();
        }
    }

    public void closeAllFragment(){
        mActivity.closeAllFragment();
    }

    protected Fragment getCurrentFragment(){
        return (Fragment) this;
    }
    public void loadFragment(Fragment toFragment, Fragment thisFragment){

        try {
            mActivity.loadFragment(toFragment,thisFragment);
        } catch (Exception e) {
            e.printStackTrace();
            DebugUtils.Log("loadFragment","Error : "+e.getLocalizedMessage());
        }

    }

    public void loadFragmentNoAnim(Fragment toFragment, Fragment thisFragment){

        try {
            mActivity.loadFragmentNoAnim(toFragment,thisFragment);
        } catch (Exception e) {
            e.printStackTrace();
            DebugUtils.Log("loadFragmentNoAnim","Error : "+e.getLocalizedMessage());
        }

    }
}
