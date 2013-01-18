package notenet;

import com.trolltech.qt.core.QUrl;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.webkit.QWebView;




public class VisualizerWindow extends QWebView {
	 
	    public VisualizerWindow() {
	    	super();
	        this.load(QUrl.fromLocalFile("C:/Users/Edwin/git/NoteNet/NoteNet/bin/notenet/index.html")); // new QUrl("C:/Users/Edwin/git/NoteNet/NoteNet/bin/notenet/index.html")); //VisualizerWindow.class.getResource("index.html").toExternalForm()));	 
	    }
	   
	    public void executeScript(String script){
			script = script.replace('-', 'x');
			script += "start();";
	    	System.out.println("Executing script: " + script);
	    	//webEngine.executeScript(script);
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