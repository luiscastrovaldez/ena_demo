package pe.minagri.googlemap.newkml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

@Root
@NamespaceList({
@Namespace(reference="http://www.opengis.net/kml/2.2"),
@Namespace(reference="http://www.google.com/kml/ext/2.2", prefix="gx"),
@Namespace(reference="http://www.opengis.net/kml/2.2", prefix="kml"),
@Namespace(reference="http://www.w3.org/2005/Atom", prefix="Atom")
})
public class Kml {

	@Element(name  = "Document", required=true)
	private Document document;

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
	
	
}
