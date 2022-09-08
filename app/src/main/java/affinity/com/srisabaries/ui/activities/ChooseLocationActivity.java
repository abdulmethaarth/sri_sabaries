package affinity.com.srisabaries.ui.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import affinity.com.srisabaries.R;
import affinity.com.srisabaries.adapters.PlacesAutoCompleteAdapter;
import affinity.com.srisabaries.models.PlaceModel;
import affinity.com.srisabaries.utils.AppConstants;
import affinity.com.srisabaries.utils.AppUtilMethods;
import butterknife.BindView;

import static org.acra.ACRA.LOG_TAG;

public class ChooseLocationActivity extends BaseActivity implements AdapterView.OnItemClickListener,GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_cross)
    ImageView ivCross;
    @BindView(R.id.atv_Address)
    AutoCompleteTextView atvAddress;
    @BindView(R.id.lv_address)
    ListView lvAddress;
    private PlacesAutoCompleteAdapter mAdapter;
    private int mFromWhere;
    private static final int GOOGLE_API_CLIENT_ID = 0;

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        super.initData();
    }

    @Override
    protected void initViews() {
        atvAddress.requestFocus();
        AppUtilMethods.showKeyBoard(this);

        //set listeners
        ivBack.setOnClickListener(this);
        ivCross.setOnClickListener(this);
        lvAddress.setOnItemClickListener(this);
    }

    @Override
    protected void initVariables() {
        mFromWhere = getIntent().getIntExtra(AppConstants.TAG_FROM_WHERE, 0);
        String type = mFromWhere == AppConstants.FROM_PROFILE ? "" : AppConstants.TYPE_CITY;
        mAdapter = new PlacesAutoCompleteAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        //lvAddress.setAdapter(mAdapter);
        atvAddress.setAdapter(mAdapter);

        mGoogleApiClient = new GoogleApiClient.Builder(ChooseLocationActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        atvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppUtilMethods.hideKeyBoard(ChooseLocationActivity.this);
                final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                Log.i(LOG_TAG, "Selected: " + item.description);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
                Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {




    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                return;
            }

            // Setup Geocoder
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses;

            final Place place = places.get(0);
            // Attempt to Geocode from place lat & long
            try {

                addresses = geocoder.getFromLocation(
                        place.getLatLng().latitude,
                        place.getLatLng().longitude,
                        1);

                if (addresses.size() > 0) {

                    // Here are some results you can geocode
                    String ZIP="";
                    String city="";
                    String state="";
                    String country="";

                    if (addresses.get(0).getPostalCode() != null) {
                        ZIP = addresses.get(0).getPostalCode();
                        Log.d("ZIP", ZIP);
                    }

                    if (addresses.get(0).getLocality() != null) {
                        city = addresses.get(0).getLocality();
                        Log.d("city", city);
                    }

                    if (addresses.get(0).getAdminArea() != null) {
                        state = addresses.get(0).getAdminArea();
                        Log.d("state", state);
                    }

                    if (addresses.get(0).getCountryName() != null) {
                        country = addresses.get(0).getCountryName();
                        Log.d("country", country);
                    }

                    PlaceModel placeModel = new PlaceModel();
                    placeModel.setName(place.getName().toString());
                    placeModel.setCity(city);
                    placeModel.setState(state);

                    Intent intent = new Intent();
                    intent.putExtra(AppConstants.TAG_PLACE_MODEL, placeModel);
                    setResult(RESULT_OK, intent);
                    finish();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            places.release();


        }
    };



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_cross:
                atvAddress.setText("");
                AppUtilMethods.hideKeyBoard(this);
                break;
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        mAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }
}
