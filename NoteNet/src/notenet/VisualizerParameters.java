package notenet;

import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;

import cx.fbn.nevernote.Global;

public class VisualizerParameters extends QWidget{
	
	QComboBox maxActivated = new QComboBox();
	QComboBox fadeProportion = new QComboBox();
	QComboBox linkStrength = new QComboBox();
	QComboBox actDampening = new QComboBox();
	QComboBox actThreshold = new QComboBox();
	
	public VisualizerParameters(){
		setWindowTitle("Modify Spreading Activation Parameters");
		this.resize(this.width(), 300);
		QGridLayout grid = new QGridLayout(this);
        grid.setSpacing(2);
        grid.addWidget(new QLabel("Maximum Activated Nodes"));
        grid.addWidget(maxActivated);
		maxActivated.setEditable(true);
		maxActivated.activatedIndex.connect(this, "maxActivatedChanged()");
		maxActivated.lineEdit().setText(String.valueOf(Global.MAX_ACTIVATED));
        grid.addWidget(new QLabel("Activation Fade Proportion"));
        grid.addWidget(fadeProportion);
        fadeProportion.setEditable(true);
        fadeProportion.activatedIndex.connect(this, "fadeProportionChanged()");
        fadeProportion.lineEdit().setText(String.valueOf(Global.FADE_PROPORTION));
//        grid.addWidget(new QLabel("Strength of Links"));
//        grid.addWidget(linkStrength);
//        linkStrength.setEditable(true);
//        linkStrength.activatedIndex.connect(this, "linkStrengthChanged()");
        grid.addWidget(new QLabel("Spreading Activation Dampening"));
        grid.addWidget(actDampening);
        actDampening.setEditable(true);
        actDampening.activatedIndex.connect(this, "actDampeningChanged()");
        actDampening.lineEdit().setText(String.valueOf(Global.ACTIVAION_PROPOGATION_DAMPENING));
        grid.addWidget(new QLabel("Minimum Activation Threshold"));
        grid.addWidget(actThreshold);
        actThreshold.setEditable(true);
        actThreshold.activatedIndex.connect(this, "actThresholdChanged()");
        actThreshold.lineEdit().setText(String.valueOf(Global.ACTIVAION_PROPOGATION_THRESHOLD));
	}
	
	private void maxActivatedChanged(){
		String string = maxActivated.currentText();
		int num = 0;
		try{
			num = Integer.parseInt(string);
		} catch(NumberFormatException e){
			System.out.println("String in maxActivated could not be converted to a whole number");
			maxActivated.lineEdit().setText(String.valueOf(Global.MAX_ACTIVATED));
			return;
		}
		Global.MAX_ACTIVATED = num;		
	}
	
	private void fadeProportionChanged(){
		String string = fadeProportion.currentText();
		double num = 0;
		try{
			num = Double.parseDouble(string);
		} catch(NumberFormatException e){
			System.out.println("String in fadeProportion could not be converted to a number");
	        fadeProportion.lineEdit().setText(String.valueOf(Global.FADE_PROPORTION));
			return;
		}
		Global.FADE_PROPORTION = num;		
	}
	
	private void linkStrengthChanged(){
		String string = linkStrength.currentText();
		double num = 0;
		try{
			num = Double.parseDouble(string);
		} catch(NumberFormatException e){
			System.out.println("String in linkStrength could not be converted to a number");
			linkStrength.lineEdit().setText(String.valueOf(Global.LINK_STRENGTH_FINDRELATED));
			return;
		}
		Global.LINK_STRENGTH_FINDRELATED = num;		
	}

	private void actDampeningChanged(){
		String string = actDampening.currentText();
		double num = 0;
		try{
			num = Double.parseDouble(string);
		} catch(NumberFormatException e){
			System.out.println("String in actDampening could not be converted to a number");
			actDampening.lineEdit().setText(String.valueOf(Global.ACTIVAION_PROPOGATION_DAMPENING));
			return;
		}
		Global.ACTIVAION_PROPOGATION_DAMPENING = num;		
	}

	private void actThresholdChanged(){
		String string = actThreshold.currentText();
		double num = 0;
		try{
			num = Double.parseDouble(string);
		} catch(NumberFormatException e){
			System.out.println("String in actThreshold could not be converted to a number");
			actThreshold.lineEdit().setText(String.valueOf(Global.ACTIVAION_PROPOGATION_THRESHOLD));
			return;
		}
		Global.ACTIVAION_PROPOGATION_THRESHOLD = num;		
	}


}
