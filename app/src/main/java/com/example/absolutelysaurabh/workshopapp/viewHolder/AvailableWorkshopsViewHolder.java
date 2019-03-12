package com.example.absolutelysaurabh.workshopapp.viewHolder;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.absolutelysaurabh.workshopapp.R;
import com.example.absolutelysaurabh.workshopapp.data.WorkshopsDbHelper;
import com.example.absolutelysaurabh.workshopapp.fragments.AvailableWorkshopsFragment;
import com.example.absolutelysaurabh.workshopapp.fragments.LoginFragment;
import com.example.absolutelysaurabh.workshopapp.model.Workshop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * Created by absolutelysaurabh on 25/1/18.
 */

public class AvailableWorkshopsViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "AvailableWorkshopsFragment";

    public Context context;
    public ImageView picture;
    public TextView title;
    public TextView description;
    private WorkshopsDbHelper workshopsDbHelper;
    private ArrayList<Workshop> al_workshops;

    // Firebase instance variables
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;

    public AvailableWorkshopsViewHolder(LayoutInflater inflater, ViewGroup parent, final ArrayList<Workshop> al_workshops) {

        super(inflater.inflate(R.layout.item_workshop, parent, false));

        this.context = inflater.getContext();
        this.al_workshops = al_workshops;

        picture = (ImageView) itemView.findViewById(R.id.workshop_image);
        title = (TextView) itemView.findViewById(R.id.workshop_title);
        description = (TextView) itemView.findViewById(R.id.workshop_details);

        workshopsDbHelper = new WorkshopsDbHelper(context);

        Button button = (Button) itemView.findViewById(R.id.appy_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Initialize Firebase Auth
                mFirebaseAuth = FirebaseAuth.getInstance();
                mFirebaseUser = mFirebaseAuth.getCurrentUser();
                //check if the user is loffed-in
                if (mFirebaseUser == null) {
                    //Initially land to LoginFragment
                    Fragment fragment = new LoginFragment();
                    //replacing the fragment
                    if (fragment != null) {
                        FragmentTransaction ft = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(TAG);
                        ft.commit();
                    }
                    return;
                } else {
                    //if user is logged-in
                    workshopsDbHelper.insertWorkshopApplied(al_workshops.get(getAdapterPosition()).getTitle(),
                            al_workshops.get(getAdapterPosition()).getDescription(), al_workshops.get(getAdapterPosition()).getUrl_to_image());

                    workshopsDbHelper.deleteWorkshop(al_workshops.get(getAdapterPosition()).getWorkshop_id());
                    al_workshops.remove(getAdapterPosition());
                    Toast.makeText(context,"Applied!",Toast.LENGTH_SHORT).show();

                    //notify the recyclerview
                    AvailableWorkshopsFragment.availableWorkshopAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
