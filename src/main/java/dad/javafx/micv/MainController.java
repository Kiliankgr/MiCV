package dad.javafx.micv;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dad.javafx.micv.clases.CV;
import dad.javafx.micv.clases.Contacto;
import dad.javafx.micv.clases.Personal;
import dad.javafx.micv.controllers.ConocimientosController;
import dad.javafx.micv.controllers.ContactoController;
import dad.javafx.micv.controllers.ExperienciaController;
import dad.javafx.micv.controllers.FormacionController;
import dad.javafx.micv.controllers.PersonalController;
import dad.javafx.micv.utils.JAXBUtils;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainController implements Initializable {

	// View : FXML
	//-------------------------------------------------------------------------
	
	@FXML
	private BorderPane view;
	
	@FXML 
	private TabPane rootTab;

    @FXML
    private MenuItem nuevoMenuItem;

    @FXML
    private MenuItem abrirMenuItem;

    @FXML
    private MenuItem guardarMenuItem;

    @FXML
    private MenuItem guardarOtroMenuItem;

    @FXML
    private MenuItem salirMenuItem;

    @FXML
    private Tab personalTab;

    @FXML
    private Tab contactoTab;

    @FXML
    private Tab formacionTab;

    @FXML
    private Tab experienciaTab;

    @FXML
    private Tab conocimientosTab;
	
  //-------------------------------------------------------------------------
    
    // Model
    // Básicamente vamos a tener el objeto CV
    private ObjectProperty<CV> cv = new SimpleObjectProperty<CV>();
    
	// Tabs controllers
	private PersonalController personalController;
	private ContactoController contactoController;
	private FormacionController formacionController;
	private ExperienciaController experienciaController;
	private ConocimientosController conocimientosController;
	
	private File currentFile;
	
	public MainController() throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainView.fxml"));
		loader.setController(this);
		loader.load();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Debemos cargar ahora todos las pestañas
		try {
			personalController = new PersonalController();
			personalTab.setContent(personalController.getRootView());
			
			contactoController = new ContactoController();
			contactoTab.setContent(contactoController.getRootView());

			formacionController = new FormacionController();
			formacionTab.setContent(formacionController.getRootView());
			
			experienciaController = new ExperienciaController();
			experienciaTab.setContent(experienciaController.getRootView());
			
			conocimientosController = new ConocimientosController();
			conocimientosTab.setContent(conocimientosController.getRootView());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		cv.set(new CV());
		cv.get().setPersonal(new Personal());
		cv.get().setContacto(new Contacto());
		loadMainData();
		
		//cargamos los eventos de los menus
		nuevoMenuItem.setOnAction(evt -> onMenuNew() );
		abrirMenuItem.setOnAction( evt -> onMenuOpen() );
		guardarMenuItem.setOnAction( evt -> onMenuSave() );
		guardarOtroMenuItem.setOnAction( evt -> onMenuSaveOther() );
		salirMenuItem.setOnAction( evt -> onMenuExit() );	
	}
	
	private void onMenuNew() {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Nuevo documento");
		alert.setHeaderText("Establecer nuevo documento .cv");
		alert.setContentText("¿ Está seguro de crear un nuevo documento ?");
		
		if( alert.showAndWait().get() == ButtonType.OK ) {
			// Limpiamos todos los datos
			cv.set(new CV());
			cv.get().setPersonal(new Personal());
			cv.get().setContacto(new Contacto());
			loadMainData();
		}
		
		currentFile = null;
	}

	private void onMenuExit() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Cerrar documento");
		alert.setHeaderText("Cerrar documento");
		alert.setContentText("¿Está usted seguro de cerrar el documento?");
		
		if( alert.showAndWait().get() == ButtonType.OK ) {
			//salimos
			Platform.exit();
		}
		
		
	}

	private void onMenuSave() {
			
		// Si no estamos trabajando sobre un fichero guardamos en un nuevo archivo
		if( currentFile == null ) {
			onMenuSaveOther();
			return;
		}
		try {
			JAXBUtils.save(cv.get(), currentFile);
				
		} catch (Exception e) {
			enviarError("Error al guardar el fichero: " + currentFile.getName());
		}
	}

	private void onMenuSaveOther() {
		
		FileChooser browser = new FileChooser();
		browser.setTitle("Guardar CV como...");
		//asignamos extencion
		browser.getExtensionFilters().add(new ExtensionFilter("XML", "*.xml"));
		browser.setInitialFileName("newCV.xml");
		browser.setInitialDirectory(new File(System.getProperty("user.dir") + "/files"));
		
		File file = browser.showSaveDialog(getRootView().getScene().getWindow());
		
		if( file != null ) {
			
			try {
				JAXBUtils.save(cv.get(), file);
			} catch (Exception e) {
				enviarError("Error al guardar el fichero: " + file.getName());
			}
		}
	}

	private void onMenuOpen() {
		
		FileChooser browser = new FileChooser();
		browser.getExtensionFilters().add(new ExtensionFilter("XML", "*.xml"));
		browser.setTitle("Abrir CV");
		browser.setInitialDirectory(new File(System.getProperty("user.dir") + "/files"));
		currentFile = browser.showOpenDialog(getRootView().getScene().getWindow());
		
		if( currentFile != null ) {
			
			// Ahora podemos empezar a cargar el archivo
			// Usamos el JAXB para leer el XML
			
			try {
				
				CV myCV = JAXBUtils.load(CV.class, currentFile);
				
				// Ahora lo cargamos en nuestro ObjectProperty
				cv.set(myCV);
				loadMainData();
				
			} catch (Exception e) {
				enviarError("Error al abrir el fichero: " + currentFile.getName());
			}
		}
	}

	private void loadMainData() {
		
		// Cargamos todos los datos
		
		Personal personal = cv.get().getPersonal();
		personalController.setPersonal(personal);
		
		Contacto contacto = cv.get().getContacto();
		contactoController.setContacto(contacto);
		
		// Ahora bindeamos las listas de las otras pestañas para tenerlos actualizados
		formacionController.titulosProperty().bindBidirectional(cv.get().formacionProperty());
		experienciaController.listExperienciaProperty().bindBidirectional(cv.get().experienciasProperty());
		conocimientosController.conocimientosProperty().bindBidirectional(cv.get().habilidadesProperty());
	}
	
	public static void enviarError( String msg ) {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setContentText("Error del lector del CV");
		alert.setContentText(msg);		
		alert.showAndWait();
		Platform.exit();
	}

	public BorderPane getRootView() {
		return view;
	}

}
