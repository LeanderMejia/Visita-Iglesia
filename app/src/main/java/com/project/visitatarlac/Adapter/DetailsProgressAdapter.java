package com.project.visitatarlac.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.project.visitatarlac.Class.HelperClass;
import com.project.visitatarlac.Fragment.ChurchMapFragment;
import com.project.visitatarlac.Fragment.HomeFragment;
import com.project.visitatarlac.Fragment.ProgressFragment;
import com.project.visitatarlac.Model.ChurchMapModel;
import com.project.visitatarlac.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailsProgressAdapter extends RecyclerView.Adapter<DetailsProgressAdapter.MyViewHolder> {

    HelperClass helperClass = new HelperClass();
    private Context context;
    private ArrayList<ChurchMapModel> finishChurchArrayList = new ArrayList<>();

    public DetailsProgressAdapter() {
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView nearbyChurchCard;
        private TextView churchName, churchAddress, km, dateAndTime;
        private ImageView churchImg;
        private ImageButton clearBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            nearbyChurchCard = itemView.findViewById(R.id.nearby_church_card);
            churchName = itemView.findViewById(R.id.church_name);
            churchAddress = itemView.findViewById(R.id.church_address);
            dateAndTime = itemView.findViewById(R.id.date_and_time);
            km = itemView.findViewById(R.id.km);
            churchImg = itemView.findViewById(R.id.church_img);
            clearBtn = itemView.findViewById(R.id.clear_btn);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_church_layout, parent, false);
        DetailsProgressAdapter.MyViewHolder holder = new DetailsProgressAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String drawableStr = finishChurchArrayList.get(position).getChurchImage();
        int id = context.getResources().getIdentifier(drawableStr, "drawable", context.getPackageName());
        Drawable drawable = context.getResources().getDrawable(id);

        holder.churchName.setText(finishChurchArrayList.get(position).getChurchName());
        holder.churchAddress.setText(finishChurchArrayList.get(position).getChurchAddress());
        holder.dateAndTime.setText(finishChurchArrayList.get(position).getDateAndTime());
        holder.km.setText(finishChurchArrayList.get(position).getKm() + " km");
        Glide.with(context).load(drawable).into(holder.churchImg);
        holder.nearbyChurchCard.setClickable(false);

        holder.clearBtn.setOnClickListener(view -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Delete Church")
                    .setMessage("Are you sure you want to delete this church? This action cannot be undone.").
                    setNegativeButton("CANCEL", null)
                    .setPositiveButton("DELETE", (dialog, which) -> {
                        clearChurch(position, activity);
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return finishChurchArrayList.size();
    }

    public void setFinishChurchArrayList(ArrayList<ChurchMapModel> finishChurchArrayList) {
        this.finishChurchArrayList = finishChurchArrayList;
    }

    private void clearChurch(int position, AppCompatActivity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("FINISH_CHURCH", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("storedData", null);
        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String churchName = jsonObject.getString("churchName");

                    if (finishChurchArrayList.get(position).getChurchName().equals(churchName)) {
                        jsonArray.remove(i);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("storedData", jsonArray.toString());
                        editor.apply();

                        if (jsonArray.length() > 0) {
                            helperClass.setFragment(activity.getSupportFragmentManager(), R.id.frame_layout, new ProgressFragment(), null);
                        } else {
                            editor.clear();
                            editor.apply();

                            SharedPreferences sharedPreferences2 = activity.getSharedPreferences("INITIAL_DISTANCE_KM", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                            editor2.clear();
                            editor2.apply();
                            helperClass.setFragment(activity.getSupportFragmentManager(), R.id.frame_layout, new HomeFragment(), null);
                        }
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
