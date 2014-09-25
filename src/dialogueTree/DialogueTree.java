package dialogueTree;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.ini4j.ConfigParser;
import org.ini4j.ConfigParser.InterpolationException;
import org.ini4j.ConfigParser.NoOptionException;
import org.ini4j.ConfigParser.NoSectionException;
import org.ini4j.ConfigParser.ParsingException;


public class DialogueTree {
	//sorry
	private HashMap<String, Dialogue> dialogues = new HashMap<String, Dialogue>();
	private Scanner scanner;
	
	public DialogueTree(String configFile) {
		ConfigParser config = new ConfigParser();
		scanner = new Scanner(System.in);
		try {
			config.read(configFile);
		} catch (ParsingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			for (int i = 0; i < config.sections().size(); i++){ //set up dialogues
				String sectionName = config.sections().get(i);
				dialogues.put(sectionName, new Dialogue(config.get(sectionName, "text"), null));
			}
			for (int i = 0; i < dialogues.size(); i++){ //set up options
				List<Option> options = new ArrayList<Option>();
				String sectionName = config.sections().get(i);
				for (int j = 1; j < config.options(sectionName).size(); j++){
					String key = config.options(sectionName).get(j);
					Dialogue destination;
					if (key.equals("back")){
						String newDest = sectionName.substring(0, sectionName.length()-1);
						if (newDest.endsWith("-")){
							newDest = newDest.substring(0, sectionName.length()-1);
						} else {
							newDest = "base";
						}
						destination = dialogues.get(newDest);
					} else {
						if (sectionName.equals("base")){
							destination = dialogues.get("branch" + key);
						} else {
							destination = dialogues.get(sectionName + "-" + key);
						}
					}
					Option newOption = new Option(config.get(sectionName, key), destination);
					if (key.equals("quit")){
						newOption.isQuit = true;
					} else if (key.equals("back")){
						newOption.isBack = true;
					}
					options.add(newOption);
				}
				dialogues.get(sectionName).options = options;
			}
		} catch (NoSectionException e) {
			e.printStackTrace();
		} catch (NoOptionException e) {
			e.printStackTrace();
		} catch (InterpolationException e) {
			e.printStackTrace();
		}		
	}
	
	public void begin() {
		Dialogue currentDialogue = dialogues.get("base");
		while (currentDialogue != null) {
			System.out.println("\n" + currentDialogue.baseText);
			for (int i = 0; i < currentDialogue.options.size(); i++){
				System.out.println(i+1 + " " + currentDialogue.options.get(i).text);
			}
			
			//Get user input
			int userChoice = Integer.parseInt(scanner.next()) - 1;
			Option selectedOption = currentDialogue.options.get(userChoice);
			if (selectedOption.isQuit) {
				break;
			} else {
				currentDialogue = selectedOption.destination;
			}
		}
		System.out.println("Dialogue completed");
	}
}
