package com.juba.presscom.ui.main.Home;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.juba.presscom.model.Post;

public interface HomeContractor {

    Query getPostQuery();

    FirestoreRecyclerOptions<Post> getPostList();

    interface HomeView {
    }
}
