package notenet;

import org.bouncycastle.util.test.Test;

import com.trolltech.qt.core.QEventLoop;
import com.trolltech.qt.core.QUrl;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.webkit.QWebView;




public class VisualizerWindow extends QWebView {
	
	private boolean loaded = false;
	private String scriptQueue = "";
	
	public static void main(String[] args){
		 QApplication.instance();
		QMainWindow test = new QMainWindow();
		test.setCentralWidget(new VisualizerWindow());
	}
	 
	    public VisualizerWindow() {
	    	super();
	    	this.loadFinished.connect(this, "loadFinished()");
	    	this.load(new QUrl(VisualizerWindow.class.getResource("index.html").toExternalForm()));	 
	       	//	        this.load(new QUrl("http://xkcd.com"));
	    }
	    
	    public void loadFinished(){	  
	    	loaded = true;
	    	executeScript(scriptQueue);
	    	scriptQueue = "";
	    	System.out.println("Done!");
	    }
	   
	    public void executeScript(String script){
			script = script.replace('-', 'x');
//			script += "start();";
			if(loaded){
		    	System.out.println("// Executing script: \n" + script);
		    	this.page().mainFrame().evaluateJavaScript(script);
			} else{
				scriptQueue += script;
			}
	    }
	    
	    public void start(){
	    	executeScript("start();");
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
					"var n" + act.getNoteGuid() + " = {id: \'"+ act.getNoteGuid() + "\', name: \'" + act.getName() + "\', activation: " + act.getActivation() + " };\n" +
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