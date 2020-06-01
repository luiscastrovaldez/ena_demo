package pe.minagri.googlemap.newkml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Text;

public class LinearRing {
	
	@Path("coordinates")
	@Text(required=false)
	private String coordinates;

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}
	
	

}
