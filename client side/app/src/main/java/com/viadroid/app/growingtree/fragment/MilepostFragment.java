package com.viadroid.app.growingtree.fragment;

import android.os.Bundle;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.viadroid.app.growingtree.App;
import com.viadroid.app.growingtree.R;
import com.viadroid.app.growingtree.activity.AddMilepostActivity;
import com.viadroid.app.growingtree.adapter.MilepostAdapter;
import com.viadroid.app.growingtree.base.BaseFragment;
import com.viadroid.app.growingtree.base.BaseRvAdapter;
import com.viadroid.app.growingtree.entries.Milestone;
import com.viadroid.app.growingtree.entries.MilestoneList;
import com.viadroid.app.growingtree.entries.ResultWrapper;
import com.viadroid.app.growingtree.util.Constants;
import com.viadroid.app.growingtree.util.GsonUtil;
import com.viadroid.app.growingtree.util.L;
import com.viadroid.app.growingtree.util.OkHttp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 里程碑
 */
public class MilepostFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MilepostFragment";

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    private MilepostAdapter mAdapter;
    private List<Milestone> mMilestones = new ArrayList<>();

    public static MilepostFragment newInstance() {
        MilepostFragment fragment = new MilepostFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_milepost;
    }

    @Override
    protected void init() {

        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new MilepostAdapter(mMilestones);
        recycler_view.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseRvAdapter.OnItemClickListener<Milestone>() {
            @Override
            public void onItemClick(View view, Milestone data, int position) {
//                showToast("click item:" + position);
            }
        });
        getData();

        mRefreshLayout.setOnRefreshListener(this);
    }


    @Override
    public void onRefresh() {
        getData();
    }

    @OnClick(R.id.add)
    void addMilepost() {
        startActivity(AddMilepostActivity.class);
    }

    private void getData() {

        String bid = App.getApp().getBaby().getId() + "";


        OkHttp.getInstance()
                .addQueryParameter("bid", bid)
                .asyncGet(Constants.GET_MILESTONE_LIST_URL, new OkHttp.NetCallBack() {
                    @Override
                    public void onFailure(Call call) {
                        showToast("Network Error" + call.request().url());
                        mRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(Call call, Response response, String bodyStr) {
                        Type type = new TypeToken<ResultWrapper<MilestoneList>>() {
                        }.getType();
                        ResultWrapper<MilestoneList> result = GsonUtil.fromJson(bodyStr, type);
//                        showToast(result.getMsg());

                        if (result.getCode() == 200) {
                            L.d(TAG, "onSuccess: " + result.getData().getList().size());
                            mMilestones.clear();
                            mMilestones.addAll(result.getData().getList());
                            mAdapter.notifyDataSetChanged();
                        }
                        mRefreshLayout.setRefreshing(false);
                    }
                });

    }
}
