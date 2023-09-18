package com.project.visitatarlac.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.visitatarlac.Adapter.ChurchVicariateListAdapter;
import com.project.visitatarlac.Adapter.VicariateListRefAdapter;
import com.project.visitatarlac.Class.HelperClass;
import com.project.visitatarlac.Model.VicariateListModel;
import com.project.visitatarlac.Model.VicariateListRefModel;
import com.project.visitatarlac.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReferenceFragment extends Fragment {

    HelperClass helperClass = new HelperClass();
    private ArrayList<VicariateListRefModel> vicariateListRefAdapterArrayList = new ArrayList<>();
    private RecyclerView vicariateListRefRecView;
    private TextView descriptionLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_reference, container, false);

        vicariateListRefRecView = viewGroup.findViewById(R.id.vicariate_list_ref_rec_view);
        descriptionLink = viewGroup.findViewById(R.id.description_link);

        descriptionLink.setMovementMethod(LinkMovementMethod.getInstance());
        return viewGroup;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getVicariateRefJSONData();

        VicariateListRefAdapter adapter = new VicariateListRefAdapter();
        adapter.setVicariateListRefAdapterArrayList(vicariateListRefAdapterArrayList);
        vicariateListRefRecView.setAdapter(adapter);
        vicariateListRefRecView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getVicariateRefJSONData() {
        try {
            int jsonArraySize = helperClass.getJSONArray(getActivity(), "vicariateReference").length();
            for (int i = 0; i < jsonArraySize; i++) {
                JSONObject jsonObject = helperClass.getJSONArray(getActivity(), "vicariateReference").getJSONObject(i);
                String vicariateName = jsonObject.getString("vicariateName");
                String categoryNameReference = jsonObject.getString("categoryNameReference");

                vicariateListRefAdapterArrayList.add(new VicariateListRefModel(vicariateName, categoryNameReference));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}