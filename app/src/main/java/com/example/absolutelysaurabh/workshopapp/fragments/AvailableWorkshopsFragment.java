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
import android.widget.Toolbar;

import com.example.absolutelysaurabh.workshopapp.R;
import com.example.absolutelysaurabh.workshopapp.adapter.AvailableWorkshopAdapter;
import com.example.absolutelysaurabh.workshopapp.data.WorkshopsDbHelper;
import com.example.absolutelysaurabh.workshopapp.model.Workshop;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AvailableWorkshopsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AvailableWorkshopsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvailableWorkshopsFragment extends Fragment {

    View availableWorkshopsFragment;

    private WorkshopsDbHelper workshopsDbHelper;
    public static ArrayList<Workshop> al_workshops;

    SharedPreferences.Editor editor;

    public static AvailableWorkshopAdapter availableWorkshopAdapter;
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

    public AvailableWorkshopsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AvailableWorkshopsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AvailableWorkshopsFragment newInstance(String param1, String param2) {
        AvailableWorkshopsFragment fragment = new AvailableWorkshopsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        availableWorkshopsFragment = inflater.inflate(R.layout.fragment_available_workshops, container, false);

        al_workshops = new ArrayList<>();

        workshopsDbHelper = new WorkshopsDbHelper(getContext());
        setToolbarTitle();

        recyclerView = (RecyclerView) availableWorkshopsFragment.findViewById(R.id.available_workshops_recyclerview);

        if(workshopsDbHelper.numberOfRowsInWorkshops()==0){

            //insertDataIntoWorkshopsTable
            insertDataIntoAvailableWorkshopsTable();
        }

        showAvailableWorkshopsFromDatabase();

        return availableWorkshopsFragment;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setToolbarTitle(){
        android.support.v7.widget.Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Available Workshops");
    }

    public void insertDataIntoAvailableWorkshopsTable(){

        workshopsDbHelper.insertWorkshop("Kotlin Programming workshop.", "Intro to Kotlin programming by Bjarne Stroustrope.", (R.drawable.kotlincourseimage), 0);
        workshopsDbHelper.insertWorkshop("Android Development workshop.", "Intro to java programming by Bjarne Stroustrope.", (R.drawable.android), 0);
        workshopsDbHelper.insertWorkshop("MVP architecture workshop.", "Intro to Model View Presenter in android", (R.drawable.mvp_image), 0);
        workshopsDbHelper.insertWorkshop("Android with RxJava workshop.", "Android with RxJava programming by Bjarne Stroustrope.", (R.drawable.rxjavaseriescover), 0);
        workshopsDbHelper.insertWorkshop("Event Bus in Android workshop.", "Intro to event bus in android.", (R.drawable.event_bus), 0);
        workshopsDbHelper.insertWorkshop("Retrofit in Android workshop.", "Using Rterofit in android.", (R.drawable.retrofitfeature_graphic), 0);
        workshopsDbHelper.insertWorkshop("Java Programming workshop.", "Intro to java programming by Bjarne Stroustrope.", (R.drawable.java_image), 0);

        workshopsDbHelper.insertWorkshop("AR Programming workshop.", " Augmented Reality WOrkshop by Bjarne Stroustrope.", (R.drawable.ar), 0);
        workshopsDbHelper.insertWorkshop("Game Engines Workshop.", "Game Enginws workshop by Bjarne Stroustrope.", (R.drawable.game_engine), 0);
        workshopsDbHelper.insertWorkshop("Getting started with Godot workshop.", "Intro to Godot game engine by Bjarne Stroustrope.", (R.drawable.godot), 0);
        workshopsDbHelper.insertWorkshop("Using OpenGL in andorid workshop.", "OpenGL in andorid programming by Bjarne Stroustrope.", (R.drawable.open_gl), 0);
        workshopsDbHelper.insertWorkshop("Rajawali framework for OpenGL Android", "Using Rajawali in Android programming by Bjarne Stroustrope.", (R.drawable.rajawali), 0);
        workshopsDbHelper.insertWorkshop("Backend with Node.js workshop.", "Node.js programming by Bjarne Stroustrope.", (R.drawable.nodejs), 0);
        workshopsDbHelper.insertWorkshop("8085 microprocessor workshop.", "Programming on 8085 microcontroller by Bjarne Stroustrope.", (R.drawable.microprocessor), 0);
        workshopsDbHelper.insertWorkshop("Assembly Language Programming workshop.", "Intro to Assembly language programming by Bjarne Stroustrope.", (R.drawable.asssembly_language), 0);

    }

    public void showAvailableWorkshopsFromDatabase(){

        SQLiteDatabase db = workshopsDbHelper.getReadableDatabase();
        Cursor rs = db.rawQuery("SELECT * FROM " + workshopsDbHelper.TABLE_NAME_WORKSHOPS,null);

        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {

                String title = rs.getString(rs.getColumnIndex(WorkshopsDbHelper.COLUMN_WORKSHOP_TITLE));
                String description = rs.getString(rs.getColumnIndex(WorkshopsDbHelper.COLUMN_WORKSHOP_DESC));
                int urlToImage = rs.getInt(rs.getColumnIndex(WorkshopsDbHelper.COLUMN_WORKSHOP_URL_TO_IMAGE));
                int is_applied = rs.getInt(rs.getColumnIndex(WorkshopsDbHelper.COLUMN_IS_APPLIED));
                String workshop_id = rs.getString(rs.getColumnIndex(WorkshopsDbHelper.COLUMN_ID));

                if(is_applied == 0) {

                    Workshop currentWorkshop = new Workshop(title, description, urlToImage, is_applied, workshop_id);
                    al_workshops.add(currentWorkshop);
                }
                rs.moveToNext();
            }
        }
        rs.close();

        availableWorkshopAdapter = new AvailableWorkshopAdapter(recyclerView.getContext(), al_workshops);
        recyclerView.setAdapter(availableWorkshopAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onResume(){
        super.onResume();
       // ((MainActivity) getActivity()).setActionBarTitle("Dashboard");
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
