package dad.javafx.micv.controllers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.javafx.micv.MainController;
import dad.javafx.micv.clases.Nacionalidad;
import dad.javafx.micv.clases.Personal;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PersonalController implements Initializable {

	private final String paisesSource = "files/paises.csv";
	private final String nacionalidadesSource = "files/nacionalidades.csv";
	
	// View
	@FXML
	private GridPane view;
	
	@FXML
	private TextField dniText;

	@FXML
	private TextField nombreText;

	@FXML
	private TextField apellidosText;

	@FXML
	private DatePicker fechaDatePicker;

	@FXML
	private TextArea direccionText;

	@FXML
	private TextField codigoPostalText;

	@FXML
	private TextField localidadText;

	@FXML
	private ComboBox<String> paisCombo;

	@FXML
	private ListView<Nacionalidad> nacionalidadList;

	@FXML
	private Button addButton;

	@FXML
	private Button quitarButton;
	
	//---------------------------------------------------------
	
	// Model, sólo guardamos una referencia al personal
	private ObjectProperty<Personal> personal = new SimpleObjectProperty<Personal>();
	
	// Una lista para los paises
	private ListProperty<String> paisesList = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
	
	// Guardamos una lista de las nacionalidades, sólo la cargamos al principio
	private ArrayList<String> nacionalidadesList = new ArrayList<>();
	
	public PersonalController() throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/PersonalView.fxml"));
		loader.setController(this);
		loader.load();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Cargamos las nacionalidades
		getNacionalidades();
		
		// Bindeamos los paises a su lista correspondiente
		paisCombo.itemsProperty().bind(paisesList);

		// Cargamos los paises
		loadPaises();
		

		// Cada vez que actualizamos el objeto, avisamos
		personal.addListener((o, ol, nv) -> updateBinds(ol, nv));
		personal.set(new Personal()); // Empezamos con uno nuevo
		
		addButton.setOnAction( e -> onAddAction(e) );
		quitarButton.setOnAction( e -> onRemoveAction(e) );
		
		quitarButton.disableProperty().bind( nacionalidadList.getSelectionModel().selectedItemProperty().isNull() );
		
	}

	
	//  Cargamos las nacionalidades del fichero
	
	private void getNacionalidades() {
		
		FileInputStream file = null;
		InputStreamReader in = null;
		BufferedReader reader = null;
		
		try {
			
			file = new FileInputStream(nacionalidadesSource);
			in = new InputStreamReader(file, StandardCharsets.ISO_8859_1);
			reader = new BufferedReader(in);
			
			String line = null;
			
			while( (line = reader.readLine()) != null ) {
				
				String str = line.trim();//quitamos posibles caracters en blanco
				str = str.substring(0,1).toUpperCase() + str.substring(1);
				nacionalidadesList.add(str);
			}
			
		} catch(IOException e ) {
			MainController.enviarError("Error al abrir el archivo de files/nacionaliddades.csv");
			
		} finally {
			
			try {
				if( reader != null ) {
					reader.close();
				}
				
				if( in != null ) {
					in.close();
				}
				
				if( file != null ) {
					file.close();
				}
				
			} catch (IOException e) {
				MainController.enviarError("Error al cerrar el archivo de files/nacionaliddades.csv");
			}
		}		
	}

	/**
	 * Cargamos los diferentes paises a partir de su archivo
	 */
	private void loadPaises() {
		
		FileInputStream file = null;
		InputStreamReader in = null;
		BufferedReader reader = null;
		
		try {
			
			file = new FileInputStream(paisesSource);
			in = new InputStreamReader(file, StandardCharsets.ISO_8859_1);
			reader = new BufferedReader(in);
			
			String line = null;
			
			while( (line = reader.readLine()) != null ) {
				paisesList.add(line.trim());
			}
			
		} catch(IOException e ) {
			MainController.enviarError("Error al abrir el archivo de files/paises.csv");
			
		} finally {
			
			try {
				if( reader != null ) {
					reader.close();
				}
				
				if( in != null ) {
					in.close();
				}
				
				if( file != null ) {
					file.close();
				}
				
			} catch (IOException e) {
				MainController.enviarError("Error al cerrar el archivo de files/paises.csv");
			}
		}
		
	}

	
	//Quitamos una nacionalidad de la lista
	
	private void onRemoveAction(ActionEvent e) {

		// Cogemos el elemento seleccionado actualmente y lo quitamos de la lista
		Nacionalidad miNacionalidad = nacionalidadList.getSelectionModel().getSelectedItem();
		if( miNacionalidad != null ) {
			getPersonal().getNacionaliades().remove(miNacionalidad);
		}
	}

	
	 // Añadimos una nacionalidad a la lista 
	 
	private void onAddAction(ActionEvent e) {

		// Mostramos el diálogo correspondiente
		ChoiceDialog<String> dialog = new ChoiceDialog<>();
		dialog.setTitle("Nueva nacionalidad");
		dialog.setHeaderText("Añadir nacionalidad");
		dialog.setContentText("Seleccione una nacionalidad");
		dialog.getItems().addAll(nacionalidadesList);
		dialog.setSelectedItem(nacionalidadesList.get(0)); // Seleccionamos el primero por defecto
		
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(getClass().getResource("/images/cv64x64.png").toString()));
		
		Optional<String> response = dialog.showAndWait();
		
		if( response.isPresent() && response.get() != null ) {
			getPersonal().nacionaliadesProperty().add(new Nacionalidad(response.get()));
		}
	}

	public final ObjectProperty<Personal> personalProperty() {
		return this.personal;
	}
	

	public final Personal getPersonal() {
		return this.personalProperty().get();
	}
	

	public final void setPersonal(final Personal personal) {
		this.personalProperty().set(personal);
	}
	
	/*
	  Necesitamos actualizar los bindings cada vez
	  que actualizamos el objeto, ya que creamos
	  uno nuevo con sus propias properties.
	 */
	private void updateBinds(Personal oldPersonal, Personal nuevoPersonal) {
		
		if( oldPersonal != null) {
			
			nombreText.textProperty().unbindBidirectional(oldPersonal.nombreProperty());
			apellidosText.textProperty().unbindBidirectional(oldPersonal.apellidosProperty());
			codigoPostalText.textProperty().unbindBidirectional(oldPersonal.codigoPostalProperty());
			localidadText.textProperty().unbindBidirectional(oldPersonal.localidadProperty());
			fechaDatePicker.valueProperty().unbindBidirectional(oldPersonal.fechaNacimientoProperty());
			dniText.textProperty().unbindBidirectional(oldPersonal.identificacionProperty());
			direccionText.textProperty().unbindBidirectional(oldPersonal.direccionProperty());
			paisCombo.valueProperty().unbindBidirectional(oldPersonal.paisProperty());
		}
		
		// Bidireccional, puesto que queremos añadir cambios en el CV de una persona
		
		Bindings.bindBidirectional(nombreText.textProperty(), nuevoPersonal.nombreProperty());
		Bindings.bindBidirectional(apellidosText.textProperty(), nuevoPersonal.apellidosProperty());
		Bindings.bindBidirectional(codigoPostalText.textProperty(), nuevoPersonal.codigoPostalProperty());
		Bindings.bindBidirectional(localidadText.textProperty(), nuevoPersonal.localidadProperty());
		Bindings.bindBidirectional(fechaDatePicker.valueProperty(), nuevoPersonal.fechaNacimientoProperty());
		Bindings.bindBidirectional(dniText.textProperty(), nuevoPersonal.identificacionProperty());
		Bindings.bindBidirectional(direccionText.textProperty(), nuevoPersonal.direccionProperty());
		Bindings.bindBidirectional(paisCombo.valueProperty(), nuevoPersonal.paisProperty());
		//recordar que en el bindeo bidireccional hay que poner el de la interfaz primero
		nacionalidadList.itemsProperty().bindBidirectional(nuevoPersonal.nacionaliadesProperty());
	}
	
	public GridPane getRootView() {
		return view;
	}

}
