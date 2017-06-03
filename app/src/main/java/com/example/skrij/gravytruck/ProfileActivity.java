package com.example.skrij.gravytruck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.skrij.gravytruck.adapters.ProfileAdapter;
import com.example.skrij.gravytruck.helpers.SystemPrefsHelper;
import com.example.skrij.gravytruck.informations.ProfileInformation;

import java.util.ArrayList;
import java.util.List;


public class ProfileActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ProfileAdapter.OnItemClickListener OnItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String systemPrefUserName = SystemPrefsHelper.getInstance(ProfileActivity.this).getSystemPrefString("username");
        TextView userName = (TextView) findViewById(R.id.username_profile_textview);
        userName.setText(systemPrefUserName);

        mRecyclerView = (RecyclerView) findViewById(R.id.profile_recycler_view);
        Update();
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    public ArrayList<ProfileInformation> getData() {

        final ArrayList<ProfileInformation> data = new ArrayList<>();

        ProfileInformation profileInfo = new ProfileInformation("Proposez un food truck");
        data.add(profileInfo);
        ProfileInformation profileInfo_2 = new ProfileInformation("Les foodtrucks que j'aime");
        data.add(profileInfo_2);
        ProfileInformation profileInfo_3 = new ProfileInformation("Donnez votre avis");
        data.add(profileInfo_3);
        ProfileInformation profileInfo_4 = new ProfileInformation("Se déconnecter");
        data.add(profileInfo_4);

        return data;
    }

    void Update() {
        List<ProfileInformation> mi = getData();
        ProfileAdapter mAdapter = new ProfileAdapter(this, mi, OnItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.invalidate();
    }
}
