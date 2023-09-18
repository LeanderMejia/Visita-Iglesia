package com.project.visitatarlac.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.visitatarlac.Adapter.ChurchVicariateListAdapter;
import com.project.visitatarlac.Class.HelperClass;
import com.project.visitatarlac.Model.VicariateListModel;
import com.project.visitatarlac.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChurchesFragment extends Fragment {

    HelperClass helperClass = new HelperClass();
    private ArrayList<VicariateListModel> vicariateListModelArrayList = new ArrayList<>();
    private RecyclerView churchVicariateRecView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_churches, container, false);

        churchVicariateRecView = viewGroup.findViewById(R.id.church_list_rec_view);

        return viewGroup;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getVicariateJSONData();

        ChurchVicariateListAdapter adapter = new ChurchVicariateListAdapter();
        adapter.setVicariateListModelArrayList(vicariateListModelArrayList);
        churchVicariateRecView.setAdapter(adapter);
        churchVicariateRecView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getVicariateJSONData() {
        try {
            int jsonArraySize = helperClass.getJSONArray(getActivity(), "listOfVicariate").length();
            for (int i = 0; i < jsonArraySize; i++) {
                JSONObject jsonObject = helperClass.getJSONArray(getActivity(), "listOfVicariate").getJSONObject(i);
                String vicariateName = jsonObject.getString("vicariateName");
                String vicariateImg = jsonObject.getString("vicariateImg");
                String categoryName = jsonObject.getString("categoryName");

                vicariateListModelArrayList.add(new VicariateListModel(vicariateName, vicariateImg, categoryName));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}