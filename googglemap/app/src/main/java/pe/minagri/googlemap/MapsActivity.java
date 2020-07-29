package pe.minagri.googlemap;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;

import java.util.ArrayList;
import java.util.List;


import pe.minagri.googlemap.kml.ParsingStructure;
import pe.minagri.googlemap.sql.database.ServiceDatabase;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnPolygonClickListener {

    private GoogleMap mapa;

    private CargandoInformacionPoligonos cargandoInformacionPoligonos;
    private DibujandoPoligono dibujandoPoligono;
    private UbicacionActual ubicacionActual;
    private GenerandoKml generandoKml;

    private List<LatLng> puntos;
    private List<LatLng> total;

    private String idPolygon;
    private String idPolygonParent;


    private static MarkerOptions posicionActualMarker;


    private static CameraPosition cameraPosition;

    private FloatingActionButton actualPosicion, tipoMapa, limpiar;

    private ServiceDatabase serviceDatabase;

    private static List<String> kmls;


    private static List<LatLng> points;
    private static List<List<LatLng>> puntosTotales;

    public static List<ParsingStructure> parsingStr = null;

    private AssetManager assetManager;

    private TextView area;
    private EditText nombre;
    private Button guardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        area = (TextView)findViewById(R.id.area);
        nombre = (EditText)findViewById(R.id.nombre);
        guardar = (Button) findViewById(R.id.guardar);

        assetManager = getBaseContext().getAssets();
        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.WRITE_EXTERNAL_STORAGE_REQUEST_CODE);

            }
        } else {
            cargandoComponentes();

        }

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nombre.getText().toString() != null && !nombre.getText().toString().isEmpty()){
                    if (puntos.size() != 0 && puntos.size() < 3) {
                        Toast.makeText(getApplication(), "Debe tener al menos 3 puntos", Toast.LENGTH_SHORT).show();
                    } else {
                        cargadoInfoMapa(nombre.getText().toString());
                        puntos = new ArrayList<>();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Campo Nombre es Obligatorio ", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void creaandoFolders(){
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

    }

    private void cargandoComponentes() {

        creaandoFolders();

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        puntos = new ArrayList<LatLng>();

        actualPosicion = (FloatingActionButton) findViewById(R.id.actualPosicion);
        tipoMapa = (FloatingActionButton) findViewById(R.id.tipoMapa);

        limpiar = (FloatingActionButton) findViewById(R.id.limpiar);

        serviceDatabase = ServiceDatabase.get(this);

        kmls = new ArrayList<String>();


        puntosTotales = new ArrayList<List<LatLng>>();

        actualPosicion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                    } else {

                        ActivityCompat.requestPermissions(MapsActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                Constants.GPS_STORAGE_REQUEST_CODE);

                    }
                } else {
                    // Permission has already been granted
                    ubicacionActual = new UbicacionActual(MapsActivity.this, mapa);
                    ubicacionActual.execute();
                }


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
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(Constants.UPV, 15));

        mapa.addMarker(new MarkerOptions()
                .position(Constants.UPV)
                .title(Constants.MINAGRI)
                .snippet(Constants.MINAGRI)
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

        cargadoInfoMapa(null);
    }

    @Override
    public void onMapClick(LatLng puntoPulsado) {


        mapa.addMarker(new MarkerOptions()
                .position(puntoPulsado)
                .title("Latitud/Longitud")
                .snippet("Latitud/Longitud " + puntoPulsado.latitude + "/" + puntoPulsado.longitude)
                .icon(BitmapDescriptorFactory
                        .fromResource(android.R.drawable.ic_menu_myplaces))
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



    @Override
    public void onPolygonClick(Polygon polygon) {

        String areaPolygon = polygon.getTag().toString().split("#")[0];
        final String id = polygon.getTag().toString().split("#")[1];
        idPolygon = id;
        pe.minagri.googlemap.sql.Polygon polygon1 = serviceDatabase.getPolygon(id);

        final String nombrePolygon = polygon1.getName();
        nombre.setText(nombrePolygon);
        area.setText(areaPolygon);

    }


    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();


        int index = 0;
        switch (view.getId()) {
            case R.id.campoMarte:
                if (checked)
                    kmls.add(Constants.MUESTRA_CAMPO_MARTE_KML);
                else {
                    if (kmls.contains(Constants.MUESTRA_CAMPO_MARTE_KML)) {
                        index = kmls.indexOf(Constants.MUESTRA_CAMPO_MARTE_KML);
                        kmls.remove(index);

                    }
                }

                break;
            case R.id.smEnaBeSM:
                if (checked)

                    kmls.add(Constants.SM_ENA_BE_SM_KML);
                else {
                    if (kmls.contains(Constants.SM_ENA_BE_SM_KML)) {
                        index = kmls.indexOf(Constants.SM_ENA_BE_SM_KML);
                        kmls.remove(index);

                    }
                }

                break;
            case R.id.smEnaViru:
                if (checked)
                    kmls.add(Constants.SM_ENA_VIRU_KML);
                else {
                    if (kmls.contains(Constants.SM_ENA_VIRU_KML)) {
                        index = kmls.indexOf(Constants.SM_ENA_VIRU_KML);
                        kmls.remove(index);
                    }
                }

                break;
        }


        cargadoInfoMapa(null);

    }

    private void cargadoInfoMapa(String name){

        try {
            mapa.clear();
            Thread.sleep(1000);
            generandoKml = new GenerandoKml(kmls, mapa, MapsActivity.this, assetManager);
            generandoKml.execute();

            Thread.sleep(1000);

            if (name != null) {
                dibujandoPoligono = new DibujandoPoligono(MapsActivity.this, puntos, serviceDatabase, mapa, false);
                dibujandoPoligono.execute(name,idPolygon);
            }

            Thread.sleep(1000);
            cargandoInformacionPoligonos = new CargandoInformacionPoligonos(MapsActivity.this, serviceDatabase, mapa);
            cargandoInformacionPoligonos.execute();
        } catch (Exception e ){

        }

    }


    @Override
    protected void onRestart() {
        super.onRestart();

        cargadoInfoMapa(null);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constants.WRITE_EXTERNAL_STORAGE_REQUEST_CODE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    cargandoComponentes();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case Constants.GPS_STORAGE_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


}
