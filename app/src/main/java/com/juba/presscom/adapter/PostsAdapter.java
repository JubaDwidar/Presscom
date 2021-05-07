package com.juba.presscom.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.juba.presscom.R;
import com.juba.presscom.model.Post;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends FirestoreRecyclerAdapter<Post, PostsAdapter.PostsHolder> {

    private static final String TAG = "PostsAdapter";
    private Context context;

  /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PostsAdapter(@NonNull FirestoreRecyclerOptions<Post> options, Context context) {
        super(options);
        this.context = context; }


    @NonNull
    @Override
    public PostsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
        Glide.get(parent.getContext());
        return new PostsHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull PostsHolder holder, int position, @NonNull Post model) {
       Glide.with(context)
                .load(model.getuImage())
                .placeholder(R.drawable.profile_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.userImage);

        holder.name.setText(model.getName());
        holder.desc.setText(model.getDesc());
        Glide.with(context)
                .load(model.getImage())
                .into(holder.postPic);


    }



    public class PostsHolder extends RecyclerView.ViewHolder {

        private CircleImageView userImage;
        private TextView desc, name;
        private ImageView postPic;

        public PostsHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.user_post_pic);
            desc = itemView.findViewById(R.id.user_post_desc);
            postPic = itemView.findViewById(R.id.posts_image);
            name = itemView.findViewById(R.id.user_post_name);
        }
    }
}

