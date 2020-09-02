package tw.y_studio.ptt.UI;

import android.app.Activity;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    protected Context mContext = null;
    protected Activity mActivity = null;

    public void setActivity(Activity activity){
        this.mActivity = activity;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;

    }

    protected void onAnimOver() {
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

    }

    @Override
    public Context getContext(){
        if(this.mContext == null){
            return super.getContext();
        }else {
            return this.mContext;
        }
    }

    public Activity getThisActivity(){
        if(this.mActivity != null){
            return this.mActivity;
        }else if(this.mContext != null){
            return (Activity) this.mContext;
        }else{
            return getActivity();
        }
    }
}
