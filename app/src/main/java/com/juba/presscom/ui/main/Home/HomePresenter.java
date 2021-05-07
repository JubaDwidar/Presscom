package com.juba.presscom.ui.main.Home;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.juba.presscom.model.Post;

public class HomePresenter {

    private HomeContractor homeContractor;
    private HomeContractor.HomeView homeView;

    public HomePresenter(HomeContractor.HomeView homeView) {
        this.homeView = homeView;
        homeContractor=new HomeContractorImp();
    }

    public FirestoreRecyclerOptions<Post> getPostList() {
        return homeContractor.getPostList();
    }
}
