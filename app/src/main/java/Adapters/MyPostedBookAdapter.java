package Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import Models.PostedModel;
import com.app.bookbudiapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyPostedBookAdapter extends RecyclerView.Adapter<MyPostedBookAdapter.ViewHolder> {

    List<PostedModel> listItem;
    Activity context;

    private static final String URI = "https:bookbudiapp.herokuapp.com/deleteRow";

    ProgressDialog prg;

    public MyPostedBookAdapter(List<PostedModel> listItem, Activity context){

        this.listItem = listItem;
        this.context = context;
    }

    @NonNull
    @Override
    public MyPostedBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.posted_book,viewGroup,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyPostedBookAdapter.ViewHolder viewHolder, final int i) {

        prg = new ProgressDialog(context);
        prg.setMessage("Deleting item...");
        prg.setCancelable(false);

        final PostedModel model =  listItem.get(i);

        final String id = model.getPostId();

        viewHolder.userBookName.setText(model.getPurchaseBookName());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.openbook);

        Glide.with(context).load(model.getPurchaseImage()).apply(requestOptions).into(viewHolder.userPostBook);

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Item will be deleted permanently.");
                builder.setCancelable(true);
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        prg.show();

                        OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(20, TimeUnit.SECONDS)
                                .readTimeout(20,TimeUnit.SECONDS)
                                .writeTimeout(20, TimeUnit.SECONDS)
                                .build();

                        RequestBody formBody = new FormBody.Builder().add("postId",id).build();

                        Request request = new Request.Builder().url(URI).post(formBody).build();

                        client.newCall(request).enqueue(new Callback() {

                            @Override
                            public void onResponse(Call call, final Response response) throws IOException {

                                context.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        try {

                                            String str = response.body().string();

                                            if(str.equals("Deleted")){

                                                prg.dismiss();

                                                int position = viewHolder.getAdapterPosition();

                                                listItem.remove(position);
                                                notifyItemRemoved(position);
                                                notifyItemRangeChanged(position,listItem.size());
                                            }

                                        }catch (IOException e) {
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
                                        TastyToast.makeText(context,e.getMessage(),TastyToast.LENGTH_LONG,TastyToast.ERROR).show();
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
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userPostBook;
        TextView userBookName;
        Button delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userPostBook = (itemView).findViewById(R.id.userPostBook);
            userBookName = (itemView).findViewById(R.id.userBookName);
            delete = (itemView).findViewById(R.id.delete);
        }
    }

}
