package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bookbudiapp.R;

import java.util.List;

import Models.FaqModel;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {

    List<FaqModel> qnsList;
    Context context;

    public FaqAdapter(List<FaqModel> qnsList, Context context) {
        this.qnsList = qnsList;
        this.context = context;
    }

    @NonNull
    @Override
    public FaqAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_row_layout,parent,false);

        ViewHolder view = new ViewHolder(v);

        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull FaqAdapter.ViewHolder holder, int position) {

        FaqModel model = qnsList.get(position);

        holder.qns.setText(model.getFaqQns());
        holder.ans.setText(model.getFaqAns());
    }

    @Override
    public int getItemCount() {
        return qnsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView qns,ans;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            qns = itemView.findViewById(R.id.qns);
            ans = itemView.findViewById(R.id.ans);
        }
    }
}
