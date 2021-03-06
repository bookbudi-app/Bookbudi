package Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.bookbudiapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import Models.HomeOffersModel;

public class HomeOfferAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<HomeOffersModel> banners;

    public HomeOfferAdapter(List<HomeOffersModel> banners,Context context){

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
        View view = layoutInflater.inflate(R.layout.bannerlayout,container,false);

        ImageView bannerImage = view.findViewById(R.id.offerImage);

        HomeOffersModel model = banners.get(position);

        RequestOptions options = new RequestOptions();
        options.placeholder(R.color.offerImagePlaceholder);

            Glide.with(context).load(model.getOffers()).apply(options).into(bannerImage);


        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
