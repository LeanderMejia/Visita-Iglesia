package com.project.visitatarlac.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.visitatarlac.Adapter.ChurchVicariateListAdapter;
import com.project.visitatarlac.Adapter.OurTeamAdapter;
import com.project.visitatarlac.Class.HelperClass;
import com.project.visitatarlac.Model.OurTeamModel;
import com.project.visitatarlac.Model.VicariateListModel;
import com.project.visitatarlac.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OurTeamFragment extends Fragment {

    HelperClass helperClass = new HelperClass();
    private ArrayList<OurTeamModel> ourTeamModelArrayList = new ArrayList<>();
    private RecyclerView ourTeamRecView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_our_team, container, false);

        ourTeamRecView = viewGroup.findViewById(R.id.our_team_rec_view);

        return viewGroup;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getOurTeamJSONData();

        OurTeamAdapter adapter = new OurTeamAdapter();
        adapter.setOurTeamModelArrayList(ourTeamModelArrayList);
        ourTeamRecView.setAdapter(adapter);
        ourTeamRecView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getOurTeamJSONData() {
        try {
            int jsonArraySize = helperClass.getJSONArray(getActivity(), "ourTeam").length();
            for (int i = 0; i < jsonArraySize; i++) {
                JSONObject jsonObject = helperClass.getJSONArray(getActivity(), "ourTeam").getJSONObject(i);
                String name = jsonObject.getString("name");
                String position = jsonObject.getString("position");
                String img = jsonObject.getString("img");

                ourTeamModelArrayList.add(new OurTeamModel(name, position, img));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}