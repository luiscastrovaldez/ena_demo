package pe.minagri.googlemap.sql.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import pe.minagri.googlemap.sql.Cabecera;
import pe.minagri.googlemap.sql.Detalle;

@Database(entities = {Cabecera.class, Detalle.class}, version = 1)
public abstract class MapaDataBase extends RoomDatabase {

    public abstract CabeceraDao getCabeceraDao();
    public abstract DetalleDao getDetalleDao();


}
