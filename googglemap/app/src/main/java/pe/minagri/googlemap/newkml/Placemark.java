package pe.minagri.googlemap.newkml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Placemark {

	@Attribute( required = false)
	private String id;
	
	@Element
	private String name;
	
	@Element(name = "Snippet")
	private Snippet snippet;
	
	@Element
	private String description;
	
	@Element(required = false)
	private String styleUrl;
	
	@Element(name = "MultiGeometry", required = false)
	private MultiGeometry multiGeometry;
	
	@Element(name = "Point", required = false)
	private Point point;
	

	public String getStyleUrl() {
		return styleUrl;
	}

	public void setStyleUrl(String styleUrl) {
		this.styleUrl = styleUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Snippet getSnippet() {
		return snippet;
	}

	public void setSnippet(Snippet snippet) {
		this.snippet = snippet;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MultiGeometry getMultiGeometry() {
		return multiGeometry;
	}

	public void setMultiGeometry(MultiGeometry multiGeometry) {
		this.multiGeometry = multiGeometry;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}
	
	
	
	
}
