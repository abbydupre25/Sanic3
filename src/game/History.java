package game;

import java.util.ArrayList;
import java.util.HashMap;

/** Keeps track of quests. Quests are given a key by the NPC and store the strings 'undiscovered'
 * 'pending', or 'done' */
public class History {
	private HashMap<String, String> quests;
	private static ArrayList<String> allowedStates = new ArrayList<String>(){{
		add("undiscovered");
		add("pending");
		add("done");
	}};
	
	public History() {
		this.quests = new HashMap<String, String>();
	}
	
	private class Quest {
		private HashMap<String, String> objectives;
		private String state;
		
		public Quest() {
			state = allowedStates.get(0);
		}
		
		public String getState() {
			return state;
		}
		
		public void setState(String state) {
			this.state = state;
		}

		public HashMap<String, String> getObjectives() {
			return objectives;
		}

		public void setObjectives(HashMap<String, String> objectives) {
			this.objectives = objectives;
		}
	}
	
	/** Creates new flag with default value 'undiscovered' */
	public void add(String flagName) {
		add(flagName, "undiscovered");
	}
	
	public void add(String flagName, String state) {
		if(!quests.containsKey(flagName)){
			quests.put(flagName, state);
		}
	}
	
	public String get(String flagName) {
		if(!quests.containsKey(flagName)){
			add(flagName);
		} 
		return quests.get(flagName);
	}
	
	public void set(String key, String state) {
		if(!allowedStates.contains(state)){
			System.out.println("INVALID STATE: " + key);
		} else {
			if(quests.containsKey(key)){
				quests.remove(key);
			}
			quests.put(key, state);
		}
	}
	
	public boolean contains(String flagName) {
		return quests.containsKey(flagName);
	}
}
