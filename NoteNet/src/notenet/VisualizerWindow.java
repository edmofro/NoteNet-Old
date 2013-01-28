package notenet;

import org.bouncycastle.util.test.Test;

import com.trolltech.qt.core.QEventLoop;
import com.trolltech.qt.core.QUrl;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.webkit.QWebView;

import cx.fbn.nevernote.gui.BrowserWindow;




public class VisualizerWindow extends QWebView {
	
	private boolean loaded = false;
	private String scriptQueue = "";
	private int maxNameLength = 30;
	QBridge bridge = new QBridge(this);
	BrowserWindow browser;
	
	public static void main(String[] args){
		 QApplication.instance();
		QMainWindow test = new QMainWindow();
		test.setCentralWidget(new VisualizerWindow(null));
	}
	 
	    public VisualizerWindow(BrowserWindow browserWindow) {
	    	super();
	    	browser = browserWindow;
	    	this.page().mainFrame().javaScriptWindowObjectCleared.connect(this, "javaScriptWindowObjectCleared()");
	    	this.load(new QUrl(VisualizerWindow.class.getResource("index.html").toExternalForm()));	
//	    	this.titleChanged.connect(this, "loadFinished()"); 
	    }
	    
	    @SuppressWarnings("unused")
		private final void javaScriptWindowObjectCleared()
	    {
	    	System.out.println("Connecting bridge");
	        page().mainFrame().addToJavaScriptWindowObject("bridge", bridge);
	        page().mainFrame().evaluateJavaScript("bridgeConnected = true;");
	    }
	    
	    public void loadFinished(){	
	    	this.page().mainFrame().evaluateJavaScript("width = " + this.size().width() + ", height = " + this.size().height() + ";");
	    	this.page().mainFrame().evaluateJavaScript("svg.attr(\"width\", width).attr(\"height\", height);force.size([width,height]);start();");
	    	this.page().mainFrame().evaluateJavaScript(scriptQueue);
	    	scriptQueue="";
	    	loaded=true;
	    }
	   
	    public void executeScript(String script){
			script += "start();";
			if(loaded){
//		    	System.out.println("// Executing script: \n" + script);
		    	this.page().mainFrame().evaluateJavaScript(script);
			} else{
				scriptQueue += script;
			}
	    }
	    
	    public void start(){
//	    	executeScript("start();");
	    }
	    
	    public void remove(ActivationNode node) {
			String guid = dashReplace(node.getNoteGuid()); //Replace dashes with spaces in potential variable names so they don't confuse javascript
			String script = 
					"removeById( '" + guid + "');\n";
			executeScript(script);
		}

		public void fade(ActivationNode act) {
			String guid = dashReplace(act.getNoteGuid()); //Replace dashes with spaces in potential variable names so they don't confuse javascript
			String script = 
					"changeActivationById( '" + guid + "', " + act.getActivation() + ");\n";
			executeScript(script);
		}
		
		public void boost(ActivationNode act, String linkNode, double linkStrength){
			String guid = dashReplace(act.getNoteGuid()); //Replace dashes with spaces in potential variable names so they don't confuse javascript
			String script = 
					"changeActivationById( '" + guid + "', " + act.getActivation() + ");\n";
			if(linkNode!=null){
				linkNode = dashReplace(linkNode);
				script += 
					"linkNodes( '" + guid + "', '" + linkNode + "', " + linkStrength + ");";
			}
			executeScript(script);
		}

		public void add(ActivationNode act, String linkNode, double linkStrength) {
			String name = act.getName(); 
			if(name.length()>maxNameLength){ 				//Shorten names if they are too long
				name = name.substring(0, maxNameLength - 3) + "...";
			}
			String guid = dashReplace(act.getNoteGuid()); //Replace dashes with spaces in potential variable names so they don't confuse javascript
			String script = 
					"var n" + guid + " = {id: \'" + guid + "\', name: \'" + name + "\', activation: " + act.getActivation() + " };\n" +
				    "nodes.push(n" + guid + ");\n";
			if(linkNode!=null){
				linkNode = dashReplace(linkNode);
				script +=
//					"var n" + linkNode + "= findById( '" + linkNode + "');\n" + 
//					"links.push({source: n" + act.getNoteGuid() + ", target: n" + linkNode + ", strength: " + (int) (linkStrength * 10) + "})\n";
					"linkNodes( '" + guid + "', '" + linkNode + "', " + linkStrength + ");";
			}
			executeScript(script);			
		}
		
		public String dashReplace(String withDashes){
			return withDashes.replace('-', 'x');
		}
		
		public void clicked(String guid){
			System.out.println(guid + " was clicked");
			browser.noteClicked(guid);
		}
	}