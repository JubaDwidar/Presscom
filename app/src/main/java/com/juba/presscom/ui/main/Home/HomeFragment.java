package com.juba.presscom.ui.main.Home;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.juba.presscom.R;
import com.juba.presscom.adapter.PostsAdapter;


public class HomeFragment extends Fragment implements HomeContractor.HomeView {
    private static final String TAG = "HomeFragment";
    private RecyclerView postList;
    private PostsAdapter adapter;
    private HomePresenter homePresenter;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        postList = view.findViewById(R.id.posts_view);

        setRecyclerView();
        super.onViewCreated(view, savedInstanceState);
    }

    private void setRecyclerView() {
        homePresenter = new HomePresenter(this);
        postList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostsAdapter(homePresenter.getPostList(), getContext());
        postList.setAdapter(adapter);
        adapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
