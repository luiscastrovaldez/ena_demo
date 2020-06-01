package pe.minagri.googlemap.newkml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

@Root
public class MultiGeometry {

	@Element(name = "Polygon", required = false)
	private Polygon polygon;
	
	@Element(name = "LineString", required = false)
	@Path("LineString")	
	private LineString lineString;
	
	

	public Polygon getPolygon() {
		return polygon;
	}

	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}

	public LineString getLineString() {
		return lineString;
	}

	public void setLineString(LineString lineString) {
		this.lineString = lineString;
	}
	
	
}
