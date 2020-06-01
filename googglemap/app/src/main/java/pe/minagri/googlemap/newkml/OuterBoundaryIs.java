package pe.minagri.googlemap.newkml;

import org.simpleframework.xml.Element;

public class OuterBoundaryIs {

	@Element(name = "LinearRing")
	private LinearRing linearRing;

	public LinearRing getLinearRing() {
		return linearRing;
	}

	public void setLinearRing(LinearRing linearRing) {
		this.linearRing = linearRing;
	}
	
	
}
