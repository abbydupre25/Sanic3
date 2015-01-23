package game;

public class Objective {
	private boolean complete;
	private boolean discovered;
	private String desc;
	private Quest parent;
	
	public Objective(String desc) {
		complete = false;
		this.desc = desc;
		this.discovered = false;
	}
	
	public void setParent(Quest quest) {
		parent = quest;
	}
	
	public void discover() {
		discovered = true;
	}
	
	public void complete() {
		complete = true;
		parent.nextObjective();
	}
	
	public boolean isDiscovered() {
		return discovered;
	}
	
	public boolean isComplete() {
		return complete;
	}
	
	/** Returns the description for this objective */
	public String getDesc() {
		return desc;
	}
}
