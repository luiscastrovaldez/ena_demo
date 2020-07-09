package pe.minagri.googlemap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;


import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import pe.minagri.googlemap.newkml.Kml;
import pe.minagri.googlemap.newkml.Placemark;

public class GenerandoKml extends AsyncTask<String, Integer, String> {

    private static CameraPosition cameraPosition;
    private GoogleMap mapa;
    private String result;
    private ProgressDialog mProgressDialog;
    private int mProgressStatus = 0;
    private List<String> kmls;
    private Activity activity;

    private static List<List<LatLng>> puntos1;
    private static List<List<LatLng>> puntos2;
    private static List<List<LatLng>> puntos3;

    private AssetManager assetManager;

    public GenerandoKml(List<String> kmls, GoogleMap mapa, Activity activity, AssetManager assetManager) {
        this.kmls = kmls;
        this.mapa = mapa;
        this.activity = activity;
        this.assetManager = assetManager;
    }

    @Override
    public void onPreExecute() {
        mProgressStatus = 0;
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Importando KML");
        mProgressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        InputStream is = null;
        puntos1 = new ArrayList<List<LatLng>>();
        puntos2 = new ArrayList<List<LatLng>>();
        puntos3 = new ArrayList<List<LatLng>>();

        try {

            for (int i = 0; i < this.kmls.size(); i++) {



                if (kmls.get(i).equals(Constants.MUESTRA_CAMPO_MARTE_KML)) {
                    if (puntos1.size() == 0)
                        puntos1.addAll(nuevasCoodenadas(Constants.MUESTRA_CAMPO_MARTE_KML));
                }

                if (kmls.get(i).equals(Constants.SM_ENA_BE_SM_KML)) {
                    if (puntos2.size() == 0)
                        puntos2.addAll(nuevasCoodenadas(Constants.SM_ENA_BE_SM_KML));
                }

                if (kmls.get(i).equals(Constants.SM_ENA_VIRU_KML)) {
                    if (puntos3.size() == 0)
                        puntos3.addAll(nuevasCoodenadas(Constants.SM_ENA_VIRU_KML));
                }

            }


            result = "in";
            Thread.sleep(1000);

        } catch (Exception e) {
            e.printStackTrace();
            result = "out";
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mProgressDialog.setProgress(mProgressStatus);
    }

    @Override
    public void onPostExecute(String result) {

        if (result.equalsIgnoreCase("in")) {

            for (int i = 0; i < puntos1.size(); i++) {
                mapa.addPolygon(new PolygonOptions().strokeColor(Color.BLACK).addAll(puntos1.get(i))).setStrokeWidth(4);
                cameraProperties(puntos1.get(0).get(0));

            }


            for (int i = 0; i < puntos2.size(); i++) {
                mapa.addPolygon(new PolygonOptions().strokeColor(Color.BLUE).addAll(puntos2.get(i))).setStrokeWidth(4);
                cameraProperties(puntos2.get(0).get(0));

            }

            for (int i = 0; i < puntos3.size(); i++) {
                mapa.addPolygon(new PolygonOptions().strokeColor(Color.RED).addAll(puntos3.get(i))).setStrokeWidth(4);
                cameraProperties(puntos3.get(0).get(0));

            }


        }

        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private List<List<LatLng>> nuevasCoodenadas(String path) {
        List<List<LatLng>> puntosTotales1 = null;
        InputStream is = null;
        try {
            List<LatLng> points1;
            is = assetManager.open(path, AssetManager.ACCESS_BUFFER);
            Serializer serializer = new Persister();
            puntosTotales1 = new ArrayList<List<LatLng>>();
            Kml kml = serializer.read(Kml.class, is);
            System.out.println(kml.getDocument().getId());
            System.out.println(kml.getDocument().getFolder().getName());
            List<Placemark> placemarks = kml.getDocument().getFolder().getPlacemarks();
            for (Placemark placemark : placemarks) {
                points1 = new ArrayList<LatLng>();
                if (placemark.getMultiGeometry() != null && placemark.getMultiGeometry().getPolygon() != null) {
                    String coordenadas = placemark.getMultiGeometry().getPolygon().getOuterBoundaryIs().getLinearRing()
                            .getCoordinates().trim();

                    if (coordenadas.contains("0.000000")) {
                        coordenadas = coordenadas.replace("0.000000", "");
                    } else if (coordenadas.contains("0 -")) {
                        coordenadas = coordenadas.replace("0 -", "-");
                    }

                    String[] puntos = coordenadas.split(",");
                    for (int j = 0; j < puntos.length; j = j + 2) {
                        try {
                            double latitude = Double.parseDouble(puntos[j + 1]);
                            double longitude = Double.parseDouble(puntos[j]);
                            LatLng latLng = new LatLng(latitude, longitude);
                            points1.add(latLng);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    puntosTotales1.add(points1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return puntosTotales1;
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
