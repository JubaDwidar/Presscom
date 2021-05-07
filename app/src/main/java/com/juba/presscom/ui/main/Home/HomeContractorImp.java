package com.juba.presscom.ui.main.Home;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.juba.presscom.model.Post;

import static com.juba.presscom.utils.Constants.post_node;

public class HomeContractorImp implements HomeContractor {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public Query getPostQuery() {

        return firestore.collection(post_node);    }

    @Override
    public FirestoreRecyclerOptions<Post> getPostList()  {
        return new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(getPostQuery(), Post.class)
                .build();
    }


}
