package pe.minagri.googlemap.newkml;


import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;


public class Folder {

	@Attribute
	private String id;
	
	@Element
	private String name;
	
	@Element(required = false)
	private String description;
	
	
	@Element(name = "Snippet")
	private Snippet snippet;
	
	private final List<Placemark> placemarks;

    public Folder(@ElementList(name="placemarks", inline = false) List<Placemark> placemarks) {
        this.placemarks = placemarks;
    }

    @ElementList(name="placemarks")
    public List<Placemark> getPlacemarks() {
        return placemarks;
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

	
	
}
