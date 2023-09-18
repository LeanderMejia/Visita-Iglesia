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
import com.project.visitatarlac.Fragment.ChurchMapFragment;
import com.project.visitatarlac.Model.ChurchMapModel;

import com.project.visitatarlac.R;

import java.util.ArrayList;
import java.util.Collections;

public class NearbyChurchAdapter extends RecyclerView.Adapter<NearbyChurchAdapter.MyViewHolder> {

    HelperClass helperClass = new HelperClass();
    private Context context;
    private ArrayList<ChurchMapModel> nearbyChurchModelArrayList = new ArrayList<>();

    public NearbyChurchAdapter() { }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView nearbyChurchCard;
        private TextView churchName, churchAddress, km;
        private ImageView churchImg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            nearbyChurchCard = itemView.findViewById(R.id.nearby_church_card);
            churchName = itemView.findViewById(R.id.church_name);
            churchAddress = itemView.findViewById(R.id.church_address);
            km = itemView.findViewById(R.id.km);
            churchImg = itemView.findViewById(R.id.church_img);
        }
    }

    @NonNull
    @Override
    public NearbyChurchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_church_layout, parent, false);
        NearbyChurchAdapter.MyViewHolder holder = new NearbyChurchAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyChurchAdapter.MyViewHolder holder, int position) {
        String drawableStr = nearbyChurchModelArrayList.get(position).getChurchImage();
        int id = context.getResources().getIdentifier(drawableStr, "drawable", context.getPackageName());
        Drawable drawable = context.getResources().getDrawable(id);

        holder.churchName.setText(nearbyChurchModelArrayList.get(position).getChurchName());
        holder.churchAddress.setText(nearbyChurchModelArrayList.get(position).getChurchAddress());
        holder.km.setText(nearbyChurchModelArrayList.get(position).getKm() + " km");
        Glide.with(context).load(drawable).into(holder.churchImg);

        holder.nearbyChurchCard.setOnClickListener(view -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment fragment = new ChurchMapFragment();
            double latitude = nearbyChurchModelArrayList.get(position).getLatitude();
            double longitude = nearbyChurchModelArrayList.get(position).getLongitude();
            String churchName = nearbyChurchModelArrayList.get(position).getChurchName();
            String churchAddress = nearbyChurchModelArrayList.get(position).getChurchAddress();
            fragment.setArguments(helperClass.createChurchMarkerBundle("NEARBY_LATITUDE", latitude, "NEARBY_LONGITUDE", longitude, "NEARBY_TITLE", churchName, "NEARBY_SNIPPET", churchAddress, null, null, null, null, null, null));
            helperClass.setFragment(activity.getSupportFragmentManager(), R.id.frame_layout, fragment, null);
            nearbyChurchModelArrayList.clear();
        });
    }

    @Override
    public int getItemCount() {
        return nearbyChurchModelArrayList.size();
    }

    public void setNearbyChurchModelArrayList(ArrayList<ChurchMapModel> nearbyChurchModelArrayList) {
        Collections.sort(nearbyChurchModelArrayList, (nearbyChurchModel1, nearbyChurchModel2) -> Float.compare(nearbyChurchModel1.getKm(), nearbyChurchModel2.getKm()));
        this.nearbyChurchModelArrayList = nearbyChurchModelArrayList;
    }
}
