package dad.javafx.micv.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.javafx.micv.clases.Experiencia;
import dad.javafx.micv.clases.Titulo;
import dad.javafx.micv.utils.InsertTableDialog;
import dad.javafx.micv.utils.LocalDateTableCell;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;


public class ExperienciaController implements Initializable {

	// View 
	
    @FXML
    private HBox view;

    @FXML
    private TableView<Experiencia> experienciaTable;

    @FXML
    private Button addButton;

    @FXML
    private Button quitarButton;
    
    @FXML
    private TableColumn<Titulo, LocalDate> desdeColumn;

    @FXML
    private TableColumn<Titulo, LocalDate> hastaColumn;
    
    // Model
    private ListProperty<Experiencia> listExperiencia = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
    
	public ExperienciaController() throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ExperienciaView.fxml"));
		loader.setController(this);
		loader.load();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		desdeColumn.setCellFactory(LocalDateTableCell::new); 
		hastaColumn.setCellFactory(LocalDateTableCell::new);
		
		experienciaTable.itemsProperty().bindBidirectional(listExperiencia);
		
		addButton.setOnAction( e -> onAddAction() );
		quitarButton.setOnAction( e -> onRemoveAction() );
		
		quitarButton.disableProperty().bind( experienciaTable.getSelectionModel().selectedItemProperty().isNull() );		
		
	}
	
	private void onRemoveAction() {
		
		Experiencia experiencia = experienciaTable.getSelectionModel().getSelectedItem();
		
		if( experiencia != null ) {
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Eliminar experiencia");
			alert.setContentText("¿Está seguro de eliminar "+ experiencia.getDenominacion()+"?");
			
			if( alert.showAndWait().get() == ButtonType.OK ) {
				listExperiencia.remove(experiencia);
			}
		}
	}

	private void onAddAction() {

		InsertTableDialog<Experiencia> dialog = new InsertTableDialog<>("Añadir experiencia", "Empleador", Experiencia.class);
		
		Optional<Experiencia> experiencia = dialog.showAndWait();
		
		if( experiencia.isPresent() ) {
			listExperiencia.add(experiencia.get());
		}
	}
	
	public HBox getRoot() {
		return view;
	}

	public final ListProperty<Experiencia> listExperienciaProperty() {
		return this.listExperiencia;
	}
	

	public final ObservableList<Experiencia> getListExperiencia() {
		return this.listExperienciaProperty().get();
	}
	

	public final void setListExperiencia(final ObservableList<Experiencia> listExperiencia) {
		this.listExperienciaProperty().set(listExperiencia);
	}
		

}
