package pe.minagri.googlemap.newkml;

import org.simpleframework.xml.Element;

public class LabelStyle {

	@Element
	private String color;
	
	@Element
	private String scale;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}
	
	
}
