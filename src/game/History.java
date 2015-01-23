package game;

import java.util.HashMap;

/** Keeps track of flags. Flags are given a key by the NPC and store the strings 'undiscovered'
 * 'pending', or 'done' */
public class History {
	private HashMap<String, String> flag;
	private HashMap<String, Quest> quests;
	
	public History() {
		this.flag = new HashMap<String, String>();
		quests = new HashMap<String, Quest>();
	}
	
	/** Creates new flag with default value 'undiscovered' */
	public void add(String flagName) {
		add(flagName, "undiscovered");
	}
	
	public void add(String flagName, String state) {
		if(!flag.containsKey(flagName)){
			flag.put(flagName, state);
		}
	}
	
	public String get(String flagName) {
		if(!flag.containsKey(flagName)){
			add(flagName);
		} 
		return flag.get(flagName);
	}
	
	public void set(String key, String state) {
		if(!Quest.allowedStates.contains(state)){
			System.out.println("INVALID STATE: " + key);
		} else {
			if(flag.containsKey(key)){
				flag.remove(key);
			}
			flag.put(key, state);
		}
	}
	
	public boolean contains(String flagName) {
		return flag.containsKey(flagName);
	}

	public HashMap<String, Quest> getQuests() {
		return quests;
	}

	public void setQuests(HashMap<String, Quest> quests) {
		this.quests = quests;
	}
}
