package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bookbudiapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import Models.LoadHomeBooks;

public class HomeBookAdapter extends RecyclerView.Adapter<HomeBookAdapter.ViewHolder> {

    ArrayList<LoadHomeBooks> list;
    Context context;

    public HomeBookAdapter(ArrayList<LoadHomeBooks> list,Context context){

        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_book_layout,viewGroup,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBookAdapter.ViewHolder viewHolder, int i) {

        LoadHomeBooks model = list.get(i);

        String id = model.getbUid();
        String bookId = model.getbId();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.openbook);

        Glide.with(context).load(model.getbImage()).apply(requestOptions).into(viewHolder.homeBookImage);

        viewHolder.homeBookName.setText(model.getbName());
        viewHolder.homeSubject.setText(model.getbSub());
        viewHolder.homeClass.setText(model.getbClass());

        viewHolder.homeMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(context,"Hello",Toast.LENGTH_SHORT).show();
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
}
