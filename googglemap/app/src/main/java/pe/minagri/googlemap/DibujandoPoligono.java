package pe.minagri.googlemap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pe.minagri.googlemap.sql.Cabecera;
import pe.minagri.googlemap.sql.Detalle;
import pe.minagri.googlemap.sql.database.ServiceDatabase;

public class DibujandoPoligono extends AsyncTask<String, Void, Void> {

    private ProgressDialog dialog;
    private Activity activity;
    private List<LatLng> puntos;
    private LatLng[] arrayTotal;
    private double areaTotal;
    private ServiceDatabase serviceDatabase;
    private GoogleMap mapa;


    public DibujandoPoligono(Activity activity, List<LatLng> puntos, ServiceDatabase serviceDatabase, GoogleMap mapa) {
        this.activity = activity;
        this.puntos = puntos;
        this.serviceDatabase = serviceDatabase;
        this.mapa = mapa;
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

                        Cabecera cabecera = new Cabecera();
                        cabecera.setArea(areaTotal);
                        cabecera.setTipoGrafico(args[0]);

                        serviceDatabase.addCabecera(cabecera);
                        Detalle detalle = null;
                        for (LatLng punto : arrayTotal) {
                            detalle = new Detalle();
                            detalle.setLatitud(punto.latitude);
                            detalle.setLongitud(punto.longitude);
                            detalle.setUidCabecera(cabecera.getUid());
                            serviceDatabase.addDetalle(detalle);
                        }

                        int color = 0;

                        if (Constants.LOTE.equals(args[0])) {
                            color = Color.GREEN;
                        } else {
                            color = Color.RED;
                        }



                        Polygon polygon = mapa.addPolygon(new PolygonOptions()
                                .add(arrayTotal)
                                .strokeColor(color).strokeWidth(2)
                                .fillColor(Color.TRANSPARENT));
                        polygon.setClickable(true);
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