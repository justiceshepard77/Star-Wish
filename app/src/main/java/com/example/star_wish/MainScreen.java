package com.example.star_wish;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        ArrayList<String> ImgList = new ArrayList<String>();
        ArrayList<String> TextList = new ArrayList<String>();
        ImageView im1 = findViewById(R.id.im1);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                    try {
                        org.jsoup.nodes.Document doc = (org.jsoup.nodes.Document) Jsoup.connect("https://buybuggle.com/pages/21-gadgets/")
                                                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                                                        .referrer("http://www.google.com")
                                                        .timeout(1000*5)
                                                        .get();
                        Elements allinfo = (Elements) doc.getElementsByTag("img");
                        //System.out.println(allinfo);
                        for (Element element : allinfo){
                            try {
                                String imgs = element.attr("src");
                                String[] imgs2 = imgs.split("\n");
                                for (int j = 0; j < imgs2.length; j++){
                                    String string = "https://buybuggle.com/pages/21-gadgets/";
                                    string = string + imgs2[j];
                                    ImgList.add(string);
                                }
                            } catch(Exception e){ }
                        }
                        System.out.println(ImgList);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(im1).load(ImgList.get(1)).into(im1);
                            }
                        });
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (HttpStatusException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        });
    }

    public void showAlert(View view) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out?");

        // add the buttons
        builder.setPositiveButton("Log out", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                logout_user(view);
            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void logout_user(View view) {
        FirebaseAuth.getInstance().signOut();
        //startActivity(new Intent(this, FirstActivity.class));
        Toast.makeText(this, "Logged out", Toast.LENGTH_LONG).show();
        finish();
    }

}