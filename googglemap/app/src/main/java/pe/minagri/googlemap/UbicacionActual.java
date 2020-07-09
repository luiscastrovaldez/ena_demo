package pe.minagri.googlemap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class UbicacionActual extends AsyncTask<String, Void, Void> {


    private ProgressDialog dialog;
    private double longitude;
    private double latitude;
    private static CameraPosition cameraPosition;
    private GoogleMap mapa;
    private Activity activity;

    private LocationTrack locationTrack;
    private static MarkerOptions posicionActualMarker;

    public UbicacionActual(Activity activity, GoogleMap mapa) {
        this.activity = activity;
        this.mapa = mapa;
    }
    protected void onPreExecute() {

        dialog = new ProgressDialog(activity);
        dialog.setMessage(Constants.OBTENIENDO_UBICACION_ACTUAL);
        dialog.setCancelable(false);
        dialog.show();
    }

    protected Void doInBackground(final String... args) {
        try {
            Thread.sleep(1000);


            activity.runOnUiThread(new Runnable() {
                public void run() {
                    locationTrack = new LocationTrack(activity);
                    if (locationTrack.canGetLocation()) {
                        longitude = locationTrack.getLongitude();
                        latitude = locationTrack.getLatitude();
                    } else {
                        locationTrack.showSettingsAlert();
                    }
                }
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(final Void unused) {

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        posicionActualMarker = new MarkerOptions().position(new LatLng(latitude, longitude));
        String title = "Actual Posicion " + " Lat: " + latitude + " Long: " + longitude;
        posicionActualMarker.title(title);
        posicionActualMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

        mapa.addMarker(posicionActualMarker);

        cameraProperties(new LatLng(latitude, longitude));

    }


    private void cameraProperties(LatLng latLng) {

        if (latLng != null) {
            cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
            mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mapa.getUiSettings().setIndoorLevelPickerEnabled(true);
            mapa.getUiSettings().setZoomControlsEnabled(true);
            mapa.getUiSettings().setRotateGesturesEnabled(true);
            mapa.getUiSettings().setZoomGesturesEnabled(true);
            mapa.getUiSettings().setCompassEnabled(true);
            mapa.getUiSettings().setMapToolbarEnabled(true);
        }

    }
}
