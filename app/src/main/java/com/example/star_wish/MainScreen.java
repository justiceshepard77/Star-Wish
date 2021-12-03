package com.example.star_wish;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainScreen extends AppCompatActivity {

    String[] categories = {"Best Selling", "For Mom", "For Dad", "Electronics", "Home"};
    private String selectedCategory = "Best Selling";
    private String oldCategory = "Best Selling";


    private ImageView bestSellingImageView;
    private ImageView momImageView;
    private ImageView dadImageView;
    private ImageView electronicsImageView;
    private ImageView homeImageView;

    int bestSellingImage = R.drawable.bestselling;
    int forMomImage = R.drawable.mom;
    int forDadImage = R.drawable.dad;
    int electronicsImage = R.drawable.electronics;
    int homeImage = R.drawable.home;

    int bestSellingImage_selected = R.drawable.bestselling_selected;
    int forMomImage_selected = R.drawable.mom_selected;
    int forDadImage_selected = R.drawable.dad_selected;
    int electronicsImage_selected = R.drawable.electronics_selected;
    int homeImage_selected = R.drawable.home_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
         bestSellingImageView = findViewById(R.id.bestSellingImageView);
         momImageView = findViewById(R.id.momImageView);
         dadImageView = findViewById(R.id.dadImageView);
         electronicsImageView = findViewById(R.id.electronicsImageView);
         homeImageView = findViewById(R.id.homeImageView);

        addButtons();
    }

    public void deselectOldCategory(String oldCat, String newCat) {

        switch (oldCat) {
            case "Best Selling":
                bestSellingImageView.setImageResource(bestSellingImage);
            case "For Mom":
                momImageView.setImageResource(forMomImage);
            case "For Dad":
                dadImageView.setImageResource(forDadImage);
            case "Electronics":
                electronicsImageView.setImageResource(electronicsImage);
            case "Home":
                homeImageView.setImageResource(homeImage);
            default:
                Log.v("Star Wish","In the default case for some reason...");
        }
        oldCategory = newCat;
    }

    public void addButtons() {
        LinearLayout bestSellingButton = (LinearLayout )findViewById(R.id.bestSellingButton);
        LinearLayout forMomButton = (LinearLayout )findViewById(R.id.forMomButton);
        LinearLayout forDadButton = (LinearLayout )findViewById(R.id.forDadButton);
        LinearLayout electronicsButton = (LinearLayout )findViewById(R.id.electronicsButton);
        LinearLayout homeButton = (LinearLayout )findViewById(R.id.homeButton);

        bestSellingImageView.setImageResource(bestSellingImage_selected);

        bestSellingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Star Wish", "Pressed bestSellingButton!");
                selectedCategory = "Best Selling";
                Log.v("Star Wish", oldCategory);
                Log.v("Star Wish", selectedCategory);

                if (oldCategory.equals(selectedCategory)) {
                    // do nothing
                    Log.v("Star Wish", "Already selected that category!");

                    return;
                }
                deselectOldCategory(oldCategory,selectedCategory);
                bestSellingImageView.setImageResource(bestSellingImage_selected);

            }
        });

        forMomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Star Wish", "Pressed forMomButton!");
                selectedCategory = "For Mom";
                Log.v("Star Wish", oldCategory);
                Log.v("Star Wish", selectedCategory);

                if (oldCategory.equals(selectedCategory)) {
                    // do nothing
                    Log.v("Star Wish", "Already selected that category!");

                    return;
                }
                deselectOldCategory(oldCategory,selectedCategory);
                momImageView.setImageResource(forMomImage_selected);

            }
        });
        forDadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Star Wish", "Pressed forDadButton!");
                selectedCategory = "For Dad";
                Log.v("Star Wish", oldCategory);
                Log.v("Star Wish", selectedCategory);

                if (oldCategory.equals(selectedCategory)) {
                    // do nothing
                    Log.v("Star Wish", "Already selected that category!");

                    return;
                }
                deselectOldCategory(oldCategory,selectedCategory);
                dadImageView.setImageResource(forDadImage_selected);

                }
        });
        electronicsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Star Wish", "Pressed electronicsButton!");
                selectedCategory = "Electronics";

                if (oldCategory.equals(selectedCategory)) {
                    // do nothing
                    Log.v("Star Wish", "Already selected that category!");

                    return;
                }
                deselectOldCategory(oldCategory,selectedCategory);
                electronicsImageView.setImageResource(electronicsImage_selected);
            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Star Wish", "Pressed homeButton!");
                selectedCategory = "Home";

                if (oldCategory.equals(selectedCategory)) {
                    // do nothing
                    Log.v("Star Wish", "Already selected that category!");

                    return;
                }
                deselectOldCategory(oldCategory,selectedCategory);

                homeImageView.setImageResource(homeImage_selected);

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