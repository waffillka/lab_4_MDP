package com.example.lab_4;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lab_4.Models.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class FragmentList extends Fragment implements View.OnClickListener, myAdapter.onItemCickListener{

    FloatingActionButton buttonAdd;
    private View head;
    private TextView curUser;
    private RecyclerView recycler;
    private myAdapter adapter;

    private List<Post> posts;

    SharedPreferences sp;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //
        buttonAdd = getView().findViewById(R.id.buttonAdd);
        head = getView().findViewById(R.id.head);
        curUser = getView().findViewById(R.id.cur_user);
        buttonAdd.setOnClickListener(this);
        head.setOnClickListener(this);
        curUser.setOnClickListener(this);

        sp = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        curUser.setText(sp.getString("username", "admin"));

        posts = new ArrayList<>();

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        adapter = new myAdapter(posts, this);
        recycler = getView().findViewById(R.id.my_list);
        recycler.addItemDecoration(itemDecorator);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);
    }

    public void addPost(Post post){
        posts.add(0, post);
        //adapter.setItems(posts);
        adapter.notifyDataSetChanged();

        LinearLayoutManager layoutManager = (LinearLayoutManager) recycler
                .getLayoutManager();
        layoutManager.scrollToPositionWithOffset(0, 0);
    }

    @Override
    public void onClick(View view) {
        OnAddButtonListener listener = (OnAddButtonListener) getActivity();
        if (view.getId() == R.id.buttonAdd) {
            listener.onAddButtonClicked(buttonAdd);
        }
        if (view.getId() == R.id.head){
            RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(getContext()) {
                @Override protected int getVerticalSnapPreference() {
                    return LinearSmoothScroller.SNAP_TO_START;
                }
            };
            smoothScroller.setTargetPosition(0);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recycler
                    .getLayoutManager();
            layoutManager.startSmoothScroll(smoothScroller);
        }
        if (view.getId() == R.id.cur_user){
            listener.onUserButtonClick();
        }
    }

    public void setImageMusic(Uri imageUri, Uri musicUri){
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        OnAddButtonListener listener = (OnAddButtonListener) getActivity();
        listener.onItemClick(posts.get(position));
    }

    public interface OnAddButtonListener {
        void onAddButtonClicked(FloatingActionButton add);
        void onUserButtonClick();
        void onItemClick(Post post);
    }

}