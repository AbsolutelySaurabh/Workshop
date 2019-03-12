package com.example.absolutelysaurabh.workshopapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.support.v7.widget.Toolbar;

import com.example.absolutelysaurabh.workshopapp.R;
import com.example.absolutelysaurabh.workshopapp.adapter.DashboardAdapter;
import com.example.absolutelysaurabh.workshopapp.adapter.AvailableWorkshopAdapter;
import com.example.absolutelysaurabh.workshopapp.data.WorkshopsDbHelper;
import com.example.absolutelysaurabh.workshopapp.model.Workshop;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    View fragment_dashboard;

    private WorkshopsDbHelper workshopsDbHelper;
    public static ArrayList<Workshop> al_workshops;

    SharedPreferences.Editor editor;
    private RelativeLayout placeholder;

    DashboardAdapter dashboardAdapter;
    public static RecyclerView recyclerView;
    View listItemView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragment_dashboard = inflater.inflate(R.layout.fragment_dashboard, container, false);

        placeholder = fragment_dashboard.findViewById(R.id.placeholder);

        al_workshops = new ArrayList<>();
        workshopsDbHelper = new WorkshopsDbHelper(getContext());
        if(workshopsDbHelper.numberOfRowsInAppliedWorkshops() == 0){
            placeholder.setVisibility(View.VISIBLE);
        }

        setToolbarTitle();

        recyclerView = (RecyclerView) fragment_dashboard.findViewById(R.id.dashboard_workshops_recyclerview);

        getAppliedWorkshopsFromDatabase();

        return fragment_dashboard;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setToolbarTitle(){
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
    }

    public void getAppliedWorkshopsFromDatabase(){

        SQLiteDatabase db = workshopsDbHelper.getReadableDatabase();
        Cursor rs = db.rawQuery("SELECT * FROM " + workshopsDbHelper.TABLE_NAME_APPLIED_WORKSHOPS,null);

        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {

                String title = rs.getString(rs.getColumnIndex(WorkshopsDbHelper.COLUMN_WORKSHOP_TITLE));
                String description = rs.getString(rs.getColumnIndex(WorkshopsDbHelper.COLUMN_WORKSHOP_DESC));
                int urlToImage = rs.getInt(rs.getColumnIndex(WorkshopsDbHelper.COLUMN_WORKSHOP_URL_TO_IMAGE));
                String workshop_id = rs.getString(rs.getColumnIndex(WorkshopsDbHelper.COLUMN_ID));

                Workshop currentWorkshop = new Workshop(title, description, urlToImage, workshop_id);
                al_workshops.add(currentWorkshop);
                rs.moveToNext();
            }
        }
        rs.close();

        dashboardAdapter = new DashboardAdapter(recyclerView.getContext(), al_workshops);
        recyclerView.setAdapter(dashboardAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
