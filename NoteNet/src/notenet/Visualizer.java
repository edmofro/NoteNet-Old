package notenet;

import cx.fbn.nevernote.Global;
import cx.fbn.nevernote.NeverNote;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Visualizer extends Application{
	

    private Scene scene;
    VisualizerWindow view;
    
    @Override public void start(Stage stage) {
    	view = new VisualizerWindow(this);
        // create the scene
        stage.setTitle("Web View");
        scene = new Scene(view,750,500, Color.web("#666970"));
        stage.setScene(scene);
        //scene.getStylesheets().add("webviewsample/BrowserToolbar.css");     
        System.out.println("Showing stage");
        stage.show();
    }
    
    public void visualize(){
    	launch(null);
    	System.out.println("done");
	}
    		
	public static void main(String... args) {
		System.out.println(VisualizerWindow.class.getResource("index.html").toExternalForm());

		System.out.println(VisualizerWindow.class.getResource("index.html"));
	}

	public void runApp() {
		Platform.runLater(new Runnable() {
            @Override public void run() {
            	String[] args = {};
            	NeverNote.visualizeMain(args, view); 
            }
          });		
	}
	
}
