package com.example.lab_4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lab_4.Models.Post;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;


public class FragmentFullPost extends Fragment implements View.OnClickListener {

    private EditText header;
    private ImageView image;
    private TextView desc;
    private ImageButton cross;
    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        container.setClickable(true);
        return inflater.inflate(R.layout.fragment_full_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        header = getView().findViewById(R.id.my_full_header);
        image = getView().findViewById(R.id.my_full_pic);
        desc = getView().findViewById(R.id.my_full_desc);
        cross = getView().findViewById(R.id.button_close_full_post);
        cross.setOnClickListener(this);

    }

    public void fillPost(Post post){
        this.header.setText(post.getHeader());
        this.image.setImageURI(post.getImage());
        this.desc.setText(post.getDescription());

        BetterLinkMovementMethod
                .linkify(Linkify.ALL, desc)
                .setOnLinkClickListener((textView, url) -> {

                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                })
                .setOnLinkLongClickListener((textView, url) -> {
                    // Handle long-clicks.
                    return true;
                });
    }

    @Override
    public void onClick(View v) {
        OnButtonClickListener listener = (OnButtonClickListener) getActivity();
        listener.onCloseFullButtonClicked();
    }

    public interface OnButtonClickListener {
        void onCloseFullButtonClicked();
    }
}