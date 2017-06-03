package com.example.skrij.gravytruck.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.example.skrij.gravytruck.R;
import com.example.skrij.gravytruck.adapters.CommentAdapter;
import com.example.skrij.gravytruck.helpers.JsonObjectRequestWithNull;
import com.example.skrij.gravytruck.helpers.SystemPrefsHelper;
import com.example.skrij.gravytruck.informations.CommentInformation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by skrij on 24/05/2017.
 */

public class RecommendationFragment extends Fragment {

    EditText commentSend;

    private RecyclerView mRecyclerView;
    private CommentAdapter mAdapter;

    private JSONObject jsonTruckComment;
    private String truck_hash;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recommendation, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recommendation_recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(RecommendationFragment.this.getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        commentSend = (EditText) view.findViewById(R.id.comment_edittext);

        commentSend.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                boolean result = false;

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    RequestQueue queue = Volley.newRequestQueue(RecommendationFragment.this.getContext());
                    String url = getResources().getString(R.string.api_address) + "/comments?key=" + getResources().getString(R.string.api_key);

                    final String content = commentSend.getText().toString();
                    // Get data
                    HashMap<String, String> params = new HashMap<>();
                    params.put("username", SystemPrefsHelper.getInstance(RecommendationFragment.this.getContext()).getSystemPrefString("username"));
                    params.put("truck_hash", truck_hash);
                    params.put("content", content);

                    JsonObjectRequestWithNull request_json = new JsonObjectRequestWithNull(Request.Method.POST, url, new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    mAdapter.data.add(new CommentInformation(SystemPrefsHelper.getInstance(RecommendationFragment.this.getContext()).getSystemPrefString("username"), content));
                                    mAdapter.notifyDataSetChanged();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e("Error: ", error.getMessage());
                        }
                    });
                    queue.add(request_json);

                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    result = true;
                }
                return result;
            }
        });

        return view;

    }

    public ArrayList<CommentInformation> getData() {

        final ArrayList<CommentInformation> data = new ArrayList<>();

        if (jsonTruckComment == null) {
            Toast.makeText(RecommendationFragment.this.getContext(), "No comments yet ! ", Toast.LENGTH_LONG).show();
            return null;
        }
        try {
            JSONArray comments = jsonTruckComment.getJSONArray("comments");
            for (int i = 0; i < comments.length(); i++) {
                JSONObject obj = comments.getJSONObject(i);
                String user_name = obj.getString("username");
                String user_comment = obj.getString("content");
                CommentInformation commentInfo = new CommentInformation(user_name, user_comment);
                data.add(commentInfo);
            }
            return data;
        } catch (Exception e) {
            Toast.makeText(RecommendationFragment.this.getContext(), "Something went wrong :(", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    void Update(JSONObject jsonData, String _truckHash) {
        if (jsonData == null)
            return;
        jsonTruckComment = jsonData;
        truck_hash = _truckHash;
        List<CommentInformation> mi = getData();
        mAdapter = new CommentAdapter(getActivity(), mi);
        //      }
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.invalidate();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof RecommendationFragment.OnFragmentInteractionListener)) {
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
