package tw.y_studio.ptt.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class BaseActivity extends AppCompatActivity {

    public void closeAllFragment() {}

    public void loadFragment(Fragment toFragment, Fragment thisFragment) throws Exception {}

    public void loadFragmentNoAnim(Fragment toFragment, Fragment thisFragment) throws Exception {}
}
