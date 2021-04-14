package com.example.lab_4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private final int PERMISSION_REQUEST_CODE = 1;
    private final int CREATE_POST = 2;

    private FloatingActionButton buttonAdd;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("login", MODE_PRIVATE);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (!sp.getBoolean("logged", false)) {
            FragmentLogIn frag = new FragmentLogIn();
            ft.replace(R.id.list_layout, frag, "fragment_log");
        }
        else {
            FragmentList frag = new FragmentList();
            ft.replace(R.id.list_layout, frag, "fragment_list");
        }
        ft.addToBackStack(null);

        ft.commit();
    }

    public void AddOnClick(View view){
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    @Override
    public void onAddButtonClicked(FloatingActionButton add) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ActivityPost frag = new ActivityPost();

        ft.replace(R.id.pick_layout, frag, "fragment_pick");
        ft.addToBackStack(null);

        ft.commit();

        buttonAdd = add;
        buttonAdd.setClickable(false);
    }

    private void buttonSetActive(){
        buttonAdd.setClickable(true);
        buttonAdd.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.my_button));
    }

    @Override
    public void onCloseButtonClicked() {
        closeKeyboard();
        buttonSetActive();
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
    }

    @Override
    public void onConfirmButtonClicked(String header, Uri imageUri, String desc) {
        closeKeyboard();
        FragmentManager fm = getSupportFragmentManager();
        FragmentList frag = (FragmentList) fm.findFragmentById(R.id.list_layout);
        frag.addPost(new Post(header, imageUri, desc));

        buttonSetActive();
        fm.popBackStack();
        Toast.makeText(this,"Posted", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLogInClicked(String name) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        FragmentList frag = new FragmentList();

        ft.replace(R.id.list_layout, frag, "fragment_list");
        ft.addToBackStack(null);

        ft.commit();
    }

    @Override
    public void onSignUpClicked() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        FragmentSignUp frag = new FragmentSignUp();

        ft.replace(R.id.list_layout, frag, "fragment_sign");
        ft.addToBackStack(null);

        ft.commit();
    }

    @Override
    public void onConfirmSignClicked() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.remove(fm.findFragmentById(R.id.list_layout));

        FragmentLogIn frag = new FragmentLogIn();

        ft.replace(R.id.list_layout, frag, "fragment_log");
        ft.addToBackStack(null);

        ft.commit();

        fm.executePendingTransactions();
        frag.setUsernamePass();
    }

    @Override
    public void onCancelClicked() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        FragmentLogIn frag = new FragmentLogIn();

        ft.replace(R.id.list_layout, frag, "fragment_log");
        ft.addToBackStack(null);
        //ft.remove(fm.findFragmentById(R.id.pick_layout));

        ft.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.list_layout) instanceof FragmentList) {
            if (fm.findFragmentById(R.id.pick_layout) instanceof ActivityPost ||
                    fm.findFragmentById(R.id.pick_layout) instanceof FragmentFullPost) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(fm.findFragmentById(R.id.pick_layout));
                ft.commit();
                //fm.popBackStack();
                onCloseButtonClicked();
                System.out.println("yes!!!!");
            } else
                dialogOnClick();
        }
        else if (fm.findFragmentById(R.id.list_layout) instanceof FragmentSignUp)
            onCancelClicked();
        else
            finish();
    }

    public void dialogOnClick (){
        AlertDialog.Builder dial = new AlertDialog.Builder(this, R.style.MyDialogTheme);

        dial.setMessage(getString(R.string.exit_dialog)).setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListAdapter.stopPlay();
                        sp.edit().putBoolean("logged", false).apply();
                        onCancelClicked();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = dial.create();
        alert.setTitle(getString(R.string.log_out));
        alert.show();
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onUserButtonClick(){
        dialogOnClick();
    }

    @Override
    public void onItemClick(Post post) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        FragmentFullPost frag = new FragmentFullPost();

        ft.replace(R.id.pick_layout, frag, "fragment_post");
        ft.addToBackStack(null);

        ft.commit();
        fm.executePendingTransactions();

        frag.fillPost(post);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_POST){
            if (resultCode == RESULT_OK){
                buttonSetActive();
                Toast.makeText(this,"Posted", Toast.LENGTH_LONG).show();
            }
            else {
                buttonSetActive();
                Toast.makeText(this,"Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onCloseFullButtonClicked() {
        buttonSetActive();
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
    }
}