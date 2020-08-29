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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;

import java.util.ArrayList;
import java.util.List;


import pe.minagri.googlemap.kml.ParsingStructure;
import pe.minagri.googlemap.sql.Point;
import pe.minagri.googlemap.sql.database.ServiceDatabase;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback, GoogleMap.OnMapClickListener , AdapterView.OnItemSelectedListener {

    private GoogleMap mapa;

    private CargandoInformacionPoligonos cargandoInformacionPoligonos;
    private DibujandoPoligono dibujandoPoligono;
    private UbicacionActual ubicacionActual;
    private GenerandoKml generandoKml;

    private List<LatLng> puntos;
    private List<LatLng> total;

    private String idPolygon;
    private String idPolygonParent;

    private static CameraPosition cameraPosition;

    private FloatingActionButton actualPosicion, tipoMapa, limpiar;

    private ServiceDatabase serviceDatabase;

    private List<String> kmls;


    private static List<LatLng> points;
    private static List<List<LatLng>> puntosTotales;

    public static List<ParsingStructure> parsingStr = null;

    private AssetManager assetManager;

    private TextView area;
    private EditText nombre;
    private Button guardar;
    private Spinner parcelas,lotes;

    void cargarInformacionParcelas(){
        List<Combo> list = new ArrayList<>();
        list.add(new Combo("0","Seleccionar Parcela"));
        List<pe.minagri.googlemap.sql.Polygon> polygons = serviceDatabase.getPolygons();
        for (pe.minagri.googlemap.sql.Polygon polygon : polygons) {
            list.add(new Combo(polygon.getUid(),polygon.getName()));
        }

        ArrayAdapter<Combo> dataAdapter = new ArrayAdapter<Combo>(this,
                android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parcelas.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
    }

    void cargarInformacionLotes(){
        List<Combo> list = new ArrayList<>();
        list.add(new Combo("0","Seleccionar Lote  "));
        /*
        List<pe.minagri.googlemap.sql.Polygon> polygons = serviceDatabase.getPolygons();
        for (pe.minagri.googlemap.sql.Polygon polygon : polygons) {
            list.add(new Combo(polygon.getUid(),polygon.getName()));
        }
        */
        ArrayAdapter<Combo> dataAdapter = new ArrayAdapter<Combo>(this,
                android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lotes.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();

    }

    void iniciandoObjetos() throws Exception{
        area = (TextView)findViewById(R.id.area);
        nombre = (EditText)findViewById(R.id.nombre);
        guardar = (Button) findViewById(R.id.guardar);
        assetManager = getBaseContext().getAssets();
        actualPosicion = (FloatingActionButton) findViewById(R.id.actualPosicion);
        tipoMapa = (FloatingActionButton) findViewById(R.id.tipoMapa);
        limpiar = (FloatingActionButton) findViewById(R.id.limpiar);
        serviceDatabase = ServiceDatabase.get(this);
        kmls = new ArrayList<String>();
        puntos = new ArrayList<>();
        parcelas = (Spinner) findViewById(R.id.parcelas);
        parcelas.setOnItemSelectedListener(this);
        lotes = (Spinner) findViewById(R.id.lotes);

    }
    void botonesMenu() throws Exception{
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nombre.getText().toString() != null && !nombre.getText().toString().isEmpty()){
                    if (puntos.size() != 0 && puntos.size() < 3) {
                        Toast.makeText(getApplication(), "Debe tener al menos 3 puntos", Toast.LENGTH_SHORT).show();
                    } else {

                        AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Confirmacion")
                                .setMessage("¿ Desea guardar la Informacion ?")

                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {


                                        try {
                                            cargadoInfoMapa(nombre.getText().toString(),idPolygon);
                                            puntos = new ArrayList<>();

                                        } catch ( Exception e ){
                                            e.printStackTrace();
                                            Toast.makeText(MapsActivity.this,
                                                    "Error al guardar la informacion " + e.getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Campo Nombre es Obligatorio ", Toast.LENGTH_LONG).show();
                }
            }
        });

        actualPosicion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MapsActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            Constants.GPS_STORAGE_REQUEST_CODE);
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
                try {
                    cargadoInfoMapa(null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error al cargar la informacion ", Toast.LENGTH_LONG).show();
                }
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

    void solicitarPermisosCarpetas() throws Exception{
        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        } else {
            crearCarpetas();
        }
    }

    void solicitarPermisoMapa(){
        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.GPS_STORAGE_REQUEST_CODE);
        } else {
            // Permission has already been granted
            cargandoMapa();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        try {
            iniciandoObjetos();
            botonesMenu();
            solicitarPermisosCarpetas();
            solicitarPermisoMapa();
            cargarInformacionParcelas();
            cargarInformacionLotes();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error al crear la carpeta db ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Combo combo = (Combo) parcelas.getSelectedItem();
        if (combo != null) {
            pe.minagri.googlemap.sql.Polygon poligono  = serviceDatabase.getPolygon(combo.getId());
            if (poligono != null){
                nombre.setText(poligono.getName());
                area.setText(poligono.getArea().toString());
                idPolygon = poligono.getUid();
                List<Point> points = serviceDatabase.getPointByPolygonId(poligono.getUid());
                puntos = new ArrayList<>();
                for (Point point : points) {
                    LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
                    puntos.add(latLng);
                }


            }
        }

        //Toast.makeText(getApplicationContext(),combo.getId() , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void crearCarpetas() throws Exception {
        File mediaStorageDir = new File(Constants.PATH_MINAGRI_MAIN_FOLDER);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Toast.makeText(getApplicationContext(), "Error al crear la carpeta enamap ", Toast.LENGTH_LONG).show();
                return;
            }
        }

        mediaStorageDir = new File(Constants.PATH_MINAGRI_DB_FOLDER);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Toast.makeText(getApplicationContext(), "Error al crear la carpeta db ", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    private void cargandoMapa() {

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapa.getUiSettings().setZoomControlsEnabled(false);
        mapa.setOnMapClickListener(this);
        //mapa.setOnPolygonClickListener(this);
        try {
            cargadoInfoMapa(null, null);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error Cargando Informacion", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapClick(LatLng puntoPulsado) {


        mapa.addMarker(new MarkerOptions()
                .position(puntoPulsado)
                .title("Nuevo Punto")
                .snippet("Latitud/Longitud " + puntoPulsado.latitude + "/" + puntoPulsado.longitude)
                .icon(BitmapDescriptorFactory
                        .fromResource(android.R.drawable.ic_menu_myplaces))
                .anchor(0.5f, 0.5f));

        puntos.add(puntoPulsado);
        graficarLinea(puntos);
        nombre.setText("");
        area.setText("");

    }

    void graficarLinea(List<LatLng> puntos) {
        if (puntos.size() > 1) {
            LatLng[] array = new LatLng[puntos.size()];
            puntos.toArray(array);
            Polyline line = mapa.addPolyline(new PolylineOptions()
                    .add(array)
                    .width(5)
                    .color(Color.BLACK));
        }
    }

    public void onCheckboxClicked(View view) throws Exception {

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


        cargadoInfoMapa(null,null);

    }

    private void cargadoInfoMapa(String name, String id) throws Exception {

        try {
            mapa.clear();
            Thread.sleep(1000);
            generandoKml = new GenerandoKml(kmls, mapa, MapsActivity.this, assetManager);
            generandoKml.execute();

            Thread.sleep(1000);

            if (name != null) {
                dibujandoPoligono = new DibujandoPoligono(MapsActivity.this, puntos, serviceDatabase, mapa, false);
                dibujandoPoligono.execute(name,id);
            }

            Thread.sleep(1000);
            cargandoInformacionPoligonos = new CargandoInformacionPoligonos(MapsActivity.this, serviceDatabase, mapa);
            cargandoInformacionPoligonos.execute();

        } catch (Exception e ){
            Toast.makeText(getApplicationContext(), "Error Cargando Informacion", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onRestart() {
        super.onRestart();

        try {
            cargadoInfoMapa(null, null);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error Cargando Informacion", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constants.WRITE_EXTERNAL_STORAGE_REQUEST_CODE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try {
                        crearCarpetas();
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            throw e;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

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
                    cargandoMapa();
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
