package dad.javafx.micv.utils;


import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import dad.javafx.micv.clases.Experiencia;
import dad.javafx.micv.clases.Titulo;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class InsertTableDialog<T> extends Dialog<T> {

	
	 // Constructor para insertar las experiencias o formaciones ya que el formato es practicamente el mismo
	 
	public InsertTableDialog(String titulo, String tipo, Class<T> myClass ) {
		super();
		setTitle(titulo);
		
		Stage stage = (Stage) getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(getClass().getResource("/images/cv64x64.png").toString()));
		
		GridPane root = new GridPane();
		root.setHgap(5);
		root.setVgap(5);
		
		Label denenominacionLabel = new Label("Denominación");
		TextField denominacionText = new TextField();
		root.addRow(0, denenominacionLabel, denominacionText);
			
		Label organizadorOEmpleadorLabel = new Label(tipo);
		TextField organizadorOEmpleadorText = new TextField();
		root.addRow(1,  organizadorOEmpleadorLabel, organizadorOEmpleadorText);
		
		GridPane.setHgrow(denominacionText, Priority.ALWAYS);
		GridPane.setHgrow(organizadorOEmpleadorText, Priority.ALWAYS);
		
		Label desdeLabel = new Label("Desde");
		DatePicker desdeDatePicker = new DatePicker();
		root.addRow(2, desdeLabel, desdeDatePicker);

		Label hastaLabel = new Label("Hasta");
		DatePicker hastaDatePicker = new DatePicker();
		root.addRow(3,  hastaLabel, hastaDatePicker);
		
		this.getDialogPane().setContent(root);
		this.getDialogPane().setPrefWidth(400);
		
		ButtonType createBt = new ButtonType("Crear", ButtonData.OK_DONE);//confirmar
		ButtonType cancelBt = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);////cancelar
		
		this.getDialogPane().getButtonTypes().addAll(createBt, cancelBt);
		
		this.setResultConverter( bt -> {

			// Aquí nos viene bien usar genéricos, puesto que ambos tienen similares constructores
			if( bt.getButtonData() == ButtonData.OK_DONE ) {
				return myClass.cast( myClass == Experiencia.class ?
						new Experiencia(desdeDatePicker.getValue(), hastaDatePicker.getValue(), denominacionText.getText(), organizadorOEmpleadorText.getText())
					:   new Titulo(desdeDatePicker.getValue(), hastaDatePicker.getValue(), denominacionText.getText(), organizadorOEmpleadorText.getText()));
			}
			return null;
		});
	}
}
