package com.project.visitatarlac.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.visitatarlac.Model.OurTeamModel;
import com.project.visitatarlac.R;

import java.util.ArrayList;

public class OurTeamAdapter extends RecyclerView.Adapter<OurTeamAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<OurTeamModel> ourTeamModelArrayList = new ArrayList<>();

    public OurTeamAdapter() { }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, position;
        private ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = itemView.findViewById(R.id.name);
            position = itemView.findViewById(R.id.position);
            img = itemView.findViewById(R.id.img);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.our_team_layout, parent, false);
        OurTeamAdapter.MyViewHolder holder = new OurTeamAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String drawableStr = ourTeamModelArrayList.get(position).getImg();
        int id = context.getResources().getIdentifier(drawableStr, "drawable", context.getPackageName());
        Drawable drawable = context.getResources().getDrawable(id);

        holder.name.setText(ourTeamModelArrayList.get(position).getName());
        holder.position.setText(ourTeamModelArrayList.get(position).getPosition());
        Glide.with(context).load(drawable).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return ourTeamModelArrayList.size();
    }

    public void setOurTeamModelArrayList(ArrayList<OurTeamModel> ourTeamModelArrayList) {
        this.ourTeamModelArrayList = ourTeamModelArrayList;
    }
}
