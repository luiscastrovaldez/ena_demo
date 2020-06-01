package pe.minagri.googlemap.bean;

import com.google.android.gms.maps.model.LatLng;

public class CoordenadasBean {

    public String id;
    public Double area;
    public String tipoGrafico;
    private LatLng[] arrayTotal;

    public CoordenadasBean(){

    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getTipoGrafico() {
        return tipoGrafico;
    }

    public void setTipoGrafico(String tipoGrafico) {
        this.tipoGrafico = tipoGrafico;
    }

    public LatLng[] getArrayTotal() {
        return arrayTotal;
    }

    public void setArrayTotal(LatLng[] arrayTotal) {
        this.arrayTotal = arrayTotal;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
