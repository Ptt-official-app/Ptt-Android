package tw.y_studio.ptt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import tw.y_studio.ptt.R;
import tw.y_studio.ptt.adapter.SearchBoardsAdapter;
import tw.y_studio.ptt.ui.BaseFragment;
import tw.y_studio.ptt.ui.CustomLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonInfoFragment extends BaseFragment {

    public static PersonInfoFragment newInstance() {
        Bundle args = new Bundle();
        PersonInfoFragment fragment = new PersonInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static PersonInfoFragment newInstance(Bundle args) {
        PersonInfoFragment fragment = new PersonInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView recyclerView;
    private SearchBoardsAdapter mdapter;
    private List<Map<String, Object>> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_info_fragment_layout, container, false);

        setMainView(view);

        recyclerView = findViewById(R.id.persion_info_fragment_recyclerView);

        Bundle bundle = getArguments(); // 取得Bundle
        final String title_ = bundle.getString("Title");

        mdapter = new SearchBoardsAdapter(getCurrentActivity(), data);

        final CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mdapter);

        return view;
    }

    protected void onAnimOver() {}
}
