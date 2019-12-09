package dad.javafx.micv.clases;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@XmlType
public class Correo {

	private StringProperty correo = new SimpleStringProperty();

	public Correo() {}
	
	public Correo(String email) {
		this.correo.set(email);
	}

	public final StringProperty emailProperty() {
		return this.correo;
	}
	

	@XmlAttribute(name="direccion")
	public final String getEmail() {
		return this.emailProperty().get();
	}
	

	public final void setEmail(final String email) {
		this.emailProperty().set(email);
	}
	
	

	
	
	
}
