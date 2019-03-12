package com.example.absolutelysaurabh.workshopapp.viewHolder;

import android.content.Context;
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
import com.example.absolutelysaurabh.workshopapp.model.Workshop;

import java.util.ArrayList;

/**
 * Created by absolutelysaurabh on 25/1/18.
 */

public class DashboardViewHolder extends RecyclerView.ViewHolder {

    public Context context;
    public ImageView picture;
    public TextView title;
    public TextView description;
    private WorkshopsDbHelper workshopsDbHelper;
    private ArrayList<Workshop> al_workshops;

    public DashboardViewHolder(LayoutInflater inflater, ViewGroup parent, final ArrayList<Workshop> al_workshops) {

        super(inflater.inflate(R.layout.item_dashboard, parent, false));

        this.context = inflater.getContext();
        this.al_workshops = al_workshops;

        picture = (ImageView) itemView.findViewById(R.id.workshop_image);
        title = (TextView) itemView.findViewById(R.id.workshop_title);
        description = (TextView) itemView.findViewById(R.id.workshop_details);
    }
}

