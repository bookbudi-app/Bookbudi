package Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import Models.LoginBannerModel;
import com.app.bookbudiapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class OnboardAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<LoginBannerModel> banners;

    public OnboardAdapter(List<LoginBannerModel> banners,Context context){

        this.banners = banners;
        this.context = context;
    }

  /*  public OnboardAdapter(Context context){

        this.context = context;
    }  */

  /*  public int[] slide_images = {

            R.drawable.booklover,
            R.drawable.learning,
            R.drawable.studnt
    };

    public String[] slide_desc = {

            "Have spare books,sell it on Bookbudi",
            "Get connected with other students like you",
            "Find your book across thousands of books available"
    };  */

    @Override
    public int getCount() {
        return banners.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.onboard_frag,container,false);

        ImageView slideImage = view.findViewById(R.id.slideImage);
        TextView slideDesc = view.findViewById(R.id.slideDesc);

        LoginBannerModel model = banners.get(position);

        slideDesc.setText(model.getDesc());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.openbook);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);


        Glide.with(context).load(model.getImage()).apply(requestOptions).into(slideImage);

    //    slideImage.setImageResource(slide_images[position]);
      //  slideDesc.setText(slide_desc[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
