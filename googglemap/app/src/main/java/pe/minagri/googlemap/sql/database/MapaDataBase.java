package pe.minagri.googlemap.sql.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import pe.minagri.googlemap.sql.Point;
import pe.minagri.googlemap.sql.Polygon;

@Database(entities = {Polygon.class, Point.class}, version = 2)
public abstract class MapaDataBase extends RoomDatabase {

    public abstract PolygonDao getPolygonDao();
    public abstract PointDao getPointDao();

}
