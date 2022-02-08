package com.atakmap.android.OTN;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import com.atakmap.android.OTN.plugin.R;
import com.atakmap.android.gui.PluginSpinner;
import com.atakmap.android.maps.MapItem;
import com.atakmap.android.maps.PointMapItem;
import com.atakmap.android.routes.Route;
import com.atakmap.android.routes.RoutePlannerInterface;
import com.atakmap.android.routes.RouteGenerationTask;
import com.atakmap.android.routes.RoutePlannerOptionsView;


import com.atakmap.android.routes.RoutePointPackage;
import com.atakmap.android.routes.nav.NavigationCue;
import com.atakmap.coremap.log.Log;
import com.atakmap.coremap.maps.coords.GeoPoint;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.GraphHopperConfig;
import com.graphhopper.ResponsePath;
import com.graphhopper.config.CHProfile;
import com.graphhopper.config.Profile;
import com.graphhopper.util.PointList;

import java.util.List;
import java.util.Map;

public class OTNRouter implements RoutePlannerInterface, AdapterView.OnItemSelectedListener {
    private final GraphHopperConfig jConfig;
    private int selectedProfile = 0;
    private final Context pluginContext;

    public OTNRouter(GraphHopperConfig jConfig, Context pluginContext){

        this.jConfig = jConfig;
        this.pluginContext = pluginContext;
    }

    /**
     * Gets the descriptive name of the planner.
     *
     * @return the descriptive name of the planner
     */
    public String getDescriptiveName(){
        return "OTN offline router";
    }

    /**
     * Planner requires a network to be used.
     *
     * @return true if an active network is required.
     */
    public boolean isNetworkRequired(){
        return false;
    }

    /**
     * Gets the RouteGenerationTask for this planner that is run when initially generating a route.
     * @param routeGenerationEventListener The listener that should be associated with this task.
     * @return A RouteGenerationTask for this planner.
     */
    public RouteGenerationTask getRouteGenerationTask(
            RouteGenerationTask.RouteGenerationEventListener routeGenerationEventListener){
           ;
     return new OTNroutingTask( routeGenerationEventListener,  jConfig , selectedProfile );
    }

    /**
     * Gets the additional options specific for the planner that may effect the
     * results.
     */
    public RoutePlannerOptionsView getOptionsView(AlertDialog parent){
        RoutePlannerOptionsView view= (RoutePlannerOptionsView) LayoutInflater.from(pluginContext).inflate(R.layout.otnplanneroption, null);

        //routing Area

        PluginSpinner AreaSpinner = ( PluginSpinner) view.findViewById(R.id.areaspinner);

        // profile
        PluginSpinner profileSpinner = ( PluginSpinner) view.findViewById(R.id.profilesSpinner);
        ArrayAdapter<String> profileAdapter = new ArrayAdapter<String>( pluginContext , android.R.layout.simple_spinner_dropdown_item );
        for (  Profile item : jConfig.getProfiles() ){
            profileAdapter.add( item.getName() );
        }
        profileSpinner.setAdapter(profileAdapter);
        profileSpinner.setOnItemSelectedListener( this );




        //profile type
        PluginSpinner typeSpinner = ( PluginSpinner) view.findViewById( R.id.typeSpinner );
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>( pluginContext , android.R.layout.simple_spinner_dropdown_item );

        typeAdapter.add( "speed (ch)" );
        typeAdapter.add( "hybrit (alt)" );
        typeAdapter.add( "standard" );
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener( this );



        return view;


    }



    /**
     * Gets any additional options for the planner that are needed at the time of navigating a route.
     *
     * @return Null if the planner does not support additional options, the options otherwise
     */
    public RoutePlannerOptionsView getNavigationOptions(AlertDialog parent){
        return null;
    }

    /**
     * Gets whether or not the planner is capable of supporting re-routing.
     */
    public boolean isRerouteCapable(){
        return false;
    }

    /**
     * Gets whether or not the planner is capable of supporting routing around regions.
     */
    public boolean canRouteAroundRegions(){
        return false;
    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long iD) {

        Log.d("OTN" , "view listener "+Integer.toString(position) + " Id:"+Long.toString(iD) + "adapter ID " + Integer.toString (adapterView.getId() ) );
        switch (adapterView.getId()){
            case (R.id.profilesSpinner ):
                Log.d("OTN" , "view listener profile spinner" );
                break;
            case ( R.id.areaspinner ):
                Log.d("OTN" , "view listener area spinner" );

                break;
            case ( R.id.typeSpinner ):
                Log.d("OTN" , "view listener type spinner" );
            default:
                return;


        }
        if (position == Math.round(iD) ) {
            selectedProfile = position;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



}

