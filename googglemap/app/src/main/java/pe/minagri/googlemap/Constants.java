package pe.minagri.googlemap;


import android.Manifest;
import android.os.Environment;

import java.io.File;

public final class Constants {

    private Constants(){

    }

    public static final String PATH_MINAGRI = Environment.getExternalStorageDirectory() + File.separator + "enamap";
    public static final String PATH_MINAGRI_KML = Environment.getExternalStorageDirectory() + File.separator + "enamap" + File.separator + "kml";
    public static final String PATH_MINAGRI_ARCHIVOS = Environment.getExternalStorageDirectory() + File.separator + "enamap" + File.separator + "csv";
    public static final String PATH_MINAGRI_DB = Environment.getExternalStorageDirectory() + File.separator + "enamap" + File.separator + "db";
    public static final String PATH_MINAGRI_FOTOS = Environment.getExternalStorageDirectory() + File.separator + "enamap" + File.separator + "fotos";

    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int FICHA_REQUEST_CODE = 10;
    public static final int LISTADO_REQUEST_CODE = 20;
    public static final int ARCHIVOS_REQUEST_CODE = 30;
    public static final int MAPAS_REQUEST_CODE = 40;

    public static final int PERMISSION_ALL = 1;

    public static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_NETWORK_STATE

    };

    public static final String NUMERO_DNI_ENCUESTADOR = "numeroDniEncuestador";
    public static final String NOMBRE_ENCUESTADOR = "nombreEncuestador";
    public static final String DEPARTAMENTO = "departamento";
    public static final String PROVINCIA = "provincia";
    public static final String DISTRITO = "distrito";
    public static final String ID = "id";

}
