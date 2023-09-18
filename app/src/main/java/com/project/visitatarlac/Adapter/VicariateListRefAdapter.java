package com.project.visitatarlac.Adapter;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.visitatarlac.Class.HelperClass;
import com.project.visitatarlac.Model.ChurchListRefModel;
import com.project.visitatarlac.Model.VicariateListRefModel;
import com.project.visitatarlac.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VicariateListRefAdapter extends RecyclerView.Adapter<VicariateListRefAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<VicariateListRefModel> vicariateListRefAdapterArrayList = new ArrayList<>();
    private ArrayList<ChurchListRefModel> churchListRefArrayList = new ArrayList<>();
    private HelperClass helperClass = new HelperClass();

    public VicariateListRefAdapter() { }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView vicariateCardRef;
        private TextView vicariateName;
        private ImageButton showButton;
        private RecyclerView churchListRefRecView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            vicariateCardRef = itemView.findViewById(R.id.vicariate_card_ref);
            vicariateName = itemView.findViewById(R.id.vicariate_name);
            showButton = itemView.findViewById(R.id.show_button);
            churchListRefRecView = itemView.findViewById(R.id.church_list_ref_rec_view);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vicariate_list_ref_layout, parent, false);
        VicariateListRefAdapter.MyViewHolder holder = new VicariateListRefAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.vicariateName.setText(vicariateListRefAdapterArrayList.get(position).getVicariateName());

        holder.showButton.setOnClickListener(view -> {
            if (holder.churchListRefRecView.getVisibility() == View.GONE) {
                AutoTransition autoTransition = new AutoTransition();
                autoTransition.setDuration(300);
                TransitionManager.beginDelayedTransition(holder.vicariateCardRef, autoTransition);
                holder.churchListRefRecView.setVisibility(View.VISIBLE);
                holder.showButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);

                getChurchRefJSONData(vicariateListRefAdapterArrayList.get(position).getCategoryNameReference());
                ChurchListRefAdapter adapter = new ChurchListRefAdapter();
                adapter.setChurchListRefArrayList(churchListRefArrayList);
                holder.churchListRefRecView.setAdapter(adapter);
                holder.churchListRefRecView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                Transition transition = new Fade();
                transition.setDuration(600);
                TransitionManager.beginDelayedTransition(holder.vicariateCardRef, transition);
                holder.churchListRefRecView.setVisibility(View.GONE);
                holder.showButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vicariateListRefAdapterArrayList.size();
    }

    public void setVicariateListRefAdapterArrayList(ArrayList<VicariateListRefModel> vicariateListRefAdapterArrayList) {
        this.vicariateListRefAdapterArrayList = vicariateListRefAdapterArrayList;
    }

    private void getChurchRefJSONData(String categoryNameReference) {
        churchListRefArrayList.clear();
        AppCompatActivity activity = (AppCompatActivity) context;
        try {
            int jsonArraySize = helperClass.getJSONArray(activity, categoryNameReference).length();
            for (int i = 0; i < jsonArraySize; i++) {
                JSONObject jsonObject = helperClass.getJSONArray(activity, categoryNameReference).getJSONObject(i);
                String churchName = jsonObject.getString("churchName");
                String source = jsonObject.getString("source");

                churchListRefArrayList.add(new ChurchListRefModel(churchName, source));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
