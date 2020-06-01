package pe.minagri.googlemap.sql;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "CABECERA")
public class Cabecera {

    @PrimaryKey
    @NonNull
    public String uid;

    @NonNull
    @ColumnInfo(name = "tipo_grafico")
    public String tipoGrafico;

    @NonNull
    @ColumnInfo(name = "area")
    public Double area;

    public Cabecera() {
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
    public String getTipoGrafico() {
        return tipoGrafico;
    }

    public void setTipoGrafico(@NonNull String tipoGrafico) {
        this.tipoGrafico = tipoGrafico;
    }

    @NonNull
    public Double getArea() {
        return area;
    }

    public void setArea(@NonNull Double area) {
        this.area = area;
    }
}
