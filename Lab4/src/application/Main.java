package application;

import java.util.Vector;

import custom.Threads.ThreadController;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;



public class Main extends Application implements EventHandler<ActionEvent> {

	//GUI elements
	private Label infoDataInputLabel = new Label("Input real values of discrete function(separated by spaces, will skip non-numbers)");
	private TextField dataInputField = new TextField();
	private Button sendDataButton = new Button("Process");
	private Label infoMaxIndLabel = new Label("Indexes of local maximums: ");
	private Label maxIndexesLabel = new Label();
	private Label infoMinIndLabel = new Label("Indexes of local minimums: ");
	private Label minIndexesLabel = new Label();
	
	private float userInputFloat[];
	
	//Object that creates threads to process data
	private ThreadController executor = new ThreadController("SlaveThreads");
	
	
	//The main
	public static void main(String[] args) throws InterruptedException {
		launch(args);
	}
	
	
	//Main JavaFX GUI method
	@Override
	public void start(Stage stage) {
		stage.setTitle("Lab4, var 22");
		
		GridPane layout = new GridPane();
		layout.setHgap(2);
		layout.setVgap(2);
			//layout.setGridLinesVisible(true);	//for debug purposes
			
		Scene scene = new Scene(layout, 450, 100);
		stage.setScene(scene);
		
		//Placing and configuring elements
		layout.add(infoDataInputLabel, 0, 0, 2, 1);
		layout.add(dataInputField, 1, 1);
		layout.add(sendDataButton, 0, 1);
		sendDataButton.setOnAction(this);	//On action, triggers handle() method of this class
		layout.add(infoMinIndLabel, 0, 2);
		layout.add(minIndexesLabel, 1, 2);
		layout.add(infoMaxIndLabel, 0, 3);
		layout.add(maxIndexesLabel, 1, 3);
		
		stage.show();
	}
	
	
	//Method to handle all events
	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == sendDataButton)
		{
			//read string from text field, turn it into floats,
			//feed that array into thread thing
			
			String userData = dataInputField.getText();
			Vector<Float> dataVal = new Vector<Float>();
			
			//Cut into words, add everything that parses to float array
			String dataDivided[] = userData.split(" ");
			for(int i = 0; i < dataDivided.length; i++)
			{
				try {
					dataVal.add(Float.parseFloat(dataDivided[i]));
				}
				catch (NumberFormatException e) {
					//Couldn't convert this word to float, skip to next
					continue;
				}
			}
			
			//Convert Float[] to float[]. Again, no easy way...
			Float[] rawFloatArr = dataVal.toArray(new Float[0]);
			float[] tempFloatArr = new float[dataVal.size()];
			for(int i = 0; i < dataVal.size(); i++)
			{
				tempFloatArr[i] = rawFloatArr[i].floatValue();
			}
			userInputFloat = tempFloatArr;			
			
			//Start processing
			executor.startJob(userInputFloat);
			
			minIndexesLabel.setText("");
			maxIndexesLabel.setText("");
			
			//Retrieve results from object, place them into corresponding labels
			//Before results are done, nullptr is returned. Needs some waiting
			while(true)
			{
				int[] tempArr = executor.getLocalMin();
				if(tempArr == null)
				{
					try {
						Thread.sleep(10);
					} 
					catch (InterruptedException e) {
						//Nothing i guess. Timing is not important here
					}
					continue;
				}
				minIndexesLabel.setText(intArrToString(tempArr));
				break;
			}
			//When 1st wait completes, means all work is done. Can use 2nd part
			maxIndexesLabel.setText(intArrToString(executor.getLocalMax()));
		}	
	}
	
	
	//Converts int[] to String
	private String intArrToString(int[] arr) {
		String intString = new String();
		for(int i = 0; i<arr.length; i++)
		{
			intString += arr[i] + " ";
		}
		
		return intString;
	}
}