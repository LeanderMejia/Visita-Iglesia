package com.project.visitatarlac.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.project.visitatarlac.Adapter.ChurchVicariateListAdapter;
import com.project.visitatarlac.Adapter.PrayerListAdapter;
import com.project.visitatarlac.Class.HelperClass;
import com.project.visitatarlac.Model.PrayersModel;
import com.project.visitatarlac.Model.VicariateListModel;
import com.project.visitatarlac.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PrayersFragment extends Fragment {

    HelperClass helperClass = new HelperClass();
    private RecyclerView prayersRecView;
    private AutoCompleteTextView searchInput;
    private ArrayList<PrayersModel> prayersModelArrayList = new ArrayList<>();
    private ArrayList<String> prayerNameArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_prayers, container, false);

        prayersRecView = viewGroup.findViewById(R.id.prayer_list_rec_view);
        searchInput = viewGroup.findViewById(R.id.search_input);

        return viewGroup;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPrayersJSONData();

        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, prayerNameArrayList);
        searchInput.setAdapter(searchAdapter);
        searchInput.setThreshold(1);
        performSearch();

        PrayerListAdapter adapter = new PrayerListAdapter();
        adapter.setPrayersModelArrayList(prayersModelArrayList);
        prayersRecView.setAdapter(adapter);
        prayersRecView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStop() {
        super.onStop();
        prayerNameArrayList.clear();
    }

    private void getPrayersJSONData() {
        try {
            int jsonArraySize = helperClass.getJSONArray(getActivity(), "prayers").length();
            for (int i = 0; i < jsonArraySize; i++) {
                JSONObject jsonObject = helperClass.getJSONArray(getActivity(), "prayers").getJSONObject(i);
                String prayerName = jsonObject.getString("prayerName");
                String prayerSubtitle = jsonObject.getString("prayerSubtitle");
                String prayerDescription = jsonObject.getString("prayerDescription");
                String prayerImg = jsonObject.getString("prayerImg");

                prayerNameArrayList.add(prayerName);
                prayersModelArrayList.add(new PrayersModel(prayerName, prayerSubtitle, prayerDescription, prayerImg));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void performSearch() {
        searchInput.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                getSearchResult();
                return true;
            }
            return false;
        });
    }

    private void getSearchResult(){
        int index = prayerNameArrayList.indexOf(searchInput.getText().toString());
        if (index < 0) {
            searchInput.clearFocus();
            searchInput.getText().clear();
            return;
        }

        View view = getActivity().getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        searchInput.clearFocus();
        searchInput.getText().clear();

        Fragment fragment = new PrayersInfoFragment();
        fragment.setArguments(helperClass.createPrayerBundle(prayersModelArrayList.get(index).getPrayerName(), prayersModelArrayList.get(index).getPrayerDescription(), prayersModelArrayList.get(index).getPrayerImg()));
        helperClass.setFragment(getActivity().getSupportFragmentManager(), R.id.frame_layout, fragment, null);
        prayersModelArrayList.clear();
        prayerNameArrayList.clear();

    }
}