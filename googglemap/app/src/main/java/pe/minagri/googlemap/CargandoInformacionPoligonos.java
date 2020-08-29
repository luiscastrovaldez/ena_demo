package pe.minagri.googlemap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import pe.minagri.googlemap.bean.CoordenadasBean;
import pe.minagri.googlemap.sql.Point;
import pe.minagri.googlemap.sql.Polygon;
import pe.minagri.googlemap.sql.database.ServiceDatabase;

public class CargandoInformacionPoligonos extends AsyncTask<String, Integer, String> {
    private String result;
    private ProgressDialog mProgressDialog;
    private int mProgressStatus = 0;
    private Activity activity;
    private ServiceDatabase serviceDatabase;
    private GoogleMap mapa;

    private List<LatLng> puntosCargar;
    private List<CoordenadasBean> beans;

    private LatLng[] arrayTotal;

    public CargandoInformacionPoligonos(Activity activity, ServiceDatabase serviceDatabase, GoogleMap mapa) {
        this.activity = activity;
        this.serviceDatabase = serviceDatabase;
        this.mapa = mapa;
    }

    @Override
    public void onPreExecute() {
        mProgressStatus = 0;
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(Constants.OBTENIENDO_INFORMACION_POLIGONO);
        mProgressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        InputStream is = null;
        puntosCargar = new ArrayList<>();
        beans = new ArrayList<>();
        try {

            List<Polygon> polygons = serviceDatabase.getPolygons();
            List<Point> points = null;
            CoordenadasBean bean = null;
            for (Polygon polygon : polygons) {
                bean = new CoordenadasBean();
                bean.setTipoGrafico(polygon.getType());
                bean.setId(polygon.getUid());
                bean.setArea(polygon.getArea());
                points = serviceDatabase.getPointByPolygonId(polygon.getUid());
                puntosCargar = new ArrayList<>();
                for (Point point : points) {
                    LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
                    puntosCargar.add(latLng);
                }
                arrayTotal = new LatLng[puntosCargar.size()];
                puntosCargar.toArray(arrayTotal);
                bean.setArrayTotal(arrayTotal);
                beans.add(bean);
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


        int color = 0;
        if (result.equalsIgnoreCase("in")) {
            for (CoordenadasBean bean : beans) {

                if (bean.tipoGrafico.equals(Constants.LOTE)) {
                    color = Color.GREEN;
                } else {
                    color = Color.BLUE;
                }

                /*
                com.google.android.gms.maps.model.Polygon polygon = mapa.addPolygon(new PolygonOptions()
                        .add(bean.getArrayTotal())
                        .strokeColor(color).strokeWidth(2)
                        .fillColor(Color.TRANSPARENT));
                polygon.setClickable(true);

                */


                Polyline line = mapa.addPolyline(new PolylineOptions()
                        .add(bean.getArrayTotal())
                        .width(5)
                        .color(color));

                //polygon.setTag(bean.getArea() + "#" + bean.getId() + "#" + bean.getTipoGrafico());
            }
        }

        if (mProgressDialog.isShowing()) {
            ((MapsActivity)activity).cargarInformacionParcelas();
            mProgressDialog.dismiss();
        }
    }
}