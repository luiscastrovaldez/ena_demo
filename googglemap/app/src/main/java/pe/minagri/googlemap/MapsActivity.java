package pe.minagri.googlemap;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import pe.minagri.googlemap.bean.CoordenadasBean;
import pe.minagri.googlemap.newkml.Kml;
import pe.minagri.googlemap.kml.ParsingStructure;
import pe.minagri.googlemap.newkml.Placemark;
import pe.minagri.googlemap.kml.SAXXMLParser;
import pe.minagri.googlemap.sql.Cabecera;
import pe.minagri.googlemap.sql.Detalle;
import pe.minagri.googlemap.sql.database.ServiceDatabase;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnPolygonClickListener {

    private GoogleMap mapa;
    private final LatLng UPV = new LatLng(-12.063403, -77.039376);

    private static final String MINAGRI = "Minagri";

    private List<LatLng> puntos;
    private List<LatLng> total;

    private double areaTotal;

    private LatLng[] arrayTotal;

    private LocationTrack locationTrack;
    private static MarkerOptions posicionActualMarker;


    private static CameraPosition cameraPosition;

    private FloatingActionButton actualPosicion, tipoMapa, generarPoligono, limpiar;

    private ServiceDatabase serviceDatabase;

    private static List<String> kmls;
    private static List<List<LatLng>> puntos1;
    private static List<List<LatLng>> puntos2;
    private static List<List<LatLng>> puntos3;
    private static List<List<LatLng>> puntos4;
    private static List<List<LatLng>> puntos5;
    private static List<List<LatLng>> puntos6;

    private static List<LatLng> points;
    private static List<List<LatLng>> puntosTotales;
    private String rutaKml;
    public static List<ParsingStructure> parsingStr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            cargandoComponentes();

            CargandoPoligonos cargandoPoligonos = new CargandoPoligonos();
            cargandoPoligonos.execute();
        }




    }

    private void crearFolder(){
        File mediaStorageDir = new File(Constants.PATH_MINAGRI);
        String csvFile = mediaStorageDir.getAbsolutePath() + File.separator;

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Toast.makeText(getApplicationContext(), "Error al crear la carpeta del minagri ", Toast.LENGTH_LONG).show();
                return;
            }
        }

        mediaStorageDir = new File(Constants.PATH_MINAGRI_DB);
        csvFile = mediaStorageDir.getAbsolutePath() + File.separator;

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Toast.makeText(getApplicationContext(), "Error al crear la carpeta db ", Toast.LENGTH_LONG).show();
                return;
            }
        }

        mediaStorageDir = new File(Constants.PATH_MINAGRI_FOTOS);
        csvFile = mediaStorageDir.getAbsolutePath() + File.separator;

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Toast.makeText(getApplicationContext(), "Error al crear la carpeta fotos ", Toast.LENGTH_LONG).show();
                return;
            }
        }

        mediaStorageDir = new File(Constants.PATH_MINAGRI_KML);
        csvFile = mediaStorageDir.getAbsolutePath() + File.separator;

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Toast.makeText(getApplicationContext(), "Error al crear la carpeta kml ", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    private void cargandoComponentes() {

        crearFolder();

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        puntos = new ArrayList<LatLng>();

        actualPosicion = (FloatingActionButton) findViewById(R.id.actualPosicion);
        tipoMapa = (FloatingActionButton) findViewById(R.id.tipoMapa);
        generarPoligono = (FloatingActionButton) findViewById(R.id.generarPoligono);
        limpiar = (FloatingActionButton) findViewById(R.id.limpiar);

        serviceDatabase = ServiceDatabase.get(this);

        kmls = new ArrayList<String>();
        puntos1 = new ArrayList<List<LatLng>>();
        puntos2 = new ArrayList<List<LatLng>>();
        puntos3 = new ArrayList<List<LatLng>>();
        puntos4 = new ArrayList<List<LatLng>>();
        puntos5 = new ArrayList<List<LatLng>>();
        puntos6 = new ArrayList<List<LatLng>>();
        puntosTotales = new ArrayList<List<LatLng>>();
        rutaKml = Constants.PATH_MINAGRI_KML;

        actualPosicion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ObteniendoCoordenadas load = new ObteniendoCoordenadas();
                load.execute();
            }
        });

        generarPoligono.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Generar Poligono")
                        .setMessage("Generar Poligono")

                        .setPositiveButton("Lote", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                GenerandoPoligono load1 = new GenerandoPoligono();
                                load1.execute("LOTE");

                                ObteniendoCoordenadas load = new ObteniendoCoordenadas();
                                load.execute();

                                CargandoPoligonos load2 = new CargandoPoligonos();
                                load2.execute();

                                GenerandoKml generandoKml = new GenerandoKml(kmls);
                                generandoKml.execute();


                            }
                        }).setNegativeButton("Parcela", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                GenerandoPoligono load1 = new GenerandoPoligono();
                                load1.execute("PARCELA");

                                ObteniendoCoordenadas load = new ObteniendoCoordenadas();
                                load.execute();

                                CargandoPoligonos load2 = new CargandoPoligonos();
                                load2.execute();

                                GenerandoKml generandoKml = new GenerandoKml(kmls);
                                generandoKml.execute();

                            }
                        })
                        .show();








            }
        });

        limpiar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mapa.clear();
                puntos = new ArrayList<LatLng>();
            }
        });


        tipoMapa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mapa.getMapType() == 1) {
                    mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else {
                    mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }
        });



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapa.getUiSettings().setZoomControlsEnabled(false);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(UPV, 15));

        mapa.addMarker(new MarkerOptions()
                .position(UPV)
                .title(MINAGRI)
                .snippet(MINAGRI)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .anchor(0.5f, 0.5f));
        mapa.setOnMapClickListener(this);
        mapa.setOnPolygonClickListener(this);
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            //mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setCompassEnabled(true);
        }
    }

    @Override
    public void onMapClick(LatLng puntoPulsado) {


        mapa.addMarker(new MarkerOptions()
                .position(puntoPulsado)
                .title("UPV")
                .snippet("Latitud/Longitud " + puntoPulsado.latitude + "/" + puntoPulsado.longitude)
                .icon(BitmapDescriptorFactory
                        .fromResource(android.R.drawable.ic_menu_compass))
                .anchor(0.5f, 0.5f));

        puntos.add(puntoPulsado);
        graficarLinea(puntos);

    }

    void graficarLinea(List<LatLng> puntos) {
        if (puntos.size() > 1) {
            LatLng[] array = new LatLng[puntos.size()];
            puntos.toArray(array);
            Polyline line = mapa.addPolyline(new PolylineOptions()
                    .add(array)
                    .width(5)
                    .color(Color.YELLOW));
        }
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        mapa.addMarker(new MarkerOptions()
                .position(latLng)
                .title("UPV")
                .snippet("Latitud/Longitud " + latLng.latitude + "/" + latLng.longitude)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("K")) {
                dist = dist * 1.609344;
            } else if (unit.equals("N")) {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }

    @Override
    public void onPolygonClick(Polygon polygon) {

        String area = polygon.getTag().toString().split("#")[0];
        final String id = polygon.getTag().toString().split("#")[1];
        final String tipo = polygon.getTag().toString().split("#")[2];

        AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Area Total")
                .setMessage("Area Calculada  " + tipo + " = " + area +  " \nDesea Eliminar el Poligono")

                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        serviceDatabase.deleteDetalleByCabeceraId(id);
                        serviceDatabase.deleteCabeceraById(id);

                        CargandoPoligonos load2 = new CargandoPoligonos();
                        load2.execute();

                        GenerandoKml generandoKml = new GenerandoKml(kmls);
                        generandoKml.execute();


                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();

    }

    private class ObteniendoCoordenadas extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;
        double longitude;
        double latitude;

        protected void onPreExecute() {

            dialog = new ProgressDialog(MapsActivity.this);
            dialog.setMessage("Obteniendo Ubicacion Actual...");
            dialog.setCancelable(false);
            dialog.show();
        }

        protected Void doInBackground(final String... args) {
            try {
                Thread.sleep(1000);


                runOnUiThread(new Runnable() {
                    public void run() {
                        locationTrack = new LocationTrack(MapsActivity.this);
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
    }


    private class GenerandoPoligono extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;
        double longitude;
        double latitude;

        protected void onPreExecute() {

            dialog = new ProgressDialog(MapsActivity.this);
            dialog.setMessage("Generando Poligono...");
            dialog.setCancelable(false);
            dialog.show();
        }

        protected Void doInBackground(final String... args) {
            try {
                Thread.sleep(1000);

                runOnUiThread(new Runnable() {
                    public void run() {

                        if (puntos!= null && puntos.size() > 2) {

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

                            mapa.clear();
                            Polygon polygon = mapa.addPolygon(new PolygonOptions()
                                    .add(arrayTotal)
                                    .strokeColor(Color.GREEN)
                                    .fillColor(Color.LTGRAY));
                            polygon.setClickable(true);
                            puntos = new ArrayList<>();


                        } else if (puntos.size() != 0){
                                Toast.makeText(getApplicationContext(), "Debe tener al menos 3 puntos", Toast.LENGTH_SHORT).show();
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

                CargandoPoligonos load2 = new CargandoPoligonos();
                load2.execute();

                GenerandoKml generandoKml = new GenerandoKml(kmls);
                generandoKml.execute();

            }
        }
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


    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        String ruta;
        int index = 0;
        switch (view.getId()) {
            case R.id.checkbox1:
                ruta = rutaKml + File.separator + "Vias Vecinal LG.kml";
                if (checked)

                    kmls.add(ruta);
                else {
                    if (kmls.contains(ruta)) {
                        index = kmls.indexOf(ruta);
                        kmls.remove(index);
                        puntos1 = new ArrayList<List<LatLng>>();
                    }
                }

                break;
            case R.id.checkbox2:

                ruta = rutaKml + File.separator + "Centros Poblados LG.kml";
                if (checked)

                    kmls.add(ruta);
                else {
                    if (kmls.contains(ruta)) {
                        index = kmls.indexOf(ruta);
                        kmls.remove(index);
                        puntos2 = new ArrayList<List<LatLng>>();
                    }
                }

                break;
            case R.id.checkbox3:
                ruta = rutaKml + File.separator + "Vias Depart LG.kml";
                if (checked)

                    kmls.add(ruta);
                else {
                    if (kmls.contains(ruta)) {
                        index = kmls.indexOf(ruta);
                        kmls.remove(index);
                        puntos3 = new ArrayList<List<LatLng>>();
                    }
                }

                break;
            case R.id.checkbox4:
                ruta = rutaKml + File.separator + "Catastro Rural LG 2018.kml";
                if (checked)

                    kmls.add(ruta);
                else {
                    if (kmls.contains(ruta)) {
                        index = kmls.indexOf(ruta);
                        kmls.remove(index);
                        puntos4 = new ArrayList<List<LatLng>>();
                    }
                }

                break;
            case R.id.checkbox5:
                ruta = rutaKml + File.separator + "SM_ENA_VIRU.kml";
                if (checked)

                    kmls.add(ruta);
                else {
                    if (kmls.contains(ruta)) {
                        index = kmls.indexOf(ruta);
                        kmls.remove(index);
                        puntos5 = new ArrayList<List<LatLng>>();
                    }
                }

                break;
            case R.id.checkbox6:
                ruta = rutaKml + File.separator + "MUESTRA_CAMPO_MARTE.kml";
                if (checked)

                    kmls.add(ruta);
                else {
                    if (kmls.contains(ruta)) {
                        index = kmls.indexOf(ruta);
                        kmls.remove(index);
                        puntos6 = new ArrayList<List<LatLng>>();
                    }
                }

                break;

        }

        CargandoPoligonos load2 = new CargandoPoligonos();
        load2.execute();

        GenerandoKml generandoKml = new GenerandoKml(kmls);
        generandoKml.execute();


    }


    public class GenerandoKml extends AsyncTask<String, Integer, String> {
        private String result;
        private ProgressDialog mProgressDialog;
        private int mProgressStatus = 0;
        private List<String> kmls;

        public GenerandoKml(List<String> kmls) {
            this.kmls = kmls;
        }

        @Override
        public void onPreExecute() {
            mProgressStatus = 0;
            mProgressDialog = new ProgressDialog(MapsActivity.this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("Cargando KML");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            InputStream is = null;
            try {

                for (int i = 0; i < this.kmls.size(); i++) {

                    if (!kmls.get(i).equals(rutaKml + File.separator + "SM_ENA_VIRU.kml") && !kmls.get(i).equals(rutaKml + File.separator + "MUESTRA_CAMPO_MARTE.kml")) {
                        is = new FileInputStream(this.kmls.get(i));
                        parsingStr = SAXXMLParser.parse(is);
                        result = "in";
                        is.close();

                        for (ParsingStructure parsingStructure : parsingStr) {
                            points = new ArrayList<LatLng>();
                            String coordenadas = parsingStructure.getCoordinates();
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
                                    points.add(latLng);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            puntosTotales.add(points);
                            mProgressStatus++;
                            publishProgress(mProgressStatus);

                        }
                        if (kmls.get(i)
                                .equals(rutaKml + File.separator + "Vias Vecinal LG.kml")) {
                            if (puntos1.size() == 0)
                                puntos1.addAll(puntosTotales);
                        }

                        if (kmls.get(i).equals(rutaKml + File.separator + "Centros Poblados LG.kml")) {
                            if (puntos2.size() == 0)
                                puntos2.addAll(puntosTotales);
                        }

                        if (kmls.get(i).equals(rutaKml + File.separator + "Vias Depart LG.kml")) {
                            if (puntos3.size() == 0)
                                puntos3.addAll(puntosTotales);
                        }
                        if (kmls.get(i).equals(rutaKml + File.separator + "Catastro Rural LG 2018.kml")) {
                            if (puntos4.size() == 0)
                                puntos4.addAll(puntosTotales);

                        }
                    } else {
                        if (kmls.get(i).equals(rutaKml + File.separator + "SM_ENA_VIRU.kml")) {
                            if (puntos5.size() == 0)
                                puntos5.addAll(nuevasCoodenadas(rutaKml + File.separator + "SM_ENA_VIRU.kml"));
                        }

                        if (kmls.get(i).equals(rutaKml + File.separator + "MUESTRA_CAMPO_MARTE.kml")) {
                            if (puntos6.size() == 0)
                                puntos6.addAll(nuevasCoodenadas(rutaKml + File.separator + "MUESTRA_CAMPO_MARTE.kml"));
                        }
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
                    mapa.addPolyline(new PolylineOptions().color(Color.RED).addAll(puntos1.get(i))).setWidth(4);
                    cameraProperties(puntos1.get(0).get(0));
                }

                LatLng latLng = null;
                for (int i = 0; i < puntos2.size(); i++) {

                    latLng = puntos2.get(i).get(0);
                    posicionActualMarker = new MarkerOptions().position(latLng)
                            .title("Centro Poblados" + " Lat: " + latLng.latitude + " Long: "
                                    + latLng.longitude);

                    posicionActualMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                    mapa.addMarker(posicionActualMarker);

                    cameraProperties(puntos2.get(0).get(0));
                }

                for (int i = 0; i < puntos3.size(); i++) {
                    mapa.addPolyline(new PolylineOptions().color(Color.BLUE).addAll(puntos3.get(i))).setWidth(4);
                    cameraProperties(puntos3.get(0).get(0));
                }


                for (int i = 0; i < puntos4.size(); i++) {
                    mapa.addPolygon(new PolygonOptions().strokeColor(Color.BLACK).addAll(puntos4.get(i))).setStrokeWidth(4);
                    cameraProperties(puntos4.get(0).get(0));

                }


                for (int i = 0; i < puntos5.size(); i++) {
                    mapa.addPolygon(new PolygonOptions().strokeColor(Color.BLUE).addAll(puntos5.get(i))).setStrokeWidth(4);
                    cameraProperties(puntos5.get(0).get(0));

                }

                for (int i = 0; i < puntos6.size(); i++) {
                    mapa.addPolygon(new PolygonOptions().strokeColor(Color.RED).addAll(puntos6.get(i))).setStrokeWidth(4);
                    cameraProperties(puntos6.get(0).get(0));

                }


            }

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }


    public class CargandoPoligonos extends AsyncTask<String, Integer, String> {
        private String result;
        private ProgressDialog mProgressDialog;
        private int mProgressStatus = 0;


        List<LatLng> puntosCargar;
        List<CoordenadasBean> beans;

        public CargandoPoligonos() {

        }

        @Override
        public void onPreExecute() {
            mProgressStatus = 0;
            mProgressDialog = new ProgressDialog(MapsActivity.this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("Cargando poligonos");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            InputStream is = null;
            puntosCargar = new ArrayList<>();
            beans = new ArrayList<>();
            try {

                List<Cabecera> cabeceras = serviceDatabase.getCabeceras();
                List<Detalle> detalles = null;
                CoordenadasBean bean = null;
                for (Cabecera cabecera : cabeceras) {
                    bean = new CoordenadasBean();
                    bean.setTipoGrafico(cabecera.getTipoGrafico());
                    bean.setId(cabecera.getUid());
                    bean.setArea(cabecera.getArea());
                    detalles = serviceDatabase.getDetallesByCabeceraId(cabecera.uid);
                    puntosCargar = new ArrayList<>();
                    for (Detalle detalle : detalles) {
                        LatLng latLng = new LatLng(detalle.getLatitud(), detalle.getLongitud());
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

            mapa.clear();

            if (result.equalsIgnoreCase("in")) {
                for (CoordenadasBean bean : beans) {
                    Polygon polygon = mapa.addPolygon(new PolygonOptions()
                            .add(bean.getArrayTotal())
                            .strokeColor(Color.GREEN)
                            .fillColor(Color.LTGRAY));
                    polygon.setClickable(true);
                    polygon.setTag(bean.getArea() + "#" + bean.getId() + "#" + bean.getTipoGrafico());
                    puntos = new ArrayList<>();
                }
            }

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    private List<List<LatLng>> nuevasCoodenadas(String path) {
        List<List<LatLng>> puntosTotales1 = null;
        try {
            List<LatLng> points1;

            Serializer serializer = new Persister();
            File source = new File(path);
            puntosTotales1 = new ArrayList<List<LatLng>>();
            Kml kml = serializer.read(Kml.class, source);
            System.out.println(kml.getDocument().getId());
            System.out.println(kml.getDocument().getFolder().getName());
            List<Placemark> placemarks = kml.getDocument().getFolder().getPlacemarks();
            for (Placemark placemark : placemarks) {
                points1 = new ArrayList<LatLng>();
                if (placemark.getMultiGeometry() != null && placemark.getMultiGeometry().getPolygon() != null) {
                    System.out.println("Poligono");
                    String coordenadas = placemark.getMultiGeometry().getPolygon().getOuterBoundaryIs().getLinearRing()
                            .getCoordinates().trim();

                    if (coordenadas.contains("0.000000")) {
                        coordenadas = coordenadas.replace("0.000000", "");
                    } else if (coordenadas.contains("0 -")) {
                        coordenadas = coordenadas.replace("0 -", "-");
                    }
                    System.out.println(coordenadas);

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

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        CargandoPoligonos load2 = new CargandoPoligonos();
        load2.execute();

        GenerandoKml generandoKml = new GenerandoKml(kmls);
        generandoKml.execute();


    }

}
