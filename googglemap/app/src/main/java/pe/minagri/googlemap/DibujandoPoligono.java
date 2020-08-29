package pe.minagri.googlemap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pe.minagri.googlemap.sql.Point;
import pe.minagri.googlemap.sql.Polygon;
import pe.minagri.googlemap.sql.database.ServiceDatabase;

public class DibujandoPoligono extends AsyncTask<String, Void, Void> {

    private ProgressDialog dialog;
    private Activity activity;
    private List<LatLng> puntos;
    private LatLng[] arrayTotal;
    private double areaTotal;
    private ServiceDatabase serviceDatabase;
    private GoogleMap mapa;
    boolean isLote;


    public DibujandoPoligono(Activity activity, List<LatLng> puntos, ServiceDatabase serviceDatabase, GoogleMap mapa, boolean isLote) {
        this.activity = activity;
        this.puntos = puntos;
        this.serviceDatabase = serviceDatabase;
        this.mapa = mapa;
        this.isLote = isLote;
    }

    protected void onPreExecute() {

        dialog = new ProgressDialog(this.activity);
        dialog.setMessage(Constants.TITULO_GENERANDO_POLIGONO);
        dialog.setCancelable(false);
        dialog.show();
    }

    protected Void doInBackground(final String... args) {
        try {
            Thread.sleep(1000);

            this.activity.runOnUiThread(new Runnable() {
                public void run() {

                    if (puntos != null && puntos.size() > 2) {

                        puntos.add(puntos.get(0));
                        arrayTotal = new LatLng[puntos.size()];
                        puntos.toArray(arrayTotal);
                        areaTotal = PolygonUtils.computeArea(Arrays.asList(arrayTotal));
                        int color = 0;
                        Polygon polygon = null;
                        if (args[1] != null) {
                            polygon = serviceDatabase.getPolygon(args[1]);
                        } else {
                            polygon = new Polygon();
                        }

                        polygon.setArea(areaTotal);
                        polygon.setName(args[0]);
                        if (isLote){
                            polygon.setType("LOTE");
                            polygon.setUidParent(null);
                            color = Color.GREEN;
                        } else {
                            polygon.setType("PARCELA");
                            color = Color.BLUE;
                        }


                        if (args[1] != null) {
                            serviceDatabase.updatePolygon(polygon);
                        } else {
                            serviceDatabase.addPolygon(polygon);
                        }

                        Point point = null;
                        for (LatLng punto : arrayTotal) {
                            point = new Point();
                            point.setLatitude(punto.latitude);
                            point.setLongitude(punto.longitude);
                            point.setUidPolygon(polygon.getUid());
                            serviceDatabase.addPoint(point);
                        }

                        /*
                        com.google.android.gms.maps.model.Polygon polygonMap = mapa.addPolygon(new PolygonOptions()
                                .add(arrayTotal)
                                .strokeColor(color).strokeWidth(2)
                                .fillColor(Color.TRANSPARENT));
                        polygonMap.setClickable(true);
                        */

                        Polyline line = mapa.addPolyline(new PolylineOptions()
                                .add(arrayTotal)
                                .width(5)
                                .color(color));

                        puntos = new ArrayList<>();


                    } else if (puntos.size() != 0) {
                        Toast.makeText(activity.getApplicationContext(), "Debe tener al menos 3 puntos", Toast.LENGTH_SHORT).show();
                    }

                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }

    protected void onPostExecute(final Void unused) {

        if (dialog.isShowing()) {
            dialog.dismiss();
/*
            MapsActivity.CargandoPoligonos load2 = new MapsActivity.CargandoPoligonos();
            load2.execute();

            MapsActivity.GenerandoKml generandoKml = new MapsActivity.GenerandoKml(kmls);
            generandoKml.execute();
*/


        }
    }
}