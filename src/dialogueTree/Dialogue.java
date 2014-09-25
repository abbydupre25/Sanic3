package dialogueTree;
import java.util.List;


public class Dialogue {
	public String baseText;
	public List<Option> options;
	
	public Dialogue(String baseText, List<Option> options) {
		this.baseText = baseText;
		this.options = options;
	}
}
