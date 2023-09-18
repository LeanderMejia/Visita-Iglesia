package com.project.visitatarlac.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.visitatarlac.Model.ChurchListRefModel;
import com.project.visitatarlac.R;

import java.util.ArrayList;

public class ChurchListRefAdapter extends RecyclerView.Adapter<ChurchListRefAdapter.MyViewHolder> {

    private ArrayList<ChurchListRefModel> churchListRefArrayList = new ArrayList<>();

    public ChurchListRefAdapter() { }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView churchName, source;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            churchName = itemView.findViewById(R.id.church_name);
            source = itemView.findViewById(R.id.source);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.church_list_ref_layout, parent, false);
        ChurchListRefAdapter.MyViewHolder holder = new ChurchListRefAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.churchName.setText(churchListRefArrayList.get(position).getChurchName());
        holder.source.setText(churchListRefArrayList.get(position).getSource());
    }

    @Override
    public int getItemCount() {
        return churchListRefArrayList.size();
    }

    public void setChurchListRefArrayList(ArrayList<ChurchListRefModel> churchListRefArrayList) {
        this.churchListRefArrayList = churchListRefArrayList;
    }
}
