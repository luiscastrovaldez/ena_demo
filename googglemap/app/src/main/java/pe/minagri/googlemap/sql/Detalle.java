package pe.minagri.googlemap.sql;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "DETALLE")
public class Detalle {


    @PrimaryKey
    @NonNull
    public String uidDetalle;

    @NonNull
    public String uidCabecera;

    @NonNull
    @ColumnInfo(name = "latitud")
    public Double latitud;

    @NonNull
    @ColumnInfo(name = "longitud")
    public Double longitud;



    public Detalle() {
        uidDetalle = UUID.randomUUID().toString();
    }

    @NonNull
    public String getUidDetalle() {
        return uidDetalle;
    }

    public void setUidDetalle(@NonNull String uidDetalle) {
        this.uidDetalle = uidDetalle;
    }

    @NonNull
    public String getUidCabecera() {
        return uidCabecera;
    }

    public void setUidCabecera(@NonNull String uidCabecera) {
        this.uidCabecera = uidCabecera;
    }

    @NonNull
    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(@NonNull Double latitud) {
        this.latitud = latitud;
    }

    @NonNull
    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(@NonNull Double longitud) {
        this.longitud = longitud;
    }
}
