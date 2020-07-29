package pe.minagri.googlemap.sql.database;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Room;

import java.util.List;

import pe.minagri.googlemap.sql.Point;
import pe.minagri.googlemap.sql.Polygon;

public class ServiceDatabase {

    @SuppressLint("StaticFieldLeak")
    private static ServiceDatabase serviceDatabase;

    private PolygonDao polygonDao;
    private PointDao pointDao;


    private static final String DB_NAME = "/storage/emulated/0/enamap/db/minagri.db";

    private ServiceDatabase(Context context) {
        Context appContext = context.getApplicationContext();
        MapaDataBase database = Room.databaseBuilder(appContext, MapaDataBase.class, DB_NAME)
                .allowMainThreadQueries().build();
        polygonDao = database.getPolygonDao();
        pointDao = database.getPointDao();

    }

    public static ServiceDatabase get(Context context) {
        if (serviceDatabase == null) {
            serviceDatabase = new ServiceDatabase(context);
        }
        return serviceDatabase;
    }

   public List<Polygon> getPolygons() {
        return polygonDao.getPolygons();
    }


    public Polygon getPolygon(String uid) {
        return polygonDao.getPolygon(uid);
    }


    public void addPolygon(Polygon polygon) {
        polygonDao.addPolygon(polygon);
    }

    public void updatePolygon(Polygon polygon) {
        polygonDao.updatePolygon(polygon);
    }

    public void deletePolygon(Polygon polygon) {
        polygonDao.deletePolygon(polygon);
    }

    /*
    public List<Lote> getDetalles() {
        return detalleDao.getDetalles();
    }

    public Lote getDetalle(String uidDetalle) {
        return detalleDao.getDetalle(uidDetalle);
    }

    public List<Lote> getDetallesByCabeceraId(String uidCabecera) {
        return detalleDao.getDetalleByCabeceraId(uidCabecera);
    }
*/

    public void addPoint(Point point) {
        pointDao.addPoint(point);
    }
    public List<Point> getPointByPolygonId(String polygonId) {
        return pointDao.getPointByPolygonId(polygonId);
    }
}
