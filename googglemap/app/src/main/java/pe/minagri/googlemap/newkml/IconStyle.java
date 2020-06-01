package pe.minagri.googlemap.newkml;

import org.simpleframework.xml.Element;

public class IconStyle {
	
	@Element
	private String scale;
	
	@Element(name = "Icon")
	private Icon icon;

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	
	

}
