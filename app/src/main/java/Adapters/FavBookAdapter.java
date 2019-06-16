package Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.RecoverySystem;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Models.FavouriteBooksModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FavBookAdapter extends RecyclerView.Adapter<FavBookAdapter.ViewHolder> {

    Activity context;
    List<FavouriteBooksModel> lists;
    ProgressDialog prg;
    FirebaseAuth fAuth;
    FirebaseUser user;

    private static final String URL = "https://bookbudiapp.herokuapp.com/removeFromWishlist";

    public FavBookAdapter(List<FavouriteBooksModel> lists,Activity context){

        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public FavBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

       View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favourite_book_row,viewGroup,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavBookAdapter.ViewHolder viewHolder, int i) {

        prg = new ProgressDialog(context);
        prg.setMessage("Removing book...");
        prg.setCancelable(false);

        FavouriteBooksModel model = lists.get(i);

        final String userId = model.getFavBookId();
        final String bookId = model.getFavUserId();

        viewHolder.userBookName.setText(model.getFavbName());

        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.openbook);

        Glide.with(context).load(model.getFavImg()).apply(options).into(viewHolder.userFavBook);

        viewHolder.unFav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Book will no longer be in your favourites.");
                builder.setCancelable(true);
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();

                    }
                });
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        prg.show();

                        fAuth = FirebaseAuth.getInstance();
                        user = fAuth.getCurrentUser();

                        OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(22, TimeUnit.SECONDS)
                                .readTimeout(22,TimeUnit.SECONDS)
                                .writeTimeout(22,TimeUnit.SECONDS)
                                .build();

                        RequestBody body = new FormBody.Builder()
                                .add("uId",user.getUid())
                                .add("bId",bookId)
                                .build();

                        Request request = new Request.Builder().post(body).url(URL).build();

                        client.newCall(request).enqueue(new Callback() {

                            @Override
                            public void onResponse(Call call, final Response response) {

                                context.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        try {

                                            String str = response.body().string();

                                            Log.d("Response",str);

                                            if(str.equals("Deleted")){

                                                prg.dismiss();

                                                int position = viewHolder.getAdapterPosition();

                                                lists.remove(position);
                                                notifyItemRemoved(position);
                                                notifyItemRangeChanged(position,lists.size());

                                            }

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }

                            @Override
                            public void onFailure(Call call, final IOException e) {

                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        prg.dismiss();
                                        TastyToast.makeText(context,e.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                                    }
                                });
                            }

                        });
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userFavBook;
        TextView userBookName;
        Button unFav;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userFavBook = (itemView).findViewById(R.id.userFavBook);
            userBookName = (itemView).findViewById(R.id.userBookName);
            unFav = (itemView).findViewById(R.id.unFav);
        }
    }

}
