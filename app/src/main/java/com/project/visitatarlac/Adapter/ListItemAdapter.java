package com.project.visitatarlac.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.visitatarlac.R;

import java.util.ArrayList;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.MyViewHolder> {

    private ArrayList<String> arrayList = new ArrayList<>();

    public ListItemAdapter() {}

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView listItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            listItem = itemView.findViewById(R.id.list_item);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_text, parent, false);
        ListItemAdapter.MyViewHolder holder = new ListItemAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.listItem.setText("• " + arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setArrayList(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }
}
