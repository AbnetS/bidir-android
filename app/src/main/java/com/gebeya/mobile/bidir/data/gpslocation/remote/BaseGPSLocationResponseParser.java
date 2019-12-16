package com.gebeya.mobile.bidir.data.gpslocation.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.gpslocation.GPSLocation;
import com.gebeya.mobile.bidir.data.gpslocation.GPSLocationResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by abuti on 5/17/2018.
 */

public class BaseGPSLocationResponseParser implements GPSLocationResponseParser{

    @Override
    public GPSLocationResponse parse(@NonNull JsonObject object, @NonNull String acatApplicationID, @NonNull String cropACATID) throws Exception {
        final GPSLocationResponse response = new GPSLocationResponse();
        final GPSLocation gpsLocation = new GPSLocation();
        response.gpsLocations = new ArrayList<>();

        try{
            final JsonArray polygonArray = object.getAsJsonArray("polygon");
            if (polygonArray.size() > 0){
                for (int i=0 ;i < polygonArray.size(); i++){
                    gpsLocation.acatApplicationID = acatApplicationID;
                    gpsLocation.cropACATID = cropACATID;
                    JsonObject polygonObject = polygonArray.get(i).getAsJsonObject();
                    gpsLocation.latitude = polygonObject.get("latitude").getAsDouble();
                    gpsLocation.longitude = polygonObject.get("longitude").getAsDouble();
                    response.gpsLocations.add(gpsLocation);
                }
            }
            else{
                JsonObject singleGPSObject = object.getAsJsonObject("single_point");
                gpsLocation.acatApplicationID = acatApplicationID;
                gpsLocation.cropACATID = cropACATID;
                gpsLocation.latitude = singleGPSObject.get("latitude").getAsDouble();
                gpsLocation.longitude = singleGPSObject.get("longitude").getAsDouble();
                response.gpsLocations.add(gpsLocation);

            }
            return response;

        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
