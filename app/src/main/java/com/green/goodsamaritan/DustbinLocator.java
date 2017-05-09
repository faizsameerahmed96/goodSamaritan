package com.green.goodsamaritan;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;

public class DustbinLocator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dustbin_locator);

        initView();
    }

    private void initView() {
        findViewById(R.id.fab_add_dustbin_adl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(DustbinLocator.this), 2);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.iv_navigate_dustbin_adl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Make more attractive loading... Show message Searching for nearest bin
                ApplicationUtils.showLoading(DustbinLocator.this, true);
                Toast.makeText(DustbinLocator.this, "Searching nearest bin!", Toast.LENGTH_LONG).show();

                if (!SmartLocation.with(DustbinLocator.this).location().state().locationServicesEnabled()) {
                    //TODO Show prompt to enable location
                    Toast.makeText(DustbinLocator.this, "Enable location services.", Toast.LENGTH_LONG).show();
                    ApplicationUtils.showLoading(DustbinLocator.this, false);
                    return;
                }

                //First get current location
                if (!SmartLocation.with(DustbinLocator.this).location().state().isGpsAvailable()) {
                    Toast.makeText(DustbinLocator.this, "GPS not available. Not getting accurate location...", Toast.LENGTH_LONG).show();
                }

                SmartLocation.with(DustbinLocator.this).location()
                        .oneFix()
                        .config(LocationParams.BEST_EFFORT)
                        .start(new OnLocationUpdatedListener() {
                            @Override
                            public void onLocationUpdated(Location location) {
                                final double slat = location.getLatitude();
                                final double slong = location.getLongitude();
                                Log.e("message", "Lat: " + slat + "Long: " + slong);
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Dusbins");
                                query.whereNear("location", new ParseGeoPoint(location.getLatitude(), location.getLongitude()));
                                query.getFirstInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        ApplicationUtils.showLoading(DustbinLocator.this, false);
                                        if (e == null) {
                                            ParseGeoPoint point = object.getParseGeoPoint("location");
                                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                                    Uri.parse("http://maps.google.com/maps?saddr=" + slat + "," + slong +
                                                            "&daddr=" + point.getLatitude() + "," + point.getLongitude()));
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(DustbinLocator.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            ApplicationUtils.showLoading(DustbinLocator.this, true);
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                Log.e("message", place.toString());
                if (resultCode == RESULT_OK) {
                    //Add the dustbin pin
                    ParseObject object = new ParseObject("Dusbins");
                    object.put("byUser", ParseUser.getCurrentUser());
                    object.put("location", new ParseGeoPoint(place.getLatLng().latitude, place.getLatLng().longitude));
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            ApplicationUtils.showLoading(DustbinLocator.this, false);
                            if (e == null) {
                                Toast.makeText(DustbinLocator.this, "The dustbin's pin has been added to the database.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(DustbinLocator.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    ApplicationUtils.showLoading(DustbinLocator.this, false);
                }
            } else {
                Toast.makeText(DustbinLocator.this, "Location was not selected", Toast.LENGTH_LONG).show();
            }
        }
    }
}
