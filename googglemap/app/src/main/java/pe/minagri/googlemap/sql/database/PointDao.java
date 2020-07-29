package pe.minagri.googlemap.sql.database;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pe.minagri.googlemap.sql.Point;



@Dao
public interface PointDao {

    /*
    @Query("SELECT * FROM Polygon")
    List<Polygon> getPolygons();

    @Query("SELECT * FROM Polygon WHERE uid LIKE :uid")
    Polygon getPolygon(String uid);



    @Query("DELETE FROM Polygon WHERE uid = :uid")
    void deletePolygonById(String uid);

    */

    @Query("SELECT * FROM Point WHERE uidPolygon LIKE :uidPolygon")
    List<Point> getPointByPolygonId(String uidPolygon);

    @Insert
    void addPoint(Point point);

    @Delete
    void deletePoint(Point point);

    @Update
    void updatePoint(Point point);


}
