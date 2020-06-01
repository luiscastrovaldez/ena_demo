package pe.minagri.googlemap.sql.database;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pe.minagri.googlemap.sql.Cabecera;


@Dao
public interface CabeceraDao {

    @Query("SELECT * FROM CABECERA")
    List<Cabecera> getCabeceras();

    @Query("SELECT * FROM CABECERA WHERE uid LIKE :uid")
    Cabecera getCabecera(String uid);

    @Query("DELETE FROM CABECERA WHERE uid = :uid")
    void deleteCabeceraById(String uid);

    @Insert
    void addCabecera(Cabecera cabecera);

    @Delete
    void deleteCabecera(Cabecera cabecera);

    @Update
    void updateCabecera(Cabecera cabecera);

}
