package dad.javafx.micv.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.javafx.micv.clases.Contacto;
import dad.javafx.micv.clases.Correo;
import dad.javafx.micv.clases.Telefono;
import dad.javafx.micv.clases.Web;
import dad.javafx.micv.clases.Telefono.TipoTelefono;
import dad.javafx.micv.utils.TelefonoDialog;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ContactoController implements Initializable {

	// Propiedades de diálogo de las tablas
	
	// View 
    @FXML
    private VBox view;

    @FXML
    private TableView<Telefono> telefonosTable;

    @FXML
    private TableColumn<Telefono, TipoTelefono> tipoColumn;

    @FXML
    private Button addTelefonoButton;

    @FXML
    private Button quitarTelefonoButton;

    @FXML
    private TableView<Correo> correosTable;

    @FXML
    private Button addCorreoButton;

    @FXML
    private Button quitarCorreoButton;

    @FXML
    private TableView<Web> webTable;

    @FXML
    private Button addWebButton;

    @FXML
    private Button quitarWebButton;
    // Model
    private ObjectProperty<Contacto> contacto = new SimpleObjectProperty<Contacto>();
    
	public ContactoController() throws IOException {		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ContactoView.fxml"));
		loader.setController(this);
		loader.load();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
			
		// Para la lista de tipos de teléfonos, tenemos que hacerlo manualmente
		tipoColumn.setCellFactory(ComboBoxTableCell.forTableColumn(Telefono.getTiposTelefono()));
		
		contacto.addListener( (o, ol, nv) -> updateBinds(ol, nv));
		contacto.set(new Contacto());
		//podemos hacerlo con distintos actions
		addTelefonoButton.setOnAction( e -> onAddTelefono(e) );
		addCorreoButton.setOnAction( e ->onAddCorreo(e) );
		addWebButton.setOnAction( e -> onAddWeb(e) );
		//o con el mismo pero asegurandonos en el action con cual pulsamos
		quitarTelefonoButton.setOnAction( e -> onQuitarTelefono(e));
		quitarCorreoButton.setOnAction( e -> onQuitarCorreo(e));
		quitarWebButton.setOnAction( e -> onQuitarWeb(e));
		//desctivar botton cuando no hay nada seleccionado
		quitarTelefonoButton.disableProperty().bind( telefonosTable.getSelectionModel().selectedItemProperty().isNull() );
		quitarCorreoButton.disableProperty().bind( correosTable.getSelectionModel().selectedItemProperty().isNull() );
		quitarWebButton.disableProperty().bind( webTable.getSelectionModel().selectedItemProperty().isNull() );
	}
	
	//eliminar telefono
	private void onQuitarTelefono(ActionEvent e) {
		Telefono telefonoObj = telefonosTable.getSelectionModel().getSelectedItem();
		String encabezado = "Eliminar teléfono";
		String contenido ="¿Está seguro de eliminar este teléfono?";
				
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(encabezado);
		alert.setContentText(contenido);		
		if(telefonoObj != null && alert.showAndWait().get() == ButtonType.OK) {
			getContacto().getTelefonos().remove(telefonoObj);
		}
	}
	//eliminar correo
	private void onQuitarCorreo(ActionEvent e) {
		Correo correoObj = correosTable.getSelectionModel().getSelectedItem();
		String encabezado = "Eliminar correo";
		String contenido ="¿Está seguro de eliminar este correo?";
				
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(encabezado);
		alert.setContentText(contenido);		
		
		if( correoObj != null && alert.showAndWait().get() == ButtonType.OK)  {
			getContacto().getEmails().remove(correoObj);
		}
	}
	//eliminar web
	private void onQuitarWeb(ActionEvent e) {
		Web webItem = webTable.getSelectionModel().getSelectedItem();
		String encabezado = "Eliminar web";
		String contenido ="¿Está seguro de eliminar esta web?";
				
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(encabezado);
		alert.setContentText(contenido);		
		if( webItem != null && alert.showAndWait().get() == ButtonType.OK ) {
			getContacto().getWebs().remove(webItem);
		}
	}
	
	 //Añadimos un nuevo e-mail
	 
	private void onAddCorreo(ActionEvent e) {
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Nuevo e-mail");
		dialog.setHeaderText("Crear una nueva dirección de correo.");
		dialog.setContentText("E-mail:");
		
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(getClass().getResource("/images/cv64x64.png").toString()));
		
		Optional<String> emailTxt = dialog.showAndWait();
		
		if( emailTxt.isPresent() && emailTxt.get() != null && !emailTxt.get().isBlank() ) {
			getContacto().getEmails().add(new Correo(emailTxt.get()));
		}	
	}
	
	
	//Añadimos una nueva URL
	
	private void onAddWeb(ActionEvent e) {
		
		TextInputDialog dialog = new TextInputDialog("http://");
		dialog.setTitle("Nueva web");
		dialog.setHeaderText("Crear una nueva dirección web.");
		dialog.setContentText("URL:");
		
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(getClass().getResource("/images/cv64x64.png").toString()));
		
		Optional<String> webTxt = dialog.showAndWait();
		
		if( webTxt.isPresent() && webTxt.get() != null && !webTxt.get().isBlank() ) {
			getContacto().getWebs().add(new Web(webTxt.get()));
		}	
	}

		//Añadimos un nuevo teléfono con sus respectivos campos
	
	private void onAddTelefono(ActionEvent e) {
		
		TelefonoDialog dialog = new TelefonoDialog();
		
		Optional<Telefono> tlf = dialog.showAndWait();
		
		if( tlf.isPresent() && tlf.get() != null ) {		
			// Añadimos el nuevo teléfono
			getContacto().getTelefonos().add(tlf.get());
		}
	}
	

	private void updateBinds(Contacto oldContacto, Contacto nuevoContacto) {

		if( oldContacto != null ) {
			telefonosTable.itemsProperty().unbindBidirectional(oldContacto.telefonosProperty());
			correosTable.itemsProperty().unbindBidirectional(oldContacto.emailsProperty());
			webTable.itemsProperty().unbindBidirectional(oldContacto.websProperty());
		}
		
		Bindings.bindBidirectional(telefonosTable.itemsProperty(), nuevoContacto.telefonosProperty());
		Bindings.bindBidirectional(correosTable.itemsProperty(), nuevoContacto.emailsProperty());
		Bindings.bindBidirectional(webTable.itemsProperty(), nuevoContacto.websProperty());
	}

	public VBox getRoot() {
		return view;
	}

	public final ObjectProperty<Contacto> contactoProperty() {
		return this.contacto;
	}
	

	public final Contacto getContacto() {
		return this.contactoProperty().get();
	}
	

	public final void setContacto(final Contacto contacto) {
		this.contactoProperty().set(contacto);
	}
	
}
