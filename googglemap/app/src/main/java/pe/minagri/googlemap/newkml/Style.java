package pe.minagri.googlemap.newkml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Style {
	
	@Attribute
	private String id;

	
	@Element(name = "LabelStyle")
	private LabelStyle labelStyle;
	
	@Element(name = "LineStyle",required = false)
	private LineStyle lineStyle;
	
	@Element(name = "PolyStyle")
	private PolyStyle polyStyle;
	
	
	@Element(name = "IconStyle",required = false)
	private IconStyle iconStyle;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LabelStyle getLabelStyle() {
		return labelStyle;
	}

	public void setLabelStyle(LabelStyle labelStyle) {
		this.labelStyle = labelStyle;
	}

	public LineStyle getLineStyle() {
		return lineStyle;
	}

	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

	public PolyStyle getPolyStyle() {
		return polyStyle;
	}

	public void setPolyStyle(PolyStyle polyStyle) {
		this.polyStyle = polyStyle;
	}

	public IconStyle getIconStyle() {
		return iconStyle;
	}

	public void setIconStyle(IconStyle iconStyle) {
		this.iconStyle = iconStyle;
	}
	
	

}
