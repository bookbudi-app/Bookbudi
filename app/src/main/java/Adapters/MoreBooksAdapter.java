package Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

import java.util.List;

import Models.MoreBooksModel;

public class MoreBooksAdapter extends RecyclerView.Adapter<MoreBooksAdapter.ViewHolder> {

    Context context;
    List<MoreBooksModel> list;

    public MoreBooksAdapter(List<MoreBooksModel> list,Context context){

        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MoreBooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.more_books_row,viewGroup,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreBooksAdapter.ViewHolder viewHolder, int i) {

        MoreBooksModel model = list.get(i);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.openbook);

        Glide.with(context).load(model.getMoreBookImg()).apply(requestOptions).into(viewHolder.moreBookImage);

        viewHolder.moreBookName.setText(model.getMoreBookName());
        viewHolder.moreSubject.setText(model.getMoreBookSub());
        viewHolder.moreClass.setText(model.getMoreBookClass());
        viewHolder.moreCity.setText(model.getMoreBookCity());

   /*     viewHolder.moreBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Hello",Toast.LENGTH_SHORT).show();
            }
        });  */
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView moreBookName,moreSubject,moreClass,moreCity;
       // Button moreBut;
        ImageView moreBookImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            moreBookImage = itemView.findViewById(R.id.moreBookImage);
            moreBookName = itemView.findViewById(R.id.moreBookName);
            moreSubject = itemView.findViewById(R.id.moreSubject);
            moreClass = itemView.findViewById(R.id.moreClass);
            moreCity = itemView.findViewById(R.id.moreCity);
           // moreBut = itemView.findViewById(R.id.moreBut);

        }
    }

}
