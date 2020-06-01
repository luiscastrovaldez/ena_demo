package pe.minagri.googlemap.newkml;

import org.simpleframework.xml.Element;

public class Polygon {

	@Element
	private OuterBoundaryIs outerBoundaryIs;

	public OuterBoundaryIs getOuterBoundaryIs() {
		return outerBoundaryIs;
	}

	public void setOuterBoundaryIs(OuterBoundaryIs outerBoundaryIs) {
		this.outerBoundaryIs = outerBoundaryIs;
	}
	
	
}
