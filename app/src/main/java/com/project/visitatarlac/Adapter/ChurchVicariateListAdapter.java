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
import com.project.visitatarlac.Fragment.ChurchListFragment;
import com.project.visitatarlac.Model.VicariateListModel;
import com.project.visitatarlac.R;

import java.util.ArrayList;

public class ChurchVicariateListAdapter extends RecyclerView.Adapter<ChurchVicariateListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<VicariateListModel> vicariateListModelArrayList = new ArrayList<>();
    private HelperClass helperClass = new HelperClass();

    public ChurchVicariateListAdapter() {}

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView vicariateName;
        private ImageView vicariateImg;
        private CardView vicariateCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            vicariateName = itemView.findViewById(R.id.vicariate_name);
            vicariateImg = itemView.findViewById(R.id.vicariate_img);
            vicariateCard = itemView.findViewById(R.id.vicariate_card_ref);
        }
    }

    @NonNull
    @Override
    public ChurchVicariateListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.church_vicariate_list_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChurchVicariateListAdapter.MyViewHolder holder, int position) {
        String drawableStr = vicariateListModelArrayList.get(position).getVicariateImg();
        int id = context.getResources().getIdentifier(drawableStr, "drawable", context.getPackageName());
        Drawable drawable = context.getResources().getDrawable(id);

        holder.vicariateName.setText(vicariateListModelArrayList.get(position).getVicariateName());
        Glide.with(context).load(drawable).into(holder.vicariateImg);

        holder.vicariateCard.setOnClickListener(view -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment fragment = new ChurchListFragment();
            String categoryName = vicariateListModelArrayList.get(position).getCategoryName();
            String vicariateName = vicariateListModelArrayList.get(position).getVicariateName();
            fragment.setArguments(helperClass.createBundle("CATEGORY_NAME", categoryName, "VICARIATE_NAME", vicariateName));
            helperClass.setFragment(activity.getSupportFragmentManager(), R.id.frame_layout, fragment, null);
            vicariateListModelArrayList.clear();
        });
    }

    @Override
    public int getItemCount() {
        return vicariateListModelArrayList.size();
    }

    public void setVicariateListModelArrayList(ArrayList<VicariateListModel> vicariateListModelArrayList) {
        this.vicariateListModelArrayList = vicariateListModelArrayList;
        notifyDataSetChanged();
    }
}
