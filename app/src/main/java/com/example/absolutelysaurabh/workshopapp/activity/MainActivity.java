package com.example.absolutelysaurabh.workshopapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.absolutelysaurabh.workshopapp.R;
import com.example.absolutelysaurabh.workshopapp.fragments.AvailableWorkshopsFragment;
import com.example.absolutelysaurabh.workshopapp.fragments.LoginFragment;
import com.example.absolutelysaurabh.workshopapp.fragments.DashboardFragment;
import com.example.absolutelysaurabh.workshopapp.fragments.SignupFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AvailableWorkshopsFragment.OnFragmentInteractionListener,
        DashboardFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener, SignupFragment.OnFragmentInteractionListener {

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;

    NavigationView nav;
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser != null) {
            mUsername = mFirebaseUser.getDisplayName();
        }

        // initialize the views
        initializeViews();

        //set the avatar in navigationView
        setUserProfileInNavigationView();

        if (mFirebaseUser == null) {
            //if not logged-in open the AvailableWorshops screen.
            Fragment fragment = new AvailableWorkshopsFragment();
            //replacing the fragment
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
            nav.getMenu().getItem(0).setChecked(true);

        } else {
            //if logged-in, Initially land to DashboardScreen
            Fragment fragment = new DashboardFragment();
            //replacing the fragment
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
            nav.getMenu().getItem(1).setChecked(true);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initializeViews(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setMainToolbarTitle();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        nav = (NavigationView) findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(this);

    }

    public void setMainToolbarTitle(){

        if (mFirebaseUser == null) {
            toolbar.setTitle("Available Workshops");
        }else{
            toolbar.setTitle("Dashboard");
        }
    }


    public void setUserProfileInNavigationView(){

        nav = ( NavigationView ) findViewById( R.id.nav_view );
        if( nav != null ){
            LinearLayout mParent = ( LinearLayout ) nav.getHeaderView( 0 );

            if( mParent != null ){

                SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
                String photoUrl = prefs.getString("photo_url", null);
                String user_name = prefs.getString("name", "User");

                if(photoUrl!=null) {
                    Log.e("Photo Url: ", photoUrl);

                    TextView userName = (TextView) mParent.findViewById(R.id.user_name);
                    userName.setText(user_name);

                    ImageView user_imageView = (ImageView) mParent.findViewById(R.id.user_avatar);

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.ic_edit_black_24dp);
                    requestOptions.error(R.drawable.ic_edit_black_24dp);

                    Glide.with(this).load(photoUrl).apply(requestOptions).thumbnail(0.5f).into(user_imageView);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:

                // Initialize Firebase Auth
                mFirebaseAuth = FirebaseAuth.getInstance();
                mFirebaseUser = mFirebaseAuth.getCurrentUser();
                //check if the user is loffed-in
                if (mFirebaseUser == null) {
                    //if the user is not logged-in
                    Toast toast=Toast.makeText(this,"Login First!",Toast.LENGTH_SHORT);
                    toast.show();
                }else {

                    mFirebaseAuth.signOut();
                    //Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                    mUsername = "ANONMOUS";

                    //take to the available workshops fragment
                    //if not logged-in open the AvailableWorshops screen.
                    Fragment fragment = new AvailableWorkshopsFragment();
                    //replacing the fragment
                    if (fragment != null) {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();
                    }

                    Toast toast = Toast.makeText(this, "Logged-out successfully!", Toast.LENGTH_SHORT);
                    toast.show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //creating fragment object
        Fragment fragment = null;

        if (id == R.id.nav_available_workshops) {

            //open available worksops fragment
            fragment = new AvailableWorkshopsFragment();

        } else if (id == R.id.nav_dashboard) {

            // Initialize Firebase Auth
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseUser = mFirebaseAuth.getCurrentUser();
            //check if the user is loffed-in
            if (mFirebaseUser == null) {
                //if the user is not logged-in
                Toast toast=Toast.makeText(getApplicationContext(),"Login First!",Toast.LENGTH_SHORT);
                toast.show();
            }else{
                //open dashboard fragment
                fragment = new DashboardFragment();
            }

        } else if (id == R.id.nav_settings) {
            Toast toast=Toast.makeText(getApplicationContext(),"Coming Soon!!",Toast.LENGTH_SHORT);
            toast.show();
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
