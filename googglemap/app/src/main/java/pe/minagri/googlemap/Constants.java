package pe.minagri.googlemap;


import android.Manifest;
import android.os.Environment;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;

public final class Constants {

    public static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_NETWORK_STATE

    };

    public static final String PATH_MINAGRI_MAIN_FOLDER = Environment.getExternalStorageDirectory() + File.separator + "enamap";
    public static final String PATH_MINAGRI_DB_FOLDER = PATH_MINAGRI_MAIN_FOLDER + File.separator + "db";

    public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    public static final int GPS_STORAGE_REQUEST_CODE = 2;

    public static final int PERMISSION_ALL = 1;

    public static final String TITULO_GENERANDO_POLIGONO = "Dibujando Poligono...";
    public static final String OBTENIENDO_INFORMACION_POLIGONO = "Obteniendo Informacion...";

    public static final String OBTENIENDO_UBICACION_ACTUAL = "Obteniendo Ubicacion Actual...";

    public static final String LOTE = "Lote";
    public static final String PARCELA = "Parcela";

    public static final String MUESTRA_CAMPO_MARTE_KML = "MUESTRA_CAMPO_MARTE.kml";
    public static final String SM_ENA_BE_SM_KML = "SM_ENA_BE_SM.kml";
    public static final String SM_ENA_VIRU_KML = "SM_ENA_VIRU.kml";




}
