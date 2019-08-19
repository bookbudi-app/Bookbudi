package Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bookbudiapp.BookDetail;
import com.app.bookbudiapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.util.ArrayList;

import Models.LoadHomeBooks;

/*public class HomeBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int LAYOUT_ADS= 0;
    private static final int LAYOUT_BOOKS= 1;

    private ArrayList<LoadHomeBooks> list;
    private Context context;

    VideoController mVideoController;

    public HomeBookAdapter(ArrayList<LoadHomeBooks> list,Context context){

        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {

        if((position % 2) == 0){

            return LAYOUT_ADS;
        }else{

            return LAYOUT_BOOKS;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view =null;
        RecyclerView.ViewHolder viewHolder = null;

        if(viewType==LAYOUT_ADS)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adview_layout,parent,false);
            viewHolder = new AdViewHolder(view);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_book_row,parent,false);
            viewHolder= new BookViewHolder(view);
        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder.getItemViewType() == LAYOUT_ADS){

            AdViewHolder adHolder = (AdViewHolder)holder;

            adHolder.mAdView.setAdUnitId("ca-app-pub-1218708258118994/3329063313");
            adHolder.mAdView.setAdSize(new AdSize(AdSize.FULL_WIDTH, 200));

            adHolder.mAdView.setVideoOptions(new VideoOptions.Builder().setStartMuted(true).build());

            mVideoController = ((AdViewHolder) holder).mAdView.getVideoController();

            mVideoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });

            ((AdViewHolder) holder).mAdView.setAdListener(new AdListener(){

                @Override
                public void onAdLoaded() {
                    if(mVideoController.hasVideoContent()){

                    }else{

                    }
                }
            });

            ((AdViewHolder) holder).mAdView.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());

        }else{

            BookViewHolder bookHolder = (BookViewHolder)holder;

            LoadHomeBooks model = list.get(position);

            final String bookId = model.getbUid();
            final String bookName = model.getbName();
            final String bookImage = model.getbImage();
            final String bookSub = model.getbSub();
            final String bookClass = model.getbClass();
            //User Id
            final String bId = model.getbId();

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.openbook);

            Glide.with(context).load(model.getbImage()).apply(requestOptions).into(bookHolder.homeBookImage);

            bookHolder.homeBookName.setText(model.getbName());
            bookHolder.homeSubject.setText(model.getbSub());
            bookHolder.homeClass.setText(model.getbClass());

            bookHolder.homeMore.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent i = new Intent(context, BookDetail.class);
                    i.putExtra("bId",bookId);
                    i.putExtra("bookName",bookName);
                    i.putExtra("bookImage",bookImage);
                    i.putExtra("bookSub",bookSub);
                    i.putExtra("bookClass",bookClass);
                    i.putExtra("uid",bId);
                    context.startActivity(i);
                    ((Activity)context).finish();

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {

        NativeExpressAdView mAdView;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);

            mAdView = itemView.findViewById(R.id.mAdView);
        }
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        TextView homeBookName,homeSubject,homeClass;
        Button homeMore;
        ImageView homeBookImage;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            homeBookName = itemView.findViewById(R.id.homeBookName);
            homeSubject = itemView.findViewById(R.id.homeSubject);
            homeClass = itemView.findViewById(R.id.homeClass);
            homeMore = itemView.findViewById(R.id.homeMore);
            homeBookImage = itemView.findViewById(R.id.homeBookImage);

        }
    }

    public void setFilter(ArrayList<LoadHomeBooks> filterBooks){

        list = new ArrayList<>();
        list.addAll(filterBooks);
        notifyDataSetChanged();

    }
}  */





/**
 * Original code
 */

public class HomeBookAdapter extends RecyclerView.Adapter<HomeBookAdapter.ViewHolder> {

    private ArrayList<LoadHomeBooks> list;
    private Context context;

    public HomeBookAdapter(ArrayList<LoadHomeBooks> list,Context context){

        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_book_row_layout,viewGroup,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBookAdapter.ViewHolder viewHolder, int i) {

        LoadHomeBooks model = list.get(i);

        final String bookId = model.getbUid();
        final String bookName = model.getbName();
        final String bookImage = model.getbImage();
        final String bookSub = model.getbSub();
        final String bookClass = model.getbClass();
        //User Id
        final String bId = model.getbId();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.openbook);

        Glide.with(context).load(model.getbImage()).apply(requestOptions).into(viewHolder.homeBookImage);

        viewHolder.homeBookName.setText(model.getbName());
        viewHolder.homeSubject.setText(model.getbSub());
        viewHolder.homeClass.setText(model.getbClass());

        viewHolder.homeMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, BookDetail.class);
                i.putExtra("bId",bookId);
                i.putExtra("bookName",bookName);
                i.putExtra("bookImage",bookImage);
                i.putExtra("bookSub",bookSub);
                i.putExtra("bookClass",bookClass);
                i.putExtra("uid",bId);
                context.startActivity(i);
                ((Activity)context).finish();

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView homeBookName,homeSubject,homeClass;
        Button homeMore;
        ImageView homeBookImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            homeBookName = itemView.findViewById(R.id.homeBookName);
            homeSubject = itemView.findViewById(R.id.homeSubject);
            homeClass = itemView.findViewById(R.id.homeClass);
            homeMore = itemView.findViewById(R.id.homeMore);
            homeBookImage = itemView.findViewById(R.id.homeBookImage);
        }
    }

    public void setFilter(ArrayList<LoadHomeBooks> filterBooks){

        list = new ArrayList<>();
        list.addAll(filterBooks);
        notifyDataSetChanged();

    }
}
