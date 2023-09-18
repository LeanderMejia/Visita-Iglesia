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
import com.project.visitatarlac.Fragment.PrayersInfoFragment;
import com.project.visitatarlac.Model.PrayersModel;
import com.project.visitatarlac.R;

import java.util.ArrayList;

public class PrayerListAdapter extends RecyclerView.Adapter<PrayerListAdapter.MyViewHolder> {

    HelperClass helperClass = new HelperClass();
    private Context context;
    private ArrayList<PrayersModel> prayersModelArrayList = new ArrayList<>();

    public PrayerListAdapter() { }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView prayerName, prayerSubtitle;
        private ImageView prayerImg;
        private CardView prayerItemCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            prayerName = itemView.findViewById(R.id.prayer_name);
            prayerSubtitle = itemView.findViewById(R.id.prayer_subtitle);
            prayerImg = itemView.findViewById(R.id.prayer_img);
            prayerItemCard = itemView.findViewById(R.id.prayer_item_card);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prayer_list_layout, parent, false);
        PrayerListAdapter.MyViewHolder holder = new PrayerListAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String drawableStr = prayersModelArrayList.get(position).getPrayerImg();
        int id = context.getResources().getIdentifier(drawableStr, "drawable", context.getPackageName());
        Drawable drawable = context.getResources().getDrawable(id);

        holder.prayerName.setText(prayersModelArrayList.get(position).getPrayerName());
        holder.prayerSubtitle.setText(prayersModelArrayList.get(position).getPrayerSubtitle());
        Glide.with(context).load(drawable).into(holder.prayerImg);

        holder.prayerItemCard.setOnClickListener(view -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment fragment = new PrayersInfoFragment();
            fragment.setArguments(helperClass.createPrayerBundle(prayersModelArrayList.get(position).getPrayerName(), prayersModelArrayList.get(position).getPrayerDescription(), prayersModelArrayList.get(position).getPrayerImg()));
            helperClass.setFragment(activity.getSupportFragmentManager(), R.id.frame_layout, fragment, null);
            prayersModelArrayList.clear();
        });
    }

    @Override
    public int getItemCount() {
        return prayersModelArrayList.size();
    }

    public void setPrayersModelArrayList(ArrayList<PrayersModel> prayersModelArrayList) {
        this.prayersModelArrayList = prayersModelArrayList;
        notifyDataSetChanged();
    }
}
