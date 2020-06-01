package pe.minagri.googlemap.sql.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pe.minagri.googlemap.sql.Cabecera;
import pe.minagri.googlemap.sql.Detalle;

@Dao
public interface DetalleDao {

    @Query("SELECT * FROM DETALLE")
    List<Detalle> getDetalles();

    @Query("SELECT * FROM DETALLE WHERE uidDetalle LIKE :uidDetalle")
    Detalle getDetalle(String uidDetalle);

    @Query("SELECT * FROM DETALLE WHERE uidCabecera = :uidCabecera")
    List<Detalle> getDetalleByCabeceraId(String uidCabecera);

    @Query("DELETE FROM DETALLE WHERE uidCabecera = :uidCabecera")
    void deleteDetalleByCabeceraId(String uidCabecera);

    @Insert
    void addDetalle(Detalle detalle);

    @Delete
    void deleteDetalle(Detalle detalle);

    @Update
    void updateDetalle(Detalle detalle);

}
