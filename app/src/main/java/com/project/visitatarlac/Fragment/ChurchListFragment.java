package com.project.visitatarlac.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.visitatarlac.Adapter.ChurchListAdapter;
import com.project.visitatarlac.Class.HelperClass;
import com.project.visitatarlac.Model.ChurchInfoModel;
import com.project.visitatarlac.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChurchListFragment extends Fragment {

    private HelperClass helperClass = new HelperClass();
    private TextView categoryTitle;
    private RecyclerView churchListRecView;
    private ArrayList<ChurchInfoModel> churchInfoModelArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_church_list, container, false);

        categoryTitle = viewGroup.findViewById(R.id.category_title);
        churchListRecView = viewGroup.findViewById(R.id.church_list_rec_view);

        return viewGroup;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getVicariateJSONData();

        ChurchListAdapter adapter = new ChurchListAdapter();
        adapter.setChurchInfoModelArrayList(churchInfoModelArrayList);
        churchListRecView.setAdapter(adapter);
        churchListRecView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getVicariateJSONData() {
        try {
            Bundle bundle = getArguments();
            String categoryName = bundle.getString("CATEGORY_NAME");
            String vicariateName = bundle.getString("VICARIATE_NAME");
            categoryTitle.setText(vicariateName + " Parishes");

            int jsonArraySize = helperClass.getJSONArray(getActivity(), categoryName).length();
            for (int i = 0; i < jsonArraySize; i++) {
                JSONObject jsonObject = helperClass.getJSONArray(getActivity(), categoryName).getJSONObject(i);
                String churchName = jsonObject.getString("churchName");
                String churchAddress = jsonObject.getString("churchAddress");
                String churchDescription = jsonObject.getString("churchDescription");
                String churchImg = jsonObject.getString("churchImg");

                JSONArray churchInfoArray = jsonObject.getJSONArray("churchInfo");
                ArrayList<String> churchInfo = new ArrayList<>();
                for (int j = 0; j < churchInfoArray.length(); j++) {
                    churchInfo.add(churchInfoArray.getString(j));
                }
                churchInfoModelArrayList.add(new ChurchInfoModel(churchName, churchAddress, churchDescription, churchImg, churchInfo));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}