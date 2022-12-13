package com.example.nonequi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;


public class HistoryTransactionListAdapter extends RecyclerView.Adapter<HistoryTransactionListAdapter.ViewHolder>{

    private List<HistoryTransactionList> mData;
    private LayoutInflater mInflater;
    private Context context;

    public HistoryTransactionListAdapter(List<HistoryTransactionList> itemList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public HistoryTransactionListAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.history_transaction_list, null);
        return new HistoryTransactionListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
    }

    public void setItems(List<HistoryTransactionList> items) {mData = items;}


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageTransactionIncoming;
        TextView name, money, by_for;
        LinearLayout linearLayoutTransactions;

        ViewHolder(View itemView) {
            super(itemView);

            imageTransactionIncoming = itemView.findViewById(R.id.imageIncoming);
            name = itemView.findViewById(R.id.textViewPersonName);
            money = itemView.findViewById(R.id.textViewMoneyHistory);
            by_for = itemView.findViewById(R.id.textViewBy);
            linearLayoutTransactions = itemView.findViewById(R.id.linearLayoutIncoming);
        }

        void bindData(final HistoryTransactionList item) {
            name.setText(item.getNameSender());
            money.setText(item.getMoneyHTL());

            /*if(Objects.equals(item.getNameSender(), DBConnection.users.getName())) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(60, 0, 0, 0);
                linearLayoutTransactions.setLayoutParams(layoutParams);



            } else {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 60, 0);
                linearLayoutTransactions.setLayoutParams(layoutParams);
            }*/

        }
    }
}
