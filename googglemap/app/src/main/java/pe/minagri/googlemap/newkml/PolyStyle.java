package pe.minagri.googlemap.newkml;

import org.simpleframework.xml.Element;

public class PolyStyle {
	
	@Element
	private String color;
	
	@Element(required = false)
	private String outline;
	

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getOutline() {
		return outline;
	}

	public void setOutline(String outline) {
		this.outline = outline;
	}
	
	

}
