package com.app.bookbudiapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.List;

public class BannerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    Context context;
    private List<BannerModel> banners;

    public BannerAdapter(List<BannerModel> banners,Context context){

        this.banners = banners;
        this.context = context;
    }

    @Override
    public int getCount() {
        return banners.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.bannerlayout, null);

        ImageView offerImage = view.findViewById(R.id.offerImage);

        BannerModel model = banners.get(position);

        Glide.with(context).load(model.getBannerImage()).into(offerImage);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((LinearLayout)object);
    }
}
