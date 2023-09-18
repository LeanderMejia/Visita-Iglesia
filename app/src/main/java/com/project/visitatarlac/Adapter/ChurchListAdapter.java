package com.project.visitatarlac.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.visitatarlac.Class.HelperClass;
import com.project.visitatarlac.Fragment.ChurchInfoFragment;
import com.project.visitatarlac.Model.ChurchInfoModel;
import com.project.visitatarlac.R;

import java.util.ArrayList;

public class ChurchListAdapter extends RecyclerView.Adapter<ChurchListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ChurchInfoModel> churchInfoModelArrayList = new ArrayList<>();
    private HelperClass helperClass = new HelperClass();

    public ChurchListAdapter() {}

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView churchListCard;
        private TextView churchName, churchAddress;
        private ImageView churchImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            churchListCard = itemView.findViewById(R.id.nearby_church_card);
            churchName = itemView.findViewById(R.id.name);
            churchAddress = itemView.findViewById(R.id.address);
            churchImg = itemView.findViewById(R.id.img);
        }
    }

    @NonNull
    @Override
    public ChurchListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.church_list_layout, parent, false);
        ChurchListAdapter.MyViewHolder holder = new ChurchListAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChurchListAdapter.MyViewHolder holder, int position) {
        String drawableStr = churchInfoModelArrayList.get(position).getChurchImg();
        int id = context.getResources().getIdentifier(drawableStr, "drawable", context.getPackageName());
        Drawable drawable = context.getResources().getDrawable(id);

        holder.churchName.setText(churchInfoModelArrayList.get(position).getChurchName());
        holder.churchAddress.setText(churchInfoModelArrayList.get(position).getChurchAddress());
        Glide.with(context).load(drawable).into(holder.churchImg);

        holder.churchListCard.setOnClickListener(view -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment fragment = new ChurchInfoFragment();

            String churchName = churchInfoModelArrayList.get(position).getChurchName();
            String churchAddress = churchInfoModelArrayList.get(position).getChurchAddress();
            String churchDescription = churchInfoModelArrayList.get(position).getChurchDescription();
            String churchImg = churchInfoModelArrayList.get(position).getChurchImg();
            ArrayList<String> churchInfo = churchInfoModelArrayList.get(position).getChurchInfo();

            fragment.setArguments(helperClass.createChurchInfoBundle(churchName, churchAddress, churchDescription, churchImg, churchInfo));
            helperClass.setFragment(activity.getSupportFragmentManager(), R.id.frame_layout, fragment, "CHURCH_LIST_TAG");
            churchInfoModelArrayList.clear();
        });
    }

    @Override
    public int getItemCount() {
        return churchInfoModelArrayList.size();
    }

    public void setChurchInfoModelArrayList(ArrayList<ChurchInfoModel> churchInfoModelArrayList) {
        this.churchInfoModelArrayList = churchInfoModelArrayList;
        notifyDataSetChanged();
    }
}
