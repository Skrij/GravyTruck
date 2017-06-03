package com.example.skrij.gravytruck.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.skrij.gravytruck.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ParentFragment extends Fragment {

    public JSONObject jsonTruckInformations;
    public JSONObject jsonTruckMenu;
    public JSONObject jsonLikeState;
    public JSONObject jsonTruckComment;

    public String truckHash;
    public String truckDistance;

    private InformationFragment infoFragment;
    private MenuFragment menuFragment;
    private RecommendationFragment commandFragment;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parent, container, false);

        Button information_button = (Button) view.findViewById(R.id.information_button);
        Button menu_button = (Button) view.findViewById(R.id.menu_button);
        Button recommendation_button = (Button) view.findViewById(R.id.recommendation_button);

        infoFragment = new InformationFragment();
        menuFragment = new MenuFragment();
        commandFragment = new RecommendationFragment();

        information_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.show(infoFragment);
                transaction.hide(menuFragment);
                transaction.hide(commandFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.show(menuFragment);
                transaction.hide(infoFragment);
                transaction.hide(commandFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        recommendation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.show(commandFragment);
                transaction.hide(infoFragment);
                transaction.hide(menuFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Update all the fragments with the good data and display one of them
        infoFragment.Update(jsonTruckInformations, truckDistance);
        menuFragment.Update(jsonTruckMenu);
        commandFragment.Update(jsonTruckComment, truckHash);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.child_fragment_container, menuFragment).hide(menuFragment);
        transaction.add(R.id.child_fragment_container, commandFragment).hide(commandFragment);
        transaction.add(R.id.child_fragment_container, infoFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof ParentFragment.OnFragmentInteractionListener)) {
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
        void messageFromParentFragment(Uri uri);
    }

    public void UpdateSubfragments() {

        if (jsonTruckInformations != null)
            infoFragment.Update(jsonTruckInformations, truckDistance);
        if (jsonTruckMenu != null)
            menuFragment.Update(jsonTruckMenu);
        if (jsonTruckComment != null)
            commandFragment.Update(jsonTruckComment, truckHash);
        if (jsonLikeState != null) {
            try {
                boolean like = jsonLikeState.getBoolean("exist");
                String like_hash = null;
                if (like)
                    like_hash = jsonLikeState.getString("like_hash");
                infoFragment.UpdateLike(like, like_hash);
            } catch (JSONException e) {
            }
        }
    }

    public void ChangeToCommandChildFragment() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.hide(infoFragment);
        transaction.hide(menuFragment);
        transaction.show(commandFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}