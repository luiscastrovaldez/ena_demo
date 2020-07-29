package pe.minagri.googlemap.sql.database;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pe.minagri.googlemap.sql.Polygon;


@Dao
public interface PolygonDao {

    @Query("SELECT * FROM Polygon")
    List<Polygon> getPolygons();

    @Query("SELECT * FROM Polygon WHERE uid LIKE :uid")
    Polygon getPolygon(String uid);

    @Query("SELECT * FROM Polygon WHERE uidParent LIKE :uidParent")
    Polygon getPolygonByParentId(String uidParent);

    @Query("DELETE FROM Polygon WHERE uid = :uid")
    void deletePolygonById(String uid);

    @Insert
    void addPolygon(Polygon polygon);

    @Delete
    void deletePolygon(Polygon polygon);

    @Update
    void updatePolygon(Polygon polygon);

}
