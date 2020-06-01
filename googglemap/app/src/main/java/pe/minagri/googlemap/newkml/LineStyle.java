package pe.minagri.googlemap.newkml;

import org.simpleframework.xml.Element;

public class LineStyle {

	@Element
	private String color;
	
	@Element(required = false)
	private double width;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	
	
}
