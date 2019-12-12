package dad.javafx.micv.controllers;

import java.io.IOException;
import java.net.URL;


import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;

public class ConocimientosController implements Initializable {

	// View 
    @FXML
    private HBox view;

    @FXML
    private TableView<?> conocimientoTable;

    @FXML
    private Button addConocimientoButton;

    @FXML
    private Button addIdiomaButton;

    @FXML
    private Button eliminarButton;
    
    @FXML
    private TableColumn<?, ?> nivelColumn;
    
    // Model  
	public ConocimientosController() throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ConocimientosView.fxml"));
		loader.setController(this);
		loader.load();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		eliminarButton.setOnAction( e -> onQuitarAction() );		
		addConocimientoButton.setOnAction( e -> onAddAction() );
		addIdiomaButton.setOnAction( e -> onAddAction() );
	}
	
	private void onQuitarAction() {			
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("Eliminar conocimiento");
		alert.setContentText("Aún no implementado");
		alert.showAndWait();
	}

	private void onAddAction() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("Añadir conocimiento");
		alert.setContentText("Aún no implementado");	
		alert.showAndWait();
	}
	
	public HBox getRoot() {
		return view;
	}	
}
