package com.example.moneycontrol;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ArticlesFragments extends Fragment {
    private View ArticlesView;
    private RecyclerView myArticlesList;
    private DatabaseReference ArticlesRef, UsersRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    public ArticlesFragments() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ArticlesView= inflater.inflate(R.layout.articles, container, false);
        myArticlesList= ArticlesView.findViewById(R.id.recycler_articles);
        myArticlesList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth = FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        ArticlesRef = FirebaseDatabase.getInstance().getReference().child("Articles").child(currentUserId);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("User");
        return ArticlesView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Articles>()
                .setQuery(ArticlesRef, Articles.class)
                .build();
        FirebaseRecyclerAdapter<Articles,ArticlesViewHolder> adapter
                = new FirebaseRecyclerAdapter<Articles, ArticlesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ArticlesViewHolder holder, int position, @NonNull Articles model) {
            String usersIDS = getRef(position).getKey();
            UsersRef.child(usersIDS).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("image")){
                        String ArticleImage= dataSnapshot.child("image").getValue().toString();
                        String ArticleName= dataSnapshot.child("name").getValue().toString();
                        String ArticleDesc= dataSnapshot.child("description").getValue().toString();
                        String ArticlePrice= dataSnapshot.child("price").getValue().toString();
                        holder.articleName.setText(ArticleName);
                        holder.articleDescription.setText(ArticleDesc);
                        holder.articlePrice.setText(ArticlePrice);
                        Glide.with(getContext()).load(ArticleImage).placeholder(R.drawable.ic_item).into(holder.imgArticle);

                    }else{
                        String ArticleName= dataSnapshot.child("name").getValue().toString();
                        String ArticleDesc= dataSnapshot.child("description").getValue().toString();
                        String ArticlePrice= dataSnapshot.child("price").getValue().toString();
                        holder.articleName.setText(ArticleName);
                        holder.articleDescription.setText(ArticleDesc);
                        holder.articlePrice.setText(ArticlePrice);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            }

            @NonNull
            @Override
            public ArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.article_item, viewGroup, false);
                ArticlesViewHolder viewHolder = new ArticlesViewHolder(view);
                return viewHolder;
            }
        };

        myArticlesList.setAdapter(adapter);
        adapter.startListening();
    }
    public static class ArticlesViewHolder extends RecyclerView.ViewHolder{
        TextView articleName, articleDescription, articlePrice;
        ImageView imgArticle;

        public ArticlesViewHolder(@NonNull View itemView) {
            super(itemView);
            articleName=itemView.findViewById(R.id.article_name);
            articleDescription =itemView.findViewById(R.id.article_desc);
            articlePrice=itemView.findViewById(R.id.article_price);
            imgArticle=itemView.findViewById(R.id.article_img);
        }
    }
}
