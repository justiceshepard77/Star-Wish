package com.example.star_wish;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Gift> gifts;
    LayoutInflater inflter;
    public CustomAdapter(Context applicationContext, ArrayList<Gift> gifts) {
        this.context = applicationContext;
        this.gifts = gifts;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return gifts.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.gift_grid_item, null); // inflate the layout
        ImageView giftImage = (ImageView) view.findViewById(R.id.giftImage); // get the reference of ImageView
        TextView textView = view.findViewById(R.id.giftTitle);
        TextView PriceView = view.findViewById(R.id.Price);
        TextView Description = view.findViewById(R.id.Description);


        textView.setText(gifts.get(i).title);
        PriceView.setText(gifts.get(i).cost);
        Description.setText(gifts.get(i).description);

        new CustomAdapter.DownloadedImageFromUrl((ImageView) giftImage).execute(gifts.get(i).imageUrl);

        return view;
    }


    private class DownloadedImageFromUrl extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadedImageFromUrl(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}


