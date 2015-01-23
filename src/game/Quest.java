package game;

import java.util.ArrayList;

public class Quest {
	private ArrayList<Objective> objectives;
	private String state;
	public static ArrayList<String> allowedStates = new ArrayList<String>(){{
		add("undiscovered");
		add("pending");
		add("done");
	}};
	private String name;
	private String desc;
	private int currentObjectiveID;
	
	public Quest(String name, String desc, ArrayList<Objective> objectives) {
		state = allowedStates.get(0);
		this.setName(name);
		this.setDesc(desc);
		this.objectives = objectives;
		currentObjectiveID = 0;
		for(Objective o : objectives) {
			o.setParent(this);
		}
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}

	public ArrayList<Objective> getObjectives() {
		return objectives;
	}

	public void setObjectives(ArrayList<Objective> objectives) {
		this.objectives = objectives;
	}
	
	public int getObjectiveID() {
		return currentObjectiveID;
	}
	
	public void nextObjective() {
		System.out.println("Completed objective " + currentObjectiveID);
		if(currentObjectiveID+1 < objectives.size()){
			currentObjectiveID++;
		} else {
			setState("done");
		}
	}
	
	public boolean isComplete() {
		return (state.equals("done"));
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}