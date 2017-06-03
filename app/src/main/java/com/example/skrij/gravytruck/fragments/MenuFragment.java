package com.example.skrij.gravytruck.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.skrij.gravytruck.R;
import com.example.skrij.gravytruck.adapters.MenuAdapter;
import com.example.skrij.gravytruck.informations.MenuInformation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skrij on 24/05/2017.
 */

public class MenuFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MenuAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private JSONObject jsonTruckInformations;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.menu_recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);

        mLayoutManager = new LinearLayoutManager(MenuFragment.this.getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    public ArrayList<MenuInformation> getData() {

        final ArrayList<MenuInformation> data = new ArrayList<>();

        if (jsonTruckInformations == null) {
            Toast.makeText(MenuFragment.this.getContext(), "No menu added yet ! ", Toast.LENGTH_LONG).show();
            return null;
        }
        try {
            // Get all the truck's menu
            JSONArray foods = jsonTruckInformations.getJSONArray("foods");
            for (int i = 0; i < foods.length(); i++) {
                JSONObject obj = foods.getJSONObject(i);
                String product_name = obj.getString("name");
                String product_price = obj.getString("price");
                MenuInformation menuInfo = new MenuInformation(product_name, product_price);
                data.add(menuInfo);
            }
            return data;
        } catch (Exception e) {
            Toast.makeText(MenuFragment.this.getContext(), "Something went wrong :(", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    void Update(JSONObject jsonData) {
        if (jsonData == null)
            return;
        jsonTruckInformations = jsonData;
        List<MenuInformation> mi = getData();
        mAdapter = new MenuAdapter(getActivity(), mi);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.invalidate();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnFragmentInteractionListener)) {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromChildFragment(Uri uri);
    }
}
