package notenet;

public class ActivationNode implements Comparable<ActivationNode>{
	
	private String noteGuid;
	private double activation; // between 0 and 1
	private int position;
	private String name;
	
	public ActivationNode(String guid, String name, double act){
		noteGuid = guid;
		activation = act;
		this.name = name;
	}

	// Abstract activation
	public ActivationNode(String guid, String name) {
		noteGuid = guid;
		this.name = name;
		activation = 0;
	}

	public double getActivation() {
		return activation;
	}

	public void setActivation(double activation) {
		this.activation = activation;
		enforceActivationConstraints();
	}
	
	public void changeActivation(double actChange) {
		activation+=actChange;		
		enforceActivationConstraints();
	}
	
	private void enforceActivationConstraints() {
		if(activation>1) activation = 1;
		if(activation<0) activation = 0;		
	}

	public String getNoteGuid() {
		return noteGuid;
	}

	@Override
	public int compareTo(ActivationNode other) {
		return (int) (10000 * (activation - other.activation));	
	}
	
	public boolean equals(Object other){
		if(!(other instanceof ActivationNode)){
			return false;
		}
		ActivationNode act = (ActivationNode) other;
		if(act.getNoteGuid().equals(noteGuid)) return true;
		return false;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void fade(double proportionFade) {
		activation*=(1-proportionFade);
		
	}

	public String getName() {
		return name;
	}

	
	


}
