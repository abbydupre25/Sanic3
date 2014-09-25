package dialogueTree;


public class Option {
	public String text;
	public Dialogue destination;
	public Dialogue parent;
	public boolean isQuit;
	public boolean isBack;
	
	public Option(String text, Dialogue destination){
		this.destination = destination;
		this.text = text;
		this.isQuit = false;
		this.isBack = false;
	}
}
