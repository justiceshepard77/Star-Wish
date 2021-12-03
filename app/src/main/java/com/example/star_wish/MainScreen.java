package com.example.star_wish;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                    try {
                        org.jsoup.nodes.Document doc = (org.jsoup.nodes.Document) Jsoup.connect("https://buybuggle.com/pages/21-gadgets/nw.html?f=1DHjBmtIfjyt&n=1DHjOzELLM0N&ts=0hoBgmBju8Sd&utm_source=google&utm_medium=cpc&utm_content=128345719632&utm_term=hi%20tech%20gadgets&utm_campaign=14818841512&campid=14818841512&adGroupId=128345719632&feedItemId=&target=kwd-10999971&locInterestMs=&locPhysicalMs=9031645&matchtype=b&network=g&device=c&devicemodel=&deviceType=desktop&campaignType=search&creative=548956418118&keyword=hi%20tech%20gadgets&placement=&category=&cacheBuster=13372771987901311262&adposition=&cid={cid}&gclid=Cj0KCQiA-qGNBhD3ARIsAO_o7ympWKGyKPw4kroOu5KcgKWsK_zdAY_GhsPD8A0WvyN3fTwx1cT4OSYaAm0rEALw_wcB&vid=fYVlvOt8BzftIhpD4neFsMOh8x")
                                                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                                                        .referrer("http://www.google.com")
                                                        .timeout(1000*5)
                                                        .get();
                        org.w3c.dom.Element allinfo = (Element) doc.getElementById("multiBrandAdvertorial");
                        Log.e("allinfo", allinfo.toString());
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