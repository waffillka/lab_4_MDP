package com.example.lab_4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_4.Models.Post;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder> {

    private List<Post> posts = new ArrayList<>();
    private onItemCickListener mListener;

    public myAdapter(List<Post> posts, onItemCickListener listener){
        this.posts = posts;
        mListener = listener;
    }

    public void setItems(Collection<Post> posts){
        this.posts.clear();
        this.posts.addAll(posts);
        notifyDataSetChanged();
    }

    public void clearItems(){
        posts.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_item, parent, false);
        return new myViewHolder(view, mListener);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.bind(posts.get(position));
    }

    class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView header;
        private ImageView picture;
        private TextView description;
        onItemCickListener listener;

        public myViewHolder(View item, onItemCickListener listener){
            super(item);
            header = item.findViewById(R.id.my_header);
            picture = item.findViewById(R.id.my_pic);
            description = item.findViewById(R.id.my_desc);
            this.listener = listener;

            item.setOnClickListener(this);
        }

        public void bind(Post post){
            header.setText(post.getHeader());
            picture.setImageURI(post.getImage());
            description.setText(post.getDescription());

            picture.setVisibility(post.getImage() != null ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getAdapterPosition());
        }
    }

    public interface onItemCickListener{
        void onItemClick(int position);
    }
}