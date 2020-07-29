package pe.minagri.googlemap.sql;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "Point")
public class Point {

    @PrimaryKey
    @NonNull
    public String uid;

    @NonNull
    @ColumnInfo(name = "uidPolygon")
    public String uidPolygon;

    @NonNull
    @ColumnInfo(name = "latitude")
    public Double latitude;

    @NonNull
    @ColumnInfo(name = "longitude")
    public Double longitude;

    public Point() {
        uid = UUID.randomUUID().toString();
    }

    @NonNull
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(@NonNull Double latitude) {
        this.latitude = latitude;
    }

    @NonNull
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(@NonNull Double longitude) {
        this.longitude = longitude;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    @NonNull
    public String getUidPolygon() {
        return uidPolygon;
    }

    public void setUidPolygon(@NonNull String uidPolygon) {
        this.uidPolygon = uidPolygon;
    }
}
