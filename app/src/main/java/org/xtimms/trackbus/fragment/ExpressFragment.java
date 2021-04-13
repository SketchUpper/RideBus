package org.xtimms.trackbus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.activity.TimelineActivity;
import org.xtimms.trackbus.adapter.ExpressAdapter;
import org.xtimms.trackbus.model.Route;
import org.xtimms.trackbus.presenter.ExpressFragmentPresenter;

import java.util.List;

public class ExpressFragment extends AppBaseFragment implements ExpressFragmentPresenter.View {

    private RecyclerView mRecyclerView;

    public static ExpressFragment newInstance() {
        return new ExpressFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_express, container, false);
    }

    @Override
    public void setAdapter(List<Route> routeList) {
        ExpressAdapter mExpressAdapter = new ExpressAdapter(routeList);
        mRecyclerView.setAdapter(mExpressAdapter);
        mExpressAdapter.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = TimelineActivity.newIntent(getActivity(), routeList.get(position));
            startActivity(intent);
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerView_express);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        ExpressFragmentPresenter presenter = new ExpressFragmentPresenter(this);
        presenter.setAdapter();
    }
}
