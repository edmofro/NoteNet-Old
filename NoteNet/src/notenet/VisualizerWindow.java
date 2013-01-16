package notenet;


import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;


public class VisualizerWindow extends Region {
	 
	    final WebView view = new WebView();
	    final WebEngine webEngine = view.getEngine();
	     
	    public VisualizerWindow(final Visualizer visualizer) {
	        //apply the styles
	        getStyleClass().add("browser");
	        //call the rest of app when page is rendered
	        webEngine.getLoadWorker().stateProperty().addListener(
	                new ChangeListener<State>() {
	                    @Override
	                    public void changed(ObservableValue ov, State oldState, State newState) {
	                        if (newState == State.SUCCEEDED) {
	                            visualizer.runApp();
	                        }
	                    }
	                });
	        // load the web page
	        webEngine.load(VisualizerWindow.class.getResource("index.html").toExternalForm());
	        //add the web view to the scene
	        getChildren().add(view);
	 
	    }
	    private Node createSpacer() {
	        Region spacer = new Region();
	        HBox.setHgrow(spacer, Priority.ALWAYS);
	        return spacer;
	    }
	 
	    @Override protected void layoutChildren() {
	        double w = getWidth();
	        double h = getHeight();
	        layoutInArea(view,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
	    }
	 
	    @Override protected double computePrefWidth(double height) {
	        return 750;
	    }
	 
	    @Override protected double computePrefHeight(double width) {
	        return 500;
	    }
	    
	    public void executeScript(String script){
			script = script.replace('-', 'x');
			script += "start();";
	    	System.out.println("Executing script: " + script);
	    	webEngine.executeScript(script);
	    }
	    
	    public void remove(ActivationNode node) {
			String script = 
					"removeById( '" + node.getNoteGuid() + "');\n";
			executeScript(script);
		}

		public void fade(ActivationNode act) {
			String script = 
					"changeActivationById( '" + act.getNoteGuid() + "', " + act.getActivation() + ");\n";
			executeScript(script);
		}
		
		public void boost(ActivationNode act, String linkNode, double linkStrength){
			String script = 
					"changeActivationById( '" + act.getNoteGuid() + "', " + act.getActivation() + ");\n";
			if(linkNode!=null){
				script += 
					"linkNodes( '" + act.getNoteGuid() + "', '" + linkNode + "', " + linkStrength + ");";
			}
			executeScript(script);
		}

		public void add(ActivationNode act, String linkNode, double linkStrength) {
			String script = 
					"var n" + act.getNoteGuid() + " = {id: \'"+ act.getNoteGuid() +"\', activation: " + act.getActivation() + " };\n" +
				    "nodes.push(n" + act.getNoteGuid() + ");\n";
			if(linkNode!=null){
				script +=
//					"var n" + linkNode + "= findById( '" + linkNode + "');\n" + 
//					"links.push({source: n" + act.getNoteGuid() + ", target: n" + linkNode + ", strength: " + (int) (linkStrength * 10) + "})\n";
					"linkNodes( '" + act.getNoteGuid() + "', '" + linkNode + "', " + linkStrength + ");";
			}
			executeScript(script);			
		}
	}