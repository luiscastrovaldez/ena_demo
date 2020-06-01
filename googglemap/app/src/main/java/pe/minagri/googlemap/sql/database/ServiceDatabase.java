package pe.minagri.googlemap.sql.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import androidx.room.Query;
import androidx.room.Room;

import java.util.List;

import pe.minagri.googlemap.sql.Cabecera;
import pe.minagri.googlemap.sql.Detalle;

public class ServiceDatabase {

    @SuppressLint("StaticFieldLeak")
    private static ServiceDatabase serviceDatabase;

    private CabeceraDao cabeceraDao;
    private DetalleDao detalleDao;

    private static final String DB_NAME = "/storage/emulated/0/enamap/db/minagri.db";

    private ServiceDatabase(Context context) {
        Context appContext = context.getApplicationContext();
        MapaDataBase database = Room.databaseBuilder(appContext, MapaDataBase.class, DB_NAME)
                .allowMainThreadQueries().build();
        cabeceraDao = database.getCabeceraDao();
        detalleDao = database.getDetalleDao();

    }

    public static ServiceDatabase get(Context context) {
        if (serviceDatabase == null) {
            serviceDatabase = new ServiceDatabase(context);
        }
        return serviceDatabase;
    }

   public List<Cabecera> getCabeceras() {
        return cabeceraDao.getCabeceras();
    }

    public Cabecera getCebecera(String uid) {
        return cabeceraDao.getCabecera(uid);
    }

    public void addCabecera(Cabecera cabecera) {
        cabeceraDao.addCabecera(cabecera);
    }

    public void updateCabecera(Cabecera cabecera) {
        cabeceraDao.updateCabecera(cabecera);
    }

    public void deleteCabecera(Cabecera cabecera) {
        cabeceraDao.deleteCabecera(cabecera);
    }

    public List<Detalle> getDetalles() {
        return detalleDao.getDetalles();
    }

    public Detalle getDetalle(String uidDetalle) {
        return detalleDao.getDetalle(uidDetalle);
    }

    public List<Detalle> getDetallesByCabeceraId(String uidCabecera) {
        return detalleDao.getDetalleByCabeceraId(uidCabecera);
    }

    public void addDetalle(Detalle detalle) {
        detalleDao.addDetalle(detalle);
    }

    public void updateDetalle(Detalle detalle) {
        detalleDao.updateDetalle(detalle);
    }

    public void deleteDetalle(Detalle detalle) {
        detalleDao.deleteDetalle(detalle);
    }

    public void deleteDetalleByCabeceraId(String uidCabecera) {
        detalleDao.deleteDetalleByCabeceraId(uidCabecera);
    }

    public void deleteCabeceraById(String id) {
        cabeceraDao.deleteCabeceraById(id);
    }
}
