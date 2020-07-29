package pe.minagri.googlemap.sql;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "Polygon")
public class Polygon {

    @PrimaryKey
    @NonNull
    public String uid;

    @NonNull
    @ColumnInfo(name = "area")
    public Double area;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;

    @NonNull
    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "uidParent")
    public String uidParent;



    public Polygon() {
        uid = UUID.randomUUID().toString();
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    @NonNull
    public Double getArea() {
        return area;
    }

    public void setArea(@NonNull Double area) {
        this.area = area;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public String getUidParent() {
        return uidParent;
    }

    public void setUidParent(@NonNull String uidParent) {
        this.uidParent = uidParent;
    }


}
